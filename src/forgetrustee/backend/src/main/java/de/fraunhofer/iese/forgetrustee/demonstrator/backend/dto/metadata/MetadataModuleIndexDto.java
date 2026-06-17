
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataModuleIndexDto {

  private List<MetadataModuleDto> modules;
}
