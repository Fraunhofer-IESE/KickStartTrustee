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

//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
//import org.apache.jena.ontology.OntModelSpec;
//import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.ontapi.OntModelFactory;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RiotException;

import java.io.*;

public class OntologyMerger {

    public static void main(String[] args) {

        // --- HARDCODED paths and format (adjust to your project structure) ---
        String format = "TTL";  // or "RDF/XML", "N-TRIPLE", ...
        String corePath = "C:/git/ForgeTrustee/Ontologie/ontology-files/ttl/dtm-core.ttl";

        String[] extensionPaths = {
                "C:/git/ForgeTrustee/Ontologie/ontology-files/ttl/dtm-business.ttl",
                "C:/git/ForgeTrustee/Ontologie/ontology-files/ttl/dtm-data.ttl",
                "C:/git/ForgeTrustee/Ontologie/ontology-files/ttl/dtm-legal.ttl",
                "C:/git/ForgeTrustee/Ontologie/ontology-files/ttl/dtm-sec.ttl",
                "C:/git/ForgeTrustee/Ontologie/ontology-files/ttl/dtm-misc.ttl"
        };

        // Output directory "data"
        String outputDir = "data";
        String outputFile = outputDir + "/currentBase_"+System.currentTimeMillis()+".ttl";

        // ---------------------------------------------------------------------

        try {
            // Ensure the output folder exists
            new File(outputDir).mkdirs();

            OntModel merged = mergeOntologies(format, corePath, extensionPaths);

            try (OutputStream out = new FileOutputStream(outputFile)) {
                merged.write(out, format);
            }

            System.out.println("Merged ontology written to: " + outputFile);

        } catch (RiotException riot){
            System.err.println("Failed to merge ontologies: " + riot.getMessage());
            riot.printStackTrace();
        } catch (Exception e) {
            System.err.println("Failed to merge ontologies: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static OntModel mergeOntologies(String format,
                                           String corePath,
                                           String[] extensionPaths) throws Exception {

        // Ersatz für OWL_MEM_MICRO_RULE_INF
        OntModel model =
                OntModelFactory.createModel(OntSpecification.OWL2_FULL_MEM_RULES_INF);

        Lang lang = RDFLanguages.nameToLang(format);
        if (lang == null) {
            throw new IllegalArgumentException("Unbekanntes RDF Format: " + format);
        }

        // Core Ontologie laden
        try (InputStream in = new FileInputStream(corePath)) {
            RDFDataMgr.read(model, in, lang);
        }

        // Extensions laden
        for (String ext : extensionPaths) {
            try (InputStream in = new FileInputStream(ext)) {
                RDFDataMgr.read(model, in, lang);
            }
        }

        return model;
    }
}
