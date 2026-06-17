package de.fraunhofer.iese.forgetrustee.demonstrator.backend.repository.datatrusteemodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestMapper;

import org.mapstruct.factory.Mappers;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class DataTrusteeModelRepositoryTest {

  private static final String ONTOLOGY_BASE_URI = "https://example.org/ontology/";

  @TempDir
  Path tempDir;

  @Test
  void loadsAllModelsAndSupportsLookupByFolderId() throws IOException {
    this.writeModelFolder("alpha-model", "Alpha Trustee", "Alpha description");
    this.writeModelFolder("beta-model", "Beta Trustee", "Beta description");
    Files.createDirectories(this.tempDir.resolve("uploads").resolve("nested"));

    final DataTrusteeModelRepository repository = this.createRepository(this.tempDir);

    final List<?> models = repository.findAll();
    final Map<String, ?> modelsById = repository.findAllById();

    assertThat(models).hasSize(2);
    assertThat(modelsById).containsOnlyKeys("alpha-model", "beta-model");
    assertThat(repository.findById("alpha-model"))
        .get()
        .extracting(model -> model.getCore().getDataTrusteeName())
        .isEqualTo("Alpha Trustee");
    assertThat(repository.findById("beta-model"))
        .get()
        .extracting(model -> model.getCore().getOntologyURI())
        .isEqualTo(ONTOLOGY_BASE_URI + "BetaTrustee#");
    assertThat(repository.findById("missing")).isEmpty();
  }

  @Test
  void refreshReloadsModifiedFiles() throws IOException {
    final Path modelDirectory = this.tempDir.resolve("refreshable-model");
    Files.createDirectories(modelDirectory);
    final Path modelFile = modelDirectory.resolve("refreshable.json");
    Files.writeString(modelFile, this.minimalTrusteeJson("Refresh Trustee", "Version 1"));

    final DataTrusteeModelRepository repository = this.createRepository(this.tempDir);

    assertThat(repository.findById("refreshable-model"))
        .get()
        .extracting(model -> model.getCore().getDataTrusteeDescription())
        .isEqualTo("Version 1");

    Files.writeString(modelFile, this.minimalTrusteeJson("Refresh Trustee", "Version 2"));

    assertThat(repository.findById("refreshable-model"))
        .get()
        .extracting(model -> model.getCore().getDataTrusteeDescription())
        .isEqualTo("Version 1");

    repository.refresh();

    assertThat(repository.findById("refreshable-model"))
        .get()
        .extracting(model -> model.getCore().getDataTrusteeDescription())
        .isEqualTo("Version 2");
  }

  @Test
  void returnsEmptyResultWhenBaseDirectoryDoesNotExist() {
    final DataTrusteeModelRepository repository = this.createRepository(this.tempDir.resolve("missing"));

    assertThat(repository.findAll()).isEmpty();
    assertThat(repository.findAllById()).isEmpty();
    assertThat(repository.findById("whatever")).isEmpty();
  }

  @Test
  void failsWhenJsonInModelFolderIsMalformed() throws IOException {
    final Path modelDirectory = this.tempDir.resolve("broken-model");
    Files.createDirectories(modelDirectory);
    Files.writeString(modelDirectory.resolve("broken.json"), "not valid json");

    final DataTrusteeModelRepository repository = this.createRepository(this.tempDir);

    assertThatThrownBy(repository::findAll)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to read data trustee model JSON file");
  }

  private void writeModelFolder(String folderName, String trusteeName, String trusteeDescription)
      throws IOException {
    final Path modelDirectory = this.tempDir.resolve(folderName);
    Files.createDirectories(modelDirectory);
    Files.writeString(modelDirectory.resolve(folderName + ".json"),
        this.minimalTrusteeJson(trusteeName, trusteeDescription));
  }

  private String minimalTrusteeJson(String trusteeName, String trusteeDescription) {
    return """
        {
          "core": {
            "dataTrusteeName": "%s",
            "dataTrusteeDescription": "%s",
            "dataTrusteeOperator": "Operator %s",
            "dataTrusteeOperatorAffiliation": "ORGANIZATION",
            "rightsHolderName": "Rights Holder %s",
            "rightsHolderAffiliation": "INDIVIDUAL",
            "rightsHolderIsRepresented": "false",
            "dataOwnerName": "Data Owner %s",
            "dataConsumerName": "Data Consumer %s",
            "dataConsumerAffiliation": "ORGANIZATION"
          },
          "data": {
            "dataCategory": {
              "dataCategoryName": "Kategorie %s",
              "dataCategoryDescription": "%s"
            },
            "containPersonalInformation": "true",
            "specialPersonalInformation": "false",
            "containTradeSecrets": "false"
          },
          "implementation": {},
          "business": {},
          "objectives": {}
        }
        """.formatted(trusteeName, trusteeDescription, trusteeName, trusteeName, trusteeName,
        trusteeName, trusteeName, trusteeDescription);
  }

  private DataTrusteeModelRepository createRepository(Path basePath) {
    final BackendProperties properties = new BackendProperties();
    final BackendProperties.DataTrusteeModelProperties dataTrusteeModelProperties = new BackendProperties.DataTrusteeModelProperties();
    dataTrusteeModelProperties.setBasePath(basePath.toString());
    properties.setDataTrusteeModel(dataTrusteeModelProperties);
    properties.getOntology().setBaseUri(ONTOLOGY_BASE_URI);

    final DataTrusteeWizardRequestMapper wizardRequestMapper = Mappers
        .getMapper(DataTrusteeWizardRequestMapper.class);
    wizardRequestMapper.setBackendProperties(properties);

    final DataTrusteeModelMapper dataTrusteeModelMapper = Mappers.getMapper(DataTrusteeModelMapper.class);

    return new DataTrusteeModelRepository(properties, new ObjectMapper(), wizardRequestMapper,
        dataTrusteeModelMapper);
  }
}
