
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

import java.util.Map;

/**
 * Data Transfer Object for the presentation of a section in the Data Trustee Model. It contains a
 * key, a label, and a map of fields with their corresponding values. This DTO is used to present
 * each section of the Data Trustee Model in a structured way, making it easier for clients to
 * understand and display the information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModelSectionPresentationDto {

  private String key;

  private String label;

  private Map<String, String> fields;
}
