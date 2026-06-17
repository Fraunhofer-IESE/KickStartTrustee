
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.impl;
/* Created by chwalek on 20.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.DatabaseConnection;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.ServiceRunDatabase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileReader_ServiceRun implements DatabaseConnection {

  private final ServiceRunDatabase repo;

  @Override
  public String getBaseModelTTL() {
    final String baseUri = this.repo.getBaseUri();
    return this.getModelTTL(baseUri);
  }

  /**
   * Holt zur gegebenen URI den Pfad aus der "DB", liest die TTL-Datei unter diesem Pfad als UTF-8
   * Text ein und gibt sie zurück.
   */
  @Override
  public String getModelTTL(String uri) {
    final String pathStr = this.repo.getOntologyFilePath(uri);

    final Path path = Path.of(pathStr);
    if (!Files.exists(path)) {
      throw new IllegalStateException("TTL-Datei existiert nicht: " + path);
    }
    try {
      return Files.readString(path, StandardCharsets.UTF_8);
    } catch (final IOException e) {
      throw new RuntimeException("Fehler beim Lesen der TTL-Datei: " + path, e);
    }
  }

  /**
   * Überschreibt den Inhalt der TTL-Datei (ermittelt über URI→Pfad in der DB) mit dem übergebenen
   * String.
   */
  @Override
  public void setModelTTL(String uri, String modelTTL) {
    final String pathStr = this.repo.getOntologyFilePath(uri);

    final Path path = Path.of(pathStr);
    try {
      Files
          .createDirectories(
              path.getParent() != null ? path.getParent() : path.toAbsolutePath().getParent());
      Files.writeString(path, Objects.toString(modelTTL, ""), StandardCharsets.UTF_8);
    } catch (final IOException e) {
      throw new RuntimeException("Fehler beim Schreiben der TTL-Datei: " + path, e);
    }
  }

  @Override
  public void putNewEntry(String uri, Object newEntry) {

  }

  /**
   * Hinterlegt in der "DB" die Zuordnung URI→Pfad und erzeugt unter dem Pfad eine TTL-Datei, deren
   * Inhalt aus newEntry.toString() stammt. Existiert die Datei bereits, wird sie überschrieben.
   */
  @Override
  public void putNewEntry(String uri, String path, Object newEntry) {
    final String cleanUri = requireNonBlank(uri, "uri");
    final String cleanPath = requireNonBlank(path, "path");

    // 1) In der "Datenbank" speichern
    this.repo.put(cleanUri, cleanPath);

    // 2) Datei anlegen/überschreiben
    final Path ttlPath = Path.of(cleanPath);
    final String content = Objects.toString(newEntry, "");
    try {
      final Path parent = ttlPath.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      Files.writeString(ttlPath, content, StandardCharsets.UTF_8);
    } catch (final IOException e) {
      throw new RuntimeException("Fehler beim Erstellen/Schreiben der TTL-Datei: " + ttlPath, e);
    }
  }

  // --- Helper ---

  private static String requireNonBlank(String value, String name) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(name + " darf nicht leer sein");
    }
    return value;
  }
}
