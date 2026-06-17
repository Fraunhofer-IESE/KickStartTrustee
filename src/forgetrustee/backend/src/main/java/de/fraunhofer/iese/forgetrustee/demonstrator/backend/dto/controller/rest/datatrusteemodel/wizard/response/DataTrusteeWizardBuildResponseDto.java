
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.response;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclReportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the response of the data trustee wizard build endpoint.
 * Contains the original request data, the generated TTL, and the SHACL validation report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeWizardBuildResponseDto {

  /**
   * Data from the wizard's original request
   */
  private DataTrusteeWizardRequestDto request;

  /**
   * The created data trustee model represented as a response DTO
   */
  private DataTrusteeModelResponseDto dataTrusteeModel;

  /**
   * TTL representation of the created data trustee model
   */
  private String ttl;

  /**
   * SHACL validation report for the created data trustee model
   */
  private ShaclReportDTO shaclReport;

  /**
   * Executive summary generated from the created data trustee model
   */
  private String executiveSummary;
}
