
/*
 * Copyright (C) 2026 Fraunhofer IESE
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.impl;
/* Created by chwalek on 14.08.2025 */

import static de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText.PROP_PROCESSING_BASIS_DESCRIPTION;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.factory.OntologyFactory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation.OntologyNavigation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation.OntologySHACLConverter_Impl;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.translater.OntologyTranslater;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelLegal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardImplementation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclReportDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclResultDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.ConfigurationStrings;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.DatabaseConnection;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyBuilderService;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyWizardService;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.ServiceRunDatabase;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.executiveSummaryGenerator.ExecutiveSummaryNarrativeGenerator;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessDomain;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.FundingSource;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.AnalysisTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataLifecycle;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ForwardingTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.PreparationTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ReceptionTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.SecurityMeasure;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentEnteredBy;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.DataTrusteeGoal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataConsumer;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataHolder;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationRightsHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontapi.model.OntIndividual;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.validation.ReportEntry;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OntologyWizardService_Impl implements OntologyWizardService {

  private final OntologyNavigation navigation;

  private final OntologyFactory factory;

  private final OntologyBuilderService builder;

  private final OntologyTranslater translater;

  private final DatabaseConnection db;

  private final ServiceRunDatabase repo;

  private ExecutiveSummaryNarrativeGenerator executiveSummaryNarrativeGenerator;

  @Override
  public OntModel buildFromDataTrusteeModel(DataTrusteeWizardModel model) {

    // zentrale Validierung (wirft IllegalArgumentException bei Fehlern)
    final DataTrusteeWizardCore core = this.validateAndGetCore(model);
    final String ontologyURI = core.getOntologyURI();
    final String lang = ConfigurationStrings.lang_setting;

    //Ontologie-Instanz anlegen (falls noch nicht vorhanden)
    final Model instanceModel = this.createNewInstanceOfOntology(ontologyURI);

    //Datentreuhänder anlegen (Name ist nach Validierung garantiert vorhanden)
    final ForgeIndividual dataTrustee = this
        .createDataTrustee(core.getDataTrusteeName(), ontologyURI, lang);

    //Core-Felder als Literale ablegen
    this
        .addLiteralIfPresent(dataTrustee.getUri(),
            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#dataTrusteeDescription",
            core.getDataTrusteeDescription(), lang, ontologyURI);

    final ForgeIndividual dataTrusteeOperator = this
        .createIndividual(core.getDataTrusteeOperator(), ontologyURI,
            Affiliation.ORGANIZATION.getUri(), "de");
    this
        .addProperty(dataTrusteeOperator.getUri(),
            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
            Affiliation.ORGANIZATION.getUri(), ontologyURI, false);
    this
        .addProperty(dataTrustee.getUri(),
            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasDataTrusteeOperator",
            dataTrusteeOperator.getUri(), ontologyURI, true);
    this
            .addProperty(dataTrusteeOperator.getUri(),
                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#isOperatorOfDataTrustee",
                    dataTrustee.getUri(), ontologyURI, true);

    //Akteure (aus Core) anlegen
    // createActorIfPresent bleibt, um robust zu bleiben, falls Akteure doch mal optional werden sollen
    ForgeIndividual rightsHolder = null;
    if (core.getRightsHolderName() != null && !core.getRightsHolderName().isBlank()) {
      rightsHolder = this
          .createActorIfPresent(core.getRightsHolderName(), ontologyURI,
              StaticText.FORGETRUSTEE_AFFECTED_PARTY, lang);
      if (Affiliation.ORGANIZATION.equals(core.getRightsHolderAffiliation())) {
        this
            .addProperty(rightsHolder.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
                Affiliation.ORGANIZATION.getUri(), ontologyURI, false);
      } else if (Affiliation.INDIVIDUAL.equals(core.getRightsHolderAffiliation())) {
        this
            .addProperty(rightsHolder.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
                Affiliation.INDIVIDUAL.getUri(), ontologyURI, false);
      } else if (Affiliation.AMBIGUOUS.equals(core.getRightsHolderAffiliation())) {
        this
            .addProperty(rightsHolder.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
                Affiliation.AMBIGUOUS.getUri(), ontologyURI, false);
      }

      if ("NO".equals(core.getRightsHolderIsRepresented())) {
        this
            .addNewSubtype(ontologyURI, rightsHolder.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner");
      }

      this
          .addProperty(rightsHolder.getUri(),
              "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isAffectedPartyIn",
              dataTrustee.getUri(), ontologyURI, true);
      this
              .addProperty(dataTrustee.getUri(),
                      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#hasAffectedParty",
                      rightsHolder.getUri(), ontologyURI, true);
    }

    ForgeIndividual dataOwner = null;
    if (core.getDataOwnerName() != null && !core.getDataOwnerName().isBlank()
        && ("true".equals(core.getRightsHolderIsRepresented())
            || "YES".equals(core.getRightsHolderIsRepresented()))) {
      dataOwner = this
          .createActorIfPresent(core.getDataOwnerName(), ontologyURI,
              StaticText.FORGETRUSTEE_DATA_OWNER, lang);

      this
          .addProperty(dataOwner.getUri(),
              "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isDataOwnerOf",
              dataTrustee.getUri(), ontologyURI, true);
      this
              .addProperty(dataTrustee.getUri(),
                      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#hasDataOwner",
                       dataOwner.getUri(), ontologyURI, true);


      this
          .addProperty(dataOwner.getUri(),
              "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
              "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Organisation",
              ontologyURI, false);
    } else if (rightsHolder != null) {
      this
          .addNewSubtype(ontologyURI, rightsHolder.getUri(),
              "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner");
      this
              .addProperty(rightsHolder.getUri(),
                      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isDataOwnerOf",
                      dataTrustee.getUri(), ontologyURI, true);
      this
              .addProperty(dataTrustee.getUri(),
                      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#hasDataOwner",
                      rightsHolder.getUri(), ontologyURI, true);
    }

    ForgeIndividual dataConsumer = null;
    if (core.getDataConsumerName() != null && !core.getDataConsumerName().isBlank()) {
      dataConsumer = this
          .createActorIfPresent(core.getDataConsumerName(), ontologyURI,
              StaticText.FORGETRUSTEE_DATA_CONSUMER, lang);
      this
          .addProperty(dataConsumer.getUri(),
              "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isDataConsumerOf",
              dataTrustee.getUri(), ontologyURI, true);
      this.addProperty(dataTrustee.getUri(),
                      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#hasDataConsumer",
              dataConsumer.getUri(), ontologyURI, true);

      if (Affiliation.ORGANIZATION.equals(core.getDataConsumerAffiliation())) {
        this
            .addProperty(dataConsumer.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Organisation",
                ontologyURI, false);
      } else if (Affiliation.INDIVIDUAL.equals(core.getDataConsumerAffiliation())) {
        this
            .addProperty(dataConsumer.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Individual",
                ontologyURI, false);
      }
    }

    // 7) Datenkategorie anlegen
    final DataTrusteeWizardData legal = model.getLegal();
    ForgeIndividual dataCategory = null;
    if (legal.getDataCategory().getTitle() != null
        && !legal.getDataCategory().getTitle().isBlank()) {
      dataCategory = this
          .createDataInOntology(legal.getDataCategory().getTitle(), ontologyURI, lang);
      if (legal.getDataCategory().getDescription() != null
          && !legal.getDataCategory().getDescription().isBlank()) {
        this
            .addLiteralIfPresent(dataCategory.getUri(), RDFS.comment.getURI(),
                legal.getDataCategory().getDescription(), lang, ontologyURI);

      }
      final String personalData = legal.getContainPersonalInformation();
      final String specialPersonalData = legal.getSpecialPersonalInformation();
      final String tradeSecretData = legal.getContainTradeSecrets();

      if (core.getRightsHolderName() != null) {
        this
            .addProperty(dataCategory.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isRightlyOwnedBy",
                rightsHolder.getUri(), ontologyURI, true);
        this
                .addProperty(rightsHolder.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#hasRightsOf",
                        dataCategory.getUri(), ontologyURI, true);
      }
      if (core.getDataOwnerName() != null && !core.getDataOwnerName().isBlank()) {
        final ForgeIndividual dataOwnerOrFallback = dataOwner != null ? dataOwner : rightsHolder;
        if (dataOwnerOrFallback != null) {
        this
            .addProperty(dataCategory.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#administeredBy",
                dataOwnerOrFallback.getUri(), ontologyURI, true);
          this
                  .addProperty(dataOwnerOrFallback.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#administers",
                          dataCategory.getUri(), ontologyURI, true);
        }
      }
      if (core.getDataConsumerName() != null && !core.getDataConsumerName().isBlank()) {
        this
            .addProperty(dataCategory.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isAccessGrantedTo",
                dataConsumer.getUri(), ontologyURI, true);
        this
                .addProperty(dataConsumer.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isGrantedAccess",
                dataCategory.getUri(), ontologyURI, true);
      }
      if ("YES".equals(personalData)) {
        this
            .addNewSubtype(ontologyURI, dataCategory.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#PersonalData");
      }
      if ("YES".equals(specialPersonalData)) {
        this
            .addNewSubtype(ontologyURI, dataCategory.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#SpecialPersonalData");
      }
      if ("YES".equals(tradeSecretData)) {
        this
            .addNewSubtype(ontologyURI, dataCategory.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#TradeSecret");
      }

    }

    // 8) Legal
    if (legal != null) {
      if (legal.getProcessingBases() != null && !legal.getProcessingBases().isEmpty()) {

        for (final Map.Entry<ProcessingBasis, List<TitleDescriptionDTO>> entry : legal
            .getProcessingBases()
            .entrySet()) {
          final ProcessingBasis basis = entry.getKey();
          final List<TitleDescriptionDTO> detailsPerBasis = entry.getValue();
          final List<TitleDescriptionDTO> effectiveDetails = detailsPerBasis == null
              || detailsPerBasis.isEmpty() ? List.of((TitleDescriptionDTO) null) : detailsPerBasis;

          for (final TitleDescriptionDTO detailDto : effectiveDetails) {
            final String detail = detailDto != null ? detailDto.getDescription() : null;

            switch (basis) {
              case ProcessingBasis.CONSENT:
                ForgeIndividual consentWorkflow = null;
                final ForgeIndividual consent = null;
                this
                    .addProperty(dataTrustee.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#operatesOnProcessingBasis",
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#Consent",
                        ontologyURI, false);
                if (ConsentType.GENERAL_CONSENT.equals(legal.getConsentType())) {
                  /**
                   * consent = this.builder DONE
                   * .createIndividual(core.getDataTrusteeName() + "_GeneralConsent",
                   * ConsentType.GENERAL_CONSENT.getUri(), this.repo.getBaseUri(),
                   * ontologyURI);
                   **/
                  consentWorkflow = this.builder
                      .createIndividual(core.getDataTrusteeName() + "_GeneralConsentWorkflow",
                          ConsentType.GENERAL_CONSENT.getImplementationUri(),
                          this.repo.getBaseUri(), ontologyURI);
                  /**
                   * this DONE
                   * .addProperty(consent.getUri(),
                   * "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#implementedBy",
                   * consentWorkflow.getUri(), ontologyURI, true);
                   **/
                  this
                      .addProperty(consentWorkflow.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#implementsConsentWorkflowFor",
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#Consent",
                              ontologyURI, false);

                  if(dataOwner != null) {
                    this
                            .addProperty(dataOwner.getUri(),
                                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#initiatesGeneralConsentWorkflow",
                                    consentWorkflow.getUri(), ontologyURI, true);
                  }
                  else if (rightsHolder != null) {
                    this
                            .addProperty(rightsHolder.getUri(),
                                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#initiatesGeneralConsentWorkflow",
                                    consentWorkflow.getUri(), ontologyURI, true);
                  }
                  /**
                   * this
                   * .addProperty(dataConsumer.getUri(), DONE
                   * "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                   * consent.getUri(), ontologyURI, true);
                   **/
                  this
                      .addProperty(dataConsumer.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#Consent",
                          ontologyURI, false);
                } else if (ConsentType.INDIVIDUAL_CONSENT.equals(legal.getConsentType())) {
                  /**
                   * consent = this.builder DONE
                   * .createIndividual(core.getDataTrusteeName() + "_IndividualConsent",
                   * ConsentType.INDIVIDUAL_CONSENT.getUri(), this.repo.getBaseUri(),
                   * ontologyURI);
                   **/
                  consentWorkflow = this.builder
                      .createIndividual(core.getDataTrusteeName() + "_IndividualConsentWorkflow",
                          ConsentType.INDIVIDUAL_CONSENT.getImplementationUri(),
                          this.repo.getBaseUri(), ontologyURI);
                  /**
                   * this DONE
                   * .addProperty(consent.getUri(), DONE
                   * "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#implementedBy",
                   * consentWorkflow.getUri(), ontologyURI, true);
                   **/
                  this
                      .addProperty(consentWorkflow.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#implementsConsentWorkflowFor",
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#Consent",
                          ontologyURI, false);
                  this
                      .addProperty(dataConsumer.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#initiatesIndividualConsentWorkflow",
                          consentWorkflow.getUri(), ontologyURI, true);
                  this
                          .addProperty(consentWorkflow.getUri(),
                                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#individualConsentWorkflowInitiatedBy",
                                  dataConsumer.getUri(), ontologyURI, true);
                  /**
                   * this
                   * .addProperty(dataConsumer.getUri(), DONE
                   * "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                   * consent.getUri(), ontologyURI, true);
                   **/
                  this
                      .addProperty(dataConsumer.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#Consent",
                          ontologyURI, false);
                } else if (ConsentType.NOT_PROVIDED.equals(legal.getConsentType())) {

                }

                if (ConsentEnteredBy.RIGHTS_OWNER.equals(legal.getConsentEnteredBy())) {
                  if (rightsHolder != null && consentWorkflow != null) {
                    this
                        .addProperty(rightsHolder.getUri(),
                            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#givesConsentDirectly",
                            consentWorkflow.getUri(), ontologyURI, true);
                    this
                            .addProperty(consentWorkflow.getUri(),
                                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#getsConsentDirectly",
                            rightsHolder.getUri(), ontologyURI, true);
                  }
                } else if (ConsentEnteredBy.DATA_OWNER.equals(legal.getConsentEnteredBy())) {
                  this
                      .addProperty(rightsHolder.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#givesConsentByDataOwner",
                          consentWorkflow.getUri(), ontologyURI, true);
                  this
                          .addProperty(consentWorkflow.getUri(),
                                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#getsConsentThroughDataOwner",
                          rightsHolder.getUri(), ontologyURI, true);
                } else if (ConsentType.NOT_PROVIDED.equals(legal.getConsentType())) {
                  this
                      .addProperty(rightsHolder.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#givesConsentByDataOwner",
                          consentWorkflow.getUri(), ontologyURI, true);
                }

                if (detail != null) {
                  if (!detail.isBlank()) {
                    this
                        .addLiteralIfPresent(consentWorkflow.getUri(),
                            PROP_PROCESSING_BASIS_DESCRIPTION, detail, "de", ontologyURI);
                  }
                }

                break;

              case ProcessingBasis.AMBIGUOUS:
                this
                    .addProperty(dataConsumer.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                        ProcessingBasis.AMBIGUOUS.getUri(), ontologyURI, false);
                break;

              case ProcessingBasis.NOT_PROVIDED:
                this
                    .addProperty(dataConsumer.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                        ProcessingBasis.NOT_PROVIDED.getUri(), ontologyURI, false);
                break;

              default:
                /**
                 * final ForgeIndividual processingBasisInstance = this.builder
                 * .createIndividual(core.getDataTrusteeName() + "_" + basis.getLabelDe(),
                 * basis.getUri(), this.repo.getBaseUri(), ontologyURI);
                 **/
                this
                    .addProperty(dataConsumer.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#isBeneficiaryOf",
                        basis.getUri(), ontologyURI, false);

                /**
                 * this
                 * .addProperty(consentWorkflow.getUri(),
                 * "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#hasProcessingBasis",
                 * basis.getUri(), ontologyURI, false);
                 * if (detail != null) {
                 * if (!detail.isBlank()) {
                 * this
                 * .addLiteralIfPresent(processingBasisInstance.getUri(),
                 * PROP_PROCESSING_BASIS_DESCRIPTION, detail, "de", ontologyURI);
                 * }
                 * }
                 **/
                break;

            }
          }
        }
      }

    }

    // 9) Data/Technology
    final DataTrusteeWizardImplementation data = model.getData();
    if (data != null) {
      ForgeIndividual dataTrusteeModel = null;
      if (data.getDataTrusteeCategory() != null) {
        dataTrusteeModel = this
            .createIndividual("DTM_" + dataTrustee.getLabel(), ontologyURI,
                data.getDataTrusteeCategory().getUri(), "de");
        this
            .addProperty(dataTrustee.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#hasDataTrusteeModel",
                dataTrusteeModel.getUri(), ontologyURI, true);
        this
                .addProperty(dataTrusteeModel.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#isDataTrusteeModelOf",
                dataTrustee.getUri(), ontologyURI, true);
      }

      if (data.getArchitectureType() != null && dataTrusteeModel != null) {
        this
            .addProperty(dataTrusteeModel.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#implementedOn",
                data.getArchitectureType().getUri(), ontologyURI, false);
      }

      //=== RECEPTION
      ForgeIndividual instanceReception = null;
      if (data.getReceptionTechnologies() != null && !data.getReceptionTechnologies().isEmpty()) {
        instanceReception = this
            .createIndividual(StaticText.PREFIX_RECEPTION + dataTrustee.getLabel(), ontologyURI,
                DataLifecycle.RECEPTION.getUri(), "de");
        for (final ReceptionTechnique currentTechnology : data.getReceptionTechnologies()) {
          this
              .addProperty(instanceReception.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasReceptionTechnique",
                  currentTechnology.getUri(), ontologyURI, false);
        }

        if (data.getReceptionFrequency() != null) {
          this
              .addProperty(instanceReception.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasReceptionFrequency",
                  data.getReceptionFrequency().getUri(), ontologyURI, false);
        }
        if (data.getReceptionSecurityMeasures() != null
            && !data.getReceptionSecurityMeasures().isEmpty()) {
          for (final SecurityMeasure currentSecurityMeasure : data.getReceptionSecurityMeasures()) {
            if (SecurityMeasure.AMBIGUOUS.equals(currentSecurityMeasure)
                || SecurityMeasure.CUSTOM.equals(currentSecurityMeasure)) {
              continue;
            }
            this
                .addProperty(instanceReception.getUri(),
                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#securedByTechnique",
                    currentSecurityMeasure.getUri(), ontologyURI, false);
          }
        }

        if (data.getSourceSystem() != null && !data.getSourceSystem().isEmpty()) {
          final ForgeIndividual sourceSystem = this
              .createIndividual(StaticText.PREFIX_SOURCE_SYSTEM + data.getSourceSystem(),
                  ontologyURI, StaticText.FORGETRUSTEE_SOURCE_SYSTEM, "de");
          this
              .addProperty(instanceReception.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#usesSystem",
                  sourceSystem.getUri(), ontologyURI, true);
          this
                  .addProperty(sourceSystem.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#isUsedAsSystem",
                  instanceReception.getUri(), ontologyURI, true);
        }
      }
      if (instanceReception != null && data.getCustomReceptionSecurityTechniques() != null
          && !data.getCustomReceptionSecurityTechniques().isEmpty()) {
        for (final TitleDescriptionDTO customReceptionSecurityTechnique : data
            .getCustomReceptionSecurityTechniques()) {
          final ForgeIndividual customReceptionSec = this
              .createIndividual(
                  StaticText.PREFIX_CUSTOM + StaticText.PREFIX_RECEPTION
                      + StaticText.PREFIX_SECURITY + customReceptionSecurityTechnique.getTitle(),
                  ontologyURI, StaticText.FORGETRUSTEE_SECURITY_RECEPTION_TECHNOQUE, "de");
          this
              .addLiteralIfPresent(customReceptionSec.getUri(), RDFS.comment.getURI(),
                  customReceptionSecurityTechnique.getDescription(), "de", ontologyURI);
          this
              .addProperty(instanceReception.getUri(), StaticText.FORGETRUSTEE_PROP_SECURED_BY,
                  customReceptionSec.getUri(), ontologyURI, true);
          this
                  .addProperty(customReceptionSec.getUri(), "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#securesAsTechnique",
                  instanceReception.getUri(), ontologyURI, true);
        }
      }

      //=== PREPARATION
      ForgeIndividual instancePreparation = null;
      if (data.getPreparationTechniques() != null && !data.getPreparationTechniques().isEmpty()) {
        instancePreparation = this
            .createIndividual(StaticText.PREFIX_PREPARATION + dataTrustee.getLabel(), ontologyURI,
                DataLifecycle.PREPARATION.getUri(), "de");
        for (final PreparationTechnique currentTechnology : data.getPreparationTechniques()) {
          if (PreparationTechnique.AMBIGUOUS.equals(currentTechnology)
              || PreparationTechnique.CUSTOM.equals(currentTechnology)) {
            continue;
          }
          this
              .addProperty(instancePreparation.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasPreparationTechnique",
                  currentTechnology.getUri(), ontologyURI, false);
        }
      }
      if (instancePreparation != null && data.getCustomPreparationTechniques() != null
          && !data.getCustomPreparationTechniques().isEmpty()) {
        for (final TitleDescriptionDTO customPreparationTechnique : data
            .getCustomPreparationTechniques()) {
          final ForgeIndividual customPreparation = this
              .createIndividual(
                  StaticText.PREFIX_CUSTOM + StaticText.PREFIX_PREPARATION
                      + customPreparationTechnique.getTitle(),
                  ontologyURI, StaticText.FORGETRUSTEE_PREPARATION_TECHNIQUE, "de");
          this
              .addLiteralIfPresent(customPreparation.getUri(), RDFS.comment.getURI(),
                  customPreparationTechnique.getDescription(), "de", ontologyURI);
          this
              .addProperty(instancePreparation.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasPreparationTechnique",
                  customPreparation.getUri(), ontologyURI, true);
          this
                  .addProperty(customPreparation.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#isPreparationTechniqueOf",
                          instancePreparation.getUri(), ontologyURI, true);
        }
      }

      //=== Storage
      ForgeIndividual instanceStorage = null;
      if (data.getStorageTechnique() != null) {
        instanceStorage = this
            .createIndividual(StaticText.PREFIX_STORAGE + dataTrustee.getLabel(), ontologyURI,
                DataLifecycle.STORAGE.getUri(), "de");
        this
            .addProperty(instanceStorage.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasStorageTechnique",
                data.getStorageTechnique().getUri(), ontologyURI, false);
        this
            .addProperty(instanceStorage.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#performsStorage",
                dataTrustee.getUri(), ontologyURI, true);
        this
                .addProperty(dataTrustee.getUri(),
                        "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#triggersStorage",
                        instanceStorage.getUri(), ontologyURI, true);
      }
      if (data.getStorageRetention() != null && instanceStorage != null) {
        this
            .addProperty(instanceStorage.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasRetentionPeriod",
                data.getStorageRetention().getUri(), ontologyURI, false);
      }

      //=== Analysis
      ForgeIndividual instanceAnalysis = null;
      if (data.getAnalysisTechniques() != null) {
        instanceAnalysis = this
            .createIndividual(StaticText.PREFIX_ANALYSIS + dataTrustee.getLabel(), ontologyURI,
                DataLifecycle.ANALYSIS.getUri(), "de");
        for (final AnalysisTechnique currentTechnology : data.getAnalysisTechniques()) {
          if ((AnalysisTechnique.AMBIGUOUS.equals(currentTechnology)
              || AnalysisTechnique.CUSTOM.equals(currentTechnology)
              || AnalysisTechnique.NONE.equals(currentTechnology))) {
            continue;
          }
          this
              .addProperty(instanceAnalysis.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasDataAnalysisTechnique",
                  currentTechnology.getUri(), ontologyURI, false);
        }
        if (instanceAnalysis != null && data.getCustomAnalysisTechniques() != null
            && !data.getCustomAnalysisTechniques().isEmpty()) {
          for (final TitleDescriptionDTO customAnalysisTechnique : data
              .getCustomAnalysisTechniques()) {
            final ForgeIndividual customAnalysis = this
                .createIndividual(
                    StaticText.PREFIX_CUSTOM + StaticText.PREFIX_ANALYSIS
                        + customAnalysisTechnique.getTitle(),
                    ontologyURI, StaticText.FORGETRUSTEE_ANALYSIS_TECHNIQUE, "de");
            this
                .addLiteralIfPresent(customAnalysis.getUri(), RDFS.comment.getURI(),
                    customAnalysisTechnique.getDescription(), "de", ontologyURI);
            this
                .addProperty(instanceAnalysis.getUri(),
                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasDataAnalysisTechnique",
                    customAnalysis.getUri(), ontologyURI, true);
            this
                    .addProperty(customAnalysis.getUri(),
                            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#isDataAnalysisTechniqueOf",
                            instanceAnalysis.getUri(), ontologyURI, true);
          }
        }
      }

      //=== FORWARDING
      ForgeIndividual instanceForwading = null;
      if (data.getForwardingTechniques() != null && !data.getForwardingTechniques().isEmpty()) {
        instanceForwading = this
            .createIndividual(StaticText.PREFIX_FORWARDING + dataTrustee.getLabel(), ontologyURI,
                DataLifecycle.FORWARDING.getUri(), "de");
        for (final ForwardingTechnique currentTechnology : data.getForwardingTechniques()) {
          this
              .addProperty(instanceForwading.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasForwardingTechnique",
                  currentTechnology.getUri(), ontologyURI, false);
        }

        if (data.getForwardingFrequency() != null) {
          this
              .addProperty(instanceForwading.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasForwardingFrequency",
                  data.getForwardingFrequency().getUri(), ontologyURI, false);
        }
        if (data.getForwardingSecurityMeasures() != null
            && !data.getForwardingSecurityMeasures().isEmpty()) {
          for (final SecurityMeasure currentSecurityMeasure : data
              .getForwardingSecurityMeasures()) {
            if (SecurityMeasure.AMBIGUOUS.equals(currentSecurityMeasure)
                || SecurityMeasure.CUSTOM.equals(currentSecurityMeasure)) {
              continue;
            }
            this
                .addProperty(instanceForwading.getUri(),
                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#securedByTechnique",
                    currentSecurityMeasure.getUri(), ontologyURI, false);
          }
        }
        if (data.getTargetSystem() != null && !data.getTargetSystem().isEmpty()) {
          final ForgeIndividual targetSystem = this
              .createIndividual(StaticText.PREFIX_TARGET_SYSTEM + data.getTargetSystem(),
                  ontologyURI, StaticText.FORGETRUSTEE_TARGET_SYSTEM, "de");
          this
              .addProperty(instanceForwading.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#usesSystem",
                  targetSystem.getUri(), ontologyURI, true);
          this
                  .addProperty(targetSystem.getUri(),
                          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#isUsedAsSystemm",
                          instanceForwading.getUri(), ontologyURI, true);
        }
      }
      if (instanceForwading != null && data.getCustomForwardingSecurityTechniques() != null
          && !data.getCustomForwardingSecurityTechniques().isEmpty()) {
        for (final TitleDescriptionDTO customForwardingTechnique : data
            .getCustomForwardingSecurityTechniques()) {
          final ForgeIndividual customForwarding = this
              .createIndividual(
                  StaticText.PREFIX_CUSTOM + StaticText.PREFIX_FORWARDING
                      + StaticText.PREFIX_SECURITY + customForwardingTechnique.getTitle(),
                  ontologyURI, StaticText.FORGETRUSTEE_SECURITY_RECEPTION_TECHNOQUE, "de");
          this
              .addLiteralIfPresent(customForwarding.getUri(), RDFS.comment.getURI(),
                  customForwardingTechnique.getDescription(), "de", ontologyURI);
          this
              .addProperty(instanceForwading.getUri(), StaticText.FORGETRUSTEE_PROP_SECURED_BY,
                  customForwarding.getUri(), ontologyURI, true);
          this
                  .addProperty(customForwarding.getUri(), "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#securesAsTechnique",
                          instanceForwading.getUri(), ontologyURI, true);
        }
      }

    }

    // 10) Business
    final DataTrusteeWizardBusiness business = model.getBusiness();
    if (business != null) {
      if (business.getBusinessDomains() != null && !business.getBusinessDomains().isEmpty()) {
        for (final BusinessDomain currentDomain : business.getBusinessDomains()) {
          this
              .addProperty(dataTrustee.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasDomain",
                  currentDomain.getUri(), ontologyURI, false);
        }
      }

      if (business.getBusinessModel() != null) {
        this
            .addProperty(dataTrustee.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasBusinessForm",
                business.getBusinessModel().getUri(), ontologyURI, false);
      }

      if (business.getFundingSources() != null && !business.getFundingSources().isEmpty()) {
        for (final FundingSource currentFundingSource : business.getFundingSources()) {
          this
              .addProperty(dataTrustee.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasFundingSource",
                  currentFundingSource.getUri(), ontologyURI, false);
        }
      }

      if (business.getPaymentMethodDataOwner() != null) {
        if (dataOwner != null) {
          this
              .addProperty(dataOwner.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#usesPayModelAsDataOwner",
                  business.getPaymentMethodDataOwner().getUri(), ontologyURI, false);
        } else {
          this
              .addProperty(rightsHolder.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#usesPayModelAsDataOwner",
                  business.getPaymentMethodDataOwner().getUri(), ontologyURI, false);
        }
      }

      if (business.getPaymentMethodDataConsumer() != null) {
        this
            .addProperty(dataConsumer.getUri(),
                "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#usesPayModelAsDataConsumer",
                business.getPaymentMethodDataConsumer().getUri(), ontologyURI, false);
      }
    }

    // 11) Objectives
    final DataTrusteeWizardObjectives objectives = model.getObjectives();
    if (objectives != null) {

      if (objectives.getDataTrusteeGoals() != null && !objectives.getDataTrusteeGoals().isEmpty()) {
        for (final DataTrusteeGoal currentDataTrusteeGoal : objectives.getDataTrusteeGoals()) {
          this
              .addProperty(dataTrustee.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasDataTrusteeGoal",
                  currentDataTrusteeGoal.getUri(), ontologyURI, false);
        }
      }

      if (objectives.getMotivationRightsHolder() != null
          && !objectives.getMotivationRightsHolder().isEmpty()) {
        for (final MotivationRightsHolder motivationRightsHolder : objectives
            .getMotivationRightsHolder()) {
          if (rightsHolder != null) {
            this
                    .addProperty(rightsHolder.getUri(),
                            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasRightsHolderMotivation",
                            motivationRightsHolder.getUri(), ontologyURI, false);
          }
        }
      }

      if (objectives.getMotivationDataHolder() != null
          && !objectives.getMotivationDataHolder().isEmpty()) {
        for (final MotivationDataHolder motivationDataHolder : objectives
            .getMotivationDataHolder()) {
          if (dataOwner != null) {
            this
                .addProperty(dataOwner.getUri(),
                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasDataHolderMotivation",
                    motivationDataHolder.getUri(), ontologyURI, false);
          } else {
            this
                .addProperty(rightsHolder.getUri(),
                    "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasDataHolderMotivation",
                    motivationDataHolder.getUri(), ontologyURI, false);
          }
        }
      }


      if (objectives.getMotivationDataConsumer() != null
          && !objectives.getMotivationDataConsumer().isEmpty()) {
        for (final MotivationDataConsumer motivationDataConsumer : objectives
            .getMotivationDataConsumer()) {
          this
              .addProperty(dataConsumer.getUri(),
                  "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#hasDataConsumerMotivation",
                  motivationDataConsumer.getUri(), ontologyURI, false);
        }
      }

    }
    log
        .info("DataTrusteeModel für OntologyURI={} wurde aufgebaut (mit Platzhalter-Properties).",
            ontologyURI);

    final String ttl = this.factory.getModelTTL(ontologyURI);
    return OntologySHACLConverter_Impl.retrieveOntModelFromText(ttl, StaticText.TURTLE);

  }

  @Override
  public DataTrusteeWizardModel loadFromTtl(String ttlPath, String ontologyURI) {
    final Model jenaModel = ModelFactory.createDefaultModel();
    RDFDataMgr.read(jenaModel, ttlPath, Lang.TURTLE);

    final DataTrusteeModelReader reader = new DataTrusteeModelReader();
    return this.toWizardModel(reader.readFromTtlModel(jenaModel, ontologyURI));
  }

  @Override
  public String generateExecutiveSummary(DataTrusteeWizardModel model) {
    this.executiveSummaryNarrativeGenerator = new ExecutiveSummaryNarrativeGenerator();
    return this.executiveSummaryNarrativeGenerator.generate(model);
  }

  @Override
  public DataTrusteeWizardModel retrieveDataModelFromTTL(String ontologyURI) {
    return null;
  }

  @Override
  public String toTTL(OntModel ontModel) {
    return this.factory.toTTL(ontModel, true);
  }

  @Override
  public ShaclReportDTO validate(OntModel ontModel, DataTrusteeWizardModel wizardModel, String aDefault) {
    Model shapesModel = null;

    if ("default".equals(aDefault)) {
      shapesModel = RDFDataMgr.loadModel("data/shapes.ttl");
    }

    final Shapes shapes = Shapes.parse(shapesModel.getGraph());

    final ValidationReport report =
            ShaclValidator.get().validate(
                    shapes,
                    ontModel.getGraph()
            );

    final ShaclReportDTO dto =
            this.toDto(report);

    this.addCustomWizardValidationResults(
            dto,
            wizardModel
    );

    this.recalculateReportMetaData(dto);

    return dto;

  }

  private void addCustomWizardValidationResults(
          ShaclReportDTO dto,
          DataTrusteeWizardModel wizardModel
  ) {
    if (wizardModel == null || wizardModel.getData() == null) {
      return;
    }

    final DataTrusteeWizardImplementation data =
            wizardModel.getData();

    if (contains(data.getReceptionSecurityMeasures(), SecurityMeasure.CUSTOM)
            && isEmpty(data.getCustomReceptionSecurityTechniques())) {

      dto.getResults().add(
              customWarning(
                      "implementation.receptionSecurityMeasures",
                      "CUSTOM",
                      "CUSTOM wurde bei den Sicherheitsmaßnahmen des Datenempfangs gewählt. Bitte ergänze mindestens eine nutzerspezifische Empfangssicherheitsmaßnahme."
              )
      );
    }

    if (contains(data.getPreparationTechniques(), PreparationTechnique.CUSTOM)
            && isEmpty(data.getCustomPreparationTechniques())) {

      dto.getResults().add(
              customWarning(
                      "implementation.preparationTechniques",
                      "CUSTOM",
                      "CUSTOM wurde bei den Aufbereitungstechniken gewählt. Bitte ergänze mindestens eine nutzerspezifische Aufbereitungstechnik."
              )
      );
    }

    if (contains(data.getAnalysisTechniques(), AnalysisTechnique.CUSTOM)
            && isEmpty(data.getCustomAnalysisTechniques())) {

      dto.getResults().add(
              customWarning(
                      "implementation.analysisTechniques",
                      "CUSTOM",
                      "CUSTOM wurde bei den Analyseverfahren gewählt. Bitte ergänze mindestens ein nutzerspezifisches Analyseverfahren."
              )
      );
    }

    if (contains(data.getForwardingSecurityMeasures(), SecurityMeasure.CUSTOM)
            && isEmpty(data.getCustomForwardingSecurityTechniques())) {

      dto.getResults().add(
              customWarning(
                      "implementation.forwardingSecurityMeasures",
                      "CUSTOM",
                      "CUSTOM wurde bei den Sicherheitsmaßnahmen der Datenweitergabe gewählt. Bitte ergänze mindestens eine nutzerspezifische Weitergabesicherheitsmaßnahme."
              )
      );
    }
  }

  private ShaclResultDTO customWarning(
          String path,
          String value,
          String message
  ) {
    final ShaclResultDTO result =
            new ShaclResultDTO();

    result.setFocusNode("WizardDTO");
    result.setPath(path);
    result.setValue(value);
    result.setSourceShape("CustomWizardValidation");
    result.setSourceConstraintComponent("DTO_CUSTOM_REQUIRED");
    result.setSeverity("Warning");
    result.setMessage(message);

    return result;
  }

  private boolean isEmpty(List<?> list) {
    return list == null || list.isEmpty();
  }

  private boolean contains(
          Iterable<?> values,
          Object expected
  ) {
    if (values == null) {
      return false;
    }

    for (Object value : values) {
      if (expected.equals(value)) {
        return true;
      }
    }

    return false;
  }

  private void recalculateReportMetaData(
          ShaclReportDTO dto
  ) {
    dto.setResultsCount(
            dto.getResults().size()
    );

    dto.setConforms(
            dto.getResults()
                    .stream()
                    .noneMatch(result ->
                            result.getSeverity() != null
                                    && result.getSeverity().contains("Violation")
                    )
    );
  }

  public ShaclReportDTO toDto(ValidationReport report) {

    final ShaclReportDTO dto = new ShaclReportDTO();

    // conforms setzen
    final boolean conforms = report.conforms();
    dto.setConforms(conforms);

    // Ergebnisliste erzeugen
    final List<ShaclResultDTO> results = new ArrayList<>();

    // einzelne SHACL-Einträge iterieren
    for (ReportEntry entry : report.getEntries()) {

      // einzelnen Eintrag konvertieren
      final ShaclResultDTO resultDto = this.toResultDto(entry);

      // zur Ergebnisliste hinzufügen
      results.add(resultDto);
    }

    // Ergebnisse setzen
    dto.setResults(results);

    // Anzahl setzen
    final int resultsCount = results.size();
    dto.setResultsCount(resultsCount);

    return dto;
  }

  private ShaclResultDTO toResultDto(ReportEntry entry) {

    final ShaclResultDTO dto = new ShaclResultDTO();

    dto.setFocusNode(this.asString(entry.focusNode()));

    if (entry.resultPath() != null) {
      dto.setPath(entry.resultPath().toString());
    } else {
      dto.setPath(null);
    }

    dto.setValue(this.asString(entry.value()));
    dto.setSourceShape(this.asString(entry.source()));

    if (entry.constraint() != null
            && entry.constraint().getComponent() != null) {
      dto.setSourceConstraintComponent(
              entry.constraint().getComponent().toString()
      );
    } else {
      dto.setSourceConstraintComponent(null);
    }

    if (entry.severity() != null) {
      dto.setSeverity(entry.severity().level().toString());
    } else {
      dto.setSeverity(null);
    }

    dto.setSourceConstraintComponent(this.asString(entry.constraint().getComponent()));
    dto.setSeverity(entry.severity() != null && entry.severity().level() != null
        ? entry.severity().level().getLocalName()
        : null);
    dto.setMessage(entry.message());

    return dto;
  }

  private String asString(org.apache.jena.graph.Node node) {
    return node == null ? null : node.toString();
  }

  private DataTrusteeWizardCore validateAndGetCore(DataTrusteeWizardModel model) {
    if (model == null) {
      throw new IllegalArgumentException("DataTrusteeModel must not be null");
    }

    final DataTrusteeWizardCore core = model.getCore();
    if (core == null) {
      throw new IllegalArgumentException("DataTrusteeModel.core must not be null");
    }

    if (core.getOntologyURI() == null || core.getOntologyURI().isBlank()) {
      throw new IllegalArgumentException("ontologyURI must not be null or blank");
    }

    if (core.getDataTrusteeName() == null || core.getDataTrusteeName().isBlank()) {
      throw new IllegalArgumentException("dataTrusteeName must not be null or blank");
    }

    // Falls du analog zum Wrapper später weitere Felder als Pflicht ansehen willst,
    // kannst du die Checks einfach hier ergänzen.

    return core;
  }

  /** Helper: Actor in Ontologie anlegen, wenn Name vorhanden */
  private ForgeIndividual createActorIfPresent(String name, String ontologyURI, String roleURI,
      String langSetting) {
    if (name == null || name.isBlank()) {
      return null;
    }
    return this.createActorInOntology(name, ontologyURI, roleURI, langSetting);
  }

  /** Helper: Literal setzen, wenn String nicht leer */
  private void addLiteralIfPresent(String subjectURI, String propertyURI, String value, String lang,
      String ontologyURI) {
    if (value == null || value.isBlank()) {
      return;
    }
    this.addLiteral(subjectURI, propertyURI, value, lang, ontologyURI);
  }

  /** Helper: Liste als mehrere Literale ablegen */
  private void addLiteralsForList(String subjectURI, String propertyURI, List<String> values,
      String lang, String ontologyURI) {
    if (values == null || values.isEmpty()) {
      return;
    }
    for (final String v : values) {
      if (v != null && !v.isBlank()) {
        this.addLiteral(subjectURI, propertyURI, v, lang, ontologyURI);
      }
    }
  }

  @Override
  public Model createNewInstanceOfOntology(String newOntologyURI) {
    final Model newInstance = this.builder.createNewOntologyInstanceOfMergedBase(newOntologyURI);
    this.factory.saveNewModel(newInstance.getNsPrefixURI(""), newInstance, StaticText.TURTLE);
    return newInstance;
  }

  @Override
  public ForgeIndividual createDataTrustee(String label, String ontologyInstance_URI,
      String langSetting) {
    final ForgeIndividual dataTrustee = this.builder
        .createIndividual(label,
            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataTrustee",
            this.repo.getBaseUri(), ontologyInstance_URI);
    this.builder.addLabel(dataTrustee.getUri(), label, "de", ontologyInstance_URI);
    return dataTrustee;
  }

  @Override
  public ForgeIndividual createActorInOntology(String label, String ontologyInstance_URI,
      String role, String langSetting) {
    final ForgeIndividual actor = this.builder
        .createIndividual(label, "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#Actor",
            this.repo.getBaseUri(), ontologyInstance_URI);
    this.builder.addLabel(actor.getUri(), label, "de", ontologyInstance_URI);
    if (StaticText.FORGETRUSTEE_AFFECTED_PARTY.equals(role)) {
      this.builder
          .addAdditionalType(actor.getUri(), StaticText.FORGETRUSTEE_AFFECTED_PARTY,
              ontologyInstance_URI);
    }
    if (StaticText.FORGETRUSTEE_DATA_OWNER.equals(role)) {
      this.builder
          .addAdditionalType(actor.getUri(), StaticText.FORGETRUSTEE_DATA_OWNER,
              ontologyInstance_URI);
    }
    if (StaticText.FORGETRUSTEE_DATA_PROVIDER.equals(role)) {
      this.builder
          .addAdditionalType(actor.getUri(), StaticText.FORGETRUSTEE_DATA_PROVIDER,
              ontologyInstance_URI);
    }
    if (StaticText.FORGETRUSTEE_DATA_CONSUMER.equals(role)) {
      this.builder
          .addAdditionalType(actor.getUri(), StaticText.FORGETRUSTEE_DATA_CONSUMER,
              ontologyInstance_URI);
    }
    return actor;
  }

  @Override
  public ForgeIndividual createDataTrusteeModel(String dataTrusteeModelName, String ontologyURI,
      String langSetting) {
    final ForgeIndividual dataTrusteeModel = this.builder
        .createIndividual(dataTrusteeModelName,
            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataTrusteeModel",
            this.repo.getBaseUri(), ontologyURI);
    this.builder
        .addLabel(dataTrusteeModel.getUri(), dataTrusteeModelName, langSetting, ontologyURI);
    return dataTrusteeModel;
  }

  @Override
  public ForgeIndividual createDataInOntology(String dataName, String ontologyURI,
      String langSetting) {
    final ForgeIndividual data = this.builder
        .createIndividual(dataName,
            "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#Data",
            this.repo.getBaseUri(), ontologyURI);
    this.builder.addLabel(data.getUri(), dataName, langSetting, ontologyURI);
    return data;
  }

  @Override
  public ForgeIndividual createIndividual(String individualName, String ontologyURI,
      String classURI, String langSetting) {
    final ForgeIndividual individual = this.builder
        .createIndividual(individualName, classURI, this.repo.getBaseUri(), ontologyURI);
    this.builder.addLabel(individual.getUri(), individualName, langSetting, ontologyURI);
    return individual;
  }

  @Override
  public ForgeIndividual removeNewSubtype(String ontologyURI, String subjectURI,
      String subtypeURI) {
    return null; //TODO
  }

  @Override
  public ForgeIndividual addNewSubtype(String ontologyURI, String subjectURI, String subtypeURI) {
    this.builder.addAdditionalType(subjectURI, subtypeURI, ontologyURI);
    final ForgeIndividual addedSubtype = this.translater
        .translateToForgeIndividual(
            (OntIndividual.Named) this.navigation.getIndividual(subjectURI, ontologyURI));
    return addedSubtype;
  }

  @Override
  public void addProperty(String subject_URI, String property_URI, String object_URI,
      String ontologyInstance_URI, boolean propertyBetweenIndividuals) {
    try {
      this.builder
          .addProperty(subject_URI, property_URI, object_URI, this.repo.getUriOfBase(),
              ontologyInstance_URI, propertyBetweenIndividuals);
    } catch (final IllegalArgumentException ex) {
      throw new IllegalArgumentException(this
          .buildPropertyContextMessage(subject_URI, property_URI, object_URI, ontologyInstance_URI,
              propertyBetweenIndividuals, ex.getMessage()),
          ex);
    } catch (final RuntimeException ex) {
      throw new IllegalStateException(this
          .buildPropertyContextMessage(subject_URI, property_URI, object_URI, ontologyInstance_URI,
              propertyBetweenIndividuals, "Unexpected error while adding property"),
          ex);
    }
  }

  @Override
  public void removeProperty(String subject_URI, String property_URI, String object_URI,
      String ontologyInstance_URI, boolean propertyBetweenIndividuals) {
    if (propertyBetweenIndividuals) {
      this.builder
          .removeProperty(subject_URI, property_URI, object_URI, this.repo.getUriOfBase(),
              ontologyInstance_URI);
    } else {
    }
  }

  @Override
  public ForgeIndividual addLiteral(String subjectURI, String propertyURI, String literal,
      String lang, String ontologyURI) {
    final ForgeIndividual addedCommentIndividual = this.builder
        .addLiteral(subjectURI, propertyURI, literal, lang, this.repo.getUriOfBase(), ontologyURI);
    return addedCommentIndividual;
  }

  @Override
  public void addNameToActor(String actor_URI, String name, String modelIndividualsAreFrom_URI) {

  }

  @Override
  public void addAffiliationToActor(String actor_URI, String affiliation_URI,
      String modelIndividualsAreFrom_URI) {

  }

  @Override
  public void addDataTrusteeGoal(String goal_URI, String modelIndividualsAreFrom_URI) {

  }

  @Override
  public void addPropertyToIndividual(String subject_URI, String property_URI, String object_URI,
      String uriOfModelIndividualsAreFrom) {

  }

  @Override
  public void addData(String categoryName, String description) {

  }

  @Override
  public void addDataSubclass(String data_URI, String subclass_URI) {

  }

  @Override
  public DataTrusteeWizardModel toWizardModel(DataTrusteeModel legacy) {
    if (legacy == null) {
      return null;
    }

    final DataTrusteeWizardModel wizard = new DataTrusteeWizardModel();
    wizard.setCore(this.toWizardCore(legacy.getCore()));
    wizard.setLegal(this.toWizardData(legacy.getCore(), legacy.getLegal()));
    wizard.setData(this.toWizardImplementation(legacy.getData()));
    wizard.setBusiness(this.toWizardBusiness(legacy.getBusiness()));
    wizard.setObjectives(this.toWizardObjectives(legacy.getObjectives()));
    return wizard;
  }

  private DataTrusteeWizardCore toWizardCore(DataTrusteeModelCore legacy) {
    if (legacy == null) {
      return null;
    }
    final DataTrusteeWizardCore wizard = new DataTrusteeWizardCore();
    wizard.setOntologyURI(legacy.getOntologyURI());
    wizard.setDataTrusteeName(legacy.getDataTrusteeName());
    wizard.setDataTrusteeDescription(legacy.getDataTrusteeDescription());
    wizard.setDataTrusteeOperator(legacy.getDataTrusteeOperator());
    wizard.setDataTrusteeOperatorAffiliation(legacy.getDataTrusteeOperatorAffiliation());
    wizard.setRightsHolderName(legacy.getRightsHolderName());
    wizard.setRightsHolderAffiliation(legacy.getRightsHolderAffiliation());
    wizard.setRightsHolderIsRepresented(legacy.getRightsHolderIsRepresented());
    wizard.setDataOwnerName(legacy.getDataOwnerName());
    wizard.setDataConsumerName(legacy.getDataConsumerName());
    wizard.setDataConsumerAffiliation(legacy.getDataConsumerAffiliation());
    return wizard;
  }

  private DataTrusteeWizardData toWizardData(DataTrusteeModelCore core,
      DataTrusteeModelLegal legal) {
    if (core == null && legal == null) {
      return null;
    }
    final DataTrusteeWizardData wizard = new DataTrusteeWizardData();
    if (core != null) {
      if (wizard.getDataCategory() == null) {
        wizard.setDataCategory(new TitleDescriptionDTO());
      }
      wizard.getDataCategory().setTitle(core.getDataCategoryName());
      wizard.getDataCategory().setDescription(core.getDataCategoryDescription());
      wizard.setContainPersonalInformation(core.getContainPersonalInformation());
      wizard.setSpecialPersonalInformation(core.getSpecialPersonalInformation());
      wizard.setContainTradeSecrets(core.getContainTradeSecrets());
    }
    if (legal != null) {
      wizard.setProcessingBases(legal.getProcessingBases());
      wizard.setConsentType(legal.getConsentType());
      wizard.setObtainingConsentBy(legal.getObtainingConsentBy());
      wizard.setConsentEnteredBy(legal.getConsentEnteredBy());
    }
    return wizard;
  }

  private DataTrusteeWizardImplementation toWizardImplementation(DataTrusteeModelData legacy) {
    if (legacy == null) {
      return null;
    }
    final DataTrusteeWizardImplementation wizard = new DataTrusteeWizardImplementation();
    wizard.setDataTrusteeCategory(legacy.getDataTrusteeCategory());
    wizard.setArchitectureType(legacy.getArchitectureType());
    wizard.setReceptionTechnologies(legacy.getReceptionTechnologies());
    wizard.setReceptionFrequency(legacy.getReceptionFrequency());
    wizard.setReceptionSecurityMeasures(legacy.getReceptionSecurityMeasures());
    wizard.setCustomReceptionSecurityTechniques(legacy.getCustomReceptionSecurityTechniques());
    wizard.setSourceSystem(legacy.getSourceSystem());
    wizard.setPreparationTechniques(legacy.getPreparationTechniques());
    wizard.setCustomPreparationTechniques(legacy.getCustomPreparationTechniques());
    wizard.setStorageTechnique(legacy.getStorageTechnique());
    wizard.setStorageRetention(legacy.getStorageRetention());
    wizard.setAnalysisTechniques(legacy.getAnalysisTechniques());
    wizard.setCustomAnalysisTechniques(legacy.getCustomAnalysisTechniques());
    wizard.setForwardingTechniques(legacy.getForwardingTechniques());
    wizard.setForwardingFrequency(legacy.getForwardingFrequency());
    wizard.setForwardingSecurityMeasures(legacy.getForwardingSecurityMeasures());
    wizard.setCustomForwardingSecurityTechniques(legacy.getCustomForwardingSecurityTechniques());
    wizard.setTargetSystem(legacy.getTargetSystem());
    return wizard;
  }

  private DataTrusteeWizardBusiness toWizardBusiness(DataTrusteeModelBusiness legacy) {
    if (legacy == null) {
      return null;
    }
    final DataTrusteeWizardBusiness wizard = new DataTrusteeWizardBusiness();
    wizard.setBusinessDomains(legacy.getBusinessDomains());
    wizard.setBusinessModel(legacy.getBusinessModel());
    wizard.setFundingSources(legacy.getFundingSources());
    //    wizard.setPaymentMethods(legacy.getPaymentMethods());
    wizard.setPaymentMethodDataOwner(legacy.getPaymentMethodDataOwner());
    wizard.setPaymentMethodDataConsumer(legacy.getPaymentMethodDataConsumer());
    return wizard;
  }

  private DataTrusteeWizardObjectives toWizardObjectives(DataTrusteeModelObjectives legacy) {
    if (legacy == null) {
      return null;
    }
    final DataTrusteeWizardObjectives wizard = new DataTrusteeWizardObjectives();
    wizard.setDataTrusteeGoals(legacy.getDataTrusteeGoals());
    wizard.setMotivationRightsHolder(legacy.getMotivationRightsHolder());
    wizard.setMotivationDataHolder(legacy.getMotivationDataHolder());
    wizard.setMotivationDataConsumer(legacy.getMotivationDataConsumer());
    return wizard;
  }

  private String buildPropertyContextMessage(String subjectUri, String propertyUri,
      String objectUri, String ontologyInstanceUri, boolean propertyBetweenIndividuals,
      String detail) {
    final String relationType = propertyBetweenIndividuals ? "individual-to-individual"
        : "individual-to-concept";
    return String
        .format(
            "Failed to add property (%s): subject='%s', property='%s', object='%s', ontology='%s', detail='%s'",
            relationType, subjectUri, propertyUri, objectUri, ontologyInstanceUri, detail);
  }

}
