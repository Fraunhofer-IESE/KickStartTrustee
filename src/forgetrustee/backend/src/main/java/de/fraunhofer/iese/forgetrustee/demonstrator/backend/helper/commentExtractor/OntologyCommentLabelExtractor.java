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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentExtractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OntologyCommentLabelExtractor {

    private static final String MISSING_DOCUMENTATION_DE =
            "Keine deutsche Dokumentation vorhanden.";

    private static final String MISSING_DOCUMENTATION_EN =
            "No English documentation available.";

    private static final String MISSING_LABEL_DE =
            "Kein deutsches Label vorhanden.";

    private static final String MISSING_LABEL_EN =
            "No English label available.";

    private final Model model;

    private final OntologyCommentLabelValidationResult validationResult =
            new OntologyCommentLabelValidationResult();

    public OntologyCommentLabelExtractor(Model model) {
        this.model = model;
    }

    public Map<String, Object> extractClassEntries() {

        Map<String, Object> result = new LinkedHashMap<>();

        Set<Resource> resources = new LinkedHashSet<>();

        model.listResourcesWithProperty(
                RDF.type,
                OWL.Class
        ).forEachRemaining(resources::add);

        model.listResourcesWithProperty(
                RDF.type,
                SKOS.Concept
        ).forEachRemaining(resources::add);

        for (Resource resource : resources) {

            if (resource.isAnon()) {
                continue;
            }

            String localName = resource.getLocalName();

            if (localName == null || localName.isBlank()) {
                continue;
            }

            validateResource(resource, localName);

            result.put(
                    localName,
                    buildEntry(resource)
            );
        }

        return result;
    }

    public Map<String, Object> extractSelectedObjectPropertyEntries(
            Set<String> selectedPropertyNames
    ) {

        Map<String, Object> result = new LinkedHashMap<>();

        ResIterator properties = model.listResourcesWithProperty(
                RDF.type,
                OWL.ObjectProperty
        );

        while (properties.hasNext()) {

            Resource property = properties.nextResource();

            if (property.isAnon()) {
                continue;
            }

            String localName = property.getLocalName();

            if (localName == null || localName.isBlank()) {
                continue;
            }

            if (!selectedPropertyNames.contains(localName)) {
                continue;
            }

            validateResource(property, localName);

            result.put(
                    localName,
                    buildEntry(property)
            );
        }

        return result;
    }

    private Map<String, Object> buildEntry(Resource resource) {

        Map<String, Object> entry =
                new LinkedHashMap<>();

        entry.put(
                "labels",
                extractLabelsByLanguage(resource)
        );

        entry.put(
                "comments",
                extractDocumentationByLanguage(resource)
        );

        return entry;
    }

    public OntologyCommentLabelValidationResult getValidationResult() {
        return validationResult;
    }

    private void validateResource(
            Resource resource,
            String localName
    ) {

        checkDocumentationExists(resource, localName);

        checkLabelExists(resource, localName);

        checkDuplicateLanguageEntries(resource, localName);
    }

    private Map<String, String> extractLabelsByLanguage(
            Resource resource
    ) {

        Map<String, String> values =
                new LinkedHashMap<>();

        values.put(
                "de",
                extractLabel(resource, "de")
        );

        values.put(
                "en",
                extractLabel(resource, "en")
        );

        return values;
    }

    private Map<String, String> extractDocumentationByLanguage(
            Resource resource
    ) {

        Map<String, String> values =
                new LinkedHashMap<>();

        values.put(
                "de",
                extractDocumentation(resource, "de")
        );

        values.put(
                "en",
                extractDocumentation(resource, "en")
        );

        return values;
    }

    private String extractLabel(
            Resource resource,
            String language
    ) {

        Optional<String> label =
                getFirstLiteral(
                        resource,
                        RDFS.label,
                        language
                );

        if (label.isPresent()) {
            return label.get();
        }

        Optional<String> prefLabel =
                getFirstLiteral(
                        resource,
                        SKOS.prefLabel,
                        language
                );

        if (prefLabel.isPresent()) {
            return prefLabel.get();
        }

        return switch (language) {

            case "de" -> MISSING_LABEL_DE;

            case "en" -> MISSING_LABEL_EN;

            default -> "No label available.";
        };
    }

    private String extractDocumentation(
            Resource resource,
            String language
    ) {

        Optional<String> definition =
                getFirstLiteral(
                        resource,
                        SKOS.definition,
                        language
                );

        if (definition.isPresent()) {
            return definition.get();
        }

        Optional<String> comment =
                getFirstLiteral(
                        resource,
                        RDFS.comment,
                        language
                );

        if (comment.isPresent()) {
            return comment.get();
        }

        return switch (language) {

            case "de" ->
                    MISSING_DOCUMENTATION_DE;

            case "en" ->
                    MISSING_DOCUMENTATION_EN;

            default ->
                    "No documentation available.";
        };
    }

    private Optional<String> getFirstLiteral(
            Resource resource,
            Property property,
            String language
    ) {

        StmtIterator iterator =
                resource.listProperties(property);

        while (iterator.hasNext()) {

            Statement statement =
                    iterator.nextStatement();

            if (!statement.getObject().isLiteral()) {
                continue;
            }

            Literal literal =
                    statement.getObject().asLiteral();

            if (language.equalsIgnoreCase(
                    literal.getLanguage()
            )) {

                return Optional.of(
                        literal.getString()
                );
            }
        }

        return Optional.empty();
    }

    private void checkDocumentationExists(
            Resource resource,
            String localName
    ) {

        boolean hasAnyDefinition =
                resource.hasProperty(SKOS.definition);

        boolean hasAnyComment =
                resource.hasProperty(RDFS.comment);

        if (!hasAnyDefinition && !hasAnyComment) {

            validationResult.addMissingDocumentation(
                    localName
            );
        }
    }

    private void checkLabelExists(
            Resource resource,
            String localName
    ) {

        boolean hasAnyLabel =
                resource.hasProperty(RDFS.label)
                        || resource.hasProperty(SKOS.prefLabel);

        if (!hasAnyLabel) {

            validationResult.addMissingLabel(
                    localName
            );
        }
    }

    private void checkDuplicateLanguageEntries(
            Resource resource,
            String localName
    ) {

        checkDuplicateLanguageEntriesForProperty(
                resource,
                localName,
                RDFS.label,
                "rdfs:label"
        );

        checkDuplicateLanguageEntriesForProperty(
                resource,
                localName,
                SKOS.prefLabel,
                "skos:prefLabel"
        );

        checkDuplicateLanguageEntriesForProperty(
                resource,
                localName,
                SKOS.definition,
                "skos:definition"
        );

        checkDuplicateLanguageEntriesForProperty(
                resource,
                localName,
                RDFS.comment,
                "rdfs:comment"
        );
    }

    private void checkDuplicateLanguageEntriesForProperty(
            Resource resource,
            String localName,
            Property property,
            String propertyName
    ) {

        Map<String, Integer> languageCounter =
                new HashMap<>();

        StmtIterator iterator =
                resource.listProperties(property);

        while (iterator.hasNext()) {

            Statement statement =
                    iterator.nextStatement();

            if (!statement.getObject().isLiteral()) {
                continue;
            }

            Literal literal =
                    statement.getObject().asLiteral();

            String language =
                    literal.getLanguage();

            if (language == null
                    || language.isBlank()) {

                language = "no-language";
            }

            languageCounter.merge(
                    language,
                    1,
                    Integer::sum
            );
        }

        for (Map.Entry<String, Integer> entry
                : languageCounter.entrySet()) {

            if (entry.getValue() > 1) {

                validationResult.addDuplicateLanguageEntry(
                        localName
                                + " / "
                                + propertyName
                                + " / @"
                                + entry.getKey()
                );
            }
        }
    }

    public void writeAsJson(
            Map<String, Object> data,
            Path outputFile
    ) throws IOException {

        ObjectMapper mapper =
                new ObjectMapper();

        mapper.enable(
                SerializationFeature.INDENT_OUTPUT
        );

        try (OutputStream outputStream =
                     Files.newOutputStream(outputFile)) {

            mapper.writeValue(
                    outputStream,
                    data
            );
        }
    }

    public static Model loadTurtleModel(
            Path ttlFile
    ) {

        Model model =
                ModelFactory.createDefaultModel();

        RDFDataMgr.read(
                model,
                ttlFile.toUri().toString(),
                Lang.TURTLE
        );

        return model;
    }
}
