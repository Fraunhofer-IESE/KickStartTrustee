
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.AnalysisTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ArchitectureType;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.DataTrusteeCategory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ForwardingTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.Frequency;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.PreparationTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.ReceptionTechnique;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.SecurityMeasure;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.StorageRetention;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data.StorageTechnique;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.List;

/**
 * Wizard-specific implementation section model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTrusteeWizardImplementation {

  /**
   * Oberklasse Possible Values: Mehrwerttreuhand, Zugangstreuhand, Verwaltungstreuhand
   */
  private DataTrusteeCategory dataTrusteeCategory;

  /** Architekturtyp (URI) */
  private ArchitectureType architectureType;

  private EnumSet<ReceptionTechnique> receptionTechnologies;

  private Frequency receptionFrequency;

  private EnumSet<SecurityMeasure> receptionSecurityMeasures;

  private List<TitleDescriptionDTO> customReceptionSecurityTechniques;

  private String sourceSystem;

  private EnumSet<PreparationTechnique> preparationTechniques;

  private List<TitleDescriptionDTO> customPreparationTechniques;

  private StorageTechnique storageTechnique;

  private StorageRetention storageRetention;

  private EnumSet<AnalysisTechnique> analysisTechniques;

  private List<TitleDescriptionDTO> customAnalysisTechniques;

  private EnumSet<ForwardingTechnique> forwardingTechniques;

  private Frequency forwardingFrequency;

  private EnumSet<SecurityMeasure> forwardingSecurityMeasures;

  private List<TitleDescriptionDTO> customForwardingSecurityTechniques;

  private String targetSystem;
}
