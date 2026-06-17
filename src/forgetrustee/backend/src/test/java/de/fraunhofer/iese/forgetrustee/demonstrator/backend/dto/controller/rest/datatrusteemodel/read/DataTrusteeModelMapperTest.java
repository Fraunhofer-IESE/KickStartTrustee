
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardImplementation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardObjectives;
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

class DataTrusteeModelMapperTest {

  private final DataTrusteeModelMapper mapper = Mappers.getMapper(DataTrusteeModelMapper.class);

  @Test
  void mapsWizardModelToDomainModel() {
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

    final DataTrusteeWizardModel wizardModel = DataTrusteeWizardModel
        .builder()
        .core(DataTrusteeWizardCore
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
            .build())
        .legal(DataTrusteeWizardData
            .builder()
            .dataCategory(TitleDescriptionDTO
                .builder()
                .title("Telemetriedaten")
                .description("Fahrzeugdaten")
                .build())
            .containPersonalInformation("YES")
            .specialPersonalInformation("NO")
            .containTradeSecrets("NO")
            .processingBases(processingBases)
            .consentType(ConsentType.GENERAL_CONSENT)
            .obtainingConsentBy("Portal")
            .consentEnteredBy(ConsentEnteredBy.RIGHTS_OWNER)
            .build())
        .data(DataTrusteeWizardImplementation
            .builder()
            .dataTrusteeCategory(DataTrusteeCategory.ACCESS_DATA_TRUSTEE)
            .architectureType(ArchitectureType.CENTRAL_ROUTER)
            .receptionTechnologies(EnumSet.of(ReceptionTechnique.API))
            .customReceptionSecurityTechniques(List.of(customSecurity))
            .targetSystem("Zielsystem")
            .build())
        .business(DataTrusteeWizardBusiness
            .builder()
            .businessDomains(EnumSet.of(BusinessDomain.HEALTHCARE))
            .businessModel(BusinessModel.LIMITED_LIABILITY_COMPANY)
            .fundingSources(EnumSet.of(FundingSource.EU))
            .paymentMethodDataOwner(PayMethods.NONE)
            .paymentMethodDataConsumer(PayMethods.NONE)
            .build())
        .objectives(DataTrusteeWizardObjectives
            .builder()
            .dataTrusteeGoals(EnumSet.of(DataTrusteeGoal.INNOVATION))
            .motivationDataHolder(EnumSet.of(MotivationDataHolder.VALUE_CREATION))
            .motivationDataConsumer(EnumSet.of(MotivationDataConsumer.OPTIMIZATION))
            .build())
        .build();

    final var domainModel = this.mapper.toModel(wizardModel);

    assertThat(domainModel).isNotNull();
    assertThat(domainModel.getCore()).isNotNull();
    assertThat(domainModel.getCore().getOntologyURI())
        .isEqualTo("https://example.org/ontology/Trustee#");
    assertThat(domainModel.getCore().getDataTrusteeName()).isEqualTo("Trustee");
    assertThat(domainModel.getCore().getDataCategoryName()).isEqualTo("Telemetriedaten");
    assertThat(domainModel.getCore().getContainTradeSecrets()).isEqualTo("NO");
    assertThat(domainModel.getLegal()).isNotNull();
    assertThat(domainModel.getLegal().getProcessingBases()).containsKey(ProcessingBasis.CONSENT);
    assertThat(domainModel.getLegal().getProcessingBases().get(ProcessingBasis.CONSENT))
        .singleElement()
        .satisfies(item -> assertThat(item.getTitle()).isEqualTo("Einwilligung"));
    assertThat(domainModel.getData()).isNotNull();
    assertThat(domainModel.getData().getReceptionTechnologies())
        .containsExactly(ReceptionTechnique.API);
    assertThat(domainModel.getData().getCustomReceptionSecurityTechniques())
        .singleElement()
        .satisfies(item -> assertThat(item.getTitle()).isEqualTo("TLS"));
    assertThat(domainModel.getBusiness().getBusinessDomains())
        .containsExactly(BusinessDomain.HEALTHCARE);
    assertThat(domainModel.getObjectives().getMotivationDataConsumer())
        .containsExactly(MotivationDataConsumer.OPTIMIZATION);
  }
}
