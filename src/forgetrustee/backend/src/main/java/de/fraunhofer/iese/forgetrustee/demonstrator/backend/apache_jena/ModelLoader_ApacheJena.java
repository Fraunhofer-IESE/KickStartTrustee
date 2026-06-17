
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena;
/* Created by chwalek on 02.07.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.fileLoader.OntologyFileLoader;

//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.OntModelFactory;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.Map;

public class ModelLoader_ApacheJena implements OntologyFileLoader {

  public OntModel readModelFromFileAsModel(String path, String format) {
    OntModel model =
            OntModelFactory.createModel(OntSpecification.OWL2_DL_MEM);
    InputStream in = FileManager.class.getClassLoader().getResourceAsStream(path);
    if (in == null) {
      throw new IllegalArgumentException("File " + path + " not found.");
    }
    model.read(in, null, format);

    return model;
  }

  @Override
  public String readModelFromFile(String format, String path) {
    return "";
  }

  @Override
  public Model readAndCombineModelsFromFileAsModel(String format, String corePath,
      String[] extensionPaths) {
    OntModel mergeModel =
            OntModelFactory.createModel(OntSpecification.OWL2_FULL_MEM_RULES_INF);

    InputStream in = FileManager.class.getClassLoader().getResourceAsStream(corePath);
    if (in == null) {
      throw new IllegalArgumentException("File " + corePath + " not found.");
    }
    //mergeModel.read("file:"+corePath, format);
    mergeModel.read(in, null, format);

    //Load each extension
    for (String extensionPath : extensionPaths) {
      InputStream inExtension = FileManager.class
          .getClassLoader()
          .getResourceAsStream(extensionPath);
      if (inExtension == null) {
        throw new IllegalArgumentException("File " + extensionPath + " not found.");
      }
      //mergeModel.read("file:"+extensionPath, format);
      mergeModel.read(inExtension, null, format);
    }

    return mergeModel;
  }

  @Override
  public Shapes readSHACLFileAsShape(String path) {
    return null;
  }

  @Override
  public void saveModel(String iri, Model model) {

  }

  @Override
  public String readSHACLFile(String path) {
    return "";
  }

  @Override
  public String readAndCombineModelsFromFile(String format, String corePath,
      String[] extensionPaths) {
    return "";
  }

  public Map<String, Model> saveModel(String iri, Model model, Map<String, Model> map) {
    map.put(iri, model);
    return map;
  }
}
