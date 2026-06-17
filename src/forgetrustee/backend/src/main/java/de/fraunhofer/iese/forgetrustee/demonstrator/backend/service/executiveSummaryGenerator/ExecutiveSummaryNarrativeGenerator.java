
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.executiveSummaryGenerator;
/* Created by chwalek on 05.03.2026 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.*;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardImplementation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.PayMethods;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExecutiveSummaryNarrativeGenerator {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final CoreSectionGenerator coreGenerator = new CoreSectionGenerator();

  private final LegalSectionGenerator legalGenerator = new LegalSectionGenerator();

  private final DataSectionGenerator technicalGenerator = new DataSectionGenerator();

  private final BusinessSectionGenerator businessGenerator = new BusinessSectionGenerator();

  private final ObjectivesSectionGenerator objectivesGenerator = new ObjectivesSectionGenerator();

  /**
   * Liest eine JSON-Datei ein, mapped sie auf DataTrusteeModel
   * und erzeugt die Executive Summary (HTML).
   */
  public String generateFromJsonFile(String jsonFilePath) {
    try {
      final String json = Files.readString(Path.of(jsonFilePath));
      final DataTrusteeModel model = this.objectMapper.readValue(json, DataTrusteeModel.class);
      return this.generate(model);
    } catch (final IOException e) {
      return "Fehler beim Lesen oder Verarbeiten der JSON-Datei: " + e.getMessage();
    }
  }

  /**
   * Nimmt ein fertiges DataTrusteeModel und erzeugt
   * eine HTML-Executive-Summary.
   */
  public String generate(DataTrusteeModel model) {
    if (model == null || model.getCore() == null) {
      return "";
    }

    final StringBuilder sb = new StringBuilder();

    final String name = NarrativeUtils
        .safe(model.getCore().getDataTrusteeName(), "Datentreuhandmodell");
    sb.append("<h2>Executive Summary – Datentreuhandmodell „").append(name).append("“</h2>\n");

    sb.append(this.coreGenerator.generate(model)).append("<br/>\n");
    sb.append(this.legalGenerator.generate(model)).append("<br/>\n");
    sb.append(this.technicalGenerator.generate(model)).append("<br/>\n");
    sb.append(this.businessGenerator.generate(model)).append("<br/>\n");
    sb.append(this.objectivesGenerator.generate(model)).append("<br/>\n");

    return sb.toString();
  }

  public String generate(DataTrusteeWizardModel model) {
    return this.generate(this.toDataTrusteeModel(model));
  }

  private DataTrusteeModel toDataTrusteeModel(DataTrusteeWizardModel wizardModel) {
    if (wizardModel == null) {
      return null;
    }

    return DataTrusteeModel
        .builder()
        .core(this.toCoreModel(wizardModel.getCore(), wizardModel.getLegal()))
        .legal(this.toLegalModel(wizardModel.getLegal()))
        .data(this.toDataModel(wizardModel.getData()))
        .business(this.toBusinessModel(wizardModel.getBusiness()))
        .objectives(this.toObjectivesModel(wizardModel.getObjectives()))
        .build();
  }

  private DataTrusteeModelCore toCoreModel(
      DataTrusteeWizardCore core, DataTrusteeWizardData data) {

    if (core == null && data == null) {
      return null;
    }

    return DataTrusteeModelCore
        .builder()
        .ontologyURI(core != null ? core.getOntologyURI() : null)
        .dataTrusteeName(core != null ? core.getDataTrusteeName() : null)
        .dataTrusteeDescription(core != null ? core.getDataTrusteeDescription() : null)
        .dataTrusteeOperator(core != null ? core.getDataTrusteeOperator() : null)
        .dataTrusteeOperatorAffiliation(
            core != null ? core.getDataTrusteeOperatorAffiliation() : null)
        .rightsHolderName(core != null ? core.getRightsHolderName() : null)
        .rightsHolderAffiliation(core != null ? core.getRightsHolderAffiliation() : null)
        .rightsHolderIsRepresented(core != null ? core.getRightsHolderIsRepresented() : null)
        .dataOwnerName(core != null ? core.getDataOwnerName() : null)
        .dataConsumerName(core != null ? core.getDataConsumerName() : null)
        .dataConsumerAffiliation(core != null ? core.getDataConsumerAffiliation() : null)
        .dataCategoryName(data != null ? data.getDataCategory().getTitle() : null)
        .dataCategoryDescription(data != null ? data.getDataCategory().getDescription() : null)
        .containPersonalInformation(data != null ? data.getContainPersonalInformation() : null)
        .specialPersonalInformation(data != null ? data.getSpecialPersonalInformation() : null)
        .containTradeSecrets(data != null ? data.getContainTradeSecrets() : null)
        .build();
  }

  private DataTrusteeModelLegal toLegalModel(
      DataTrusteeWizardData legal) {

    if (legal == null) {
      return null;
    }

    return DataTrusteeModelLegal
        .builder()
        .processingBases(legal.getProcessingBases())
        .consentType(legal.getConsentType())
        .obtainingConsentBy(legal.getObtainingConsentBy())
        .consentEnteredBy(legal.getConsentEnteredBy())
        .build();
  }

  private DataTrusteeModelData toDataModel(
      DataTrusteeWizardImplementation implementation) {

    if (implementation == null) {
      return null;
    }

    return DataTrusteeModelData
        .builder()
        .dataTrusteeCategory(implementation.getDataTrusteeCategory())
        .architectureType(implementation.getArchitectureType())
        .receptionTechnologies(implementation.getReceptionTechnologies())
        .receptionFrequency(implementation.getReceptionFrequency())
        .receptionSecurityMeasures(implementation.getReceptionSecurityMeasures())
        .customReceptionSecurityTechniques(implementation.getCustomReceptionSecurityTechniques())
        .sourceSystem(implementation.getSourceSystem())
        .preparationTechniques(implementation.getPreparationTechniques())
        .customPreparationTechniques(implementation.getCustomPreparationTechniques())
        .storageTechnique(implementation.getStorageTechnique())
        .storageRetention(implementation.getStorageRetention())
        .analysisTechniques(implementation.getAnalysisTechniques())
        .customAnalysisTechniques(implementation.getCustomAnalysisTechniques())
        .forwardingTechniques(implementation.getForwardingTechniques())
        .forwardingFrequency(implementation.getForwardingFrequency())
        .forwardingSecurityMeasures(implementation.getForwardingSecurityMeasures())
        .customForwardingSecurityTechniques(implementation.getCustomForwardingSecurityTechniques())
        .targetSystem(implementation.getTargetSystem())
        .build();
  }

  private DataTrusteeModelBusiness toBusinessModel(
      DataTrusteeWizardBusiness business) {

    if (business == null) {
      return null;
    }

    final EnumSet<PayMethods> paymentMethods = EnumSet
        .noneOf(
            de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.PayMethods.class);
    if (business.getPaymentMethodDataOwner() != null) {
      paymentMethods.add(business.getPaymentMethodDataOwner());
    }
    if (business.getPaymentMethodDataConsumer() != null) {
      paymentMethods.add(business.getPaymentMethodDataConsumer());
    }

    return DataTrusteeModelBusiness
        .builder()
        .businessDomains(business.getBusinessDomains())
        .businessModel(business.getBusinessModel())
        .fundingSources(business.getFundingSources())
        .paymentMethods(paymentMethods.isEmpty() ? null : paymentMethods)
        .financingTypes(business.getFinancingTypes())
        .paymentMethodDataOwner(business.getPaymentMethodDataOwner())
        .paymentMethodDataConsumer(business.getPaymentMethodDataConsumer())
        .build();
  }

  private DataTrusteeModelObjectives toObjectivesModel(
      DataTrusteeWizardObjectives objectives) {

    if (objectives == null) {
      return null;
    }

    return DataTrusteeModelObjectives
        .builder()
        .dataTrusteeGoals(objectives.getDataTrusteeGoals())
        .motivationRightsHolder(objectives.getMotivationRightsHolder())
        .motivationDataHolder(objectives.getMotivationDataHolder())
        .motivationDataConsumer(objectives.getMotivationDataConsumer())
        .build();
  }

  // -------------------------------------------------
  // Hilfsfunktionen
  // -------------------------------------------------

  private String safe(String value, String fallback) {
    return (value == null || value.isBlank()) ? fallback : value;
  }

  private String yesNoUnknown(String value) {
    if (value == null || value.isBlank()) {
      return "keine Angabe";
    }
    final String v = value.trim().toLowerCase();
    if ("true".equals(v) || "ja".equals(v)) {
      return "ja";
    }
    if ("false".equals(v) || "nein".equals(v)) {
      return "nein";
    }
    return value;
  }

  private String enumLabelDe(Enum<?> e, String fallback) {
    if (e == null) {
      return fallback;
    }
    try {
      final Method m = e.getClass().getMethod("getLabelDe");
      final Object result = m.invoke(e);
      if (result instanceof String && !((String) result).isBlank()) {
        return (String) result;
      }
    } catch (final Exception ignored) {
    }
    return e.name();
  }

  private String enumSetLabelsDe(EnumSet<?> set, String delimiter) {
    if (set == null || set.isEmpty()) {
      return "keine Angabe";
    }
    return set
        .stream()
        .map(e -> this.enumLabelDe(e, ""))
        .filter(s -> s != null && !s.isBlank())
        .collect(Collectors.joining(delimiter));
  }

  private String titleDescriptionListToString(List<TitleDescriptionDTO> list) {
    if (list == null || list.isEmpty()) {
      return "";
    }
    return list.stream().map(td -> {
      final String title = td.getTitle() != null ? td.getTitle() : "";
      final String desc = td.getDescription() != null ? td.getDescription() : "";
      if (!title.isBlank() && !desc.isBlank()) {
        return title + " (" + desc + ")";
      } else if (!title.isBlank()) {
        return title;
      } else {
        return desc;
      }
    }).filter(s -> !s.isBlank()).collect(Collectors.joining("; "));
  }

  private String processingBasisDetailsToString(Map<ProcessingBasis, TitleDescriptionDTO> map) {

    if (map == null || map.isEmpty()) {
      return "";
    }
    return map.entrySet().stream().map(e -> {
      final String basis = this.enumLabelDe(e.getKey(), "");
      final TitleDescriptionDTO td = e.getValue();
      final String detail = (td != null && td.getDescription() != null) ? td.getDescription() : "";
      if (!detail.isBlank()) {
        return basis + ": " + detail;
      } else {
        return basis;
      }
    }).collect(Collectors.joining("; "));
  }
}
