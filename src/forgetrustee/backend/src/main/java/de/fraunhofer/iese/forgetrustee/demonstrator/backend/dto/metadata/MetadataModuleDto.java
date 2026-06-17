package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

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
public class MetadataModuleDto {

  private String moduleId;

  private String requestDtoClassName;

  private Integer order;

  private String label;

  private String heading;

  private String menuPoint;

  private String tooltip;

  private String helpText;

  private String callToAction;

  private List<String> examples;

  private List<MetadataHintDto> hints;

  private Map<String, MetadataFieldDto> fields;

  private Map<String, MetadataSectionDto> sections;
}