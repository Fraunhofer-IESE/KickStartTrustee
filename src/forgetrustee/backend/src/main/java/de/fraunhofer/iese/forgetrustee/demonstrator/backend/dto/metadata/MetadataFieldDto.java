
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

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataFieldDto {

  @Schema(description = "The unique name of the field (machine-readable)", requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @Schema(description = "The human-readable label to show in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private String label;

  private String heading;

  private String placeholder;

  private String tooltip;

  private String helpText;

  private String callToAction;

  private List<String> examples;

  private Integer minLength;

  private Integer maxLength;

  private Map<String, MetadataFieldChildDto> fields;

  @Schema(description = "The type of the field", requiredMode = Schema.RequiredMode.REQUIRED)
  private MetadataFieldType type;

  private Boolean multiple;

  private Boolean required;

  private List<RequiredRule> requiredBy;

  private Boolean disabled;

  private Boolean readonly;

  private Boolean allowUnknown;

  private String allowUnknownLabel;

  private Boolean allowNoAnswer;

  private String allowNoAnswerLabel;

  private List<MetadataFieldOption> options;

  private Boolean visible;

  private List<VisibilityRule> visibleBy;

  private AllowedValuesMode allowedValuesMode;

  private List<AllowedValuesRule> allowedValuesBy;
}
