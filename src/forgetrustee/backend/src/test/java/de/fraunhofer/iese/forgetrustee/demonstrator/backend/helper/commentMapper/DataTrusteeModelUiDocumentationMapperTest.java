package de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.TitleDescriptionRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedValueRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedProcessingBasisRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelBusinessResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelCoreResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelDataResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelLegalResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelObjectivesResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessDomain;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataTrusteeCategory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.DataTrusteeGoal;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DataTrusteeModelUiDocumentationMapperTest {

  @Test
  void enrichesLabelsAndCommentsFromOntologyDocs() {
    final OntologyDocumentationDto docs = new OntologyDocumentationDto();
    addDoc(docs, "DataTrustee", "Datentreuhänder", "Kommentar Datentreuhänder");
    addDoc(docs, "AffectedParty", "Betroffene Partei", "Kommentar Betroffene Partei");
    addDoc(docs, ontologyKey(Affiliation.ORGANIZATION.getUri()), "Organisation", "Kommentar Organisation");
    addDoc(docs, ontologyKey(DataTrusteeCategory.ACCESS_DATA_TRUSTEE.getUri()), "Zugangstreuhand", "Kommentar Kategorie");
    addDoc(docs, ontologyKey(ProcessingBasis.CONSENT.getUri()), "Einwilligung", "Kommentar Processing Basis");
    addDoc(docs, ontologyKey(BusinessDomain.HEALTHCARE.getUri()), "Gesundheitswesen", "Kommentar Business Domain");
    addDoc(docs, "Innovation", "Innovation", "Kommentar Innovation");
    addDoc(docs, ontologyKey(ConsentType.GENERAL_CONSENT.getImplementationUri()), "Allgemeine Einwilligung", "Kommentar Consent");

    final DataTrusteeModelResponseDto dto = DataTrusteeModelResponseDto
        .builder()
        .core(DataTrusteeModelCoreResponseDto
            .builder()
            .dataTrusteeName(CommentedValueRestDto.<String>builder().value("Trustee").build())
            .rightsHolderAffiliation(CommentedValueRestDto.<Affiliation>builder().value(Affiliation.ORGANIZATION).build())
            .build())
        .data(DataTrusteeModelDataResponseDto
            .builder()
            .dataTrusteeCategory(CommentedValueRestDto.<DataTrusteeCategory>builder().value(DataTrusteeCategory.ACCESS_DATA_TRUSTEE).build())
            .build())
        .legal(DataTrusteeModelLegalResponseDto
            .builder()
            .processingBases(List.of(CommentedProcessingBasisRestDto.builder().value(ProcessingBasis.CONSENT).build()))
            .consentType(CommentedValueRestDto.<ConsentType>builder().value(ConsentType.GENERAL_CONSENT).build())
            .build())
        .business(DataTrusteeModelBusinessResponseDto
            .builder()
            .businessDomains(List.of(CommentedValueRestDto.<BusinessDomain>builder().value(BusinessDomain.HEALTHCARE).build()))
            .build())
        .objectives(DataTrusteeModelObjectivesResponseDto
            .builder()
            .dataTrusteeGoals(List.of(CommentedValueRestDto.<DataTrusteeGoal>builder().value(DataTrusteeGoal.INNOVATION).build()))
            .build())
        .build();

    DataTrusteeModelUiDocumentationMapper.enrich(dto, docs, "de");

    assertThat(dto.getCore().getDataTrusteeName().getLabel()).isEqualTo("Datentreuhänder");
    assertThat(dto.getCore().getDataTrusteeName().getComment()).isEqualTo("Kommentar Datentreuhänder");
    assertThat(dto.getCore().getRightsHolderAffiliation().getLabel()).isEqualTo("Organisation");
    assertThat(dto.getData().getDataTrusteeCategory().getLabel()).isEqualTo("Zugangstreuhand");
    assertThat(dto.getLegal().getProcessingBases()).singleElement().satisfies(item -> {
      assertThat(item.getLabel()).isEqualTo("Einwilligung");
      assertThat(item.getComment()).isEqualTo("Kommentar Processing Basis");
    });
    assertThat(dto.getBusiness().getBusinessDomains()).singleElement().satisfies(item -> {
      assertThat(item.getLabel()).isEqualTo("Gesundheitswesen");
      assertThat(item.getComment()).isEqualTo("Kommentar Business Domain");
    });
    assertThat(dto.getLegal().getConsentType().getLabel()).isEqualTo("Allgemeine Einwilligung");
    assertThat(dto.getLegal().getConsentType().getComment()).isEqualTo("Kommentar Consent");
    assertThat(dto.getObjectives().getDataTrusteeGoals()).singleElement().satisfies(item -> {
      assertThat(item.getLabel()).isEqualTo("Innovation");
      assertThat(item.getComment()).isEqualTo("Kommentar Innovation");
    });
  }

  private static void addDoc(
      OntologyDocumentationDto docs,
      String key,
      String label,
      String comment
  ) {
    final OntologyDocumentationEntryDto entry = new OntologyDocumentationEntryDto();
    entry.setLabels(Map.of("de", label));
    entry.setComments(Map.of("de", comment));
    docs.put(key, entry);
  }

  private static String ontologyKey(String uri) {
    return OntologyCommentMappingSupport.extractOntologyKey(uri);
  }
}
