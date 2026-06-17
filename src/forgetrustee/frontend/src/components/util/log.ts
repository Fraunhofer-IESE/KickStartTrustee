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

import { consola, type ConsolaInstance } from "consola";

// Level aus env oder Default
const level = import.meta.env.VITE_LOG_LEVEL
  ? Number(import.meta.env.VITE_LOG_LEVEL)
  : import.meta.env.MODE === "development"
    ? 4
    : 1;

// Globales Level setzen
consola.level = level;

// Optional: Factory für Module/Scope
export const createLogger = (moduleName: string): ConsolaInstance => {
  return consola.withTag(moduleName);
};

// Beispiel Default-Logger
export const logger = createLogger("App");
