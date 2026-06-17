package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataHintDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataSectionDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.VisibilityOperator;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.VisibilityRule;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MetadataServiceSectionHintsTest {

  @Test
  void mergeSectionTextMergesSectionHintsLikeModuleHints() throws Exception {
    final MetadataService service = new MetadataService(null, null, null);

    final VisibilityRule targetRule = VisibilityRule.builder()
        .name("targetField")
        .operator(VisibilityOperator.IS_TRUE)
        .build();
    final VisibilityRule sourceRule = VisibilityRule.builder()
        .name("sourceField")
        .operator(VisibilityOperator.IS_FALSE)
        .build();

    final MetadataSectionDto target = MetadataSectionDto.builder()
        .id("collection")
        .label("Collection")
        .hints(new ArrayList<>(List.of(
            MetadataHintDto.builder()
                .name("shared")
                .text("old text")
                .visibleBy(List.of(targetRule))
                .build())))
        .build();

    final MetadataSectionDto source = MetadataSectionDto.builder()
        .id("collection")
        .label("Sammlung")
        .hints(List.of(
            MetadataHintDto.builder()
                .name("shared")
                .text("new text")
                .visibleBy(List.of(sourceRule))
                .build(),
            MetadataHintDto.builder()
                .name("additional")
                .text("additional text")
                .build()))
        .build();

    final Method mergeSectionText = MetadataService.class
        .getDeclaredMethod("mergeSectionText", MetadataSectionDto.class, MetadataSectionDto.class);
    mergeSectionText.setAccessible(true);
    mergeSectionText.invoke(service, target, source);

    assertThat(target.getHints()).hasSize(2);
    assertThat(target.getHints().get(0).getName()).isEqualTo("shared");
    assertThat(target.getHints().get(0).getText()).isEqualTo("new text");
    assertThat(target.getHints().get(0).getVisibleBy()).containsExactly(sourceRule);
    assertThat(target.getHints().get(1).getName()).isEqualTo("additional");
    assertThat(target.getHints().get(1).getText()).isEqualTo("additional text");
  }
}
