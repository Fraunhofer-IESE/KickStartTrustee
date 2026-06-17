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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeWizardRequestDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private DataTrusteeWizardCoreRequestDto core;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private DataTrusteeWizardDataRequestDto data;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private DataTrusteeWizardImplementationRequestDto implementation;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private DataTrusteeWizardBusinessRequestDto business;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private DataTrusteeWizardObjectivesRequestDto objectives;
}
