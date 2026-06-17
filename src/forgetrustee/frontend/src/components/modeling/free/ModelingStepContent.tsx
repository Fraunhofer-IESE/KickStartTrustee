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

import type { FieldValue } from "../../../types/fieldValue";
import type { DataTrusteeModelDraft } from "../../../types/dataTrusteeModelDraft";
import BusinessForm from "./business/BusinessForm";
import CoreForm from "./core/CoreForm";
import ImplementationForm from "./data/DataForm";
import DataForm from "./legal/LegalForm";
import ObjectivesForm from "./objectives/ObjectivesForm";

type ModelingStepContentProps = {
  activeStep: number;
  activeModuleId: string;
  formValues: DataTrusteeModelDraft;
  showValidationErrors?: boolean;
  missingRequiredFieldNames?: string[];
  onFieldChangeFor: (
    path: keyof DataTrusteeModelDraft,
  ) => (name: string, value?: FieldValue) => void;
};

const ModelingStepContent = ({
  activeModuleId,
  formValues,
  showValidationErrors,
  missingRequiredFieldNames,
  onFieldChangeFor,
}: ModelingStepContentProps) => {
  switch (activeModuleId) {
    case "core":
      return (
        <CoreForm
          moduleId={activeModuleId}
          allFormValues={formValues}
          onFieldChange={onFieldChangeFor("core")}
          showValidationErrors={showValidationErrors}
          missingRequiredFieldNames={missingRequiredFieldNames}
        />
      );
    case "data":
      return (
        <DataForm
          moduleId={activeModuleId}
          onFieldChange={onFieldChangeFor("data")}
          allFormValues={formValues}
          showValidationErrors={showValidationErrors}
          missingRequiredFieldNames={missingRequiredFieldNames}
        />
      );
    case "implementation":
      return (
        <ImplementationForm
          moduleId={activeModuleId}
          onFieldChange={onFieldChangeFor("implementation")}
          allFormValues={formValues}
          showValidationErrors={showValidationErrors}
          missingRequiredFieldNames={missingRequiredFieldNames}
        />
      );
    case "business":
      return (
        <BusinessForm
          moduleId={activeModuleId}
          onFieldChange={onFieldChangeFor("business")}
          allFormValues={formValues}
          showValidationErrors={showValidationErrors}
          missingRequiredFieldNames={missingRequiredFieldNames}
        />
      );
    case "objectives":
      return (
        <ObjectivesForm
          moduleId={activeModuleId}
          onFieldChange={onFieldChangeFor("objectives")}
          allFormValues={formValues}
          showValidationErrors={showValidationErrors}
          missingRequiredFieldNames={missingRequiredFieldNames}
        />
      );
    default:
      return (
        <CoreForm
          moduleId={activeModuleId}
          allFormValues={formValues}
          onFieldChange={onFieldChangeFor("core")}
          showValidationErrors={showValidationErrors}
          missingRequiredFieldNames={missingRequiredFieldNames}
        />
      );
  }
};

export default ModelingStepContent;
