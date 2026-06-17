
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessDomain;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.FundingSource;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.PayMethods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModelBusinessResponseDto {

  private List<CommentedValueRestDto<BusinessDomain>> businessDomains;

  private CommentedValueRestDto<BusinessModel> businessModel;

  private List<CommentedValueRestDto<FundingSource>> fundingSources;

  private CommentedValueRestDto<PayMethods> paymentMethodDataOwner;

  private CommentedValueRestDto<PayMethods> paymentMethodDataConsumer;

}
