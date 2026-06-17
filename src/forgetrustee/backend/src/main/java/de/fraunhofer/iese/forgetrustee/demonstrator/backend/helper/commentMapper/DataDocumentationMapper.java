
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.CommentedValueRestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelDataResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import java.util.List;

public class DataDocumentationMapper {

  private DataDocumentationMapper() {
  }

  public static void enrich(DataTrusteeModelDataResponseDto data, OntologyTextResolver resolver) {
    if (data == null || resolver == null) {
      return;
    }

    setComment(data.getDataTrusteeCategory(), resolver);

    setComment(data.getArchitectureType(), resolver);

    setComments(data.getReceptionTechnologies(), resolver);

    setComment(data.getReceptionFrequency(), resolver);

    setComments(data.getReceptionSecurityMeasures(), resolver);

    setComments(data.getPreparationTechniques(), resolver);

    setComment(data.getStorageTechnique(), resolver);

    setComment(data.getStorageRetention(), resolver);

    setComments(data.getAnalysisTechniques(), resolver);

    setComments(data.getForwardingTechniques(), resolver);

    setComment(data.getForwardingFrequency(), resolver);

    setComments(data.getForwardingSecurityMeasures(), resolver);

    setDocumentation(data.getTargetSystem(), "TargetSystem", resolver);

    setDocumentation(data.getSourceSystem(), "SourceSystem", resolver);
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

  private static <E extends Enum<E> & LabeledConcept> void setComments(
      List<CommentedValueRestDto<E>> values, OntologyTextResolver resolver) {
    if (values == null) {
      return;
    }

    for (final CommentedValueRestDto<E> value : values) {
      if (value == null || OntologyCommentMappingSupport.shouldSkip(value.getValue())) {
        continue;
      }
      document(value, resolver);
    }
  }

  private static <T extends LabeledConcept> void setComment(CommentedValueRestDto<T> value,
      OntologyTextResolver resolver) {
    if (value == null || value.getValue() == null) {
      return;
    }

    document(value, resolver);
  }

  private static <T extends LabeledConcept> void document(CommentedValueRestDto<T> value,
      OntologyTextResolver resolver) {
    if (value == null || value.getValue() == null) {
      return;
    }

    final String ontologyKey = extractOntologyKey(value.getValue().getUri());

    if (ontologyKey == null || ontologyKey.isBlank()) {
      return;
    }

    value.setLabel(resolver.label(ontologyKey));
    value.setComment(resolver.comment(ontologyKey));
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
