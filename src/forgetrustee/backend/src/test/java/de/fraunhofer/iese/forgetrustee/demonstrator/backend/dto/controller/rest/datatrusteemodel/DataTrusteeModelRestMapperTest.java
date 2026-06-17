
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardCoreRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

class DataTrusteeWizardRequestMapperTest {

  @Test
  void toModelBuildsOntologyUriFromCoreNameUsingDefaultBaseUri() {
    final DataTrusteeWizardRequestDto dto = DataTrusteeWizardRequestDto
        .builder()
        .core(DataTrusteeWizardCoreRequestDto
            .builder()
            .dataTrusteeName("Münster Trustee 42")
            .dataTrusteeDescription("Beschreibung")
            .dataTrusteeOperator("Operator GmbH")
            .dataTrusteeOperatorAffiliation(Affiliation.ORGANIZATION)
            .rightsHolderName("Rechteinhaber")
            .rightsHolderAffiliation(Affiliation.INDIVIDUAL)
            .rightsHolderIsRepresented("YES")
            .dataOwnerName("Verwalter")
            .dataConsumerName("Nutzer")
            .dataConsumerAffiliation(Affiliation.ORGANIZATION)
            .build())
        .build();

    final DataTrusteeWizardRequestMapper mapper = Mappers
        .getMapper(DataTrusteeWizardRequestMapper.class);
    mapper.setBackendProperties(new BackendProperties());
    final DataTrusteeWizardModel model = mapper.toModel(dto);

    assertThat(model).isNotNull();
    assertThat(model.getCore()).isNotNull();
    assertThat(model.getCore().getOntologyURI())
        .isEqualTo("https://www.example.de/ontology/MuensterTrustee42#");
    assertThat(model.getCore().getDataTrusteeName()).isEqualTo("Münster Trustee 42");
  }

  @Test
  void toModelUsesConfiguredOntologyBaseUri() {
    final BackendProperties properties = new BackendProperties();
    final BackendProperties.OntologyProperties ontologyProperties = new BackendProperties.OntologyProperties();
    ontologyProperties.setBasePath("data");
    ontologyProperties.setBaseUri("https://custom.example/instances");
    properties.setOntology(ontologyProperties);

    final DataTrusteeWizardRequestMapper mapper = Mappers
        .getMapper(DataTrusteeWizardRequestMapper.class);
    mapper.setBackendProperties(properties);

    final DataTrusteeWizardModel model = mapper
        .toModel(DataTrusteeWizardRequestDto
            .builder()
            .core(DataTrusteeWizardCoreRequestDto.builder().dataTrusteeName("Alpha Beta").build())
            .build());

    assertThat(model.getCore().getOntologyURI())
        .isEqualTo("https://custom.example/instances/AlphaBeta#");
  }

  @Test
  void toDtoOmitsOntologyUriButKeepsRemainingCoreFields() {
    final DataTrusteeWizardCore core = new DataTrusteeWizardCore();
    core.setOntologyURI("https://www.example.de/ontology/InternalOnly#");
    core.setDataTrusteeName("Internal Only");
    core.setDataTrusteeOperator("Operator");
    core.setDataTrusteeOperatorAffiliation(Affiliation.ORGANIZATION);
    core.setRightsHolderName("Holder");
    core.setRightsHolderAffiliation(Affiliation.INDIVIDUAL);
    core.setRightsHolderIsRepresented("NO");
    core.setDataConsumerAffiliation(Affiliation.AMBIGUOUS);

    final DataTrusteeWizardModel model = new DataTrusteeWizardModel();
    model.setCore(core);

    final DataTrusteeWizardRequestMapper mapper = Mappers
        .getMapper(DataTrusteeWizardRequestMapper.class);
    mapper.setBackendProperties(new BackendProperties());
    final DataTrusteeWizardRequestDto dto = mapper.toDto(model);

    assertThat(dto).isNotNull();
    assertThat(dto.getCore()).isNotNull();
    assertThat(dto.getCore().getDataTrusteeName()).isEqualTo("Internal Only");

    assertThat(DataTrusteeWizardCoreRequestDto.class.getDeclaredFields())
        .extracting("name")
        .doesNotContain("ontologyURI");
  }

  @Test
  void toDtoUsesDedicatedDataCategoryFieldNames() {
    final DataTrusteeWizardModel model = new DataTrusteeWizardModel();
    model.setLegal(de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardData
        .builder()
        .dataCategory(TitleDescriptionDTO.builder()
            .title("Telemetriedaten")
            .description("Fahrzeugdaten")
            .build())
        .build());

    final DataTrusteeWizardRequestMapper mapper = Mappers
        .getMapper(DataTrusteeWizardRequestMapper.class);
    mapper.setBackendProperties(new BackendProperties());

    final DataTrusteeWizardRequestDto dto = mapper.toDto(model);

    assertThat(dto.getData()).isNotNull();
    assertThat(dto.getData().getDataCategory()).isNotNull();
    assertThat(dto.getData().getDataCategory().getDataCategoryName()).isEqualTo("Telemetriedaten");
    assertThat(dto.getData().getDataCategory().getDataCategoryDescription()).isEqualTo(
        "Fahrzeugdaten");
  }

  @Test
  void requestJsonWithDedicatedDataCategoryFieldsDeserializesCorrectly() throws IOException {
    final String json = """
        {
          "core": {
            "dataTrusteeName": "Trustee",
            "dataTrusteeOperator": "Operator",
            "dataTrusteeOperatorAffiliation": "ORGANIZATION",
            "rightsHolderName": "RightsHolder",
            "rightsHolderAffiliation": "INDIVIDUAL",
            "rightsHolderIsRepresented": "NO",
            "dataOwnerName": "Owner",
            "dataConsumerName": "Consumer",
            "dataConsumerAffiliation": "ORGANIZATION"
          },
          "data": {
            "dataCategory": {
              "dataCategoryName": "Telemetriedaten",
              "dataCategoryDescription": "Fahrzeugdaten"
            }
          },
          "implementation": {},
          "business": {},
          "objectives": {}
        }
        """;

    final DataTrusteeWizardRequestDto dto = new ObjectMapper().readValue(json,
        DataTrusteeWizardRequestDto.class);

    assertThat(dto.getData()).isNotNull();
    assertThat(dto.getData().getDataCategory()).isNotNull();
    assertThat(dto.getData().getDataCategory().getDataCategoryName()).isEqualTo("Telemetriedaten");
    assertThat(dto.getData().getDataCategory().getDataCategoryDescription()).isEqualTo(
        "Fahrzeugdaten");
  }
}
