
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datenmodell für das Datentreuhandmodell.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeModel {

  /** Akteure und Daten */
  private DataTrusteeModelCore core;

  /** Rechtsgrundlage/Rechtlicher Legitimation der Datenweitergabe */
  private DataTrusteeModelLegal legal;

  /** Technik/Technischer Umsetzung & Lebenszyklus */
  private DataTrusteeModelData data;

  /** Finanzen/Business-Einordnung */
  private DataTrusteeModelBusiness business;

  /** Ziele */
  private DataTrusteeModelObjectives objectives;
}
