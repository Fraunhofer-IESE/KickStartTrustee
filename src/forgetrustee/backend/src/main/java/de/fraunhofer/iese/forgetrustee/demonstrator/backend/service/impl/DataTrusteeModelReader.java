
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
/* Created by chwalek on 24.02.2026 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelLegal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessDomain;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.FundingSource;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.PayMethods;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.AnalysisTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ArchitectureType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataLifecycle;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataTrusteeCategory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ForwardingTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.Frequency;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.PreparationTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ReceptionTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.SecurityMeasure;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.StorageRetention;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.StorageTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentEnteredBy;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.DataTrusteeGoal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataConsumer;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataHolder;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationRightsHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class DataTrusteeModelReader {

  // === Konstante URIs (wie in Ihrer buildFromDataTrusteeModel-Methode verwendet) ===
  private static final String NS_CORE = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#";

  private static final String NS_DATA = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#";

  private static final String NS_BUSINESS = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#";

  private static final String NS_LEGAL = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#";

  private static final String NS_MISC = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#";

  private static final Property PROP_DATA_TRUSTEE_DESCRIPTION = ResourceFactory
      .createProperty(NS_CORE, "dataTrusteeDescription");

  private static final Property PROP_HAS_DATA_TRUSTEE_OPERATOR = ResourceFactory
      .createProperty(NS_BUSINESS, "hasDataTrusteeOperator");

  private static final Property PROP_IS_AFFECTED_PARTY_IN = ResourceFactory
      .createProperty(NS_CORE, "isAffectedPartyIn");

  private static final Property PROP_IS_DATA_OWNER_OF = ResourceFactory
      .createProperty(NS_CORE, "isDataOwnerOf");

  private static final Property PROP_IS_DATA_CONSUMER_OF = ResourceFactory
      .createProperty(NS_CORE, "isDataConsumerOf");

  private static final Property PROP_IS_RIGHTLY_OWNED_BY = ResourceFactory
      .createProperty(NS_CORE, "isRightlyOwnedBy");

  private static final Property PROP_ADMINISTERED_BY = ResourceFactory
      .createProperty(NS_CORE, "administeredBy");

  private static final Property PROP_IS_ACCESS_GRANTED_TO = ResourceFactory
      .createProperty(NS_CORE, "isAccessGrantedTo");

  private static final Property PROP_AFFILIATION_TYPE = ResourceFactory
      .createProperty(NS_BUSINESS, "affiliationType");

  private static final Resource CLASS_DATA_TRUSTEE = ResourceFactory
      .createResource(NS_CORE + "DataTrustee");

  private static final Resource CLASS_DATA_OWNER = ResourceFactory
      .createResource(NS_CORE + "DataOwner");

  private static final Resource CLASS_PERSONAL_DATA = ResourceFactory
      .createResource(NS_CORE + "PersonalData");

  private static final Resource CLASS_SPECIAL_PERSONAL_DATA = ResourceFactory
      .createResource(NS_CORE + "SpecialPersonalData");

  private static final Resource CLASS_TRADE_SECRET = ResourceFactory
      .createResource(NS_CORE + "TradeSecret");

  // Beispiel-Properties aus Legal für die Titel/Beschreibung
  private static final Property PROP_PROCESSING_BASIS_TITLE = ResourceFactory
      .createProperty(NS_LEGAL, "processingBasisTitle");

  private static final Property PROP_PROCESSING_BASIS_DESCRIPTION = ResourceFactory
      .createProperty(NS_LEGAL, "processingBasisDescription");

  // === Haupteinstieg: TTL -> DataTrusteeModel ===

  public DataTrusteeModel readFromTtlModel(Model ttlModel, String ontologyURI) {
    // 1) DataTrustee-Resource finden
    final Resource dataTrustee = this.findDataTrustee(ttlModel, ontologyURI);
    if (dataTrustee == null) {
      throw new IllegalStateException(
          "Kein DataTrustee in Ontologie " + ontologyURI + " gefunden.");
    }

    // 2) Core
    final DataTrusteeModelCore core = this.readCore(ttlModel, ontologyURI, dataTrustee);

    // 3) Legal
    final DataTrusteeModelLegal legal = this.readLegal(ttlModel, ontologyURI, dataTrustee);

    // 4) Data/Technik
    final DataTrusteeModelData data = this.readData(ttlModel, ontologyURI, dataTrustee);

    // 5) Business
    final DataTrusteeModelBusiness business = this.readBusiness(ttlModel, ontologyURI, dataTrustee);

    // 6) Objectives
    final DataTrusteeModelObjectives objectives = this
        .readObjectives(ttlModel, ontologyURI, dataTrustee);

    return DataTrusteeModel
        .builder()
        .core(core)
        .legal(legal)
        .data(data)
        .business(business)
        .objectives(objectives)
        .build();
  }

  // === 2) Core rekonstruieren ===

  private DataTrusteeModelCore readCore(Model m, String ontologyURI, Resource dataTrustee) {
    final String name = this.getLabel(dataTrustee); // Annahme: createDataTrustee hat rdfs:label gesetzt
    final String description = this.getOptionalString(dataTrustee, PROP_DATA_TRUSTEE_DESCRIPTION);
    // Operator-Ressource holen
    final Resource operatorRes = this
        .getOptionalResource(m, dataTrustee, PROP_HAS_DATA_TRUSTEE_OPERATOR);

    // Name des Operators aus dessen rdfs:label
    final String operator = operatorRes != null ? this.getLabel(operatorRes) : null;

    // Operator-Affiliation von der Operator-Ressource
    final Affiliation dataTrusteeOperatorAffiliation = operatorRes != null
        ? this.readAffiliation(m, operatorRes)
        : null;

    // RightsHolder: Ressource, die isAffectedPartyIn -> dataTrustee hat
    final Resource rightsHolder = this.findUniqueSubject(m, PROP_IS_AFFECTED_PARTY_IN, dataTrustee);

    // DataOwner und DataConsumer suchen
    final Resource dataOwner = this.findUniqueSubject(m, PROP_IS_DATA_OWNER_OF, dataTrustee);
    final Resource dataConsumer = this.findUniqueSubject(m, PROP_IS_DATA_CONSUMER_OF, dataTrustee);

    final String rightsHolderName = rightsHolder != null ? this.getLabel(rightsHolder) : null;
    final Affiliation rightsHolderAffiliation = rightsHolder != null
        ? this.readAffiliation(m, rightsHolder)
        : null;

    String dataOwnerName = null;
    String rightsHolderIsRepresented = null;
    if (dataOwner != null) {
      dataOwnerName = this.getLabel(dataOwner);
      // heuristisch: wenn dataOwner != rightsHolder, dann war rightsHolderIsRepresented = "YES"
      if (rightsHolder != null && !rightsHolder.equals(dataOwner)) {
        rightsHolderIsRepresented = "YES";
      } else {
        rightsHolderIsRepresented = "NO";
      }
    } else if (rightsHolder != null && this.hasRdfType(rightsHolder, CLASS_DATA_OWNER)) {
      // Rechteinhaber ist selbst DataOwner
      rightsHolderIsRepresented = "NO";
    }

    final String dataConsumerName = dataConsumer != null ? this.getLabel(dataConsumer) : null;
    final Affiliation dataConsumerAffiliation = dataConsumer != null
        ? this.readAffiliation(m, dataConsumer)
        : null;

    final Resource dataCategory = this.findDataCategoryForDataTrustee(m, dataTrustee);
    final String dataCategoryName = dataCategory != null ? this.getLabel(dataCategory) : null;
    final String dataCategoryDescription = this.getOptionalString(dataCategory, RDFS.comment);
    final String containPersonalInformation = dataCategory != null
        && this.hasRdfType(dataCategory, CLASS_PERSONAL_DATA) ? "YES" : "NO";
    final String specialPersonalInformation = dataCategory != null
        && this.hasRdfType(dataCategory, CLASS_SPECIAL_PERSONAL_DATA) ? "YES" : "NO";
    final String containTradeSecrets = dataCategory != null
        && this.hasRdfType(dataCategory, CLASS_TRADE_SECRET) ? "YES" : "NO";

    return DataTrusteeModelCore
        .builder()
        .ontologyURI(ontologyURI)
        .dataTrusteeName(name)
        .dataTrusteeDescription(description)
        .dataTrusteeOperator(operator)
        .dataTrusteeOperatorAffiliation(dataTrusteeOperatorAffiliation)
        .rightsHolderName(rightsHolderName)
        .rightsHolderAffiliation(rightsHolderAffiliation)
        .rightsHolderIsRepresented(rightsHolderIsRepresented)
        .dataOwnerName(dataOwnerName)
        .dataConsumerName(dataConsumerName)
        .dataConsumerAffiliation(dataConsumerAffiliation)
        .dataCategoryName(dataCategoryName)
        .dataCategoryDescription(dataCategoryDescription)
        .containPersonalInformation(containPersonalInformation)
        .specialPersonalInformation(specialPersonalInformation)
        .containTradeSecrets(containTradeSecrets)
        .build();
  }

  // === 3) Legal rekonstruieren ===

  private DataTrusteeModelLegal readLegal(Model m, String ontologyURI, Resource dataTrustee) {
    // DataConsumer finden
    final Resource dataConsumer = this.findUniqueSubject(m, PROP_IS_DATA_CONSUMER_OF, dataTrustee);
    if (dataConsumer == null) {
      return DataTrusteeModelLegal.builder().build();
    }

    final Map<ProcessingBasis, List<TitleDescriptionDTO>> processingBases = new java.util.LinkedHashMap<>();

    // Alle isBeneficiaryOf-Objekte
    final Property isBeneficiaryOf = ResourceFactory.createProperty(NS_LEGAL, "isBeneficiaryOf");
    final StmtIterator it = m.listStatements(dataConsumer, isBeneficiaryOf, (RDFNode) null);
    while (it.hasNext()) {
      final Statement st = it.nextStatement();
      final RDFNode obj = st.getObject();
      if (!obj.isResource()) {
        continue;
      }
      final Resource res = obj.asResource();

      // a) direkt eine URI eines ProcessingBasis-Enums?
      final ProcessingBasis basis = this
          .mapUriToEnum(res.getURI(), ProcessingBasis.values(), ProcessingBasis.AMBIGUOUS);
      if (basis != null && basis != ProcessingBasis.AMBIGUOUS) {
        processingBases.computeIfAbsent(basis, key -> new ArrayList<>());
        continue;
      }

      // b) sonst: Instanz mit rdf:type einer ProcessingBasis-Klasse
      final ProcessingBasis basisByType = this.findProcessingBasisByType(m, res);
      if (basisByType != null) {
        final List<TitleDescriptionDTO> details = processingBases
            .computeIfAbsent(basisByType, key -> new ArrayList<>());

        final String detail = this.readProcessingBasisDetail(m, res);
        if (detail != null) {
          details.add(TitleDescriptionDTO.builder().description(detail).build());
        }
        continue;
      }
      // c) Sonderfall: Consent (General/Individual) -> ProcessingBasis.CONSENT
      if (this.isConsentInstance(m, res)) {
        final List<TitleDescriptionDTO> details = processingBases
            .computeIfAbsent(ProcessingBasis.CONSENT, key -> new ArrayList<>());

        final String detail = this.readConsentBasisDetail(m, res);
        if (detail != null) {
          details.add(TitleDescriptionDTO.builder().description(detail).build());
        }
      }

    }

    // Consent-Type rekonstruieren
    final ConsentType consentType = this.inferConsentType(m, dataTrustee, dataConsumer);

    // consentEnteredBy rekonstruieren (stark vereinfacht)
    final ConsentEnteredBy consentEnteredBy = this
        .inferConsentEnteredBy(m, ontologyURI, dataTrustee);

    return DataTrusteeModelLegal
        .builder()
        .processingBases(processingBases.isEmpty() ? null : processingBases)
        .consentType(consentType)
        .obtainingConsentBy(null)                  // im Forward-Code nicht als RDF vorhanden
        .consentEnteredBy(consentEnteredBy)
        .build();
  }

  private String readProcessingBasisDetail(Model m, Resource basisInstance) {
    final String desc = this.getOptionalString(basisInstance, PROP_PROCESSING_BASIS_DESCRIPTION);
    if (desc == null || desc.isBlank()) {
      return null;
    }
    return desc;
  }

  private ProcessingBasis findProcessingBasisByType(Model m, Resource instance) {
    final StmtIterator it = m.listStatements(instance, RDF.type, (RDFNode) null);
    while (it.hasNext()) {
      final Statement st = it.nextStatement();
      final RDFNode obj = st.getObject();
      if (!obj.isResource()) {
        continue;
      }
      final String typeUri = obj.asResource().getURI();
      final ProcessingBasis b = this.mapUriToEnum(typeUri, ProcessingBasis.values(), null);
      if (b != null) {
        return b;
      }
    }
    return null;
  }

  private ConsentType inferConsentType(Model m, Resource dataTrustee, Resource dataConsumer) {
    // Vereinfachte Heuristik:
    // Suche nach Individuen vom Typ GeneralConsent / IndividualConsent
    final Resource generalConsentClass = ResourceFactory
        .createResource(ConsentType.GENERAL_CONSENT.getUri());
    final Resource individualConsentClass = ResourceFactory
        .createResource(ConsentType.INDIVIDUAL_CONSENT.getUri());

    final Resource anyGeneral = this.findInstanceOf(m, generalConsentClass);
    final Resource anyIndividual = this.findInstanceOf(m, individualConsentClass);

    if (anyGeneral != null) {
      return ConsentType.GENERAL_CONSENT;
    }
    if (anyIndividual != null) {
      return ConsentType.INDIVIDUAL_CONSENT;
    }

    return ConsentType.NOT_PROVIDED;
  }

  private ConsentEnteredBy inferConsentEnteredBy(Model m, String ontologyURI,
      Resource dataTrustee) {
    // In Ihrer Forward-Methode wird ConsentEnteredBy nur über Eigenschaften wie
    //  - givesConsentDirectly
    //  - givesConsentByDataOwner
    // modelliert. Hier sehr einfache Heuristik:
    final Property givesConsentDirectly = ResourceFactory
        .createProperty(NS_LEGAL, "givesConsentDirectly");
    final Property givesConsentByDataOwner = ResourceFactory
        .createProperty(NS_LEGAL, "givesConsentByDataOwner");

    if (m.listStatements(null, givesConsentDirectly, (RDFNode) null).hasNext()) {
      return ConsentEnteredBy.RIGHTS_OWNER;
    }
    if (m.listStatements(null, givesConsentByDataOwner, (RDFNode) null).hasNext()) {
      return ConsentEnteredBy.DATA_OWNER;
    }
    return ConsentEnteredBy.NOT_PROVIDED;
  }

  // === 4) Data/Technik rekonstruieren (vereinfachtes Beispiel) ===

  private DataTrusteeModelData readData(Model m, String ontologyURI, Resource dataTrustee) {
    // DataTrusteeModel-Instanz: dataTrustee --hasDataTrusteeModel--> dtmInstance
    final Property hasDataTrusteeModel = ResourceFactory
        .createProperty(NS_CORE, "hasDataTrusteeModel");
    final Resource dtmInstance = this.getOptionalResource(m, dataTrustee, hasDataTrusteeModel);

    DataTrusteeCategory dtCategory = null;
    ArchitectureType architectureType = null;

    if (dtmInstance != null) {
      // Typ des Datentreuhändermodells -> DataTrusteeCategories
      dtCategory = this.findDataTrusteeCategory(m, dtmInstance);

      // implementedOn -> ArchitectureType
      final Property implementedOn = ResourceFactory.createProperty(NS_DATA, "implementedOn");
      final Resource archRes = this.getOptionalResource(m, dtmInstance, implementedOn);
      if (archRes != null && archRes.getURI() != null) {
        architectureType = this
            .mapUriToEnum(archRes.getURI(), ArchitectureType.values(), ArchitectureType.AMBIGUOUS);
      }
    }

    // === RECEPTION ===
    final EnumSet<ReceptionTechnique> receptionTechniques = EnumSet
        .noneOf(ReceptionTechnique.class);
    Frequency receptionFrequency = null;
    final EnumSet<SecurityMeasure> receptionSecurityMeasures = EnumSet
        .noneOf(SecurityMeasure.class);
    final List<TitleDescriptionDTO> customReceptionSecurityTechniques = new ArrayList<>();
    String sourceSystem = null;

    // Instanz des DataLifecycle RECEPTION (Annahme: genau eine oder keine)
    final Resource receptionInstance = this
        .findLifecycleInstance(m, DataLifecycle.RECEPTION, dataTrustee);
    if (receptionInstance != null) {
      // hasReceptionTechnique
      final Property hasReceptionTechnique = ResourceFactory
          .createProperty(NS_DATA, "hasReceptionTechnique");
      final StmtIterator itTech = m
          .listStatements(receptionInstance, hasReceptionTechnique, (RDFNode) null);
      while (itTech.hasNext()) {
        final RDFNode obj = itTech.nextStatement().getObject();
        if (!obj.isResource()) {
          continue;
        }
        final ReceptionTechnique t = this
            .mapUriToEnum(obj.asResource().getURI(), ReceptionTechnique.values(),
                ReceptionTechnique.AMBIGUOUS);
        if (t != null && t != ReceptionTechnique.AMBIGUOUS) {
          receptionTechniques.add(t);
        }
      }

      // hasReceptionFrequency
      final Property hasReceptionFrequency = ResourceFactory
          .createProperty(NS_DATA, "hasReceptionFrequency");
      final Resource freqRes = this
          .getOptionalResource(m, receptionInstance, hasReceptionFrequency);
      if (freqRes != null) {
        receptionFrequency = this
            .mapUriToEnum(freqRes.getURI(), Frequency.values(), Frequency.AMBIGUOUS);
      }

      // securedByTechnique
      final Property securedByTechnique = ResourceFactory
          .createProperty(NS_DATA, "securedByTechnique");
      final StmtIterator itSec = m
          .listStatements(receptionInstance, securedByTechnique, (RDFNode) null);
      while (itSec.hasNext()) {
        final RDFNode obj = itSec.nextStatement().getObject();
        if (!obj.isResource()) {
          continue;
        }
        final Resource secRes = obj.asResource();
        final SecurityMeasure s = this
            .mapUriToEnum(secRes.getURI(), SecurityMeasure.values(), SecurityMeasure.AMBIGUOUS);
        if (s != null && s != SecurityMeasure.AMBIGUOUS && s != SecurityMeasure.CUSTOM) {
          receptionSecurityMeasures.add(s);
        } else {
          // Custom-Sicherheitsmaßnahme, z.B. :CustomReceptionSecurityVPN-Tunnel
          String title = this.getLabel(secRes);
          title = this
              .stripPrefix(title, StaticText.PREFIX_CUSTOM + StaticText.PREFIX_RECEPTION
                  + StaticText.PREFIX_SECURITY);
          customReceptionSecurityTechniques
              .add(TitleDescriptionDTO
                  .builder()
                  .title(title)
                  .description(null) // Beschreibung ist in der TTL nicht modelliert
                  .build());
        }
      }

      // usesSystem -> SourceSystem
      final Property usesSystem = ResourceFactory.createProperty(NS_DATA, "usesSystem");
      final Resource sourceSystemRes = this.getOptionalResource(m, receptionInstance, usesSystem);
      if (sourceSystemRes != null) {
        final String raw = this.getLabel(sourceSystemRes);
        sourceSystem = this.stripPrefix(raw, StaticText.PREFIX_SOURCE_SYSTEM);
      }
    }

    // === PREPARATION ===
    final EnumSet<PreparationTechnique> preparationTechniques = EnumSet
        .noneOf(PreparationTechnique.class);
    final List<TitleDescriptionDTO> customPreparationTechniques = new ArrayList<>();

    final Resource preparationInstance = this
        .findLifecycleInstance(m, DataLifecycle.PREPARATION, dataTrustee);
    if (preparationInstance != null) {
      final Property hasPreparationTechnique = ResourceFactory
          .createProperty(NS_DATA, "hasPreparationTechnique");
      final StmtIterator itPrep = m
          .listStatements(preparationInstance, hasPreparationTechnique, (RDFNode) null);
      while (itPrep.hasNext()) {
        final RDFNode obj = itPrep.nextStatement().getObject();
        if (!obj.isResource()) {
          continue;
        }
        final Resource techRes = obj.asResource();
        final PreparationTechnique pt = this
            .mapUriToEnum(techRes.getURI(), PreparationTechnique.values(), null);
        if (pt != null) {
          if (pt != PreparationTechnique.AMBIGUOUS) {
            preparationTechniques.add(pt);
          }
        } else {
          // Custom-Preparation (z.B. :CustomPreparationFeatureEngineering)
          String title = this.getLabel(techRes);
          title = this.stripPrefix(title, StaticText.PREFIX_CUSTOM + StaticText.PREFIX_PREPARATION);

          customPreparationTechniques
              .add(TitleDescriptionDTO.builder().title(title).description(null).build());
        }
      }
    }

    // === STORAGE ===
    StorageTechnique storageTechnique = null;
    StorageRetention storageRetention = null;

    final Resource storageInstance = this
        .findLifecycleInstance(m, DataLifecycle.STORAGE, dataTrustee);
    if (storageInstance != null) {
      final Property hasStorageTechnique = ResourceFactory
          .createProperty(NS_DATA, "hasStorageTechnique");
      final Resource stRes = this.getOptionalResource(m, storageInstance, hasStorageTechnique);
      if (stRes != null) {
        storageTechnique = this
            .mapUriToEnum(stRes.getURI(), StorageTechnique.values(), StorageTechnique.AMBIGUOUS);
      }

      final Property hasRetentionPeriod = ResourceFactory
          .createProperty(NS_DATA, "hasRetentionPeriod");
      final Resource retRes = this.getOptionalResource(m, storageInstance, hasRetentionPeriod);
      if (retRes != null) {
        storageRetention = this
            .mapUriToEnum(retRes.getURI(), StorageRetention.values(), StorageRetention.AMBIGUOUS);
      }
    }

    // === ANALYSIS ===
    final EnumSet<AnalysisTechnique> analysisTechniques = EnumSet.noneOf(AnalysisTechnique.class);
    final List<TitleDescriptionDTO> customAnalysisTechniques = new ArrayList<>();

    final Resource analysisInstance = this
        .findLifecycleInstance(m, DataLifecycle.ANALYSIS, dataTrustee);
    if (analysisInstance != null) {
      final Property hasAnalysisTechnique = ResourceFactory
          .createProperty(NS_DATA, "hasDataAnalysisTechnique");
      final StmtIterator itAnalysis = m
          .listStatements(analysisInstance, hasAnalysisTechnique, (RDFNode) null);
      while (itAnalysis.hasNext()) {
        final RDFNode obj = itAnalysis.nextStatement().getObject();
        if (!obj.isResource()) {
          continue;
        }
        final Resource techRes = obj.asResource();
        final AnalysisTechnique at = this
            .mapUriToEnum(techRes.getURI(), AnalysisTechnique.values(), null);
        if (at != null) {
          if (at != AnalysisTechnique.AMBIGUOUS) {
            analysisTechniques.add(at);
          }
        } else {
          // ggf. Custom-Analyse aus Label
          final String title = this.getLabel(techRes);
          customAnalysisTechniques
              .add(TitleDescriptionDTO.builder().title(title).description(null).build());
        }
      }
    }

    // === FORWARDING ===
    final EnumSet<ForwardingTechnique> forwardingTechniques = EnumSet
        .noneOf(ForwardingTechnique.class);
    Frequency forwardingFrequency = null;
    final EnumSet<SecurityMeasure> forwardingSecurityMeasures = EnumSet
        .noneOf(SecurityMeasure.class);
    final List<TitleDescriptionDTO> customForwardingSecurityTechniques = new ArrayList<>();
    String targetSystem = null;

    final Resource forwardingInstance = this
        .findLifecycleInstance(m, DataLifecycle.FORWARDING, dataTrustee);
    if (forwardingInstance != null) {
      final Property hasForwardingTechnique = ResourceFactory
          .createProperty(NS_DATA, "hasForwardingTechnique");
      final StmtIterator itFwd = m
          .listStatements(forwardingInstance, hasForwardingTechnique, (RDFNode) null);
      while (itFwd.hasNext()) {
        final RDFNode obj = itFwd.nextStatement().getObject();
        if (!obj.isResource()) {
          continue;
        }
        final ForwardingTechnique ft = this
            .mapUriToEnum(obj.asResource().getURI(), ForwardingTechnique.values(),
                ForwardingTechnique.AMBIGUOUS);
        if (ft != null && ft != ForwardingTechnique.AMBIGUOUS) {
          forwardingTechniques.add(ft);
        }
      }

      final Property hasForwardingFrequency = ResourceFactory
          .createProperty(NS_DATA, "hasForwardingFrequency");
      final Resource freqResFwd = this
          .getOptionalResource(m, forwardingInstance, hasForwardingFrequency);
      if (freqResFwd != null) {
        forwardingFrequency = this
            .mapUriToEnum(freqResFwd.getURI(), Frequency.values(), Frequency.AMBIGUOUS);
      }

      final Property securedByTechniqueFwd = ResourceFactory
          .createProperty(NS_DATA, "securedByTechnique");
      final StmtIterator itSecFwd = m
          .listStatements(forwardingInstance, securedByTechniqueFwd, (RDFNode) null);
      while (itSecFwd.hasNext()) {
        final RDFNode obj = itSecFwd.nextStatement().getObject();
        if (!obj.isResource()) {
          continue;
        }
        final Resource secRes = obj.asResource();
        final SecurityMeasure s = this
            .mapUriToEnum(secRes.getURI(), SecurityMeasure.values(), SecurityMeasure.AMBIGUOUS);
        if (s != null && s != SecurityMeasure.AMBIGUOUS && s != SecurityMeasure.CUSTOM) {
          forwardingSecurityMeasures.add(s);
        } else {
          String title = this.getLabel(secRes);
          title = this
              .stripPrefix(title, StaticText.PREFIX_CUSTOM + StaticText.PREFIX_FORWARDING
                  + StaticText.PREFIX_SECURITY);
          customForwardingSecurityTechniques
              .add(TitleDescriptionDTO.builder().title(title).description(null).build());
        }
      }

      final Property usesSystemFwd = ResourceFactory.createProperty(NS_DATA, "usesSystem");
      final Resource targetRes = this.getOptionalResource(m, forwardingInstance, usesSystemFwd);
      if (targetRes != null) {
        final String raw = this.getLabel(targetRes);
        targetSystem = this.stripPrefix(raw, StaticText.PREFIX_TARGET_SYSTEM);
      }
    }

    return DataTrusteeModelData
        .builder()
        .dataTrusteeCategory(dtCategory)
        .architectureType(architectureType)

        .receptionTechnologies(receptionTechniques.isEmpty() ? null : receptionTechniques)
        .receptionFrequency(receptionFrequency)
        .receptionSecurityMeasures(
            receptionSecurityMeasures.isEmpty() ? null : receptionSecurityMeasures)
        .customReceptionSecurityTechniques(
            customReceptionSecurityTechniques.isEmpty() ? null : customReceptionSecurityTechniques)
        .sourceSystem(sourceSystem)

        .preparationTechniques(preparationTechniques.isEmpty() ? null : preparationTechniques)
        .customPreparationTechniques(
            customPreparationTechniques.isEmpty() ? null : customPreparationTechniques)

        .storageTechnique(storageTechnique)
        .storageRetention(storageRetention)

        .analysisTechniques(analysisTechniques.isEmpty() ? null : analysisTechniques)
        .customAnalysisTechniques(
            customAnalysisTechniques.isEmpty() ? null : customAnalysisTechniques)

        .forwardingTechniques(forwardingTechniques.isEmpty() ? null : forwardingTechniques)
        .forwardingFrequency(forwardingFrequency)
        .forwardingSecurityMeasures(
            forwardingSecurityMeasures.isEmpty() ? null : forwardingSecurityMeasures)
        .customForwardingSecurityTechniques(customForwardingSecurityTechniques.isEmpty() ? null
            : customForwardingSecurityTechniques)
        .targetSystem(targetSystem)
        .build();
  }

  private DataTrusteeCategory findDataTrusteeCategory(Model m, Resource dtmInstance) {
    final StmtIterator it = m.listStatements(dtmInstance, RDF.type, (RDFNode) null);
    while (it.hasNext()) {
      final RDFNode obj = it.nextStatement().getObject();
      if (!obj.isResource()) {
        continue;
      }
      final String typeUri = obj.asResource().getURI();
      final DataTrusteeCategory cat = this
          .mapUriToEnum(typeUri, DataTrusteeCategory.values(), null);
      if (cat != null) {
        return cat;
      }
    }
    return null;
  }

  private Resource findLifecycleInstance(Model m, DataLifecycle lifecycle, Resource dataTrustee) {
    final Resource lifecycleClass = ResourceFactory.createResource(lifecycle.getUri());
    final ResIterator it = m.listResourcesWithProperty(RDF.type, lifecycleClass);
    return it.hasNext() ? it.nextResource() : null;
  }

  // === 5) Business rekonstruieren (verkürzt) ===

  private DataTrusteeModelBusiness readBusiness(Model m, String ontologyURI, Resource dataTrustee) {
    final EnumSet<BusinessDomain> domains = this
        .readEnumSetObjectProperties(m, dataTrustee,
            ResourceFactory.createProperty(NS_BUSINESS, "hasDomain"), BusinessDomain.values());

    final BusinessModel businessModel = this
        .readEnumObjectProperty(m, dataTrustee,
            ResourceFactory.createProperty(NS_BUSINESS, "hasBusinessForm"), BusinessModel.values(),
            BusinessModel.AMBIGUOUS);

    final EnumSet<FundingSource> fundingSources = this
        .readEnumSetObjectProperties(m, dataTrustee,
            ResourceFactory.createProperty(NS_BUSINESS, "hasFundingSource"),
            FundingSource.values());

    final EnumSet<PayMethods> paymentMethods = this
        .readEnumSetObjectProperties(m, dataTrustee,
            ResourceFactory.createProperty(NS_BUSINESS, "offersPayModel"), PayMethods.values());

    // PayModel DataOwner / DataConsumer
    final Resource dataOwner = this
        .findUniqueSubject(m,
            ResourceFactory.createProperty(NS_BUSINESS, "usesPayModelAsDataOwner"), null);
    PayMethods paymentMethodDataOwner = null;
    if (dataOwner != null) {
      paymentMethodDataOwner = this
          .readEnumObjectProperty(m, dataOwner,
              ResourceFactory.createProperty(NS_BUSINESS, "usesPayModelAsDataOwner"),
              PayMethods.values(), null);
    }

    final Resource dataConsumer = this
        .findUniqueSubject(m,
            ResourceFactory.createProperty(NS_BUSINESS, "usesPayModelAsDataConsumer"), null);
    PayMethods paymentMethodDataConsumer = null;
    if (dataConsumer != null) {
      paymentMethodDataConsumer = this
          .readEnumObjectProperty(m, dataConsumer,
              ResourceFactory.createProperty(NS_BUSINESS, "usesPayModelAsDataConsumer"),
              PayMethods.values(), null);
    }

    return DataTrusteeModelBusiness
        .builder()
        .businessDomains(domains)
        .businessModel(businessModel)
        .fundingSources(fundingSources.isEmpty() ? null : fundingSources)
        //        .paymentMethods(paymentMethods.isEmpty() ? null : paymentMethods)
        .paymentMethodDataOwner(paymentMethodDataOwner)
        .paymentMethodDataConsumer(paymentMethodDataConsumer)
        .build();
  }

  // === 6) Objectives rekonstruieren ===

  private DataTrusteeModelObjectives readObjectives(Model m, String ontologyURI,
      Resource dataTrustee) {
    final EnumSet<DataTrusteeGoal> dataTrusteeGoals = this
        .readEnumSetObjectProperties(m, dataTrustee,
            ResourceFactory.createProperty(NS_BUSINESS, "hasDataTrusteeGoal"),
            DataTrusteeGoal.values());

    // DataOwner oder RightsHolder (wie im Forward-Code)
    final Resource dataOwner = this.findUniqueSubject(m, PROP_IS_DATA_OWNER_OF, dataTrustee);
    final Resource rightsHolder = this.findUniqueSubject(m, PROP_IS_AFFECTED_PARTY_IN, dataTrustee);
    final Resource ownerOrRightsHolder = dataOwner != null ? dataOwner : rightsHolder;

    final EnumSet<MotivationRightsHolder> motivationRightsHolder = ownerOrRightsHolder != null
        ? this
            .readEnumSetObjectProperties(m, ownerOrRightsHolder,
                ResourceFactory.createProperty(NS_BUSINESS, "hasUsageGoal"),
                MotivationRightsHolder.values())
        : null;

    final EnumSet<MotivationDataHolder> motivationDataHolder = ownerOrRightsHolder != null
        ? this
            .readEnumSetObjectProperties(m, ownerOrRightsHolder,
                ResourceFactory.createProperty(NS_BUSINESS, "hasUsageGoal"),
                MotivationDataHolder.values())
        : null;

    final Resource dataConsumer = this.findUniqueSubject(m, PROP_IS_DATA_CONSUMER_OF, dataTrustee);
    final EnumSet<MotivationDataConsumer> motivationDataConsumer = dataConsumer != null
        ? this
            .readEnumSetObjectProperties(m, dataConsumer,
                ResourceFactory.createProperty(NS_BUSINESS, "hasUsageGoal"),
                MotivationDataConsumer.values())
        : null;

    return DataTrusteeModelObjectives
        .builder()
        .dataTrusteeGoals(
            dataTrusteeGoals == null || dataTrusteeGoals.isEmpty() ? null : dataTrusteeGoals)
        .motivationRightsHolder(
            motivationRightsHolder == null || motivationRightsHolder.isEmpty() ? null
                : motivationRightsHolder)
        .motivationDataHolder(
            motivationDataHolder == null || motivationDataHolder.isEmpty() ? null
                : motivationDataHolder)
        .motivationDataConsumer(
            motivationDataConsumer == null || motivationDataConsumer.isEmpty() ? null
                : motivationDataConsumer)
        .build();
  }

  // === Hilfsfunktionen: Suche, Labels, Enums ===

  private Resource findDataTrustee(Model m, String ontologyURI) {
    final ResIterator it = m.listResourcesWithProperty(RDF.type, CLASS_DATA_TRUSTEE);
    while (it.hasNext()) {
      final Resource r = it.nextResource();
      if (r.getURI() != null && r.getURI().startsWith(ontologyURI)) {
        return r;
      }
    }
    return null;
  }

  private String getLabel(Resource r) {
    if (r == null) {
      return null;
    }
    final Statement st = r
        .getProperty(ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#label"));
    if (st != null && st.getObject().isLiteral()) {
      return st.getString();
    }
    return r.getLocalName();
  }

  private String getOptionalString(Resource r, Property p) {
    if (r == null) {
      return null;
    }
    final Statement st = r.getProperty(p);
    if (st == null || !st.getObject().isLiteral()) {
      return null;
    }
    return st.getString();
  }

  private boolean hasRdfType(Resource r, Resource type) {
    return r.hasProperty(RDF.type, type);
  }

  private Resource findDataCategoryForDataTrustee(Model m, Resource dataTrustee) {
    // dataCategory --isRightlyOwnedBy--> rightsHolder, etc.
    // Einfach: irgend eine Ressource, die isRightlyOwnedBy -> rightsHolder hat.
    final Resource rightsHolder = this.findUniqueSubject(m, PROP_IS_AFFECTED_PARTY_IN, dataTrustee);
    if (rightsHolder == null) {
      return null;
    }
    return this.findUniqueSubject(m, PROP_IS_RIGHTLY_OWNED_BY, rightsHolder);
  }

  private Resource findUniqueSubject(Model m, Property p, Resource o) {
    final StmtIterator it = o == null ? m.listStatements(null, p, (RDFNode) null)
        : m.listStatements(null, p, o);
    if (!it.hasNext()) {
      return null;
    }
    final Resource r = it.nextStatement().getSubject();
    if (it.hasNext()) {
      // Mehrere gefunden – hier könnte man Logging einbauen
    }
    return r;
  }

  private Resource getOptionalResource(Model m, Resource s, Property p) {
    if (s == null) {
      return null;
    }
    final Statement st = m.getProperty(s, p);
    if (st == null || !st.getObject().isResource()) {
      return null;
    }
    return st.getResource();
  }

  private Affiliation readAffiliation(Model m, Resource actor) {
    final Resource affRes = this.getOptionalResource(m, actor, PROP_AFFILIATION_TYPE);
    if (affRes == null) {
      return null;
    }
    return this.mapUriToEnum(affRes.getURI(), Affiliation.values(), null);
  }

  private Resource findInstanceOf(Model m, Resource clazz) {
    final ResIterator it = m.listResourcesWithProperty(RDF.type, clazz);
    return it.hasNext() ? it.nextResource() : null;
  }

  private <E extends Enum<E>> E mapUriToEnum(String uri, E[] values, E defaultVal) {
    if (uri == null) {
      return defaultVal;
    }
    for (final E v : values) {
      String enumUri;
      if (v instanceof DataTrusteeCategory) {
        enumUri = ((DataTrusteeCategory) v).getUri();
      } else if (v instanceof ArchitectureType) {
        enumUri = ((ArchitectureType) v).getUri();
      } else if (v instanceof ReceptionTechnique) {
        enumUri = ((ReceptionTechnique) v).getUri();
      } else if (v instanceof ForwardingTechnique) {
        enumUri = ((ForwardingTechnique) v).getUri();
      } else if (v instanceof PreparationTechnique) {
        enumUri = ((PreparationTechnique) v).getUri();
      } else if (v instanceof AnalysisTechnique) {
        enumUri = ((AnalysisTechnique) v).getUri();
      } else if (v instanceof StorageTechnique) {
        enumUri = ((StorageTechnique) v).getUri();
      } else if (v instanceof StorageRetention) {
        enumUri = ((StorageRetention) v).getUri();
      } else if (v instanceof Frequency) {
        enumUri = ((Frequency) v).getUri();
      } else if (v instanceof SecurityMeasure) {
        enumUri = ((SecurityMeasure) v).getUri();
      } else if (v instanceof Affiliation) {
        enumUri = ((Affiliation) v).getUri();
      } else if (v instanceof PayMethods) {
        enumUri = ((PayMethods) v).getUri();
      } else if (v instanceof BusinessDomain) {
        enumUri = ((BusinessDomain) v).getUri();
      } else if (v instanceof BusinessModel) {
        enumUri = ((BusinessModel) v).getUri();
      } else if (v instanceof FundingSource) {
        enumUri = ((FundingSource) v).getUri();
      } else if (v instanceof DataTrusteeGoal) {
        enumUri = ((DataTrusteeGoal) v).getUri();
      } else if (v instanceof MotivationRightsHolder) {
        enumUri = ((MotivationRightsHolder) v).getUri();
      } else if (v instanceof MotivationDataHolder) {
        enumUri = ((MotivationDataHolder) v).getUri();
      } else if (v instanceof MotivationDataConsumer) {
        enumUri = ((MotivationDataConsumer) v).getUri();
      } else if (v instanceof ProcessingBasis) {
        enumUri = ((ProcessingBasis) v).getUri();
      } else if (v instanceof ConsentType) {
        enumUri = ((ConsentType) v).getUri();
      } else if (v instanceof ConsentEnteredBy) {
        enumUri = ((ConsentEnteredBy) v).getUri();
      } else {
        enumUri = null;
      }

      if (enumUri != null && enumUri.equals(uri)) {
        return v;
      }
    }
    return defaultVal;
  }

  private String stripPrefix(String label, String prefix) {
    if (label == null) {
      return null;
    }
    return label.startsWith(prefix) ? label.substring(prefix.length()) : label;
  }

  private boolean isConsentInstance(Model m, Resource res) {
    final Resource generalConsentClass = ResourceFactory
        .createResource(ConsentType.GENERAL_CONSENT.getUri());
    final Resource individualConsentClass = ResourceFactory
        .createResource(ConsentType.INDIVIDUAL_CONSENT.getUri());
    return res.hasProperty(RDF.type, generalConsentClass)
        || res.hasProperty(RDF.type, individualConsentClass);
  }

  /**
   * Holt Titel/Beschreibung für CONSENT über den Workflow:
   * Consent --dtm-legal:implementedBy--> ConsentWorkflow
   * ConsentWorkflow hat processingBasisTitle / processingBasisDescription.
   */
  private String readConsentBasisDetail(Model m, Resource consentInstance) {
    final Property implementedBy = ResourceFactory.createProperty(NS_LEGAL, "implementedBy");
    final Resource workflow = this.getOptionalResource(m, consentInstance, implementedBy);
    if (workflow == null) {
      return null;
    }

    final String desc = this.getOptionalString(workflow, PROP_PROCESSING_BASIS_DESCRIPTION);
    if (desc == null || desc.isBlank()) {
      return null;
    }
    return desc;
  }

  private <E extends Enum<E>> EnumSet<E> readEnumSetObjectProperties(Model m, Resource subject,
      Property p, E[] values) {

    final EnumSet<E> result = EnumSet.noneOf(values[0].getDeclaringClass());
    if (subject == null) {
      return result;
    }
    final StmtIterator it = m.listStatements(subject, p, (RDFNode) null);
    while (it.hasNext()) {
      final RDFNode obj = it.nextStatement().getObject();
      if (!obj.isResource()) {
        continue;
      }
      final String uri = obj.asResource().getURI();
      final E e = this.mapUriToEnum(uri, values, null);
      if (e != null) {
        result.add(e);
      }
    }
    return result;
  }

  private <E extends Enum<E>> E readEnumObjectProperty(Model m, Resource subject, Property p,
      E[] values, E defaultVal) {
    if (subject == null) {
      return defaultVal;
    }
    final Resource obj = this.getOptionalResource(m, subject, p);
    if (obj == null || obj.getURI() == null) {
      return defaultVal;
    }
    return this.mapUriToEnum(obj.getURI(), values, defaultVal);
  }

  public static void main(String[] args) {
    // === HIER deinen Pfad zur TTL-Datei und die Ontology-URI setzen ===
    final String ttlPath = "C:\\git\\ForgeTrustee\\Demonstrator\\backend\\data\\httpsmustergmbh.deontology.ttl";  // oder "./resources/model.ttl"
    final String ontologyURI = "https://mustergmbh.de/ontology/";
    // ggf. an deine Ontologie-Basis-URI anpassen

    // 1) Jena Model anlegen und TTL einlesen
    final Model model = ModelFactory.createDefaultModel();
    RDFDataMgr.read(model, ttlPath, Lang.TURTLE);

    // 2) Reader verwenden
    final DataTrusteeModelReader reader = new DataTrusteeModelReader();
    final DataTrusteeModel dataTrusteeModel = reader.readFromTtlModel(model, ontologyURI);

    // 3) Ausgabe – nutzt Lombok-@Data-toString()
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT); // "beautified"
      final String json = mapper.writeValueAsString(dataTrusteeModel);
      System.out.println("==== Rekonstruiertes DataTrusteeModel ====");
      System.out.println(json);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }
}
