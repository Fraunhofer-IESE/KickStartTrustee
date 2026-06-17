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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.shacl.Shapes;

public interface OntologyReadService {

    /**
     * @param path
     * @param format
     * @return
     */
    Model readModelFromFileAsModel(String format, String path);

    String readModelFromFile(String format, String path);

    /**
     * @param corePath
     * @param extensionPaths
     * @param format
     * @return
     */
    Model readAndCombineModelsFromFileAsModel(String format, String corePath, String... extensionPaths);

    String readAndCombineModelsFromFile(String format, String corePath, String... extensionPaths);

    /**
     * @param path
     * @return
     */
    Shapes readSHACLFileAsShape(String path);

    String readSHACLFile(String path);


}
