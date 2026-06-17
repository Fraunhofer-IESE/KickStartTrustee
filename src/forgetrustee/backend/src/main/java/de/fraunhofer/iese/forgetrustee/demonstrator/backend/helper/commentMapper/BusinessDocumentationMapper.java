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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelBusinessResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessDomain;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.BusinessModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.FundingSource;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business.PayMethods;

import java.util.List;

public class BusinessDocumentationMapper {

    private BusinessDocumentationMapper() {
    }

    public static void enrich(
            DataTrusteeModelBusinessResponseDto business,
            OntologyTextResolver resolver
    ) {

        if (business == null || resolver == null) {
            return;
        }

        setComments(business.getBusinessDomains(), resolver);

        setComment(business.getBusinessModel(), resolver);

        setComments(business.getFundingSources(), resolver);

        setComment(business.getPaymentMethodDataOwner(), resolver);

        setComment(business.getPaymentMethodDataConsumer(), resolver);
    }

    private static <E extends Enum<E> & LabeledConcept> void setComments(
            List<CommentedValueRestDto<E>> values,
            OntologyTextResolver resolver
    ) {
        if (values == null) {
            return;
        }

        for (CommentedValueRestDto<E> value : values) {
            if (value == null || OntologyCommentMappingSupport.shouldSkip(value.getValue())) {
                continue;
            }

            document(value, resolver);
        }
    }

    private static <T extends Enum<T> & LabeledConcept> void setComment(
            CommentedValueRestDto<T> value,
            OntologyTextResolver resolver
    ) {
        if (value != null) {
            document(value, resolver);
        }
    }

    private static <T extends LabeledConcept> void document(
            CommentedValueRestDto<T> value,
            OntologyTextResolver resolver
    ) {
        if (value == null) {
            return;
        }

        T concept = value.getValue();
        if (concept == null) {
            return;
        }

        String ontologyKey = OntologyCommentMappingSupport.extractOntologyKey(concept.getUri());
        if (ontologyKey == null || ontologyKey.isBlank()) {
            return;
        }

        value.setLabel(resolver.label(ontologyKey));
        value.setComment(resolver.comment(ontologyKey));
    }

}
