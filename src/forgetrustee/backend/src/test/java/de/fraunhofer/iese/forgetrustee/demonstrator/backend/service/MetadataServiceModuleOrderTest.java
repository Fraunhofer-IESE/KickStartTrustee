package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleSummaryDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class MetadataServiceModuleOrderTest {

  @TempDir
  Path tempDir;

  @Test
  void listModulesUsesExplicitOrderFieldBeforeModelIndex() throws Exception {
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files.writeString(schemaDir.resolve("dtm-core.yaml"), "moduleId: dtm-core\n"
        + "order: 1\n"
        + "fields: {}\n");

    Files.writeString(schemaDir.resolve("dtm-legal.yaml"), "moduleId: dtm-legal\n"
        + "order: 2\n"
        + "fields: {}\n");

    Files.writeString(schemaDir.resolve("dtm.yaml"), "modelId: dtm\n"
        + "modules:\n"
        + "  - dtm-legal\n"
        + "  - dtm-core\n");

    Files.writeString(langDir.resolve("dtm-core.yaml"), "moduleId: dtm-core\n"
        + "label: Core\n"
        + "fields: {}\n");

    Files.writeString(langDir.resolve("dtm-legal.yaml"), "moduleId: dtm-legal\n"
        + "label: Legal\n"
        + "fields: {}\n");

    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final MetadataService service = new MetadataService(props,
        new ValidationMetadataService(validator), new MetadataConsistencyCheckService());

    final List<MetadataModuleDto> modules = service.listModules("de");
    assertThat(modules)
        .extracting(MetadataModuleDto::getOrder)
        .containsExactly(1, 2);
    assertThat(modules)
        .extracting(MetadataModuleDto::getModuleId)
        .containsExactly("dtm-core", "dtm-legal");

    final List<MetadataModuleSummaryDto> summaryModules = service
        .getMetadataModuleIndexSummary("de")
        .getModules();
    assertThat(summaryModules)
        .extracting(MetadataModuleSummaryDto::getModuleId)
        .containsExactly("dtm-core", "dtm-legal");

    assertThat(summaryModules)
        .extracting(MetadataModuleSummaryDto::getOrder)
        .containsExactly(1, 2);
  }
}
