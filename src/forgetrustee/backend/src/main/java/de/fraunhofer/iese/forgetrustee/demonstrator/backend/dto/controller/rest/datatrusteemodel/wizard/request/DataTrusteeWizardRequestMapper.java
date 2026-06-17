
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardImplementation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.TitleDescriptionRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class DataTrusteeWizardRequestMapper {

  private BackendProperties backendProperties;

  @Autowired
  public void setBackendProperties(BackendProperties backendProperties) {
    this.backendProperties = backendProperties;
  }

  @Mapping(target = "data", source = "legal")
  @Mapping(target = "implementation", source = "data")
  public abstract DataTrusteeWizardRequestDto toDto(DataTrusteeWizardModel model);

  @Mapping(target = "legal", source = "data")
  @Mapping(target = "data", source = "implementation")
  public abstract DataTrusteeWizardModel toModel(DataTrusteeWizardRequestDto dto);

  public abstract DataTrusteeWizardCoreRequestDto toDto(DataTrusteeWizardCore model);

  @Mapping(target = "ontologyURI", ignore = true)
  public abstract DataTrusteeWizardCore toModel(DataTrusteeWizardCoreRequestDto dto);

  public abstract DataTrusteeWizardDataRequestDto toDto(DataTrusteeWizardData model);

  public abstract DataTrusteeWizardData toModel(DataTrusteeWizardDataRequestDto dto);

  public abstract DataTrusteeWizardImplementationRequestDto toDto(
      DataTrusteeWizardImplementation model);

  public abstract DataTrusteeWizardImplementation toModel(
      DataTrusteeWizardImplementationRequestDto dto);

  public abstract DataTrusteeWizardBusinessRequestDto toDto(DataTrusteeWizardBusiness model);

  public abstract DataTrusteeWizardBusiness toModel(DataTrusteeWizardBusinessRequestDto dto);

  public abstract DataTrusteeWizardObjectivesRequestDto toDto(DataTrusteeWizardObjectives model);

  public abstract DataTrusteeWizardObjectives toModel(DataTrusteeWizardObjectivesRequestDto dto);

  @Mapping(target = "title", source = "dataCategoryName")
  @Mapping(target = "description", source = "dataCategoryDescription")
  protected abstract TitleDescriptionDTO toDataCategoryModel(DataCategoryRestDto dto);

  @Mapping(target = "dataCategoryName", source = "title")
  @Mapping(target = "dataCategoryDescription", source = "description")
  protected abstract DataCategoryRestDto toDataCategoryDto(TitleDescriptionDTO model);

  protected abstract TitleDescriptionRestDto toDto(TitleDescriptionDTO model);

  protected abstract TitleDescriptionDTO toModel(TitleDescriptionRestDto dto);

  @AfterMapping
  protected void addOntologyUri(DataTrusteeWizardCoreRequestDto source,
      @MappingTarget DataTrusteeWizardCore target) {
    if (target != null) {
      target
          .setOntologyURI(
              this.buildOntologyUri(source != null ? source.getDataTrusteeName() : null));
    }
  }

  private String buildOntologyUri(String dataTrusteeName) {
    final String localName = toLocalName(dataTrusteeName);
    final String baseUri = this.backendProperties != null
        && this.backendProperties.getOntology() != null
            ? this.backendProperties.getOntology().getBaseUri()
            : null;
    return normalizeBaseUri(baseUri) + localName + "#";
  }

  private static String normalizeBaseUri(String baseUri) {
    if (baseUri == null || baseUri.isBlank()) {
      return "https://www.example.de/ontology/";
    }
    return baseUri.endsWith("/") || baseUri.endsWith("#") ? baseUri : baseUri + "/";
  }

  private static String toLocalName(String value) {
    if (value == null) {
      return "_";
    }

    String out = value
        .trim()
        .replace("ä", "ae")
        .replace("ö", "oe")
        .replace("ü", "ue")
        .replace("Ä", "Ae")
        .replace("Ö", "Oe")
        .replace("Ü", "Ue")
        .replace("ß", "ss")
        .replaceAll("\\s+", "")
        .replaceAll("[^A-Za-z0-9_\\-\\.]", "");

    if (out.isEmpty()) {
      out = "_";
    }

    if (Character.isDigit(out.charAt(0))) {
      out = "_" + out;
    }

    return out;
  }

  protected List<TitleDescriptionRestDto> toDtoList(List<TitleDescriptionDTO> models) {
    if (models == null) {
      return Collections.emptyList();
    }
    return models.stream().map(this::toDto).collect(Collectors.toList());
  }

  protected List<TitleDescriptionDTO> toModelList(List<TitleDescriptionRestDto> dtos) {
    if (dtos == null) {
      return Collections.emptyList();
    }
    return dtos.stream().map(this::toModel).collect(Collectors.toList());
  }

  protected Map<ProcessingBasis, List<TitleDescriptionRestDto>> toDtoProcessingBases(
      Map<ProcessingBasis, List<TitleDescriptionDTO>> source) {
    final Map<ProcessingBasis, List<TitleDescriptionRestDto>> target = new EnumMap<>(
        ProcessingBasis.class);
    if (source == null || source.isEmpty()) {
      return target;
    }
    for (final Map.Entry<ProcessingBasis, List<TitleDescriptionDTO>> entry : source.entrySet()) {
      if (entry.getKey() != null) {
        target.put(entry.getKey(), this.toDtoList(entry.getValue()));
      }
    }
    return target;
  }

  protected Map<ProcessingBasis, List<TitleDescriptionDTO>> toModelProcessingBases(
      Map<ProcessingBasis, List<TitleDescriptionRestDto>> source) {
    final Map<ProcessingBasis, List<TitleDescriptionDTO>> target = new EnumMap<>(
        ProcessingBasis.class);
    if (source == null || source.isEmpty()) {
      return target;
    }
    for (final Map.Entry<ProcessingBasis, List<TitleDescriptionRestDto>> entry : source
        .entrySet()) {
      if (entry.getKey() != null) {
        target.put(entry.getKey(), this.toModelList(entry.getValue()));
      }
    }
    return target;
  }
}
