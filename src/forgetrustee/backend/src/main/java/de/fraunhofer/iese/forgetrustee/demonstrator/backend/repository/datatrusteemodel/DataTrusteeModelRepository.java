
/*
 * Copyright (C) 2026 Fraunhofer IESE
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.repository.datatrusteemodel;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Repository for loading {@link DataTrusteeModel} instances from local JSON model folders.
 * <p>
 * Each model lives in its own directory below the configured base path. The repository uses the
 * directory name as model id and reads the single {@code .json} file inside that directory.
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class DataTrusteeModelRepository {

  private static final String JSON_FILE_SUFFIX = ".json";

  private final BackendProperties backendProperties;

  private final ObjectMapper objectMapper;

  private final DataTrusteeWizardRequestMapper wizardRequestMapper;

  private final DataTrusteeModelMapper dataTrusteeModelMapper;

  private final AtomicReference<Map<String, DataTrusteeModel>> modelsCache = new AtomicReference<>();

  /**
   * Returns all configured models in stable id order.
   *
   * @return immutable list of all models
   */
  public List<DataTrusteeModel> findAll() {
    return List.copyOf(this.loadModels().values());
  }

  /**
   * Returns all configured models together with their ids.
   *
   * @return immutable map with model ids as keys
   */
  public Map<String, DataTrusteeModel> findAllById() {
    return this.loadModels();
  }

  /**
   * Looks up a model by its id.
   *
   * @param  modelId id from root field {@code modelId}/{@code id} or file name
   * @return         matching model if present
   */
  public Optional<DataTrusteeModel> findById(String modelId) {
    if (modelId == null || modelId.isBlank()) {
      return Optional.empty();
    }
    return Optional.ofNullable(this.loadModels().get(modelId.trim()));
  }

  /**
   * Clears the in-memory cache so changed files are re-read on the next access.
   */
  public void refresh() {
    this.modelsCache.set(null);
  }

  private Map<String, DataTrusteeModel> loadModels() {
    final Map<String, DataTrusteeModel> cached = this.modelsCache.get();
    if (cached != null) {
      return cached;
    }

    final Map<String, DataTrusteeModel> loaded = this.readModelsFromFileSystem();
    this.modelsCache.compareAndSet(null, loaded);
    return this.modelsCache.get();
  }

  private Map<String, DataTrusteeModel> readModelsFromFileSystem() {
    final Path basePath = this.getBasePath();
    if (!Files.exists(basePath)) {
      log.warn("Data trustee model base path not found: {}", basePath);
      return Collections.emptyMap();
    }
    if (!Files.isDirectory(basePath)) {
      throw new IllegalStateException(
          "Data trustee model base path is not a directory: " + basePath);
    }

    final Map<String, DataTrusteeModel> byId = new LinkedHashMap<>();
    try (Stream<Path> files = Files.list(basePath)) {
      files
          .filter(Files::isDirectory)
          .sorted()
          .map(this::readSingleModelDirectory)
          .forEach(entry -> {
            if (entry == null) {
              return;
            }
            final DataTrusteeModel existing = byId.putIfAbsent(entry.getKey(), entry.getValue());
            if (existing != null) {
              throw new IllegalStateException(
                  "Duplicate data trustee model id '" + entry.getKey() + "' in " + basePath);
            }
          });
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to read data trustee model directory: " + basePath,
          ex);
    }

    return Collections.unmodifiableMap(byId);
  }

  private Map.Entry<String, DataTrusteeModel> readSingleModelDirectory(Path modelDirectory) {
    final List<Path> jsonFiles = this.listJsonFiles(modelDirectory);
    if (jsonFiles.isEmpty()) {
      log.debug("Skipping non-model directory without JSON file: {}", modelDirectory);
      return null;
    }
    if (jsonFiles.size() > 1) {
      throw new IllegalStateException(
          "Expected exactly one JSON file in model directory: " + modelDirectory);
    }

    final String modelId = modelDirectory.getFileName().toString();
    return this.readSingleModel(modelId, jsonFiles.get(0));
  }

  private Map.Entry<String, DataTrusteeModel> readSingleModel(String modelId, Path jsonFile) {
    try {
      final DataTrusteeWizardRequestDto wizardRequest = this.objectMapper
          .readValue(jsonFile.toFile(), DataTrusteeWizardRequestDto.class);
      final DataTrusteeModel model = this.dataTrusteeModelMapper
          .toModel(this.wizardRequestMapper.toModel(wizardRequest));
      return new AbstractMap.SimpleImmutableEntry<>(modelId, model);
    } catch (final RuntimeException ex) {
      throw new IllegalStateException("Failed to read data trustee model JSON file: " + jsonFile,
          ex);
    }
  }

  private List<Path> listJsonFiles(Path modelDirectory) {
    try (Stream<Path> files = Files.list(modelDirectory)) {
      return files
          .filter(Files::isRegularFile)
          .filter(path -> path.getFileName().toString().endsWith(JSON_FILE_SUFFIX))
          .sorted()
          .toList();
    } catch (final IOException ex) {
      throw new IllegalStateException(
          "Failed to inspect data trustee model directory: " + modelDirectory, ex);
    }
  }

  private Path getBasePath() {
    if (this.backendProperties != null && this.backendProperties.getDataTrusteeModel() != null
        && this.backendProperties.getDataTrusteeModel().getBasePath() != null
        && !this.backendProperties.getDataTrusteeModel().getBasePath().isBlank()) {
      return Path.of(this.backendProperties.getDataTrusteeModel().getBasePath().trim());
    }
    return Path.of("data", "datatrusteemodels");
  }

}
