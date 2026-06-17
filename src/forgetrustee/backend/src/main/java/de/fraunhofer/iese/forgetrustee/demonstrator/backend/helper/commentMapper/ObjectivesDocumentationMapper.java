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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelObjectivesResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.DataTrusteeGoal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataConsumer;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataHolder;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationRightsHolder;

import java.util.List;

public class ObjectivesDocumentationMapper {

    private ObjectivesDocumentationMapper() {
    }

    public static void enrich(
            DataTrusteeModelObjectivesResponseDto objectives,
            OntologyTextResolver resolver
    ) {
        if (objectives == null || resolver == null) {
            return;
        }

        setComments(objectives.getDataTrusteeGoals(), resolver);

        setComments(objectives.getMotivationRightsHolder(), resolver);

        setComments(objectives.getMotivationDataHolder(), resolver);

        setComments(objectives.getMotivationDataConsumer(), resolver);
    }

    private static <E extends Enum<E>> void setComments(
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

            String ontologyKey = toOntologyKey(value.getValue());
            if (ontologyKey == null || ontologyKey.isBlank()) {
                continue;
            }

            value.setLabel(resolver.label(ontologyKey));
            value.setComment(resolver.comment(ontologyKey));
        }
    }

    private static String toOntologyKey(Enum<?> motivation) {
        if (motivation instanceof DataTrusteeGoal goal) {
            return switch (goal) {
                case DATA_SOVEREIGNTY -> "DataSovereignty";
                case DIGITAL_SOVEREIGNTY -> "DigitalSovereignty";
                case COMPLIANCE_REQUIREMENTS -> "Compliance";
                case INNOVATION -> "Innovation";
                case ECONOMIC_UTILIZATION -> "EconomicUtilization";
                case FAIR_MARKET_COMPETITION -> "FairMarketCompetition";
                case SCIENCE_RESEARCH -> "ScienceAndResearch";
                case AMBIGUOUS -> "Ambiguous";
                case NONE -> "None";
            };
        }

        if (motivation instanceof MotivationRightsHolder rightsHolder) {
            return switch (rightsHolder) {
                case QUALITY_IMPROVEMENT -> "QualityImprovement";
                case COST_REDUCTION -> "CostReduction";
                case CONTRACT_FULFILLMENT -> "ContractFulfillment";
                case SOCIAL_VALUE -> "SocietalValue";
                case AMBIGUOUS -> "Ambiguous";
                case NONE -> "None";
            };
        }

        if (motivation instanceof MotivationDataHolder dataHolder) {
            return switch (dataHolder) {
                case TRUST_INTERMEDIARY -> "TrustIntermediary";
                case VALUE_CREATION -> "ValueCreation";
                case INNOVATION -> "Innovation";
                case COMPLIANCE_REQUIREMENTS -> "Compliance";
                case INTEROPERABILITY -> "Interoperability";
                case SCIENCE_AND_RESEARCH -> "ScienceAndResearch";
                case SOCIAL_VALUE -> "SocietalValue";
                case AMBIGUOUS -> "Ambiguous";
                case NONE -> "None";
            };
        }

        if (motivation instanceof MotivationDataConsumer dataConsumer) {
            return switch (dataConsumer) {
                case INNOVATION -> "Innovation";
                case COMPLIANCE_REQUIREMENTS -> "Compliance";
                case INTEROPERABILITY -> "Interoperability";
                case VALUE_CREATION -> "ValueCreation";
                case OPTIMIZATION -> "Optimization";
                case SCIENCE_AND_RESEARCH -> "ScienceAndResearch";
                case SOCIAL_VALUE -> "SocietalValue";
                case AMBIGUOUS -> "Ambiguous";
                case NONE -> "None";
            };
        }

        return null;
    }
}
