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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper;

public class OntologyCommentMappingSupport {

    private OntologyCommentMappingSupport() {
    }

    public static boolean shouldSkip(Enum<?> value) {
        if (value == null) {
            return true;
        }

        String name = value.name();

        return name.equals("CUSTOM")
                || name.equals("NOT_PROVIDED")
                || name.equals("UNKNOWN")
                || name.equals("AMBIGUOUS")
                || name.equals("NONE");
    }

    public static String extractOntologyKey(String uri) {
        if (uri == null || uri.isBlank()) {
            return null;
        }

        if (!uri.contains("#")) {
            return uri;
        }

        return uri.substring(uri.indexOf('#') + 1);
    }
}
