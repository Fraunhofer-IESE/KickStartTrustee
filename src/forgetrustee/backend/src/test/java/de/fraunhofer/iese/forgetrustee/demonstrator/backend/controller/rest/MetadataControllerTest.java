
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleIndexSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModelDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.MetadataService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class MetadataControllerTest {

  @Autowired
  private MetadataService metadataService;

  @Test
  void listModulesReturnsCoreMetadata() {
    final List<MetadataModuleDto> modules = this.metadataService.listModules("de");

    boolean containsCore = false;
    for (final MetadataModuleDto entry : modules) {
      if ("core".equals(entry.getModuleId())) {
        containsCore = true;
        break;
      }
    }
    assertThat(containsCore).isTrue();
  }

  @Test
  void getFieldMetadataReturnsLabel() {
    final Optional<MetadataFieldDto> field = this.metadataService
        .getFieldMetadata("core", "dataTrusteeName", "de");

    assertThat(field).isPresent();
    assertThat(field.get().getLabel()).isEqualTo("Wie lautet der Name des Datentreuhandmodels?");
  }

  @Test
  void getMetadataModelReturnsOrderedModules() {
    final MetadataModelDto model = this.metadataService.getMetadataModel("de");

    assertThat(model.getModelId()).isEqualTo("dtm");
    assertThat(model.getModules()).contains("core", "data");
    assertThat(model.getModules().subList(0, 2)).containsExactly("core", "data");
  }

  @Test
  void getMetadataModuleIndexSummaryReturnsCoreMetadataWithoutFieldPayload() {
    final MetadataModuleIndexSummaryDto summary = this.metadataService.getMetadataModuleIndexSummary("de");

    assertThat(summary.getModules()).isNotEmpty();
    assertThat(summary.getModules()).anySatisfy(item -> {
      if ("core".equals(item.getModuleId())) {
        assertThat(item.getLabel()).isEqualTo("Datentreuhandmodell Core");
      }
    });
  }
}
