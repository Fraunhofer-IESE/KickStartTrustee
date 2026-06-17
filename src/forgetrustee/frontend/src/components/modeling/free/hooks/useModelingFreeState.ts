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

import { useCallback, useMemo, useState } from "react";
import type { FieldValue } from "../../../../types/fieldValue";
import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import { createEmptyDataTrusteeModelDraft } from "../modelingFreeUtils";

const DEFAULT_STEP_VALIDITY = {
  actorsAndData: false,
  data: false,
  implementation: false,
  finance: false,
  objectives: false,
};

export type StepValidity = typeof DEFAULT_STEP_VALIDITY;

const TOTAL_STEPS = 5;

const useModelingFreeState = () => {
  const initialFormValues = useMemo<DataTrusteeModelDraft>(
    () => createEmptyDataTrusteeModelDraft(),
    [],
  );

  const [activeStep, setActiveStep] = useState(1);
  const [formValues, setFormValues] = useState<DataTrusteeModelDraft>(initialFormValues);
  const [isDirty, setIsDirty] = useState(false);
  const [stepValidity, setStepValidity] = useState(DEFAULT_STEP_VALIDITY);

  const handleFormValueChange = useCallback(
    (path: keyof DataTrusteeModelDraft, name: string, value: FieldValue | undefined) => {
      setFormValues((prev) => ({
        ...prev,
        [path]: {
          ...prev[path],
          [name]: value,
        },
      }));
      setIsDirty(true);
    },
    [],
  );

  const handleFormValueChangeFor = useCallback(
    (path: keyof DataTrusteeModelDraft) => (name: string, value?: FieldValue) =>
      handleFormValueChange(path, name, value),
    [handleFormValueChange],
  );

  const handleStepClick = useCallback((step: number) => {
    setActiveStep(step);
  }, []);

  const handleNextStep = useCallback(() => {
    setActiveStep((prev) => (prev < TOTAL_STEPS ? prev + 1 : prev));
  }, []);

  const handlePrevStep = useCallback(() => {
    setActiveStep((prev) => (prev > 1 ? prev - 1 : prev));
  }, []);

  const allStepsValid = useMemo(() => Object.values(stepValidity).every(Boolean), [stepValidity]);

  return {
    activeStep,
    allStepsValid,
    formValues,
    handleFormValueChangeFor,
    handleNextStep,
    handlePrevStep,
    handleStepClick,
    initialFormValues,
    isDirty,
    setFormValues,
    setIsDirty,
    setStepValidity,
    stepValidity,
    totalSteps: TOTAL_STEPS,
  };
};

export default useModelingFreeState;
export { DEFAULT_STEP_VALIDITY };
