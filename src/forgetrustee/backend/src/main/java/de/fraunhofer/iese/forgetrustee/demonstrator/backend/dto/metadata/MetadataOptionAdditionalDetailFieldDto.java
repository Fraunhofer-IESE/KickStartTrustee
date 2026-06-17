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
public class MetadataOptionAdditionalDetailFieldDto {

  /**
   * Unique machine-readable key for the field.
   */
  @Schema(description = "Unique machine-readable key for the field", requiredMode = Schema.RequiredMode.REQUIRED)
  private String key;

  /**
   * Human-readable label displayed for this additional detail field.
   */
  @Schema(description = "Human-readable label displayed for this additional detail field", requiredMode = Schema.RequiredMode.REQUIRED)
  private String label;

  /**
   * Optional placeholder shown in the input for this field.
   */
  @Schema(description = "Optional placeholder shown in the input for this field")
  private String placeholder;

  /**
   * Optional tooltip shown for this field. Overrides the default tooltip if present.
   */
  @Schema(description = "Optional tooltip shown for this field. Overrides the default tooltip if present")
  private String tooltip;

  /**
   * Whether this additional detail field is required.
   */
  @Schema(description = "Whether this additional detail field is required")
  private Boolean required;

}
