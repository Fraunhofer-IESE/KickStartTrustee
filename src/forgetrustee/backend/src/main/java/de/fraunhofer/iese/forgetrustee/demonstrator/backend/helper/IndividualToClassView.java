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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper;
/* Created by chwalek on 04.03.2026 */
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Liest eine TTL-Datei, erzeugt für jede Instanz (Named Individual)
 * eine Klasse, die rdfs:subClassOf der ursprünglichen Klasse ist,
 * und schreibt das Ergebnis wieder als TTL.

 *
 * Beispiel-Aufruf:
 *   java example.IndividualToClassView input.ttl output.ttl

 */
public class IndividualToClassView {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java IndividualToClassView input.ttl output.ttl");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        Model model = ModelFactory.createDefaultModel();

        // TTL einlesen
        try (FileInputStream in = new FileInputStream(inputFile)) {
            RDFDataMgr.read(model, in, null, Lang.TURTLE);
        }

        // Transformation anwenden
        transformModelForWebVowlPunning(model);

        // Ergebnis als TTL schreiben
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            RDFDataMgr.write(out, model, Lang.TURTLE);
        }

        System.out.println("Transformed TTL written to: " + outputFile);
    }

    private static void transformModelForWebVowlPunning(Model model) {
        classifyIndividualsAsClasses(model);
        addDomainRangeForObjectPropertiesBetweenIndividuals(model);
    }

    /**
     * Individuals zusätzlich als Klassen deklarieren + rdfs:subClassOf ursprünglicher Klassen.

     */
    private static void classifyIndividualsAsClasses(Model model) {
        // Snapshot aller Subjekte mit rdf:type
        List<Resource> individuals = model.listSubjectsWithProperty(RDF.type).toList();

        for (Resource individual : individuals) {
            if (!individual.isURIResource()) {
                continue;
            }

            // Snapshot der Typ-Statements dieses Individuals
            List<Statement> typeStatements = individual.listProperties(RDF.type).toList();
            for (Statement typeStmt : typeStatements) {
                RDFNode typeObj = typeStmt.getObject();

                if (!typeObj.isURIResource()) {
                    continue;
                }

                Resource originalClass = typeObj.asResource();

                // Reine Schema-Klassen überspringen
                if (originalClass.equals(OWL.Class) || originalClass.equals(RDFS.Class)) {
                    continue;
                }

                // Individual wird zusätzlich als Klasse deklariert
                model.add(individual, RDF.type, OWL.Class);
                model.add(individual, RDFS.subClassOf, originalClass);
            }
        }
    }

    /**
     * Aus Objekt-Property-Assertions zwischen Individuals Domain/Range ableiten.

     */
    private static void addDomainRangeForObjectPropertiesBetweenIndividuals(Model model) {
        // Snapshot aller Statements
        List<Statement> allStatements = model.listStatements().toList();

        for (Statement stmt : allStatements) {
            Resource subj = stmt.getSubject();
            Property pred = stmt.getPredicate();
            RDFNode obj = stmt.getObject();

            // rdf:type-Statements ignorieren
            if (pred.equals(RDF.type)) {
                continue;
            }

            // Nur Tripel mit URI-Subjekt und URI-Objekt als Objekt-Properties interpretieren
            if (!subj.isURIResource() || !obj.isURIResource()) {
                continue;
            }

            Resource objRes = obj.asResource();

            // Property als owl:ObjectProperty deklarieren
            model.add(pred, RDF.type, OWL.ObjectProperty);

            // Domain/Range direkt auf die Ressourcen (die auch als Klassen deklariert wurden)
            model.add(pred, RDFS.domain, subj);
            model.add(pred, RDFS.range, objRes);
        }
    }
}
