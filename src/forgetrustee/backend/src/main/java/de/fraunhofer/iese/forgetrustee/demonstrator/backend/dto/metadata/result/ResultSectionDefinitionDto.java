package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultSectionDefinitionDto {

  private String label;

  private Map<String, ResultFieldDefinitionDto> fields;
}
