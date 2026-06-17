
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
public class MetadataModuleIndexSummaryDto {

  @Builder.Default
  @Schema(description = "List of module summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  private List<MetadataModuleSummaryDto> modules = List.of();
}
