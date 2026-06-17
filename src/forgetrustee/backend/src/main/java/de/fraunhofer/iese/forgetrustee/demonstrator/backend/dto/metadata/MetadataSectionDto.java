package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataSectionDto {

  @Schema(description = "Unique identifier for this section (machine-readable)", requiredMode = Schema.RequiredMode.REQUIRED)
  private String id;

  @Schema(description = "The human-readable label to show in the UI", requiredMode = Schema.RequiredMode.REQUIRED)
  private String label;

  private String heading;

  private String tooltip;

  private String helpText;

  private String callToAction;

  private List<String> examples;

  private List<MetadataHintDto> hints;

  private List<VisibilityRule> visibleBy;

  @Builder.Default
  private Map<String, MetadataSectionDto> subsections = new HashMap<>();

  @Builder.Default
  private Map<String, MetadataFieldDto> fields = new HashMap<>();
}