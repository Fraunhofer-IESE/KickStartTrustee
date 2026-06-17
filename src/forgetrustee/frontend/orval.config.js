/*
 Copyright 2026 Fraunhofer IESE

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import { defineConfig } from "orval";

export default defineConfig({
  api: {
    input: "./openapi/openapi.json", // Pfad zu deiner OpenAPI-Datei
    output: {
      target: "./src/api/generated", // Zielordner für axios-Clients
      baseUrl: "/api",
      client: "react-query", // nur axios-Client-Funktionen generieren
      reactQuery: {
        version: 5,
      },
      httpClient: "axios", // axios als HTTP-Client verwenden
      schemas: "./src/types/generated", // Typen-Dateien
      mode: "tags-split",
      fileExtension: ".ts",
      override: {
        query: {
          useQuery: true,
        },
        mutator: {
          path: "./src/config/orvalAxios.ts",
          name: "customInstance",
        },
      },
    },
  },
});
