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

import { useCallback, useEffect, useMemo } from "react";
import type { Dispatch, SetStateAction } from "react";
import type { MetadataModuleDto, MetadataModuleIndexSummaryDto } from "../../../../types/generated";
import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import { collectMissingRequiredFields, validateRequiredFields } from "../requiredValidation";
import { getStepFormConfig, hasAnyStepInput, toFieldValueMap } from "../modelingFreeUtils";
import type { StepValidity } from "./useModelingFreeState";

type UseModelingFreeStepValidationParams = {
  activeStep: number;
  activeStepMetadata: MetadataModuleDto | undefined;
  formValues: DataTrusteeModelDraft;
  modulesSummaryData: MetadataModuleIndexSummaryDto | undefined;
  setStepValidity: Dispatch<SetStateAction<StepValidity>>;
  stepValidity: StepValidity;
};

const useModelingFreeStepValidation = ({
  activeStep,
  activeStepMetadata,
  formValues,
  modulesSummaryData,
  setStepValidity,
  stepValidity,
}: UseModelingFreeStepValidationParams) => {
  const activeStepFormConfig = getStepFormConfig(modulesSummaryData, activeStep);

  const activeStepIsValid = useMemo(() => {
    if (!activeStepFormConfig) {
      return false;
    }

    const { formKey } = activeStepFormConfig;
    return validateRequiredFields(activeStepMetadata, toFieldValueMap(formValues[formKey]));
  }, [activeStepFormConfig, activeStepMetadata, formValues]);

  const missingRequiredFields = useMemo(() => {
    if (!activeStepFormConfig) {
      return [];
    }

    const { formKey } = activeStepFormConfig;
    return collectMissingRequiredFields(activeStepMetadata, toFieldValueMap(formValues[formKey]));
  }, [activeStepFormConfig, activeStepMetadata, formValues]);

  const missingRequiredFieldNames = useMemo(
    () => missingRequiredFields.map((field) => field.name),
    [missingRequiredFields],
  );

  const missingRequiredFieldLabels = useMemo(
    () => missingRequiredFields.map((field) => field.label),
    [missingRequiredFields],
  );

  useEffect(() => {
    if (!activeStepFormConfig) {
      return;
    }

    const { validityKey } = activeStepFormConfig;
    setStepValidity((prev) => {
      if (prev[validityKey] === activeStepIsValid) {
        return prev;
      }

      return {
        ...prev,
        [validityKey]: activeStepIsValid,
      };
    });
  }, [activeStepFormConfig, activeStepIsValid, setStepValidity]);

  const isStepValid = useCallback(
    (stepNumber: number): boolean => {
      const stepConfig = getStepFormConfig(modulesSummaryData, stepNumber);
      if (!stepConfig) {
        return false;
      }

      return stepValidity[stepConfig.validityKey];
    },
    [modulesSummaryData, stepValidity],
  );

  const stepHasInput = useMemo(
    () => ({
      actorsAndData: hasAnyStepInput(formValues.core),
      data: hasAnyStepInput(formValues.data),
      implementation: hasAnyStepInput(formValues.implementation),
      finance: hasAnyStepInput(formValues.business),
      objectives: hasAnyStepInput(formValues.objectives),
    }),
    [
      formValues.business,
      formValues.core,
      formValues.data,
      formValues.implementation,
      formValues.objectives,
    ],
  );

  const isStepEdited = useCallback(
    (stepNumber: number): boolean => {
      const stepConfig = getStepFormConfig(modulesSummaryData, stepNumber);
      if (!stepConfig) {
        return false;
      }

      return stepHasInput[stepConfig.validityKey];
    },
    [modulesSummaryData, stepHasInput],
  );

  const isStepMarkedValid = useCallback(
    (stepNumber: number): boolean => {
      const stepConfig = getStepFormConfig(modulesSummaryData, stepNumber);
      if (!stepConfig) {
        return false;
      }

      return stepHasInput[stepConfig.validityKey] && stepValidity[stepConfig.validityKey];
    },
    [modulesSummaryData, stepHasInput, stepValidity],
  );

  return {
    activeStepIsValid,
    isStepEdited,
    isStepMarkedValid,
    isStepValid,
    missingRequiredFieldLabels,
    missingRequiredFieldNames,
  };
};

export default useModelingFreeStepValidation;
