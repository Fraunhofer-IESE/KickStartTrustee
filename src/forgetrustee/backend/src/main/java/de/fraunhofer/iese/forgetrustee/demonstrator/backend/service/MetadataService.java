
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldChildDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldOption;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataHintDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModelDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleIndexDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleIndexSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataSectionDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.result.ResultFieldDefinitionDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.result.ResultPresentationDefinitionDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.result.ResultSectionDefinitionDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelPresentationDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelSectionPresentationDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for managing metadata about domain modules, including loading from external YAML files,
 * merging technical schema with language-specific text, enriching with validation constraints, and
 * providing access to metadata for API endpoints.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetadataService {

  private static final String OPTION_VALUE_AMBIGUOUS = "AMBIGUOUS";

  private static final String OPTION_VALUE_NONE = "NONE";

  /** Default language for metadata. */
  private static final String DEFAULT_LANG = "de";

  /** Default base package for domain modules. */
  private static final String DEFAULT_DOMAIN_PACKAGE = "de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request";

  /** Folder name for technical metadata schema. */
  private static final String SCHEMA_DIR = "dtm-schema";

  /** Optional index file basename for technical metadata schema. */
  private static final String SCHEMA_INDEX_BASENAME = "dtm-schema";

  /** Optional model index file defining ordered modules. */
  private static final String MODEL_INDEX_BASENAME = "dtm";

  /** Folder name for language-specific metadata. */
  private static final String I18N_DIR = "dtm-i18n";

  /** Folder name for technical result-presentation schema. */
  private static final String RESULT_SCHEMA_DIR = "dtm-result-schema";

  /** Folder name for language-specific result-presentation metadata. */
  private static final String RESULT_I18N_DIR = "dtm-result-i18n";

  /** Backend properties. */
  private final BackendProperties backendProperties;

  /** Service for validation metadata. */
  private final ValidationMetadataService validationMetadataService;

  /** Service for metadata consistency checks. */
  private final MetadataConsistencyCheckService metadataConsistencyCheckService;

  /** Object mapper for generic in-memory conversions. */
  private final ObjectMapper objectMapper = new ObjectMapper();

  /** Object mapper for YAML processing. */
  private final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());

  /** Cache for metadata by language. */
  private final Map<String, Map<String, MetadataModuleDto>> metadataCache = new ConcurrentHashMap<>();

  /** Cache for technical metadata schema. */
  private final AtomicReference<Map<String, MetadataModuleDto>> schemaCache = new AtomicReference<>();

  /** Cache for module IDs mapping. */
  private final AtomicReference<Map<String, String>> moduleIdsCache = new AtomicReference<>();

  /** Cache for result presentation metadata by key viewId|lang. */
  private final Map<String, DataTrusteeModelPresentationDto> resultPresentationCache = new ConcurrentHashMap<>();

  /**
   * List all metadata modules for the specified language.
   * 
   * @param  lang Language code.
   * @return      List of metadata modules.
   */
  public List<MetadataModuleDto> listModules(String lang) {
    final Map<String, Integer> modelOrderById = this.readModelOrderById();
    return this
        .loadMetadata(lang)
        .values()
        .stream()
        .sorted(this.moduleComparator(modelOrderById))
        .toList();
  }

  /**
   * Get a compact metadata index containing only class-level information.
   *
   * @param  lang Language code.
   * @return      Compact metadata index.
   */
  public MetadataModuleIndexSummaryDto getMetadataModuleIndexSummary(String lang) {
    final List<MetadataModuleSummaryDto> modules = this
        .listModules(lang)
        .stream()
        .map(this::toSummary)
        .toList();

    return MetadataModuleIndexSummaryDto.builder().modules(modules).build();
  }

  /**
   * Get the top-level metadata model with modules in effective output order.
   *
   * @param  lang Language code.
   * @return      Metadata model descriptor.
   */
  public MetadataModelDto getMetadataModel(String lang) {
    final List<String> modules = this
        .listModules(lang)
        .stream()
        .map(MetadataModuleDto::getModuleId)
        .filter(Objects::nonNull)
        .toList();

    String modelId = "dtm";
    final Path modelIndexPath = this.getExternalBasePath().resolve(SCHEMA_DIR);
    final Path resolvedModelIndexPath = this.resolvePreferredMetadataFile(modelIndexPath,
        MODEL_INDEX_BASENAME);
    if (resolvedModelIndexPath != null) {
      final MetadataModelDto modelIndex = this.readModelIndex(resolvedModelIndexPath);
      if (modelIndex != null && modelIndex.getModelId() != null
          && !modelIndex.getModelId().isBlank()) {
        modelId = modelIndex.getModelId();
      }
    }

    return MetadataModelDto.builder().modelId(modelId).modules(modules).build();
  }

  /**
   * Get metadata for a specific class by its ID and language.
   * 
   * @param  moduleId Class ID.
   * @param  lang     Language code.
   * @return          Optional containing the metadata class if found.
   */
  public Optional<MetadataModuleDto> getModuleMetadata(String moduleId, String lang) {
    return Optional.ofNullable(this.loadMetadata(lang).get(moduleId));
  }

  /**
   * Get metadata for a specific field within a class by its path and language.
   * 
   * @param  moduleId  Class ID.
   * @param  fieldPath Field path (dot-separated).
   * @param  lang      Language code.
   * @return           Optional containing the metadata field if found.
   */
  public Optional<MetadataFieldDto> getFieldMetadata(String moduleId, String fieldPath,
      String lang) {
    final MetadataModuleDto moduleMetadata = this.loadMetadata(lang).get(moduleId);
    if (moduleMetadata == null) {
      return Optional.empty();
    }

    // Try top-level fields first
    final MetadataFieldDto found = this.findField(moduleMetadata.getFields(), fieldPath);
    if (found != null) {
      return Optional.of(found);
    }

    // If not found, support section-prefixed paths like "sectionId.fieldName..."
    if (moduleMetadata.getSections() != null && !moduleMetadata.getSections().isEmpty()) {
      final String[] parts = fieldPath == null ? new String[0] : fieldPath.split("\\.", 2);
      if (parts.length > 0) {
        final String first = parts[0];
        final String rest = parts.length > 1 ? parts[1] : null;

        // If the first segment matches a section id, search inside that section
        final MetadataSectionDto section = moduleMetadata.getSections().get(first);
        if (section != null) {
          if (rest == null || rest.isBlank()) {
            // asking for the section itself as a field - not supported: return empty
            return Optional.empty();
          }
          final MetadataFieldDto fromSection = this.findField(section.getFields(), rest);
          if (fromSection != null) {
            return Optional.of(fromSection);
          }
        }

        // Fallback: search all sections for the field path as-is (useful if caller omitted the section)
        for (final MetadataSectionDto sec : moduleMetadata.getSections().values()) {
          if (sec == null || sec.getFields() == null) {
            continue;
          }
          final MetadataFieldDto candidate = this.findField(sec.getFields(), fieldPath);
          if (candidate != null) {
            return Optional.of(candidate);
          }
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Get mapping of class names to their IDs.
   * 
   * @return Map of class names to IDs.
   */
  public Map<String, String> getModuleIds() {
    return Collections.unmodifiableMap(this.loadModuleIds());
  }

  /**
   * Get presentation metadata for result views (e.g., compare), merged from schema and i18n.
   *
   * @param  viewId View identifier, for example "compare".
   * @param  lang   Language code.
   * @return        Presentation metadata containing section and field labels.
   */
  public DataTrusteeModelPresentationDto getResultPresentation(String viewId, String lang) {
    final String effectiveViewId = (viewId == null || viewId.isBlank()) ? "compare" : viewId.trim();
    final String langKey = this.normalizeLang(lang);
    final String cacheKey = effectiveViewId + "|" + langKey;
    return this.resultPresentationCache
        .computeIfAbsent(cacheKey, key -> this.readResultPresentation(effectiveViewId, langKey));
  }

  private DataTrusteeModelPresentationDto readResultPresentation(String viewId, String lang) {
    final Path basePath = this.getExternalBasePath();
    final Path schemaDir = basePath.resolve(RESULT_SCHEMA_DIR);
    final Path schemaPath = this.resolvePreferredMetadataFile(schemaDir, viewId);
    if (schemaPath == null) {
      throw new IllegalStateException("Result presentation schema not found for view '" + viewId
          + "' in " + schemaDir);
    }

    final ResultPresentationDefinitionDto schema = this.readResultPresentationFile(schemaPath,
        "schema");
    final ResultPresentationDefinitionDto i18n = this.readResultPresentationI18n(viewId, lang,
        basePath);
    return this.mergeResultPresentation(schema, i18n);
  }

  private ResultPresentationDefinitionDto readResultPresentationI18n(String viewId, String lang,
      Path basePath) {
    final String langKey = this.normalizeLang(lang);
    final Path langDir = basePath.resolve(RESULT_I18N_DIR).resolve(langKey);
    Path langFile = this.resolvePreferredMetadataFile(langDir, viewId);

    if (langFile == null && !DEFAULT_LANG.equals(langKey)) {
      final Path defaultLangDir = basePath.resolve(RESULT_I18N_DIR).resolve(DEFAULT_LANG);
      langFile = this.resolvePreferredMetadataFile(defaultLangDir, viewId);
    }

    if (langFile == null) {
      return null;
    }

    return this.readResultPresentationFile(langFile, "i18n");
  }

  private ResultPresentationDefinitionDto readResultPresentationFile(Path path, String kind) {
    try {
      return this.readValue(path, ResultPresentationDefinitionDto.class);
    } catch (final IOException ex) {
      throw new IllegalStateException(
          "Failed to read result presentation " + kind + " at " + path, ex);
    }
  }

  private DataTrusteeModelPresentationDto mergeResultPresentation(
      ResultPresentationDefinitionDto schema,
      ResultPresentationDefinitionDto i18n) {
    if (schema == null || schema.getSections() == null || schema.getSections().isEmpty()) {
      return DataTrusteeModelPresentationDto.builder().sections(List.of()).build();
    }

    final List<DataTrusteeModelSectionPresentationDto> sections = new java.util.ArrayList<>();
    for (final Map.Entry<String, ResultSectionDefinitionDto> sectionEntry : schema.getSections()
        .entrySet()) {
      final String sectionKey = sectionEntry.getKey();
      final ResultSectionDefinitionDto schemaSection = sectionEntry.getValue();
      if (sectionKey == null || schemaSection == null) {
        continue;
      }

      final ResultSectionDefinitionDto i18nSection = i18n == null || i18n.getSections() == null
          ? null
          : i18n.getSections().get(sectionKey);

      final String sectionLabel = this.firstNonBlank(
          i18nSection != null ? i18nSection.getLabel() : null,
          schemaSection.getLabel());

      final Map<String, String> fieldLabels = new LinkedHashMap<>();
      if (schemaSection.getFields() != null) {
        for (final Map.Entry<String, ResultFieldDefinitionDto> fieldEntry : schemaSection.getFields()
            .entrySet()) {
          final String fieldKey = fieldEntry.getKey();
          if (fieldKey == null || fieldKey.isBlank()) {
            continue;
          }

          final ResultFieldDefinitionDto schemaField = fieldEntry.getValue();
          final ResultFieldDefinitionDto i18nField = i18nSection == null
              || i18nSection.getFields() == null
                  ? null
                  : i18nSection.getFields().get(fieldKey);
          final String fieldLabel = this.firstNonBlank(
              i18nField != null ? i18nField.getLabel() : null,
              schemaField != null ? schemaField.getLabel() : null);

          if (fieldLabel != null) {
            fieldLabels.put(fieldKey, fieldLabel);
          }
        }
      }

      sections
          .add(DataTrusteeModelSectionPresentationDto.builder()
              .key(sectionKey)
              .label(sectionLabel)
              .fields(fieldLabels)
              .build());
    }

    return DataTrusteeModelPresentationDto.builder().sections(List.copyOf(sections)).build();
  }

  private String firstNonBlank(String preferred, String fallback) {
    if (preferred != null && !preferred.isBlank()) {
      return preferred;
    }
    if (fallback != null && !fallback.isBlank()) {
      return fallback;
    }
    return null;
  }

  /**
   * Load metadata for the specified language, using cache if available.
   * 
   * @param  lang Language code.
   * @return      Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> loadMetadata(String lang) {
    final String langKey = this.normalizeLang(lang);
    return this.metadataCache.computeIfAbsent(langKey, this::readMetadataByLang);
  }

  /**
   * Read metadata for the specified language from external source.
   *
   * @param  lang Language code.
   * @return      Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> readMetadataByLang(String lang) {
    final Path basePath = this.getExternalBasePath();
    final Map<String, MetadataModuleDto> schema = this.loadSchema(basePath);
    final Map<String, MetadataModuleDto> schemaCopy = this.deepCopySchema(schema);
    final Map<String, MetadataModuleDto> i18n = this.readI18nFromExternal(lang, basePath);
    if (this.isConsistencyCheckEnabled()) {
      this.metadataConsistencyCheckService
          .logMetadataStructureMismatches(this.normalizeLang(lang), schema, i18n);
    } else if (log.isDebugEnabled()) {
      log.debug("Metadata check disabled via configuration");
    }
    final Map<String, MetadataModuleDto> merged = this.mergeI18nIntoSchema(schemaCopy, i18n);
    return this.enrichWithSizeConstraints(merged);
  }

  /**
   * Load technical metadata schema, using cache if available.
   *
   * @param  basePath Base path to metadata files.
   * @return          Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> loadSchema(Path basePath) {
    final Map<String, MetadataModuleDto> cached = this.schemaCache.get();
    if (cached != null) {
      return cached;
    }
    final Map<String, MetadataModuleDto> schema = this.readSchemaFromExternal(basePath);
    this.schemaCache.compareAndSet(null, schema);
    return this.schemaCache.get();
  }

  /**
   * Read technical metadata schema from external files.
   *
   * @param  basePath Base path to metadata files.
   * @return          Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> readSchemaFromExternal(Path basePath) {
    final Path indexPath = this.resolvePreferredMetadataFile(basePath, SCHEMA_INDEX_BASENAME);
    if (indexPath != null) {
      return this.readMetadataIndex(indexPath, "schema");
    }

    final Path schemaDir = basePath.resolve(SCHEMA_DIR);
    if (!Files.exists(schemaDir)) {
      throw new IllegalStateException("Metadata schema not found at " + schemaDir);
    }

    final Map<String, MetadataModuleDto> byId = new LinkedHashMap<>();
    try (Stream<Path> files = Files.list(schemaDir)) {
      this.listMetadataModuleFiles(files)
          .forEach(path -> this.readMetadataFile(path, byId, "schema"));
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to read metadata schema", ex);
    }
    if (byId.isEmpty()) {
      throw new IllegalStateException("No metadata modules found in schema");
    }
    return this.applyModelOrder(byId, schemaDir);
  }

  /**
   * Read language-specific metadata from external files.
   *
   * @param  lang     Language code.
   * @param  basePath Base path to metadata files.
   * @return          Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> readI18nFromExternal(String lang, Path basePath) {
    final String langKey = this.normalizeLang(lang);
    final Path langDir = basePath.resolve(I18N_DIR).resolve(langKey);
    if (!Files.exists(langDir) && !DEFAULT_LANG.equals(langKey)) {
      return this.readI18nFromExternal(DEFAULT_LANG, basePath);
    }
    if (!Files.exists(langDir)) {
      throw new IllegalStateException("Metadata language resources not found for lang " + langKey);
    }

    final Map<String, MetadataModuleDto> byId = new LinkedHashMap<>();
    try (Stream<Path> files = Files.list(langDir)) {
      this.listMetadataModuleFiles(files)
          .forEach(path -> this.readMetadataFile(path, byId, langKey));
    } catch (final IOException ex) {
      throw new IllegalStateException(
          "Failed to read metadata language resources for lang " + langKey, ex);
    }
    if (byId.isEmpty()) {
      throw new IllegalStateException("No metadata modules found for lang " + langKey);
    }
    return byId;
  }

  private Map<String, MetadataModuleDto> applyModelOrder(Map<String, MetadataModuleDto> byId,
      Path schemaDir) {
    final Path modelIndexPath = this.resolvePreferredMetadataFile(schemaDir, MODEL_INDEX_BASENAME);
    if (modelIndexPath == null) {
      return byId;
    }

    final MetadataModelDto modelIndex = this.readModelIndex(modelIndexPath);
    if (modelIndex == null || modelIndex.getModules() == null
        || modelIndex.getModules().isEmpty()) {
      return byId;
    }

    final Map<String, MetadataModuleDto> ordered = new LinkedHashMap<>();
    for (final String moduleId : modelIndex.getModules()) {
      if (moduleId == null || moduleId.isBlank()) {
        continue;
      }
      final MetadataModuleDto module = byId.get(moduleId);
      if (module == null) {
        log.warn("Metadata model index references unknown moduleId '{}'", moduleId);
        continue;
      }
      ordered.put(moduleId, module);
    }

    for (final Map.Entry<String, MetadataModuleDto> entry : byId.entrySet()) {
      ordered.putIfAbsent(entry.getKey(), entry.getValue());
    }
    return ordered;
  }

  private MetadataModelDto readModelIndex(Path modelIndexPath) {
    try {
      return this.readValue(modelIndexPath, MetadataModelDto.class);
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to read metadata model index at " + modelIndexPath,
          ex);
    }
  }

  /**
   * Deep-copy schema metadata to avoid cross-language mutation.
   *
   * @param  schema Schema metadata map.
   * @return        Deep copy of schema map.
   */
  private Map<String, MetadataModuleDto> deepCopySchema(Map<String, MetadataModuleDto> schema) {
    if (schema == null) {
      return null;
    }
    return this.objectMapper
        .convertValue(schema, new TypeReference<Map<String, MetadataModuleDto>>() {
        });
  }

  /**
   * Merge language metadata into the technical schema.
   *
   * @param  schema Schema metadata map (target).
   * @param  i18n   Language metadata map (source).
   * @return        Merged metadata map.
   */
  private Map<String, MetadataModuleDto> mergeI18nIntoSchema(Map<String, MetadataModuleDto> schema,
      Map<String, MetadataModuleDto> i18n) {
    if (schema == null || schema.isEmpty()) {
      return schema;
    }
    if (i18n == null || i18n.isEmpty()) {
      return schema;
    }
    for (final Map.Entry<String, MetadataModuleDto> entry : schema.entrySet()) {
      final MetadataModuleDto target = entry.getValue();
      if (target == null) {
        continue;
      }
      final MetadataModuleDto source = i18n.get(entry.getKey());
      if (source == null) {
        continue;
      }
      this.mergeClassText(target, source);
      // structure mismatch logging is done once before merge in readMetadataByLang(...)
    }
    return schema;
  }

  private void mergeClassText(MetadataModuleDto target, MetadataModuleDto source) {
    this.applyText(target::setHeading, source.getHeading());
    this.applyText(target::setMenuPoint, source.getMenuPoint());
    this.applyText(target::setLabel, source.getLabel());
    this.applyText(target::setTooltip, source.getTooltip());
    this.applyText(target::setHelpText, source.getHelpText());
    if (source.getExamples() != null) {
      target.setExamples(source.getExamples());
    }
    this.mergeHints(target, source);

    this.mergeFieldMaps(target.getFields(), source.getFields());

    if (target.getSections() != null && source.getSections() != null) {
      for (final Map.Entry<String, MetadataSectionDto> entry : target.getSections().entrySet()) {
        final MetadataSectionDto targetSection = entry.getValue();
        if (targetSection == null) {
          continue;
        }
        final MetadataSectionDto sourceSection = source.getSections().get(entry.getKey());
        if (sourceSection == null) {
          continue;
        }
        this.mergeSectionText(targetSection, sourceSection);
      }
    }
  }

  private void mergeHints(MetadataModuleDto target, MetadataModuleDto source) {
    if (target == null || source == null || source.getHints() == null) {
      return;
    }

    if (target.getHints() == null || target.getHints().isEmpty()) {
      target.setHints(source.getHints());
      return;
    }

    final Map<String, MetadataHintDto> sourceByName = new LinkedHashMap<>();
    for (final MetadataHintDto sourceHint : source.getHints()) {
      if (sourceHint != null && sourceHint.getName() != null) {
        sourceByName.put(sourceHint.getName(), sourceHint);
      }
    }

    for (final MetadataHintDto targetHint : target.getHints()) {
      if (targetHint == null || targetHint.getName() == null) {
        continue;
      }
      final MetadataHintDto sourceHint = sourceByName.remove(targetHint.getName());
      if (sourceHint == null) {
        continue;
      }

      this.applyText(targetHint::setText, sourceHint.getText());
      if (sourceHint.getVisibleBy() != null) {
        targetHint.setVisibleBy(sourceHint.getVisibleBy());
      }
    }

    if (!sourceByName.isEmpty()) {
      for (final MetadataHintDto additionalHint : sourceByName.values()) {
        target.getHints().add(additionalHint);
      }
    }
  }

  private void mergeHints(MetadataSectionDto target, MetadataSectionDto source) {
    if (target == null || source == null || source.getHints() == null) {
      return;
    }

    if (target.getHints() == null || target.getHints().isEmpty()) {
      target.setHints(source.getHints());
      return;
    }

    final Map<String, MetadataHintDto> sourceByName = new LinkedHashMap<>();
    for (final MetadataHintDto sourceHint : source.getHints()) {
      if (sourceHint != null && sourceHint.getName() != null) {
        sourceByName.put(sourceHint.getName(), sourceHint);
      }
    }

    for (final MetadataHintDto targetHint : target.getHints()) {
      if (targetHint == null || targetHint.getName() == null) {
        continue;
      }
      final MetadataHintDto sourceHint = sourceByName.remove(targetHint.getName());
      if (sourceHint == null) {
        continue;
      }

      this.applyText(targetHint::setText, sourceHint.getText());
      if (sourceHint.getVisibleBy() != null) {
        targetHint.setVisibleBy(sourceHint.getVisibleBy());
      }
    }

    if (!sourceByName.isEmpty()) {
      for (final MetadataHintDto additionalHint : sourceByName.values()) {
        target.getHints().add(additionalHint);
      }
    }
  }

  private void mergeSectionText(MetadataSectionDto target, MetadataSectionDto source) {
    this.applyText(target::setHeading, source.getHeading());
    this.applyText(target::setLabel, source.getLabel());
    this.applyText(target::setTooltip, source.getTooltip());
    this.applyText(target::setHelpText, source.getHelpText());
    if (source.getExamples() != null) {
      target.setExamples(source.getExamples());
    }
    this.mergeHints(target, source);
    if (source.getVisibleBy() != null) {
      target.setVisibleBy(source.getVisibleBy());
    }
    this.mergeFieldMaps(target.getFields(), source.getFields());
    this.mergeSectionMaps(target.getSubsections(), source.getSubsections());
  }

  private void mergeSectionMaps(Map<String, MetadataSectionDto> target,
      Map<String, MetadataSectionDto> source) {
    if (target == null || source == null) {
      return;
    }
    for (final Map.Entry<String, MetadataSectionDto> entry : target.entrySet()) {
      final MetadataSectionDto targetSection = entry.getValue();
      if (targetSection == null) {
        continue;
      }
      final MetadataSectionDto sourceSection = source.get(entry.getKey());
      if (sourceSection == null) {
        continue;
      }
      this.mergeSectionText(targetSection, sourceSection);
    }
  }

  private void mergeFieldMaps(Map<String, MetadataFieldDto> target,
      Map<String, MetadataFieldDto> source) {
    if (target == null || source == null) {
      return;
    }
    for (final Map.Entry<String, MetadataFieldDto> entry : target.entrySet()) {
      final MetadataFieldDto targetField = entry.getValue();
      if (targetField == null) {
        continue;
      }
      final MetadataFieldDto sourceField = source.get(entry.getKey());
      if (sourceField == null) {
        continue;
      }
      this.mergeFieldText(targetField, sourceField);
    }
  }

  /**
   * Merge child-level field maps (one nested level). Child DTO is MetadataFieldChildDto which does
   * not allow further nesting.
   */
  private void mergeChildFieldMaps(Map<String, MetadataFieldChildDto> target,
      Map<String, MetadataFieldChildDto> source) {
    if (target == null || source == null) {
      return;
    }
    for (final Map.Entry<String, MetadataFieldChildDto> entry : target.entrySet()) {
      final MetadataFieldChildDto targetField = entry.getValue();
      if (targetField == null) {
        continue;
      }
      final MetadataFieldChildDto sourceField = source.get(entry.getKey());
      if (sourceField == null) {
        continue;
      }
      this.mergeChildFieldText(targetField, sourceField);
    }
  }

  private void mergeChildFieldText(MetadataFieldChildDto target, MetadataFieldChildDto source) {
    this.applyText(target::setHeading, source.getHeading());
    this.applyText(target::setLabel, source.getLabel());
    this.applyText(target::setPlaceholder, source.getPlaceholder());
    this.applyText(target::setTooltip, source.getTooltip());
    this.applyText(target::setHelpText, source.getHelpText());
    this.applyText(target::setCallToAction, source.getCallToAction());
    if (source.getExamples() != null) {
      target.setExamples(source.getExamples());
    }
    this.mergeOptions(target.getOptions(), source.getOptions());
    this.mergeSpecialOptionLabels(target, source.getOptions());
    // no further recursion - children do not have nested fields
  }

  private void mergeFieldText(MetadataFieldDto target, MetadataFieldDto source) {
    this.applyText(target::setHeading, source.getHeading());
    this.applyText(target::setLabel, source.getLabel());
    this.applyText(target::setPlaceholder, source.getPlaceholder());
    this.applyText(target::setTooltip, source.getTooltip());
    this.applyText(target::setHelpText, source.getHelpText());
    this.applyText(target::setCallToAction, source.getCallToAction());
    if (source.getExamples() != null) {
      target.setExamples(source.getExamples());
    }
    this.mergeOptions(target.getOptions(), source.getOptions());
    this.mergeSpecialOptionLabels(target, source.getOptions());

    // Merge one level of nested child fields. Child maps use MetadataFieldChildDto which
    // does not contain further nested fields, so use a dedicated merge helper.
    this.mergeChildFieldMaps(target.getFields(), source.getFields());
  }

  private void mergeOptions(List<MetadataFieldOption> target, List<MetadataFieldOption> source) {
    if (target == null || source == null) {
      return;
    }
    final Map<String, MetadataFieldOption> sourceByValue = new LinkedHashMap<>();
    for (final MetadataFieldOption option : source) {
      if (option != null && option.getValue() != null) {
        sourceByValue.put(option.getValue(), option);
      }
    }
    for (final MetadataFieldOption targetOption : target) {
      if (targetOption == null || targetOption.getValue() == null) {
        continue;
      }
      final MetadataFieldOption sourceOption = sourceByValue.get(targetOption.getValue());
      if (sourceOption == null) {
        continue;
      }
      this.applyText(targetOption::setLabel, sourceOption.getLabel());
      if (sourceOption.getAdditionalDetails() != null) {
        targetOption.setAdditionalDetails(sourceOption.getAdditionalDetails());
      }
      if (sourceOption.getDisableAdditionalDetails() != null) {
        targetOption.setDisableAdditionalDetails(sourceOption.getDisableAdditionalDetails());
      }
    }
  }

  private void applyText(java.util.function.Consumer<String> setter, String value) {
    if (value != null) {
      setter.accept(value);
    }
  }

  private void mergeSpecialOptionLabels(MetadataFieldDto target,
      List<MetadataFieldOption> sourceOptions) {
    if (target == null) {
      return;
    }
    if (Boolean.TRUE.equals(target.getAllowUnknown())) {
      this.applyText(target::setAllowUnknownLabel,
          this.resolveSpecialOptionLabel(sourceOptions, OPTION_VALUE_AMBIGUOUS,
              this.defaultSpecialOptionLabel(OPTION_VALUE_AMBIGUOUS)));
    }
    if (Boolean.TRUE.equals(target.getAllowNoAnswer())) {
      this.applyText(target::setAllowNoAnswerLabel,
          this.resolveSpecialOptionLabel(sourceOptions, OPTION_VALUE_NONE,
              this.defaultSpecialOptionLabel(OPTION_VALUE_NONE)));
    }
  }

  private void mergeSpecialOptionLabels(MetadataFieldChildDto target,
      List<MetadataFieldOption> sourceOptions) {
    if (target == null) {
      return;
    }
    if (Boolean.TRUE.equals(target.getAllowUnknown())) {
      this.applyText(target::setAllowUnknownLabel,
          this.resolveSpecialOptionLabel(sourceOptions, OPTION_VALUE_AMBIGUOUS,
              this.defaultSpecialOptionLabel(OPTION_VALUE_AMBIGUOUS)));
    }
    if (Boolean.TRUE.equals(target.getAllowNoAnswer())) {
      this.applyText(target::setAllowNoAnswerLabel,
          this.resolveSpecialOptionLabel(sourceOptions, OPTION_VALUE_NONE,
              this.defaultSpecialOptionLabel(OPTION_VALUE_NONE)));
    }
  }

  private String resolveSpecialOptionLabel(List<MetadataFieldOption> options, String value,
      String defaultLabel) {
    if (options != null) {
      for (final MetadataFieldOption option : options) {
        if (option != null && value.equals(option.getValue()) && option.getLabel() != null
            && !option.getLabel().isBlank()) {
          return option.getLabel();
        }
      }
    }
    return defaultLabel;
  }

  private String defaultSpecialOptionLabel(String value) {
    if (OPTION_VALUE_AMBIGUOUS.equals(value)) {
      return "Unklar";
    }
    if (OPTION_VALUE_NONE.equals(value)) {
      return "Keine Angabe";
    }
    return value;
  }

  /**
   * Read metadata index file and collect modules by their IDs.
   *
   * @param  indexPath Path to the index file.
   * @param  lang      Language code.
   * @return           Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> readMetadataIndex(Path indexPath, String lang) {
    try {
      final MetadataModuleIndexDto index = this
          .readValue(indexPath, MetadataModuleIndexDto.class);
      return this.collectById(index, lang);
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to read metadata for lang " + lang, ex);
    }
  }

  /**
   * Collect metadata modules by their IDs from the index.
   * 
   * @param  index Metadata index DTO.
   * @param  lang  Language code.
   * @return       Map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> collectById(MetadataModuleIndexDto index, String lang) {
    final Map<String, MetadataModuleDto> byId = new LinkedHashMap<>();
    if (index != null && index.getModules() != null) {
      for (final MetadataModuleDto item : index.getModules()) {
        if (item != null && item.getModuleId() != null) {
          byId.put(item.getModuleId(), item);
        }
      }
    }
    if (byId.isEmpty()) {
      throw new IllegalStateException("No metadata modules found for lang " + lang);
    }
    return byId;
  }

  /**
   * Read a single metadata file and add it to the collection by ID.
   * 
   * @param path Path to the metadata file.
   * @param byId Map to collect metadata modules by their IDs.
   * @param lang Language code.
   */
  private void readMetadataFile(Path path, Map<String, MetadataModuleDto> byId, String lang) {
    try {
      final MetadataModuleDto item = this.readValue(path, MetadataModuleDto.class);
      if (item != null && item.getModuleId() != null) {
        byId.put(item.getModuleId(), item);
      }
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to read metadata for lang " + lang, ex);
    }
  }

  /**
   * Enrich metadata modules with size constraints from domain modules.
   * 
   * @param  byId Map of class IDs to metadata modules.
   * @return      Enriched map of class IDs to metadata modules.
   */
  private Map<String, MetadataModuleDto> enrichWithSizeConstraints(
      Map<String, MetadataModuleDto> byId) {
    if (byId == null || byId.isEmpty()) {
      return byId;
    }

    final Map<String, String> moduleIds = this.loadModuleIds();
    if (moduleIds == null || moduleIds.isEmpty()) {
      return byId;
    }

    final Map<String, String> moduleIdToClassName = new LinkedHashMap<>();
    for (final Map.Entry<String, String> entry : moduleIds.entrySet()) {
      if (entry.getKey() != null && entry.getValue() != null) {
        moduleIdToClassName.put(entry.getValue(), entry.getKey());
      }
    }

    final String domainBasePackage = this.getDomainBasePackage();
    for (final MetadataModuleDto classDto : byId.values()) {
      if (classDto == null || classDto.getModuleId() == null) {
        continue;
      }
      final String className = moduleIdToClassName.get(classDto.getModuleId());
      if (className == null) {
        continue;
      }
      final Class<?> domainClass = this.resolveDomainClass(domainBasePackage, className);
      if (domainClass == null) {
        continue;
      }
      this.enrichFieldsFromClass(classDto.getFields(), domainClass);

      // Also enrich fields inside sections and nested subsections.
      this.enrichSectionsFromClass(classDto.getSections(), domainClass);
    }

    return byId;
  }

  /**
   * Recursively enrich metadata fields with size constraints from the domain class. Note: fields
   * may contain a single nested level of child fields (MetadataFieldChildDto).
   */
  private void enrichFieldsFromClass(Map<String, MetadataFieldDto> fields, Class<?> domainClass) {
    if (fields == null || fields.isEmpty() || domainClass == null) {
      return;
    }
    for (final Map.Entry<String, MetadataFieldDto> entry : fields.entrySet()) {
      final String fieldName = entry.getKey();
      final MetadataFieldDto metadataField = entry.getValue();
      if (fieldName == null || metadataField == null) {
        continue;
      }
      final String effectiveFieldName = this.effectivePropertyName(metadataField.getName(), fieldName);
      final Optional<Size> size = this.validationMetadataService
          .getSizeConstraint(domainClass, effectiveFieldName);
      if (size.isPresent()) {
        final Size constraint = size.get();
        if (metadataField.getMinLength() == null && constraint.min() > 0) {
          metadataField.setMinLength(constraint.min());
        }
        if (metadataField.getMaxLength() == null && constraint.max() < Integer.MAX_VALUE) {
          metadataField.setMaxLength(constraint.max());
        }
      }

      // evaluate @Schema(requiredMode=...) via ValidationMetadataService and set required flag if not present
      try {
        final Optional<Schema.RequiredMode> requiredMode = this.validationMetadataService
            .getSchemaRequiredMode(domainClass, effectiveFieldName);
        if (requiredMode.isPresent() && metadataField.getRequired() == null) {
          final Schema.RequiredMode mode = requiredMode.get();
          if (mode == Schema.RequiredMode.REQUIRED) {
            metadataField.setRequired(Boolean.TRUE);
          } else if (mode == Schema.RequiredMode.NOT_REQUIRED) {
            metadataField.setRequired(Boolean.FALSE);
          }
          // If AUTO: do not override
        }
      } catch (final Exception ex) {
        // Ignore any unexpected errors while reading schema annotations
      }

      // Enrich one level of child fields if present
      final Map<String, MetadataFieldChildDto> childFields = metadataField.getFields();
      if (childFields != null && !childFields.isEmpty()) {
        // Determine the class that owns the child properties: prefer the declared type of the parent property
        final Optional<Class<?>> parentFieldTypeOpt = this.validationMetadataService
            .getPropertyType(domainClass, effectiveFieldName);

        for (final Map.Entry<String, MetadataFieldChildDto> childEntry : childFields.entrySet()) {
          final String childName = childEntry.getKey();
          final MetadataFieldChildDto childDto = childEntry.getValue();
          if (childName == null || childDto == null) {
            continue;
          }
          final String effectiveChildName = this.effectivePropertyName(childDto.getName(), childName);

          // Resolve effective owner class for the child property. Fallback to domainClass when unknown.
          Class<?> ownerClass = domainClass;
          if (parentFieldTypeOpt.isPresent()) {
            final Class<?> parentType = parentFieldTypeOpt.get();
            // If parentType is a Collection or Map, try to resolve generic type from declared field
            if (java.util.Collection.class.isAssignableFrom(parentType)
                || java.util.Map.class.isAssignableFrom(parentType)) {
              final Optional<Class<?>> resolvedOpt = this.validationMetadataService
                  .getGenericParameterType(domainClass, effectiveFieldName);
              if (resolvedOpt.isPresent()) {
                ownerClass = resolvedOpt.get();
              }
            } else {
              ownerClass = parentType;
            }
          }

          // Retrieve metadata about child before applying changes so we can log what's found
          final Optional<Size> childSize = this.validationMetadataService
              .getSizeConstraint(ownerClass, effectiveChildName);

          Optional<Schema.RequiredMode> requiredModeOpt = Optional.empty();
          try {
            requiredModeOpt = this.validationMetadataService
                .getSchemaRequiredMode(ownerClass, effectiveChildName);
          } catch (final Exception ex) {
            // ignore any errors while probing schema annotations
          }

          // Debug logging: show resolution and whether annotations were found
          if (log.isDebugEnabled()) {
            log
                .debug(
                    "enrich child: parentField='{}', child='{}', parentType={}, ownerClass={}, sizePresent={}, schemaPresent={}",
                    effectiveFieldName, effectiveChildName,
                    parentFieldTypeOpt.map(Class::getName).orElse("<none>"),
                    ownerClass != null ? ownerClass.getName() : "<null>", childSize.isPresent(),
                    requiredModeOpt.isPresent());
          }

          if (childSize.isPresent()) {
            final Size constraint = childSize.get();
            if (childDto.getMinLength() == null && constraint.min() > 0) {
              childDto.setMinLength(constraint.min());
            }
            if (childDto.getMaxLength() == null && constraint.max() < Integer.MAX_VALUE) {
              childDto.setMaxLength(constraint.max());
            }
          }

          // evaluate @Schema(requiredMode=...) for child against the resolved owner class
          try {
            if (requiredModeOpt.isPresent() && childDto.getRequired() == null) {
              final Schema.RequiredMode mode = requiredModeOpt.get();
              if (mode == Schema.RequiredMode.REQUIRED) {
                childDto.setRequired(Boolean.TRUE);
              } else if (mode == Schema.RequiredMode.NOT_REQUIRED) {
                childDto.setRequired(Boolean.FALSE);
              }
            }
          } catch (final Exception ex) {
            // ignore
          }
        }
      }
    }
  }

  private void enrichSectionsFromClass(Map<String, MetadataSectionDto> sections,
      Class<?> domainClass) {
    if (sections == null || sections.isEmpty() || domainClass == null) {
      return;
    }
    for (final MetadataSectionDto section : sections.values()) {
      if (section == null) {
        continue;
      }
      this.enrichFieldsFromClass(section.getFields(), domainClass);
      this.enrichSectionsFromClass(section.getSubsections(), domainClass);
    }
  }

  /**
   * Resolve the domain class by its name and base package.
   * 
   * @param  basePackage Base package for domain modules.
   * @param  className   Class name to resolve.
   * @return             Resolved class, or null if not found.
   */
  private Class<?> resolveDomainClass(String basePackage, String className) {
    final String qualifiedName = className.contains(".") ? className
        : basePackage + "." + className;
    try {
      return Class.forName(qualifiedName);
    } catch (final ClassNotFoundException ex) {
      return null;
    }
  }

  /**
   * Get the base package for domain modules from backend properties or use default.
   * 
   * @return Base package for domain modules.
   */
  private String getDomainBasePackage() {
    if (this.backendProperties != null && this.backendProperties.getMetadata() != null
        && this.backendProperties.getMetadata().getDomainBasePackage() != null
        && !this.backendProperties.getMetadata().getDomainBasePackage().isBlank()) {
      return this.backendProperties.getMetadata().getDomainBasePackage().trim();
    }
    return DEFAULT_DOMAIN_PACKAGE;
  }

  /**
   * Load class IDs mapping from external source, using cache if available.
   * 
   * @return Map of class names to their IDs.
   */
  private Map<String, String> loadModuleIds() {
    final Map<String, String> cached = this.moduleIdsCache.get();
    if (cached != null) {
      return cached;
    }
    final Map<String, MetadataModuleDto> schema = this.loadSchema(this.getExternalBasePath());
    if (schema == null || schema.isEmpty()) {
      return Collections.emptyMap();
    }

    final Map<String, String> ids = new LinkedHashMap<>();
    for (final MetadataModuleDto module : schema.values()) {
      if (module == null || module.getModuleId() == null || module.getModuleId().isBlank()) {
        continue;
      }
      if (module.getRequestDtoClassName() == null || module.getRequestDtoClassName().isBlank()) {
        continue;
      }
      ids.put(module.getRequestDtoClassName().trim(), module.getModuleId());
    }

    this.moduleIdsCache.compareAndSet(null, ids);
    return this.moduleIdsCache.get();
  }

  /**
   * Get the external base path for metadata files from backend properties.
   * 
   * @return Path to the external base directory.
   */
  private Path getExternalBasePath() {
    final String basePath = this.backendProperties.getMetadata().getBasePath();

    final Path path = Path.of(basePath.trim());
    if (!Files.exists(path)) {
      throw new IllegalStateException("Metadata base path not found: " + path);
    }
    return path;
  }

  /**
   * Normalize the language code to a standard format.
   * 
   * @param  lang Language code.
   * @return      Normalized language code.
   */
  private String normalizeLang(String lang) {
    if (lang == null || lang.isBlank()) {
      return DEFAULT_LANG;
    }
    return lang.trim().toLowerCase(Locale.ROOT);
  }

  /**
   * Find a metadata field by its dot-separated path within a map of fields. Supports one level of
   * nesting: "parent.child" where parent is a top-level field and child is a MetadataFieldChildDto
   * under parent's fields map. Paths with more than 2 segments are not supported and return null.
   *
   * @param  fields    Map of field names to metadata field DTOs.
   * @param  fieldPath Dot-separated path to the desired field.
   * @return           Metadata field DTO if found, otherwise null.
   */
  private MetadataFieldDto findField(Map<String, MetadataFieldDto> fields, String fieldPath) {
    if (fields == null || fieldPath == null || fieldPath.isBlank()) {
      return null;
    }
    final String[] parts = fieldPath.split("\\.");
    if (parts.length == 0) {
      return null;
    }

    // First segment: top-level field
    final MetadataFieldDto top = fields.get(parts[0]);
    if (top == null) {
      return null;
    }
    if (parts.length == 1) {
      return top;
    }

    // Second segment: single nested child level only
    final Map<String, MetadataFieldChildDto> childFields = top.getFields();
    if (childFields == null) {
      return null;
    }
    final MetadataFieldChildDto child = childFields.get(parts[1]);
    if (child == null) {
      return null;
    }

    // Convert child DTO to a full MetadataFieldDto (no further nested fields)
    return MetadataFieldDto
        .builder()
        .name(child.getName())
        .label(child.getLabel())
        .placeholder(child.getPlaceholder())
        .tooltip(child.getTooltip())
        .helpText(child.getHelpText())
        .callToAction(child.getCallToAction())
        .examples(child.getExamples())
        .minLength(child.getMinLength())
        .maxLength(child.getMaxLength())
        // no nested fields for child
        .type(child.getType())
        .multiple(child.getMultiple())
        .required(child.getRequired())
        .requiredBy(child.getRequiredBy())
        .disabled(child.getDisabled())
        .readonly(child.getReadonly())
        .allowUnknown(child.getAllowUnknown())
        .allowUnknownLabel(child.getAllowUnknownLabel())
        .allowNoAnswer(child.getAllowNoAnswer())
        .allowNoAnswerLabel(child.getAllowNoAnswerLabel())
        .options(child.getOptions())
        .visible(child.getVisible())
        .visibleBy(child.getVisibleBy())
        .allowedValuesMode(child.getAllowedValuesMode())
        .allowedValuesBy(child.getAllowedValuesBy())
        .build();
  }

  private boolean isConsistencyCheckEnabled() {
    if (this.backendProperties == null || this.backendProperties.getMetadata() == null) {
      return true;
    }
    final Boolean enabled = this.backendProperties.getMetadata().getConsistencyCheckEnabled();
    return enabled == null || enabled.booleanValue();
  }

  private String effectivePropertyName(String metadataName, String fallbackName) {
    if (metadataName != null && !metadataName.isBlank()) {
      return metadataName;
    }
    return fallbackName;
  }

  private MetadataModuleSummaryDto toSummary(MetadataModuleDto metadataClass) {
    if (metadataClass == null) {
      return null;
    }
    return MetadataModuleSummaryDto
        .builder()
        .moduleId(metadataClass.getModuleId())
        .order(metadataClass.getOrder())
        .label(metadataClass.getLabel())
        .heading(metadataClass.getHeading())
        .menuPoint(metadataClass.getMenuPoint())
        .tooltip(metadataClass.getTooltip())
        .helpText(metadataClass.getHelpText())
        .callToAction(metadataClass.getCallToAction())
        .examples(metadataClass.getExamples())
        .build();
  }

  private Map<String, Integer> readModelOrderById() {
    final Path modelDir = this.getExternalBasePath().resolve(SCHEMA_DIR);
    final Path modelIndexPath = this.resolvePreferredMetadataFile(modelDir, MODEL_INDEX_BASENAME);
    if (modelIndexPath == null) {
      return Collections.emptyMap();
    }
    final MetadataModelDto modelIndex = this.readModelIndex(modelIndexPath);
    if (modelIndex == null || modelIndex.getModules() == null
        || modelIndex.getModules().isEmpty()) {
      return Collections.emptyMap();
    }

    final Map<String, Integer> orderById = new LinkedHashMap<>();
    int index = 0;
    for (final String moduleId : modelIndex.getModules()) {
      if (moduleId != null && !moduleId.isBlank()) {
        orderById.putIfAbsent(moduleId, Integer.valueOf(index));
        index++;
      }
    }
    return orderById;
  }

  private Comparator<MetadataModuleDto> moduleComparator(Map<String, Integer> modelOrderById) {
    return Comparator
        .comparing(MetadataModuleDto::getOrder, Comparator.nullsLast(Integer::compareTo))
        .thenComparing(module -> modelOrderById.get(module.getModuleId()),
            Comparator.nullsLast(Integer::compareTo))
        .thenComparing(MetadataModuleDto::getModuleId, Comparator.nullsLast(String::compareTo));
  }

  private <T> T readValue(Path path, Class<T> targetType) throws IOException {
    try (InputStream inputStream = Files.newInputStream(path)) {
      return this.mapperFor(path).readValue(inputStream, targetType);
    }
  }

  private ObjectMapper mapperFor(Path path) {
    return this.isYaml(path) ? this.yamlObjectMapper : this.objectMapper;
  }

  private boolean isYaml(Path path) {
    final String fileName = this.fileName(path);
    return fileName.endsWith(".yaml") || fileName.endsWith(".yml");
  }

  private boolean isSupportedMetadataFile(Path path) {
    final String fileName = this.fileName(path);
    return fileName.endsWith(".yaml") || fileName.endsWith(".yml");
  }

  private String fileName(Path path) {
    return path.getFileName().toString().toLowerCase(Locale.ROOT);
  }

  private boolean isModelIndexFile(Path path) {
    final String fileName = this.fileName(path);
    return fileName.equals(MODEL_INDEX_BASENAME + ".yaml")
        || fileName.equals(MODEL_INDEX_BASENAME + ".yml");
  }

  private List<Path> listMetadataModuleFiles(Stream<Path> files) {
    return files
        .filter(Files::isRegularFile)
        .filter(this::isSupportedMetadataFile)
        .filter(path -> !this.isModelIndexFile(path))
        .sorted(this.metadataFileOrder())
        .collect(Collectors.toList());
  }

  private Comparator<Path> metadataFileOrder() {
    return Comparator
        .comparing(this::baseName)
        .thenComparingInt(this::extensionPriority)
        .thenComparing(path -> path.getFileName().toString());
  }

  private String baseName(Path path) {
    final String fileName = this.fileName(path);
    final int index = fileName.lastIndexOf('.');
    if (index <= 0) {
      return fileName;
    }
    return fileName.substring(0, index);
  }

  private int extensionPriority(Path path) {
    final String fileName = this.fileName(path);
    if (fileName.endsWith(".yml")) {
      return 0;
    }
    if (fileName.endsWith(".yaml")) {
      return 1;
    }
    return -1;
  }

  private Path resolvePreferredMetadataFile(Path directory, String baseName) {
    final Path yml = directory.resolve(baseName + ".yml");
    final Path yaml = directory.resolve(baseName + ".yaml");
    if (Files.exists(yaml)) {
      return yaml;
    }
    if (Files.exists(yml)) {
      return yml;
    }
    return null;
  }
}
