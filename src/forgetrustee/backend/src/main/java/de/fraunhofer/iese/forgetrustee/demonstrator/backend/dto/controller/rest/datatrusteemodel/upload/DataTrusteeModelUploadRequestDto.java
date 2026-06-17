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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModelUploadRequestDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private DataTrusteeWizardRequestDto wizardData;

  @NotBlank(message = "Name must not be blank")
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @NotBlank(message = "Email must not be blank")
  @Email(message = "Email must be a valid email address")
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String honeypot;
}
