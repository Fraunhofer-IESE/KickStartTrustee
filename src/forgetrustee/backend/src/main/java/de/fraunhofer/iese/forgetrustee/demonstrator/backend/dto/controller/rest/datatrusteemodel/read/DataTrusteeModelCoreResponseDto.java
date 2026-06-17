
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModelCoreResponseDto {

  private String ontologyURI;

  private CommentedValueRestDto<String> dataTrusteeName;

  private CommentedValueRestDto<String> dataTrusteeDescription;

  private CommentedValueRestDto<String> dataTrusteeOperator;

  private CommentedValueRestDto<Affiliation> dataTrusteeOperatorAffiliation;

  private CommentedValueRestDto<String> rightsHolderName;

  private CommentedValueRestDto<Affiliation> rightsHolderAffiliation;

  private CommentedValueRestDto<String> rightsHolderIsRepresented;

  private CommentedValueRestDto<String> dataOwnerName;

  private CommentedValueRestDto<String> dataConsumerName;

  private CommentedValueRestDto<Affiliation> dataConsumerAffiliation;

  private CommentedValueRestDto<String> dataCategoryName;

  private CommentedValueRestDto<String> dataCategoryDescription;

  private CommentedValueRestDto<String> containPersonalInformation;

  private CommentedValueRestDto<String> specialPersonalInformation;

  private CommentedValueRestDto<String> containTradeSecrets;
}
