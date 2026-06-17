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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataFieldOption {

  /**
   * The underlying value (machine-readable)
   */
  @Schema(description = "The underlying value (machine-readable)", requiredMode = Schema.RequiredMode.REQUIRED)
  private String value;

  /**
   * The human-readable label to show in the UI
   */
  @Schema(description = "The human-readable label to show in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private String label;

  /**
   * Additional detail input configuration for this option
   */
  @Schema(description = "Additional detail input configuration for this option")
  private MetadataOptionAdditionalDetailsDto additionalDetails;

  /**
   * Marker indicating that the frontend may render this option in a visually separated extra area.
   */
  @Schema(description = "Marker indicating that the frontend may render this option in a visually separated extra area")
  private Boolean extraArea;

  /**
   * If true, additional detail inputs should be hidden/disabled for this option
   */
  @Schema(description = "If true, additional detail inputs should be hidden/disabled for this option")
  private Boolean disableAdditionalDetails;
}
