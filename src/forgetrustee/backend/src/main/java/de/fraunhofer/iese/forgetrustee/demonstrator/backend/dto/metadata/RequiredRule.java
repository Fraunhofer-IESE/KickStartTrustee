package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequiredRule {
  /**
   * the controlling field name
   */
  private String name;

  /**
   * operator to apply (EQUALS, NOT_EMPTY, IS_TRUE, ...)
   */
  private VisibilityOperator operator;

  /**
   * optional comparison value (for EQUALS, IN, MATCHES, ...)
   */
  private String value;
}
