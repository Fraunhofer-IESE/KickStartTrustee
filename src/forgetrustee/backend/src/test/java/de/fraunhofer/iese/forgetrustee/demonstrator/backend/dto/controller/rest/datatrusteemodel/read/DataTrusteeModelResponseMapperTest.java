
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelLegal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessDomain;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.FundingSource;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.PayMethods;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ArchitectureType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataTrusteeCategory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ReceptionTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentEnteredBy;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.DataTrusteeGoal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataConsumer;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataHolder;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

class DataTrusteeModelResponseMapperTest {

  private final DataTrusteeModelResponseMapper mapper = Mappers
      .getMapper(DataTrusteeModelResponseMapper.class);

  @Test
  void mapsDomainModelToResponseDto() {
    final TitleDescriptionDTO legalDetail = TitleDescriptionDTO
        .builder()
        .title("Einwilligung")
        .description("Einwilligung der betroffenen Person")
        .build();
    final Map<ProcessingBasis, List<TitleDescriptionDTO>> processingBases = new EnumMap<>(
        ProcessingBasis.class);
    processingBases.put(ProcessingBasis.CONSENT, List.of(legalDetail));

    final TitleDescriptionDTO customSecurity = TitleDescriptionDTO
        .builder()
        .title("TLS")
        .description("Transportverschlüsselung")
        .build();

    final DataTrusteeModel domainModel = DataTrusteeModel
        .builder()
        .core(DataTrusteeModelCore
            .builder()
            .ontologyURI("https://example.org/ontology/Trustee#")
            .dataTrusteeName("Trustee")
            .dataTrusteeDescription("Beschreibung")
            .dataTrusteeOperator("Operator GmbH")
            .dataTrusteeOperatorAffiliation(Affiliation.ORGANIZATION)
            .rightsHolderName("Rechteinhaber")
            .rightsHolderAffiliation(Affiliation.INDIVIDUAL)
            .rightsHolderIsRepresented("YES")
            .dataOwnerName("Datengeber")
            .dataConsumerName("Datennutzer")
            .dataConsumerAffiliation(Affiliation.ORGANIZATION)
            .dataCategoryName("Telemetriedaten")
            .dataCategoryDescription("Fahrzeugdaten")
            .containPersonalInformation("YES")
            .specialPersonalInformation("NO")
            .containTradeSecrets("NO")
            .build())
        .legal(DataTrusteeModelLegal
            .builder()
            .processingBases(processingBases)
            .consentType(ConsentType.GENERAL_CONSENT)
            .obtainingConsentBy("Portal")
            .consentEnteredBy(ConsentEnteredBy.RIGHTS_OWNER)
            .build())
        .data(DataTrusteeModelData
            .builder()
            .dataTrusteeCategory(DataTrusteeCategory.ACCESS_DATA_TRUSTEE)
            .architectureType(ArchitectureType.CENTRAL_ROUTER)
            .receptionTechnologies(EnumSet.of(ReceptionTechnique.API))
            .customReceptionSecurityTechniques(List.of(customSecurity))
            .targetSystem("Zielsystem")
            .build())
        .business(DataTrusteeModelBusiness
            .builder()
            .businessDomains(EnumSet.of(BusinessDomain.HEALTHCARE))
            .businessModel(BusinessModel.LIMITED_LIABILITY_COMPANY)
            .fundingSources(EnumSet.of(FundingSource.EU))
            //            .paymentMethods(EnumSet.of(PayMethods.NONE))
            .paymentMethodDataOwner(PayMethods.NONE)
            .paymentMethodDataConsumer(PayMethods.NONE)
            .build())
        .objectives(DataTrusteeModelObjectives
            .builder()
            .dataTrusteeGoals(EnumSet.of(DataTrusteeGoal.INNOVATION))
            .motivationDataHolder(EnumSet.of(MotivationDataHolder.VALUE_CREATION))
            .motivationDataConsumer(EnumSet.of(MotivationDataConsumer.OPTIMIZATION))
            .build())
        .build();

    final DataTrusteeModelResponseDto responseDto = this.mapper.toDto(domainModel);

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.getCore()).isNotNull();
    assertThat(responseDto.getCore().getOntologyURI())
        .isEqualTo("https://example.org/ontology/Trustee#");
    assertThat(responseDto.getCore().getDataTrusteeName().getValue()).isEqualTo("Trustee");
    assertThat(responseDto.getCore().getDataCategoryName().getValue()).isEqualTo("Telemetriedaten");
    assertThat(responseDto.getCore().getContainTradeSecrets().getValue()).isEqualTo("NO");
    assertThat(responseDto.getLegal()).isNotNull();
    assertThat(responseDto.getLegal().getProcessingBases())
        .singleElement()
        .satisfies(item -> {
          assertThat(item.getValue()).isEqualTo(ProcessingBasis.CONSENT);
          assertThat(item.getDetails())
              .singleElement()
              .satisfies(detail -> {
                assertThat(detail.getTitle()).isEqualTo("Einwilligung");
                assertThat(detail.getDescription()).isEqualTo("Einwilligung der betroffenen Person");
              });
        });
    assertThat(responseDto.getLegal().getConsentType().getValue())
        .isEqualTo(ConsentType.GENERAL_CONSENT);
    assertThat(responseDto.getData()).isNotNull();
    assertThat(responseDto.getData().getReceptionTechnologies())
        .singleElement()
        .satisfies(item -> assertThat(item.getValue()).isEqualTo(ReceptionTechnique.API));
    assertThat(responseDto.getData().getCustomReceptionSecurityTechniques())
        .singleElement()
        .satisfies(item -> assertThat(item.getTitle()).isEqualTo("TLS"));
    assertThat(responseDto.getBusiness().getBusinessDomains())
        .singleElement()
        .satisfies(item -> assertThat(item.getValue()).isEqualTo(BusinessDomain.HEALTHCARE));
    assertThat(responseDto.getBusiness().getBusinessModel().getValue())
        .isEqualTo(BusinessModel.LIMITED_LIABILITY_COMPANY);
    assertThat(responseDto.getBusiness().getFundingSources())
        .singleElement()
        .satisfies(item -> assertThat(item.getValue()).isEqualTo(FundingSource.EU));
    assertThat(responseDto.getBusiness().getPaymentMethodDataOwner().getValue())
        .isEqualTo(PayMethods.NONE);
    assertThat(responseDto.getObjectives().getMotivationDataConsumer())
        .singleElement()
        .satisfies(item -> assertThat(item.getValue()).isEqualTo(MotivationDataConsumer.OPTIMIZATION));
  }
}
