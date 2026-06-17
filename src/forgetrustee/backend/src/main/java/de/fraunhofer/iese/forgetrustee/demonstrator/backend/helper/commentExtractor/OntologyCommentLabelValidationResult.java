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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OntologyCommentLabelValidationResult {

    private final List<String> missingDocumentation =
            new ArrayList<>();

    private final List<String> missingLabels =
            new ArrayList<>();

    private final List<String> duplicateLanguageEntries =
            new ArrayList<>();

    public void addMissingDocumentation(String resourceName) {
        missingDocumentation.add(resourceName);
    }

    public void addMissingLabel(String resourceName) {
        missingLabels.add(resourceName);
    }

    public void addDuplicateLanguageEntry(String resourceName) {
        duplicateLanguageEntries.add(resourceName);
    }

    public List<String> getMissingDocumentation() {
        return Collections.unmodifiableList(missingDocumentation);
    }

    public List<String> getMissingLabels() {
        return Collections.unmodifiableList(missingLabels);
    }

    public List<String> getDuplicateLanguageEntries() {
        return Collections.unmodifiableList(duplicateLanguageEntries);
    }

    public boolean hasWarnings() {
        return !missingDocumentation.isEmpty()
                || !missingLabels.isEmpty()
                || !duplicateLanguageEntries.isEmpty();
    }
}
