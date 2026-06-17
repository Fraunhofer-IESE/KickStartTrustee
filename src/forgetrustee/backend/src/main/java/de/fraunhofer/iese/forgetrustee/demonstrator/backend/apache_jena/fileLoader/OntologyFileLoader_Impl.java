
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.fileLoader;
/* Created by chwalek on 07.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.validation.OntologyChecker_Test;

import lombok.RequiredArgsConstructor;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.OntModelFactory;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.ontapi.model.OntModel;
//import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OntologyFileLoader_Impl implements OntologyFileLoader {

  @Override
  public Model readModelFromFileAsModel(String path, String format) {
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
  public OntModel readAndCombineModelsFromFileAsModel(String format, String corePath,
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
  public String readAndCombineModelsFromFile(String format, String corePath,
      String[] extensionPaths) {
    /**
     * OntModelSpec spec = OntModelSpec.OWL_MEM_MICRO_RULE_INF;
     * spec.getDocumentManager().setProcessImports(false); OntModel mergeModel =
     * ModelFactory.createOntologyModel(spec); // Lade die Kern-RDF-Datei und speichere sie als
     * String String coreRdfContent = loadFileAsString(corePath); // Stelle sicher, dass alle URIs
     * korrekt sind und keine URL-codierten Zeichen enthalten coreRdfContent =
     * coreRdfContent.replaceAll("%40", "@").replaceAll("%20", " "); // Lade das OntModel aus dem
     * String mergeModel.read(coreRdfContent, null, format); // Lade jede Erweiterung for (String
     * extensionPath : extensionPaths) { String extensionRdfContent =
     * loadFileAsString(extensionPath); // Lade das OntModel aus dem String
     * mergeModel.read(extensionRdfContent, null, format); } // Kombiniere das OntModel in einem
     * String im gewünschten Format (z.B. RDF/XML oder Turtle) StringWriter writer = new
     * StringWriter(); mergeModel.write(writer, format); // Schreibe das OntModel in den
     * StringWriter im angegebenen Format return writer.toString(); // Gib den kombinierten
     * RDF-Content als String zurück
     **/
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

    // Kombiniere das OntModel in einem String im gewünschten Format (z.B. RDF/XML oder Turtle)
    StringWriter writer = new StringWriter();
    mergeModel.write(writer, format);  // Schreibe das OntModel in den StringWriter im angegebenen Format
    return writer.toString();  // Gib den kombinierten RDF-Content als String zurück
  }

  @Override
  public Shapes readSHACLFileAsShape(String path) {
    // 1. SHACL-Shapes laden
    Model shapesM = ModelFactory.createDefaultModel();
    try (InputStream shapeIn = OntologyChecker_Test.class
        .getClassLoader()
        .getResourceAsStream(path)) {

      if (shapeIn == null) {
        throw new IllegalArgumentException(
            "Shapes-Datei nicht gefunden im Ressourcen-Pfad: " + path);
      }
      shapesM.read(shapeIn, null, "TTL");
    } catch (IOException e) {
      throw new RuntimeException("Fehler beim Lesen der Shapes-Datei", e);
    }

    // 2. Shapes in SHACL-Objekt umwandeln
    Shapes shapes = Shapes.parse(shapesM);

    return shapes;
  }

  @Override
  public void saveModel(String iri, Model model) {

  }

  @Override
  public String readSHACLFile(String path) {
    try (
        InputStream shaclIn = OntologyChecker_Test.class.getClassLoader().getResourceAsStream(path);
        var reader = new InputStreamReader(shaclIn);
        BufferedReader bufferedReader = new BufferedReader(reader)) {

      if (shaclIn == null) {
        throw new IllegalArgumentException(
            "Shapes-Datei nicht gefunden im Ressourcen-Pfad: " + path);
      }

      // Lese den gesamten Inhalt der Datei und speichere ihn als String
      return bufferedReader.lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw new RuntimeException("Fehler beim Lesen der Shapes-Datei", e);
    }
  }

}
