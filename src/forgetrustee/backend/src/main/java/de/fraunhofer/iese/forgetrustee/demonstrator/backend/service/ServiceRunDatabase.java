
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

/* Created by chwalek on 12.08.2025 */

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class ServiceRunDatabase {
  private String shaclShape_TTL;

  private String uriOfBase;

  private Map<String, String> database = new HashMap<>();

  public ServiceRunDatabase() {
  }

  public String getOntologyFilePath(String uri) {
    return this.database.get(uri);
  }

  public String getBaseUri() {
    return this.uriOfBase;
  }

  public String getBaseOntologyFilePath() {
    return this.database.get(this.uriOfBase);
  }

  public void put(String uri, String path) {
    this.database.put(uri, path);
  }
}
