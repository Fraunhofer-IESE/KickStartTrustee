
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataModuleSummaryDto {

  @Schema(description = "Unique identifier of the module", requiredMode = Schema.RequiredMode.REQUIRED)
  private String moduleId;

  @Schema(description = "The order in which the module should be displayed in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer order;

  @Schema(description = "The human-readable label to show in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private String label;

  @Schema(description = "Headline to show in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private String heading;

  @Schema(description = "The menu point under which the module should be listed in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private String menuPoint;

  private String tooltip;

  private String helpText;

  private String callToAction;

  private List<String> examples;
}
