
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.TitleDescriptionRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.AnalysisTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ArchitectureType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataTrusteeCategory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ForwardingTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.Frequency;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.PreparationTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ReceptionTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.SecurityMeasure;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.StorageRetention;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.StorageTechnique;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModelDataResponseDto {

  private CommentedValueRestDto<DataTrusteeCategory> dataTrusteeCategory;

  private CommentedValueRestDto<ArchitectureType> architectureType;

  private List<CommentedValueRestDto<ReceptionTechnique>> receptionTechnologies;

  private CommentedValueRestDto<Frequency> receptionFrequency;

  private List<CommentedValueRestDto<SecurityMeasure>> receptionSecurityMeasures;

  private List<TitleDescriptionRestDto> customReceptionSecurityTechniques;

  private CommentedValueRestDto<String> sourceSystem;

  private List<CommentedValueRestDto<PreparationTechnique>> preparationTechniques;

  private List<TitleDescriptionRestDto> customPreparationTechniques;

  private CommentedValueRestDto<StorageTechnique> storageTechnique;

  private CommentedValueRestDto<StorageRetention> storageRetention;

  private List<CommentedValueRestDto<AnalysisTechnique>> analysisTechniques;

  private List<TitleDescriptionRestDto> customAnalysisTechniques;

  private List<CommentedValueRestDto<ForwardingTechnique>> forwardingTechniques;

  private CommentedValueRestDto<Frequency> forwardingFrequency;

  private List<CommentedValueRestDto<SecurityMeasure>> forwardingSecurityMeasures;

  private List<TitleDescriptionRestDto> customForwardingSecurityTechniques;

  private CommentedValueRestDto<String> targetSystem;

}
