
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldOption;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataSectionDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to check consistency between metadata schema and i18n keys.
 */
@Service
@Slf4j
public class MetadataConsistencyCheckService {

  /**
   * Compare the structure of the metadata schema with the i18n keys and log any mismatches.
   *
   * @param lang   the language code for logging context
   * @param schema the metadata schema modules to compare
   * @param i18n   the i18n metadata modules to compare
   */
  public void logMetadataStructureMismatches(String lang, Map<String, MetadataModuleDto> schema,
      Map<String, MetadataModuleDto> i18n) {
    if (schema == null || i18n == null) {
      return;
    }

    log
        .debug("Metadata check [{}]: start (schemaModules={}, i18nModules={})", lang, schema.size(),
            i18n.size());

    final AtomicInteger mismatchCount = new AtomicInteger(0);

    this.logMissingI18nIds(lang, "class", schema.keySet(), i18n.keySet(), "", mismatchCount);

    /**
     * Iterate over all modules in the schema and compare with i18n, then recursively check their
     */
    for (final Map.Entry<String, MetadataModuleDto> entry : schema.entrySet()) {
      final String moduleId = entry.getKey();
      final MetadataModuleDto schemaClass = entry.getValue();
      final MetadataModuleDto i18nClass = i18n.get(moduleId);
      if (schemaClass == null || i18nClass == null) {
        continue;
      }

      final int schemaTopFields = schemaClass.getFields() == null ? 0
          : schemaClass.getFields().size();
      final int i18nTopFields = i18nClass.getFields() == null ? 0 : i18nClass.getFields().size();
      final int schemaSections = schemaClass.getSections() == null ? 0
          : schemaClass.getSections().size();
      final int i18nSections = i18nClass.getSections() == null ? 0 : i18nClass.getSections().size();
      log
          .debug(
              "Metadata check [{}]: class='{}' (schemaFields={}, i18nFields={}, schemaSections={}, i18nSections={})",
              lang, moduleId, schemaTopFields, i18nTopFields, schemaSections, i18nSections);

      this
          .logMissingI18nIds(lang, "field", this.safeMapKeys(schemaClass.getFields()),
              this.safeMapKeys(i18nClass.getFields()), "class='" + moduleId + "'", mismatchCount);

      this
          .logSectionMismatches(lang, moduleId, "", schemaClass.getSections(),
              i18nClass.getSections(), mismatchCount);
    }

    if (mismatchCount.get() == 0) {
      log.info("Metadata check [{}]: schema and i18n keys are consistent", lang);
    } else {
      log
          .warn("Metadata check [{}]: detected {} mismatches between schema and i18n", lang,
              mismatchCount.get());
    }
  }

  /**
   * Recursively log mismatches between schema and i18n sections, including their fields and
   * options.
   */
  private void logSectionMismatches(String lang, String moduleId, String sectionPath,
      Map<String, MetadataSectionDto> schemaSections, Map<String, MetadataSectionDto> i18nSections,
      AtomicInteger mismatchCount) {
    final Set<String> schemaSectionIds = this.safeMapKeys(schemaSections);
    final Set<String> i18nSectionIds = this.safeMapKeys(i18nSections);

    log
        .debug(
            "Metadata check [{}]: section-scope class='{}' path='{}' (schemaSections={}, i18nSections={})",
            lang, moduleId, sectionPath.isBlank() ? "<root>" : sectionPath, schemaSectionIds.size(),
            i18nSectionIds.size());

    this
        .logMissingI18nIds(lang, "section", schemaSectionIds, i18nSectionIds,
            "class='"
                + moduleId
                + "'"
                + (sectionPath.isBlank() ? "" : ", parentSection='" + sectionPath + "'"),
            mismatchCount);

    for (final String sectionId : schemaSectionIds) {
      final MetadataSectionDto schemaSection = schemaSections != null
          ? schemaSections.get(sectionId)
          : null;
      final MetadataSectionDto i18nSection = i18nSections != null ? i18nSections.get(sectionId)
          : null;
      if (schemaSection == null || i18nSection == null) {
        continue;
      }

      final String fullSectionPath = sectionPath.isBlank() ? sectionId
          : sectionPath + "." + sectionId;

      final int schemaFields = schemaSection.getFields() == null ? 0
          : schemaSection.getFields().size();
      final int i18nFields = i18nSection.getFields() == null ? 0 : i18nSection.getFields().size();
      final int schemaSub = schemaSection.getSubsections() == null ? 0
          : schemaSection.getSubsections().size();
      final int i18nSub = i18nSection.getSubsections() == null ? 0
          : i18nSection.getSubsections().size();
      log
          .debug(
              "Metadata check [{}]: section='{}' class='{}' (schemaFields={}, i18nFields={}, schemaSubsections={}, i18nSubsections={})",
              lang, fullSectionPath, moduleId, schemaFields, i18nFields, schemaSub, i18nSub);

      this
          .logMissingI18nIds(lang, "field", this.safeMapKeys(schemaSection.getFields()),
              this.safeMapKeys(i18nSection.getFields()),
              "class='" + moduleId + "', section='" + fullSectionPath + "'", mismatchCount);

      this
          .logFieldMismatches(lang, moduleId, fullSectionPath, schemaSection.getFields(),
              i18nSection.getFields(), mismatchCount);
      this
          .logSectionMismatches(lang, moduleId, fullSectionPath, schemaSection.getSubsections(),
              i18nSection.getSubsections(), mismatchCount);
    }
  }

