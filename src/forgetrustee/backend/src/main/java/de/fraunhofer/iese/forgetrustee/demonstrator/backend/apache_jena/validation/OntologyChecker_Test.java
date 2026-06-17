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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.validation;
/* Created by chwalek on 06.08.2025 */

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.*;
import org.apache.jena.shacl.validation.ReportEntry;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OntologyChecker_Test {


    /**
     * Validiert das gegebene Jena-Model gegen die SHACL-Shapes im Ressourcen-Pfad.
     *
     * @param checkerFilePath Pfad zur Shapes-Datei unter src/main/resources (z.B. "dtm-shapes.ttl")
     * @param model           das zu validierende Jena-Model
     * @return true, wenn conform, false sonst
     */
    public static boolean performChecker(String checkerFilePath, Model model) {
        // 1. SHACL-Shapes laden
        Model shapesM = ModelFactory.createDefaultModel();
        try (InputStream shapeIn = OntologyChecker_Test.class
                .getClassLoader()
                .getResourceAsStream(checkerFilePath)) {

            if (shapeIn == null) {
                throw new IllegalArgumentException(
                        "Shapes-Datei nicht gefunden im Ressourcen-Pfad: " + checkerFilePath);
            }
            shapesM.read(shapeIn, null, "TTL");
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Lesen der Shapes-Datei", e);
        }

        // 2. Shapes in SHACL-Objekt umwandeln
        Shapes shapes = Shapes.parse(shapesM);

        // 3. Validierung aufsetzen und ausführen (Model → Graph)
        ValidationReport report = ShaclValidator.get()
                .validate(shapes, model.getGraph());

        // 4. Ergebnis auswerten
        if (report.conforms()) {
            System.out.println("✓ Daten sind SHACL-konform.");
            return true;
        } else {
            System.out.println("✗ SHACL-Verletzungen gefunden:");
            for (ReportEntry e : report.getEntries()) {
                // 1) Fokus-Node (betroffene Instanz)
                System.out.println(" - Fokus-Node: " + e.focusNode());

                // 2) ResultPath (Property/Pfad, an dem es gehakt hat)
                System.out.println("   Property/Path: " +
                        (e.resultPath() != null ? e.resultPath() : "(none)"));

                // 4) ConstraintComponent (MinCount, Class, usw.)
                System.out.println("   Constraint:   " +
                        (e.constraint() != null ? e.constraint().getComponent().getURI() : "(unknown)"));

                // 5) Severity (Violation, Warning, Info)
                System.out.println("   Severity:     " + e.severity().level().toString());

                // 6) Message (evtl. custom sh:message oder Default)
                System.out.println("   Message:      " +
                        String.join("; ", e.message()));

                System.out.println();
            }
            return false;
        }
    }

    /**
     * Validiert das angegebene Model (TTL) gegen die SHACL‐Shapes.
     *
     * @param checkerFilePath Pfad zur SHACL‐Shapes‐Datei im Ressourcen‐Ordner (z.B. "dtm-shapes.ttl").
     * @param pathToModel     Pfad zur Daten‐TTL im Ressourcen‐Ordner ODER auf dem Dateisystem.
     * @return true, wenn das Model alle SHACL‐Constraints erfüllt, sonst false.
     */
    public static boolean performChecker(String checkerFilePath, String pathToModel) {
        // 1. SHACL‐Shapes laden
        Model shapesModel = ModelFactory.createDefaultModel();
        try (InputStream in = openResourceOrFile(checkerFilePath)) {
            shapesModel.read(in, null, "TTL");
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Lesen der Shapes-Datei: " + checkerFilePath, e);
        }

        // 2. Shapes in SHACL‐Objekt umwandeln
        Shapes shapes = Shapes.parse(shapesModel);

        // 3. RDF‐Daten laden
        Model dataModel = ModelFactory.createDefaultModel();
        try (InputStream in = openResourceOrFile(pathToModel)) {
            dataModel.read(in, null, "TTL");
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Lesen des Daten‐Files: " + pathToModel, e);
        }

        System.out.println("Shapes-Tripel: " + shapesModel.size());
        System.out.println("Shapes-Objekte: " + shapes.getGraph().size());

        // 4. Validierung ausführen (Model → Graph)
        ValidationReport report = ShaclValidator.get()
                .validate(shapes, dataModel.getGraph());

        // 5. Ergebnis auswerten und ausgeben
        if (report.conforms()) {
            System.out.println("✓ Daten sind SHACL-konform.");
            return true;
        } else {
            for (ReportEntry e : report.getEntries()) {
                // 1) Fokus-Node (betroffene Instanz)
                System.out.println(" - Fokus-Node: " + e.focusNode());

                // 2) ResultPath (Property/Pfad, an dem es gehakt hat)
                System.out.println("   Property/Path: " +
                        (e.resultPath() != null ? e.resultPath() : "(none)"));

                // 4) ConstraintComponent (MinCount, Class, usw.)
                System.out.println("   Constraint:   " +
                        (e.constraint() != null ? e.constraint().getComponent().getURI() : "(unknown)"));

                // 5) Severity (Violation, Warning, Info)
                System.out.println("   Severity:     " + e.severity().level().toString());

                // 6) Message (evtl. custom sh:message oder Default)
                System.out.println("   Message:      " +
                        String.join("; ", e.message()));

                System.out.println();
            }
            return false;
        }
    }

    /**
     * Öffnet zuerst eine Resource aus dem Classpath, fällt andernfalls auf den regulären
     * Dateipfad zurück.
     *
     * @param path Resource‐Pfad oder Dateisystem‐Pfad
     * @return InputStream der Datei
     * @throws IOException wenn weder Resource noch Datei gefunden wird
     */
    private static InputStream openResourceOrFile(String path) throws IOException {
        // Versuche als Classpath‐Resource
        InputStream in = OntologyChecker_Test.class
                .getClassLoader()
                .getResourceAsStream(path);
        if (in != null) {
            return in;
        }
        // Fallback: regulärer Dateipfad
        Path fsPath = Path.of(path);
        if (Files.exists(fsPath)) {
            return new FileInputStream(fsPath.toFile());
        }
        throw new IOException("Datei nicht gefunden als Resource oder im Dateisystem: " + path);
    }

    public static void main(String[] args) {
        OntologyChecker_Test.performChecker("dtm-core-shapes.ttl", "invalid-instances.ttl");
    }
}
