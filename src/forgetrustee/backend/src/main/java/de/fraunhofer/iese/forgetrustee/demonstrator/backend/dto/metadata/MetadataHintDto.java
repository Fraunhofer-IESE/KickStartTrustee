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
public class MetadataHintDto {

  @Schema(description = "Der technische Name des Hinweises", requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @Schema(description = "Der anzuzeigende Hinweistext", requiredMode = Schema.RequiredMode.REQUIRED)
  private String text;

  private List<VisibilityRule> visibleBy;
}
