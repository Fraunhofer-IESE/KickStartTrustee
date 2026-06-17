
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelLegal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.TitleDescriptionRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class DataTrusteeModelResponseMapper {

  public DataTrusteeModelResponseDto toDto(DataTrusteeModel model) {
    if (model == null) {
      return null;
    }

    return DataTrusteeModelResponseDto
        .builder()
        .core(this.toCoreDto(model.getCore()))
        .data(this.toDataDto(model.getData()))
        .legal(this.toLegalDto(model.getLegal()))
        .objectives(this.toObjectivesDto(model.getObjectives()))
        .business(this.toBusinessDto(model.getBusiness()))
        .build();
  }

  protected DataTrusteeModelCoreResponseDto toCoreDto(DataTrusteeModelCore model) {
    if (model == null) {
      return null;
    }

    return DataTrusteeModelCoreResponseDto
        .builder()
        .ontologyURI(model.getOntologyURI())
        .dataTrusteeName(commented(model.getDataTrusteeName()))
        .dataTrusteeDescription(commented(model.getDataTrusteeDescription()))
        .dataTrusteeOperator(commented(model.getDataTrusteeOperator()))
        .dataTrusteeOperatorAffiliation(commented(model.getDataTrusteeOperatorAffiliation()))
        .rightsHolderName(commented(model.getRightsHolderName()))
        .rightsHolderAffiliation(commented(model.getRightsHolderAffiliation()))
        .rightsHolderIsRepresented(commented(model.getRightsHolderIsRepresented()))
        .dataOwnerName(commented(model.getDataOwnerName()))
        .dataConsumerName(commented(model.getDataConsumerName()))
        .dataConsumerAffiliation(commented(model.getDataConsumerAffiliation()))
        .dataCategoryName(commented(model.getDataCategoryName()))
        .dataCategoryDescription(commented(model.getDataCategoryDescription()))
        .containPersonalInformation(commented(model.getContainPersonalInformation()))
        .specialPersonalInformation(commented(model.getSpecialPersonalInformation()))
        .containTradeSecrets(commented(model.getContainTradeSecrets()))
        .build();
  }

  protected DataTrusteeModelDataResponseDto toDataDto(DataTrusteeModelData model) {
    if (model == null) {
      return null;
    }

    return DataTrusteeModelDataResponseDto
        .builder()
        .dataTrusteeCategory(commented(model.getDataTrusteeCategory()))
        .architectureType(commented(model.getArchitectureType()))
        .receptionTechnologies(toCommentedList(model.getReceptionTechnologies()))
        .receptionFrequency(commented(model.getReceptionFrequency()))
        .receptionSecurityMeasures(toCommentedList(model.getReceptionSecurityMeasures()))
        .customReceptionSecurityTechniques(
            this.toDtoList(model.getCustomReceptionSecurityTechniques()))
        .sourceSystem(commented(model.getSourceSystem()))
        .preparationTechniques(toCommentedList(model.getPreparationTechniques()))
        .customPreparationTechniques(this.toDtoList(model.getCustomPreparationTechniques()))
        .storageTechnique(commented(model.getStorageTechnique()))
        .storageRetention(commented(model.getStorageRetention()))
        .analysisTechniques(toCommentedList(model.getAnalysisTechniques()))
        .customAnalysisTechniques(this.toDtoList(model.getCustomAnalysisTechniques()))
        .forwardingTechniques(toCommentedList(model.getForwardingTechniques()))
        .forwardingFrequency(commented(model.getForwardingFrequency()))
        .forwardingSecurityMeasures(toCommentedList(model.getForwardingSecurityMeasures()))
        .customForwardingSecurityTechniques(
            this.toDtoList(model.getCustomForwardingSecurityTechniques()))
        .targetSystem(commented(model.getTargetSystem()))
        .build();
  }

  protected DataTrusteeModelLegalResponseDto toLegalDto(DataTrusteeModelLegal model) {
    if (model == null) {
      return null;
    }

    return DataTrusteeModelLegalResponseDto
        .builder()
        .processingBases(this.toCommentedProcessingBases(model.getProcessingBases()))
        .consentType(commented(model.getConsentType()))
        .obtainingConsentBy(model.getObtainingConsentBy())
        .consentEnteredBy(commented(model.getConsentEnteredBy()))
        .build();
  }

  protected DataTrusteeModelObjectivesResponseDto toObjectivesDto(
      DataTrusteeModelObjectives model) {
    if (model == null) {
      return null;
    }

    return DataTrusteeModelObjectivesResponseDto
        .builder()
        .dataTrusteeGoals(toCommentedList(model.getDataTrusteeGoals()))
        .motivationRightsHolder(toCommentedList(model.getMotivationRightsHolder()))
        .motivationDataHolder(toCommentedList(model.getMotivationDataHolder()))
        .motivationDataConsumer(toCommentedList(model.getMotivationDataConsumer()))
        .build();
  }

  protected DataTrusteeModelBusinessResponseDto toBusinessDto(DataTrusteeModelBusiness model) {
    if (model == null) {
      return null;
    }

    return DataTrusteeModelBusinessResponseDto
        .builder()
        .businessDomains(toCommentedList(model.getBusinessDomains()))
        .businessModel(commented(model.getBusinessModel()))
        .fundingSources(toCommentedList(model.getFundingSources()))
        .paymentMethodDataOwner(commented(model.getPaymentMethodDataOwner()))
        .paymentMethodDataConsumer(commented(model.getPaymentMethodDataConsumer()))
        .build();
  }

  protected abstract TitleDescriptionRestDto toDto(TitleDescriptionDTO model);

  protected List<TitleDescriptionRestDto> toDtoList(List<TitleDescriptionDTO> models) {
    if (models == null) {
      return Collections.emptyList();
    }
    return models.stream().map(this::toDto).collect(Collectors.toList());
  }

  protected List<CommentedProcessingBasisRestDto> toCommentedProcessingBases(
      Map<ProcessingBasis, List<TitleDescriptionDTO>> source) {
    final List<CommentedProcessingBasisRestDto> target = new ArrayList<>();
    if (source == null || source.isEmpty()) {
      return target;
    }
    for (final Map.Entry<ProcessingBasis, List<TitleDescriptionDTO>> entry : source.entrySet()) {
      if (entry.getKey() != null) {
        target
            .add(CommentedProcessingBasisRestDto
                .builder()
                .value(entry.getKey())
                .details(this.toDtoList(entry.getValue()))
                .build());
      }
    }
    return target;
  }

  private static <T> CommentedValueRestDto<T> commented(T value) {
    return CommentedValueRestDto.<T> builder().value(value).comment(null).build();
  }

  private static <E extends Enum<E>> List<CommentedValueRestDto<E>> toCommentedList(
      EnumSet<E> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }

    final List<CommentedValueRestDto<E>> result = new ArrayList<>(values.size());
    for (final E value : values) {
      result.add(commented(value));
    }
    return result;
  }

}
