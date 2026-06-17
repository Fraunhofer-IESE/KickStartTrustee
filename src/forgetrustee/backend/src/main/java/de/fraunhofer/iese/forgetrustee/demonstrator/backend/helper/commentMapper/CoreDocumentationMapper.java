
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelCoreResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.Affiliation;

public class CoreDocumentationMapper {

  private CoreDocumentationMapper() {
  }

  public static void enrich(DataTrusteeModelCoreResponseDto core, OntologyTextResolver resolver) {
    if (core == null || resolver == null) {
      return;
    }

    setDocumentation(core.getDataTrusteeName(), "DataTrustee", resolver);
    setDocumentation(core.getRightsHolderName(), "AffectedParty", resolver);
    setDocumentation(core.getDataOwnerName(), "DataOwner", resolver);
    setDocumentation(core.getDataConsumerName(), "DataConsumer", resolver);

    setDocumentation(core.getContainPersonalInformation(), "PersonalData", resolver);
    setDocumentation(core.getSpecialPersonalInformation(), "SpecialPersonalData", resolver);
    setDocumentation(core.getContainTradeSecrets(), "TradeSecret", resolver);

    setDocumentation(core.getDataTrusteeOperatorAffiliation(),
        mapAffiliationOntologyKey(core.getDataTrusteeOperatorAffiliation()), resolver);

    setDocumentation(core.getRightsHolderAffiliation(),
        mapAffiliationOntologyKey(core.getRightsHolderAffiliation()), resolver);

    setDocumentation(core.getDataConsumerAffiliation(),
        mapAffiliationOntologyKey(core.getDataConsumerAffiliation()), resolver);

    setDocumentation(core.getRightsHolderIsRepresented(), "hasDataAdministratedBy", resolver);
  }

  private static <T> void setDocumentation(
      de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedValueRestDto<T> field,
      String ontologyKey, OntologyTextResolver resolver) {
    if (field == null || ontologyKey == null || ontologyKey.isBlank()) {
      return;
    }

    field.setLabel(resolver.label(ontologyKey));
    field.setComment(resolver.comment(ontologyKey));
  }

  private static String mapAffiliationOntologyKey(
      de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedValueRestDto<Affiliation> affiliation) {
    if (affiliation == null || OntologyCommentMappingSupport.shouldSkip(affiliation.getValue())) {
      return null;
    }

    return OntologyCommentMappingSupport.extractOntologyKey(affiliation.getValue().getUri());
  }
}
