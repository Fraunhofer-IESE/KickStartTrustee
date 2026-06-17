
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataFieldDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.metadata.MetadataModuleDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class MetadataServiceSizeEnrichmentTest {

  @TempDir
  Path tempDir;

  @Test
  void enrichesFieldWithMaxLengthFromSizeAnnotation() throws Exception {
    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files
        .writeString(schemaDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "requestDtoClassName: de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore\n"
                + "fields:\n"
                + "  dataTrusteeName:\n"
                + "    name: dataTrusteeName\n");

    Files
        .writeString(langDir.resolve("dtm-core.yaml"),
            "moduleId: dtm-core\n"
                + "label: Core\n"
                + "tooltip: Test\n"
                + "helpText: Test\n"
                + "examples: []\n"
                + "fields:\n"
                + "  dataTrusteeName:\n"
                + "    label: Name\n"
                + "    tooltip: Test\n"
                + "    helpText: Test\n"
                + "    examples: []\n");

    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final ValidationMetadataService validationMetadataService = new ValidationMetadataService(validator);
    final MetadataService service = new MetadataService(props, validationMetadataService,
        new MetadataConsistencyCheckService());

    final Optional<MetadataFieldDto> field = service
        .getFieldMetadata("dtm-core", "dataTrusteeName", "de");

    assertThat(field).isPresent();
    assertThat(field.get().getMaxLength()).isEqualTo(50);
  }

  @Test
  void enrichesListChildWithMaxLengthUsingMetadataNameMapping() throws Exception {
    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files
        .writeString(schemaDir.resolve("implementation.yaml"),
            "moduleId: implementation\n"
                + "requestDtoClassName: DataTrusteeWizardImplementationRequestDto\n"
                + "fields:\n"
                + "  customReceptionSecurityMeasures:\n"
                + "    name: customReceptionSecurityTechniques\n"
                + "    type: TEXT_WITH_DESCRIPTION_LIST\n"
                + "    fields:\n"
                + "      customReceptionSecurityMeasureName:\n"
                + "        name: title\n"
                + "        type: TEXT\n"
                + "      customReceptionSecurityMeasureDescription:\n"
                + "        name: description\n"
                + "        type: TEXT\n");

    Files
        .writeString(langDir.resolve("implementation.yaml"),
            "moduleId: implementation\n"
                + "fields:\n"
                + "  customReceptionSecurityMeasures:\n"
                + "    fields:\n"
                + "      customReceptionSecurityMeasureName:\n"
                + "        label: Name\n"
                + "      customReceptionSecurityMeasureDescription:\n"
                + "        label: Beschreibung\n");

    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final ValidationMetadataService validationMetadataService = new ValidationMetadataService(validator);
    final MetadataService service = new MetadataService(props, validationMetadataService,
        new MetadataConsistencyCheckService());

    final Optional<MetadataFieldDto> titleField = service
        .getFieldMetadata("implementation", "customReceptionSecurityMeasures.customReceptionSecurityMeasureName", "de");

    final Optional<MetadataFieldDto> descriptionField = service
        .getFieldMetadata("implementation",
            "customReceptionSecurityMeasures.customReceptionSecurityMeasureDescription", "de");

    assertThat(titleField).isPresent();
    assertThat(titleField.get().getMaxLength()).isEqualTo(100);
    assertThat(descriptionField).isPresent();
    assertThat(descriptionField.get().getMaxLength()).isEqualTo(2000);
  }

  @Test
  void enrichesListChildInsideSubsectionWithMaxLength() throws Exception {
    final BackendProperties props = new BackendProperties();
    final BackendProperties.MetadataProperties metadataProps = new BackendProperties.MetadataProperties();
    final Path schemaDir = Files.createDirectories(this.tempDir.resolve("dtm-schema"));
    final Path langDir = Files.createDirectories(this.tempDir.resolve("dtm-i18n").resolve("de"));

    Files
        .writeString(schemaDir.resolve("implementation.yaml"),
            "moduleId: implementation\n"
                + "requestDtoClassName: DataTrusteeWizardImplementationRequestDto\n"
                + "sections:\n"
                + "  dataFlow:\n"
                + "    subsections:\n"
                + "      reception:\n"
                + "        fields:\n"
                + "          customReceptionSecurityMeasures:\n"
                + "            name: customReceptionSecurityTechniques\n"
                + "            type: TEXT_WITH_DESCRIPTION_LIST\n"
                + "            fields:\n"
                + "              customReceptionSecurityMeasureName:\n"
                + "                name: title\n"
                + "                type: TEXT\n"
                + "              customReceptionSecurityMeasureDescription:\n"
                + "                name: description\n"
                + "                type: TEXT\n");

    Files
        .writeString(langDir.resolve("implementation.yaml"),
            "moduleId: implementation\n"
                + "sections:\n"
                + "  dataFlow:\n"
                + "    subsections:\n"
                + "      reception:\n"
                + "        fields:\n"
                + "          customReceptionSecurityMeasures:\n"
                + "            fields:\n"
                + "              customReceptionSecurityMeasureName:\n"
                + "                label: Name\n"
                + "              customReceptionSecurityMeasureDescription:\n"
                + "                label: Beschreibung\n");

    metadataProps.setBasePath(this.tempDir.toString());
    props.setMetadata(metadataProps);

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final MetadataService service = new MetadataService(props,
        new ValidationMetadataService(validator), new MetadataConsistencyCheckService());

    final List<MetadataModuleDto> modules = service.listModules("de");
    assertThat(modules).hasSize(1);
    final MetadataFieldDto listField = modules
        .getFirst()
        .getSections()
        .get("dataFlow")
        .getSubsections()
        .get("reception")
        .getFields()
        .get("customReceptionSecurityMeasures");

    assertThat(listField.getFields().get("customReceptionSecurityMeasureName").getMaxLength())
        .isEqualTo(100);
    assertThat(
        listField.getFields().get("customReceptionSecurityMeasureDescription").getMaxLength())
            .isEqualTo(2000);
  }
}
