
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wizard-specific internal model used during build and translation workflows.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeWizardModel {
  /** Akteure und Daten */
  private DataTrusteeWizardCore core;

  /** Rechtsgrundlage/Rechtlicher Legitimation der Datenweitergabe */
  private DataTrusteeWizardData legal;

  /** Technik/Technischer Umsetzung & Lebenszyklus */
  private DataTrusteeWizardImplementation data;

  /** Finanzen/Business-Einordnung */
  private DataTrusteeWizardBusiness business;

  /** Ziele */
  private DataTrusteeWizardObjectives objectives;
}
