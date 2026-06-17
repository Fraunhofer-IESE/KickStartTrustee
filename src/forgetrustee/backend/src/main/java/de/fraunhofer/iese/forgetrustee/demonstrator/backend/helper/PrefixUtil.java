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
/* Created by chwalek on 10.12.2025 */
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.util.*;

public class PrefixUtil {

    public static void derivePrefixesFromUsedNamespaces(OntModel model) {

        // 1) Namespaces sammeln, die im Modell tatsächlich verwendet werden
        Set<String> usedNS = collectUsedNamespaces(model);

        // 2) alte PrefixMap
        Map<String,String> oldMap = model.getNsPrefixMap();
        Map<String,String> newMap = new LinkedHashMap<>();

        // 3) Default-Prefix ":" (Key "") NICHT überschreiben
        if (oldMap.containsKey("")) {
            newMap.put("", oldMap.get(""));  // PREFIX :
        }

        // 4) Standard-Prefixe immer setzen
        newMap.put("owl",  OWL.NS);
        newMap.put("rdf",  RDF.getURI());
        newMap.put("rdfs", RDFS.getURI());
        newMap.put("xsd",  XSD.NS);

        // 5) Für jeden verwendeten Namespace passende benutzerdefinierte Prefixe ableiten
        for (String ns : usedNS) {

            // Standards haben wir schon gesetzt
            if (isStandardNamespace(ns)) {
                continue;
            }

            // Default-NS nicht nochmal behandeln
            if (isDefaultNamespace(oldMap, ns)) {
                continue;
            }

            // Gibt es im alten Modell bereits ein Prefix für diesen NS?
            String existingPrefix = findPrefixForNamespace(oldMap, ns);

            String prefix;
            if (existingPrefix != null && !existingPrefix.isEmpty()
                    && !isStandardPrefix(existingPrefix)) {
                // vorhandenes benutzerdefiniertes Prefix respektieren
                prefix = existingPrefix;
            } else {
                // neues Prefix aus URL ableiten: https://.../prefix# -> "prefix"
                prefix = derivePrefixFromNamespace(ns);
            }

            // Kollisionen vermeiden (falls Prefix schon verwendet)
            prefix = makeUnique(prefix, newMap.keySet());

            // Standard-Prefixnamen nicht überschreiben
            if (isStandardPrefix(prefix)) {
                prefix = makeUnique(prefix, newMap.keySet());
            }

            newMap.put(prefix, ns);
        }

        // 6) Neue PrefixMap anwenden
        model.setNsPrefixes(newMap);
    }

    // ---- Hilfsfunktionen ---------------------------------------------------

    private static Set<String> collectUsedNamespaces(Model model) {
        Set<String> used = new HashSet<>();
        StmtIterator it = model.listStatements();

        while (it.hasNext()) {
            Statement s = it.next();

            if (s.getSubject().isURIResource()) {
                String ns = s.getSubject().getNameSpace();
                if (ns != null) used.add(ns);
            }
            String pNS = s.getPredicate().getNameSpace();
            if (pNS != null) used.add(pNS);

            if (s.getObject().isURIResource()) {
                String ns = s.getObject().asResource().getNameSpace();
                if (ns != null) used.add(ns);
            }
        }
        it.close();
        return used;
    }

    private static boolean isStandardNamespace(String ns) {
        return ns.equals(OWL.NS)
                || ns.equals(RDF.getURI())
                || ns.equals(RDFS.getURI())
                || ns.equals(XSD.NS);
    }

    private static boolean isStandardPrefix(String p) {
        return "owl".equals(p) || "rdf".equals(p) || "rdfs".equals(p) || "xsd".equals(p);
    }

    private static boolean isDefaultNamespace(Map<String,String> map, String ns) {
        return map.containsKey("") && ns.equals(map.get(""));
    }

    private static String findPrefixForNamespace(Map<String,String> map, String ns) {
        for (Map.Entry<String,String> e : map.entrySet()) {
            if (ns.equals(e.getValue())) {
                return e.getKey();
            }
        }
        return null;
    }

    // Ableitung: https://someurl/prefix#  -> "prefix"
    private static String derivePrefixFromNamespace(String ns) {
        String s = ns;
        // trailing # oder / weg
        while (s.endsWith("#") || s.endsWith("/")) {
            s = s.substring(0, s.length() - 1);
        }
        int idx = s.lastIndexOf('/');
        String candidate = (idx >= 0) ? s.substring(idx + 1) : s;

        // Minimal-Sanitisierung (Prefix darf z.B. keine Leerzeichen haben)
        candidate = candidate.replaceAll("[^A-Za-z0-9_\\-]", "_");

        if (candidate.isEmpty()) {
            candidate = "ns";
        }
        return candidate;
    }

    private static String makeUnique(String base, Set<String> existing) {
        if (!existing.contains(base)) return base;
        int i = 1;
        String cand = base + i;
        while (existing.contains(cand)) {
            i++;
            cand = base + i;
        }
        return cand;
    }
}
