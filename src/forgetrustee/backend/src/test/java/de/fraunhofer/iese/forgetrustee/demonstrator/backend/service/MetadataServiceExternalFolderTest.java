package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleIndexSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelPresentationDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class MetadataServiceExternalFolderTest {

  @TempDir
  Path tempDir;

  @Test
  void loadsMetadataFromExternalFolder() throws IOException {
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files
        .writeString(schemaDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "requestDtoClassName: DataTrusteeModelCore\n"
                + "sections:\n"
                + "  collection:\n"
                + "    fields: {}\n"
                + "fields: {}\n");

    Files
        .writeString(langDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "label: Core\n"
                + "menuPoint: Kernbereich\n"
                + "tooltip: Test\n"
                + "helpText: Test\n"
                + "examples: []\n"
                + "sections:\n"
                + "  collection:\n"
                + "    label: Datenerhebung\n"
                + "    tooltip: Test\n"
                + "    helpText: Test\n"
                + "    examples: []\n"
                + "    fields: {}\n"
                + "fields: {}\n");


    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final ValidationMetadataService validationMetadataService = new ValidationMetadataService(validator);
    final MetadataService service = new MetadataService(props, validationMetadataService,
        new MetadataConsistencyCheckService());

    final List<MetadataModuleDto> modules = service.listModules("de");
    assertThat(modules).anyMatch(item -> "dtm-core".equals(item.getModuleId()));
    assertThat(modules).anySatisfy(item -> {
      if ("dtm-core".equals(item.getModuleId())) {
        assertThat(item.getMenuPoint()).isEqualTo("Kernbereich");
        assertThat(item.getSections()).isNotNull();
        assertThat(item.getSections()).containsKey("collection");
      }
    });

    final MetadataModuleIndexSummaryDto summary = service.getMetadataModuleIndexSummary("de");
    assertThat(summary.getModules()).anySatisfy(item -> {
      if ("dtm-core".equals(item.getModuleId())) {
        assertThat(item.getLabel()).isEqualTo("Core");
        assertThat(item.getMenuPoint()).isEqualTo("Kernbereich");
      }
    });

    final Map<String, String> ids = service.getModuleIds();
    assertThat(ids).containsEntry("DataTrusteeModelCore", "dtm-core");
  }

  @Test
  void loadsMetadataFromYamlFiles() throws IOException {
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files
        .writeString(schemaDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "requestDtoClassName: DataTrusteeModelCore\n"
                + "fields: {}\n");

    Files
        .writeString(langDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "label: Core YAML\n"
                + "menuPoint: YAML Bereich\n"
                + "fields: {}\n");

    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final MetadataService service = new MetadataService(props,
        new ValidationMetadataService(validator), new MetadataConsistencyCheckService());

    final List<MetadataModuleDto> modules = service.listModules("de");
    assertThat(modules).hasSize(1);
    assertThat(modules.getFirst().getModuleId()).isEqualTo("dtm-core");
    assertThat(modules.getFirst().getLabel()).isEqualTo("Core YAML");
    assertThat(modules.getFirst().getMenuPoint()).isEqualTo("YAML Bereich");
  }

  @Test
  void exposesLabelsForAllowUnknownAndAllowNoAnswerWithoutAddingOptions() throws IOException {
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files
        .writeString(schemaDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "fields:\n"
                + "  status:\n"
                + "    name: status\n"
                + "    allowUnknown: true\n"
                + "    allowNoAnswer: true\n"
                + "    options:\n"
                + "      - value: FIXED\n");

    Files
        .writeString(langDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "fields:\n"
                + "  status:\n"
                + "    label: Status\n"
                + "    options:\n"
                + "      - value: FIXED\n"
                + "        label: Fest\n"
                + "      - value: AMBIGUOUS\n"
                + "        label: Unklar\n"
                + "      - value: NONE\n"
                + "        label: Keine Angabe\n");

    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final MetadataService service = new MetadataService(props,
        new ValidationMetadataService(validator), new MetadataConsistencyCheckService());

    final Optional<MetadataFieldDto> field = service.getFieldMetadata("dtm-core", "status", "de");

    assertThat(field).isPresent();
    assertThat(field.get().getAllowUnknownLabel()).isEqualTo("Unklar");
    assertThat(field.get().getAllowNoAnswerLabel()).isEqualTo("Keine Angabe");
    assertThat(field.get().getOptions()).extracting(option -> option.getValue())
        .containsExactly("FIXED");
  }

  @Test
  void loadsResultPresentationFromDedicatedFolders() throws IOException {
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));
    final Path resultSchemaDir = Files.createDirectories(this.tempDir.resolve("dtm-result-schema"));
    final Path resultLangDir = Files
        .createDirectories(this.tempDir.resolve("dtm-result-i18n").resolve("de"));

    Files.writeString(schemaDir.resolve("dtm-core.yaml"),
        "moduleId: dtm-core\nfields: {}\n");
    Files.writeString(langDir.resolve("dtm-core.yaml"),
        "moduleId: dtm-core\nlabel: Core\nfields: {}\n");

    Files.writeString(resultSchemaDir.resolve("compare.yaml"),
        "viewId: compare\n"
            + "sections:\n"
            + "  core:\n"
            + "    fields:\n"
            + "      rightsHolderName: {}\n"
            + "      dataTrusteeName: {}\n");
    Files.writeString(resultLangDir.resolve("compare.yaml"),
        "viewId: compare\n"
            + "sections:\n"
            + "  core:\n"
            + "    label: Akteure\n"
            + "    fields:\n"
            + "      rightsHolderName:\n"
            + "        label: Rechtehalter\n"
            + "      dataTrusteeName:\n"
            + "        label: Datentreuhandmodell\n");

    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final MetadataService service = new MetadataService(props,
        new ValidationMetadataService(validator), new MetadataConsistencyCheckService());

    final DataTrusteeModelPresentationDto presentation = service.getResultPresentation("compare",
        "de");

    assertThat(presentation.getSections()).hasSize(1);
    assertThat(presentation.getSections().getFirst().getKey()).isEqualTo("core");
    assertThat(presentation.getSections().getFirst().getLabel()).isEqualTo("Akteure");
    assertThat(presentation.getSections().getFirst().getFields())
        .containsEntry("rightsHolderName", "Rechtehalter")
        .containsEntry("dataTrusteeName", "Datentreuhandmodell");
  }
}