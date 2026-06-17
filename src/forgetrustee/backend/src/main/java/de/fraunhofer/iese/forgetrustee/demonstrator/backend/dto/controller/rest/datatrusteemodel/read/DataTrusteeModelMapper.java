
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelLegal;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelObjectives;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardBusiness;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardData;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardImplementation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardObjectives;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class DataTrusteeModelMapper {

  public DataTrusteeModel toModel(DataTrusteeWizardModel wizardModel) {
    if (wizardModel == null) {
      return null;
    }

    DataTrusteeModelCore core = this.toCore(wizardModel.getCore());
    final DataTrusteeWizardData legal = wizardModel.getLegal();
    if (core == null && legal != null) {
      core = new DataTrusteeModelCore();
    }
    if (core != null && legal != null) {
      core.setDataCategoryName(legal.getDataCategory().getTitle());
      core.setDataCategoryDescription(legal.getDataCategory().getDescription());
      core.setContainPersonalInformation(legal.getContainPersonalInformation());
      core.setSpecialPersonalInformation(legal.getSpecialPersonalInformation());
      core.setContainTradeSecrets(legal.getContainTradeSecrets());
    }

    return DataTrusteeModel
        .builder()
        .core(core)
        .legal(this.toLegal(wizardModel.getLegal()))
        .data(this.toData(wizardModel.getData()))
        .business(this.toBusiness(wizardModel.getBusiness()))
        .objectives(this.toObjectives(wizardModel.getObjectives()))
        .build();
  }

  @Mapping(target = "dataCategoryName", ignore = true)
  @Mapping(target = "dataCategoryDescription", ignore = true)
  @Mapping(target = "containPersonalInformation", ignore = true)
  @Mapping(target = "specialPersonalInformation", ignore = true)
  @Mapping(target = "containTradeSecrets", ignore = true)
  protected abstract DataTrusteeModelCore toCore(DataTrusteeWizardCore source);

  protected abstract DataTrusteeModelLegal toLegal(DataTrusteeWizardData source);

  protected abstract DataTrusteeModelData toData(DataTrusteeWizardImplementation source);

  @Mapping(target = "paymentMethods", ignore = true)
  protected abstract DataTrusteeModelBusiness toBusiness(DataTrusteeWizardBusiness source);

  protected abstract DataTrusteeModelObjectives toObjectives(DataTrusteeWizardObjectives source);
}
