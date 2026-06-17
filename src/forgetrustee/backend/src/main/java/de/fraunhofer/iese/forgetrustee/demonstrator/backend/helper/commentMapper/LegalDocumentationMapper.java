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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedProcessingBasisRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedValueRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelLegalResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentEnteredBy;

import java.util.List;

public class LegalDocumentationMapper {

  private LegalDocumentationMapper() {
  }

  public static void enrich(DataTrusteeModelLegalResponseDto legal, OntologyTextResolver resolver) {

    if (legal == null || resolver == null) {
      return;
    }

    setProcessingBasisDocumentation(legal.getProcessingBases(), resolver);

    final CommentedValueRestDto<ConsentEnteredBy> consentEnteredBy = legal.getConsentEnteredBy();
    if (consentEnteredBy != null && !OntologyCommentMappingSupport.shouldSkip(consentEnteredBy.getValue())) {
      setDocumentation(
          consentEnteredBy,
          extractOntologyKey(consentEnteredBy.getValue().getUri()),
          resolver
      );
    }

    legal.setConsentType(mapConsentTypeComment(legal.getConsentType(), resolver));
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

  private static void setProcessingBasisDocumentation(
      List<CommentedProcessingBasisRestDto> processingBases, OntologyTextResolver resolver) {
    if (processingBases == null) {
      return;
    }

    for (final CommentedProcessingBasisRestDto processingBasis : processingBases) {
      if (processingBasis == null
          || OntologyCommentMappingSupport.shouldSkip(processingBasis.getValue())) {
        continue;
      }

      final String ontologyKey = extractOntologyKey(processingBasis.getValue().getUri());
      if (ontologyKey == null || ontologyKey.isBlank()) {
        continue;
      }

      processingBasis.setLabel(resolver.label(ontologyKey));
      processingBasis.setComment(resolver.comment(ontologyKey));
    }
  }

  private static CommentedValueRestDto<de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType> mapConsentTypeComment(
      CommentedValueRestDto<de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType> consentType,
      OntologyTextResolver resolver) {

    if (consentType == null || consentType.getValue() == null) {
      return null;
    }

    if (OntologyCommentMappingSupport.shouldSkip(consentType.getValue())) {
      return null;
    }

    final String ontologyKey = extractOntologyKey(consentType.getValue().getImplementationUri());

    return CommentedValueRestDto.<de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentType> builder()
        .value(consentType.getValue())
        .label(resolver.label(ontologyKey))
        .comment(resolver.comment(ontologyKey))
        .build();
  }

  private static String extractOntologyKey(String uri) {

    if (uri == null || uri.isBlank()) {
      return null;
    }

    if (!uri.contains("#")) {
      return uri;
    }

    return uri.substring(uri.indexOf('#') + 1);
  }
}
