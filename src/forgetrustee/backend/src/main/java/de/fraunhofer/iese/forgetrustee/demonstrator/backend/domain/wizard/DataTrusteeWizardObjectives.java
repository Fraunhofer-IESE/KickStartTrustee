
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.DataTrusteeGoal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataConsumer;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationDataHolder;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives.MotivationRightsHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

/**
 * Wizard-specific objectives section model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeWizardObjectives {
  /** Ziele des Datentreuhänders */
  private EnumSet<DataTrusteeGoal> dataTrusteeGoals;

  private EnumSet<MotivationRightsHolder> motivationRightsHolder;

  private EnumSet<MotivationDataHolder> motivationDataHolder;

  private EnumSet<MotivationDataConsumer> motivationDataConsumer;
}
