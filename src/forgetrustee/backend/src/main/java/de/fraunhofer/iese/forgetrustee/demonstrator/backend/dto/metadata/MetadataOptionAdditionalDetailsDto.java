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
public class MetadataOptionAdditionalDetailsDto {

  /**
   * Tooltip used for all additional detail fields unless a field defines its own tooltip.
   */
  @Schema(description = "Tooltip used for all additional detail fields unless a field defines its own tooltip")
  private String defaultTooltip;

  /**
   * Placeholder used for all additional detail fields unless a field defines its own placeholder.
   */
  @Schema(description = "Placeholder used for all additional detail fields unless a field defines its own placeholder")
  private String defaultPlaceholder;

  /**
   * Concrete additional detail fields shown for this option.
   */
  @Schema(description = "Concrete additional detail fields shown for this option")
  private List<MetadataOptionAdditionalDetailFieldDto> fields;
}
