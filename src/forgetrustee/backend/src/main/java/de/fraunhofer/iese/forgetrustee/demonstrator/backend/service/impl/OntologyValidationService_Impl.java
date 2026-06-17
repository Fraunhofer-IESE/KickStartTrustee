
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
/* Created by chwalek on 07.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.validation.OntologyChecker;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyValidationService;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OntologyValidationService_Impl implements OntologyValidationService {

  private final OntologyChecker ontologyChecker;

  @Override
  public JsonObject validateOntology(String jsonOntology) {
    return null;
  }

  public JsonObject validateOntology(Model model) {
    return null;
  }
}
