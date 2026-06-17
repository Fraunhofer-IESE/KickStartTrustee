
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the response of the Data Trustee Model read endpoint. It contains all
 * the necessary information about the Data Trustee Model, including its core information, data,
 * legal aspects, objectives, and business information. Additionally, it includes a presentation DTO
 * that structures the information in a way that is easy to display for clients. This DTO serves as
 * the main response object when retrieving a Data Trustee Model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModelResponseDto {

  /** Core information about the Data Trustee Model */
  private DataTrusteeModelCoreResponseDto core;

  /** Data information about the Data Trustee Model */
  private DataTrusteeModelDataResponseDto data;

  /** Legal information about the Data Trustee Model */
  private DataTrusteeModelLegalResponseDto legal;

  /** Objectives information about the Data Trustee Model */
  private DataTrusteeModelObjectivesResponseDto objectives;

  /** Business information about the Data Trustee Model */
  private DataTrusteeModelBusinessResponseDto business;

  /** Presentation information about the Data Trustee Model, structured for easy display */
  private DataTrusteeModelPresentationDto presentation;
}
