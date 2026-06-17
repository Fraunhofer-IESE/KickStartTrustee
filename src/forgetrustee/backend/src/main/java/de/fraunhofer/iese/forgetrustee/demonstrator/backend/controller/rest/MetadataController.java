
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModelDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleIndexSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.MetadataService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
@Tag(name = "Metadata Controller", description = "Provides endpoints to retrieve metadata about the data model, modules, and fields.")
public class MetadataController {

  private final MetadataService metadataService;

  @GetMapping
  public MetadataModelDto getMetadataModel(
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    return this.metadataService.getMetadataModel(lang);
  }

  @GetMapping("/modules")
  public List<MetadataModuleDto> listModules(
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    return this.metadataService.listModules(lang);
  }

  @GetMapping("/modules/summary")
  public MetadataModuleIndexSummaryDto getModulesSummary(
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    return this.metadataService.getMetadataModuleIndexSummary(lang);
  }

  @GetMapping("/modules/{moduleId}")
  public MetadataModuleDto getModuleMetadata(@PathVariable("moduleId") String moduleId,
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    return this.metadataService
        .getModuleMetadata(moduleId, lang)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));
  }

  @GetMapping("/modules/{moduleId}/fields/{fieldPath:.+}")
  public MetadataFieldDto getFieldMetadata(@PathVariable("moduleId") String moduleId,
      @PathVariable("fieldPath") String fieldPath,
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    return this.metadataService
        .getFieldMetadata(moduleId, fieldPath, lang)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));
  }

  @GetMapping("/module-ids")
  public Map<String, String> getModuleIds() {
    return this.metadataService.getModuleIds();
  }
}
