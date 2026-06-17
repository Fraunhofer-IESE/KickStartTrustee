
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataModelDto {

  private String modelId;

  @Schema(description = "List of modules", requiredMode = Schema.RequiredMode.REQUIRED)
  @Builder.Default
  private List<String> modules = new ArrayList<>();;
}
