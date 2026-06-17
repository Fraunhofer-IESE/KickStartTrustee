
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.fileLoader.OntologyFileLoader_Impl;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyReadService;

import lombok.RequiredArgsConstructor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shacl.Shapes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OntologyReadService_Impl implements OntologyReadService {

  private final OntologyFileLoader_Impl ontologyLoader;

  @Override
  public Model readModelFromFileAsModel(String format, String path) {
    return this.ontologyLoader.readModelFromFileAsModel(path, format);
  }

  @Override
  public String readModelFromFile(String format, String path) {
    return "";
  }

  @Override
  public Model readAndCombineModelsFromFileAsModel(String format, String corePath,
      String... extensionPaths) {
    return this.ontologyLoader
        .readAndCombineModelsFromFileAsModel(format, corePath, extensionPaths);
  }

  @Override
  public String readAndCombineModelsFromFile(String format, String corePath,
      String... extensionPaths) {
    return this.ontologyLoader.readAndCombineModelsFromFile(format, corePath, extensionPaths);
  }

  @Override
  public Shapes readSHACLFileAsShape(String path) {
    return this.ontologyLoader.readSHACLFileAsShape(path);
  }

  @Override
  public String readSHACLFile(String path) {
    return this.ontologyLoader.readSHACLFile(path);
  }
}
