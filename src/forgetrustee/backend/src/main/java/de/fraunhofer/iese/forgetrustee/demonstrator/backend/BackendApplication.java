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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.sys.JenaSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BackendProperties.class)
@Slf4j
public class BackendApplication {
  public static void main(String[] args) {
    log.info("Starting Forgetrustee Backend Application...");
    log.info("Initializing Apache Jena System...");
    JenaSystem.init();
    log.info("Apache Jena System initialized successfully.");

    log.info("Starting Spring Boot Application...");
    SpringApplication.run(BackendApplication.class, args);
  }
}
