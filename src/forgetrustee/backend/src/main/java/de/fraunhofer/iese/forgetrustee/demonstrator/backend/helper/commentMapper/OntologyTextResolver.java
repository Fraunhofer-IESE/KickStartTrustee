
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

public class OntologyTextResolver {

  private final OntologyDocumentationDto docs;

  private final String language;

  public OntologyTextResolver(OntologyDocumentationDto docs, String language) {
    this.docs = docs;
    this.language = language;
  }

  public String comment(String ontologyKey) {
    final OntologyDocumentationEntryDto entry = this.docs.get(ontologyKey);

    if (entry == null || entry.getComments() == null) {
      return "[Missing ontology comment: " + ontologyKey + "]";
    }

    return entry
        .getComments()
        .getOrDefault(this.language,
            "[Missing ontology comment language: " + ontologyKey + " / " + this.language + "]");
  }

  public String label(String ontologyKey) {
    final OntologyDocumentationEntryDto entry = this.docs.get(ontologyKey);

    if (entry == null || entry.getLabels() == null) {
      return null;
    }

    return entry.getLabels().getOrDefault(this.language, null);
  }
}
