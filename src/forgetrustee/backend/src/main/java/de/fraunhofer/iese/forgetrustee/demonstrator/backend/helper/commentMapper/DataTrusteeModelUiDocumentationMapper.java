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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseDto;

public class DataTrusteeModelUiDocumentationMapper {

    private DataTrusteeModelUiDocumentationMapper() {
    }

    public static void enrich(
            DataTrusteeModelResponseDto dto,
            OntologyDocumentationDto docs,
            String language
    ) {
        if (dto == null || docs == null) {
            return;
        }

        OntologyTextResolver resolver =
                new OntologyTextResolver(docs, language);

        CoreDocumentationMapper.enrich(
                dto.getCore(),
                resolver
        );

        DataDocumentationMapper.enrich(
                dto.getData(),
                resolver
        );

        LegalDocumentationMapper.enrich(
                dto.getLegal(),
                resolver
        );

        ObjectivesDocumentationMapper.enrich(
                dto.getObjectives(),
                resolver
        );

        BusinessDocumentationMapper.enrich(
                dto.getBusiness(),
                resolver
        );
    }
}