  /**
   * Log mismatches between schema and i18n fields, including their options and child fields.
   */
  private void logFieldMismatches(String lang, String moduleId, String sectionPath,
      Map<String, MetadataFieldDto> schemaFields, Map<String, MetadataFieldDto> i18nFields,
      AtomicInteger mismatchCount) {
    if (schemaFields == null) {
      return;
    }

    final int i18nCount = i18nFields == null ? 0 : i18nFields.size();
    log
        .debug(
            "Metadata check [{}]: field-scope class='{}' section='{}' (schemaFields={}, i18nFields={})",
            lang, moduleId, sectionPath.isBlank() ? "<root>" : sectionPath, schemaFields.size(),
            i18nCount);

    for (final Map.Entry<String, MetadataFieldDto> entry : schemaFields.entrySet()) {
      final String fieldId = entry.getKey();
      final MetadataFieldDto schemaField = entry.getValue();
      final MetadataFieldDto i18nField = i18nFields != null ? i18nFields.get(fieldId) : null;
      if (schemaField == null || i18nField == null) {
        continue;
      }

      final String fieldPath = sectionPath.isBlank() ? fieldId : sectionPath + "." + fieldId;

      final int schemaChildren = schemaField.getFields() == null ? 0
          : schemaField.getFields().size();
      final int i18nChildren = i18nField.getFields() == null ? 0 : i18nField.getFields().size();
      final int schemaOptions = schemaField.getOptions() == null ? 0
          : schemaField.getOptions().size();
      final int i18nOptions = i18nField.getOptions() == null ? 0 : i18nField.getOptions().size();
      log
          .debug(
              "Metadata check [{}]: field='{}' class='{}' (schemaChildFields={}, i18nChildFields={}, schemaOptions={}, i18nOptions={})",
              lang, fieldPath, moduleId, schemaChildren, i18nChildren, schemaOptions, i18nOptions);

      this
          .logMissingI18nIds(lang, "child-field", this.safeMapKeys(schemaField.getFields()),
              this.safeMapKeys(i18nField.getFields()),
              "class='" + moduleId + "', field='" + fieldPath + "'", mismatchCount);

      this
          .logOptionMismatches(lang, moduleId, fieldPath, schemaField.getOptions(),
              i18nField.getOptions(), mismatchCount);
    }
  }

  /**
   * Log mismatches between schema and i18n field options.
   */
  private void logOptionMismatches(String lang, String moduleId, String fieldPath,
      List<MetadataFieldOption> schemaOptions, List<MetadataFieldOption> i18nOptions,
      AtomicInteger mismatchCount) {
    final Set<String> schemaOptionValues = this.optionValues(schemaOptions);
    final Set<String> i18nOptionValues = this.optionValues(i18nOptions);
    this
        .logMissingI18nIds(lang, "option", schemaOptionValues, i18nOptionValues,
            "class='" + moduleId + "', field='" + fieldPath + "'", mismatchCount);
  }

  private Set<String> optionValues(List<MetadataFieldOption> options) {
    final Set<String> values = new HashSet<>();
    if (options == null) {
      return values;
    }
    for (final MetadataFieldOption option : options) {
      if (option != null && option.getValue() != null) {
        values.add(option.getValue());
      }
    }
    return values;
  }

  /**
   * Safely extract keys from a map, returning an empty set if the map is null or empty.
   * 
   * @param  map the map to extract keys from
   * @return     a set of keys from the map, or an empty set if the map is null or empty
   */
  private Set<String> safeMapKeys(Map<String, ?> map) {
    if (map == null || map.isEmpty()) {
      return Collections.emptySet();
    }
    return new HashSet<>(map.keySet());
  }

  /**
   * Log any missing i18n IDs compared to the expected schema IDs, including context information.
   * 
   * @param lang          the language code for logging context
   * @param kind          the kind of metadata being checked (e.g., "class", "field", "section",
   *                        "option")
   * @param schemaIds     the set of expected IDs from the schema
   * @param i18nIds       the set of provided IDs from the i18n metadata
   * @param context       additional context information for logging (e.g., class or section path)
   * @param mismatchCount an atomic counter to track the number of mismatches found
   */
  private void logMissingI18nIds(String lang, String kind, Set<String> schemaIds,
      Set<String> i18nIds, String context, AtomicInteger mismatchCount) {
    final Set<String> expected = schemaIds == null ? Collections.emptySet() : schemaIds;
    final Set<String> provided = i18nIds == null ? Collections.emptySet() : i18nIds;

    for (final String id : expected) {
      if (!provided.contains(id)) {
        mismatchCount.incrementAndGet();
        log
            .warn("Metadata mismatch [{}]: missing i18n {} '{}' ({})", lang, kind, id,
                context == null || context.isBlank() ? "no-context" : context);
      }
    }
  }
}
