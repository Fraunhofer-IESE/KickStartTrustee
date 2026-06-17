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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest;
/* Created by chwalek on 10.12.2025 */

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestBody_AddCommentDTO {

    @Schema(description = "URI der instanzierten Ontologie", example = "https://www.example.de/ontology#")
    @NotBlank
    String ontologyURI;

    @Schema(description = "URI der Instanz, die ein Comment erhalten wird", example = "https://www.example.de/ontology#Fahrzeughalter")
    @NotBlank
    String subjectURI;

    @Schema(description = "Einzufügender Comment", example = "TestComment")
    @NotBlank
    String literal;

    @Schema(description = "Sprache des einzufügenden Comments", example = "de")
    String lang;
}
