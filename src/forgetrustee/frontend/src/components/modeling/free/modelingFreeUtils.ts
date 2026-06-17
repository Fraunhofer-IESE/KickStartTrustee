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

import type {
  DataTrusteeWizardRequestDto,
  MetadataModuleIndexSummaryDto,
} from "../../../types/generated";
import type { DataTrusteeModelDraft } from "../../../types/dataTrusteeModelDraft";
import type { FieldValue } from "../../../types/fieldValue";
import { toProcessingBasesMap } from "./legal/legalProcessingBases";

export const createEmptyDataTrusteeModelDraft = (): DataTrusteeModelDraft => ({
  core: {},
  data: {},
  implementation: {},
  business: {},
  objectives: {},
});

export const MODEL_SUBMISSION_ERROR_FALLBACK =
  "Die Modellierung konnte nicht gestartet werden. Bitte versuchen Sie es erneut.";

export const MODULE_FORM_CONFIG = {
  core: { validityKey: "actorsAndData", formKey: "core" },
  data: { validityKey: "data", formKey: "data" },
  implementation: { validityKey: "implementation", formKey: "implementation" },
  business: { validityKey: "finance", formKey: "business" },
  objectives: { validityKey: "objectives", formKey: "objectives" },
} as const;

export const toDataTrusteeModelPayload = (
  draft: DataTrusteeModelDraft,
): DataTrusteeWizardRequestDto => {
  const core = draft.core;
  const dataDraft = draft.data as Record<string, unknown>;
  const businessDraft = draft.business as Record<string, unknown>;
  const dataPayload: DataTrusteeWizardRequestDto["data"] = {
    ...(draft.data as DataTrusteeWizardRequestDto["data"]),
    processingBases: toProcessingBasesMap(dataDraft?.processingBases),
  };
  const businessPayload: DataTrusteeWizardRequestDto["business"] = {
    ...(draft.business as DataTrusteeWizardRequestDto["business"]),
    financingTypes: toProcessingBasesMap(businessDraft?.financingTypes),
  };

  return {
    core: core as DataTrusteeWizardRequestDto["core"],
    data: dataPayload,
    implementation: draft.implementation as DataTrusteeWizardRequestDto["implementation"],
    business: businessPayload,
    objectives: draft.objectives as DataTrusteeWizardRequestDto["objectives"],
  };
};

export const toFieldValueMap = (
  sectionValues: DataTrusteeModelDraft[keyof DataTrusteeModelDraft],
): Record<string, FieldValue | undefined> =>
  sectionValues as Record<string, FieldValue | undefined>;

const hasAnyFilledFieldValue = (value: FieldValue | undefined): boolean => {
  if (value === undefined || value === null) {
    return false;
  }

  if (typeof value === "string") {
    return value.trim().length > 0;
  }

  if (Array.isArray(value)) {
    return value.some((entry) => hasAnyFilledFieldValue(entry));
  }

  if (typeof value === "object") {
    return Object.values(value).some((entry) =>
      hasAnyFilledFieldValue(entry as FieldValue | undefined),
    );
  }

  return false;
};

export const hasAnyStepInput = (
  sectionValues: DataTrusteeModelDraft[keyof DataTrusteeModelDraft],
): boolean =>
  sectionValues != null &&
  Object.values(toFieldValueMap(sectionValues)).some((value) => hasAnyFilledFieldValue(value));

export const getStepFormConfig = (
  modulesSummaryData: MetadataModuleIndexSummaryDto | undefined,
  stepNumber: number,
) => {
  const moduleId = modulesSummaryData?.modules[stepNumber - 1]?.moduleId;
  if (!moduleId) {
    return undefined;
  }

  return MODULE_FORM_CONFIG[moduleId as keyof typeof MODULE_FORM_CONFIG];
};

export const extractModelSubmissionErrorMessage = (error: unknown): string => {
  if (typeof error !== "object" || error === null) {
    return MODEL_SUBMISSION_ERROR_FALLBACK;
  }

  const maybeAxiosError = error as {
    message?: unknown;
    response?: {
      data?:
        | {
            message?: unknown;
            error?: unknown;
          }
        | string;
    };
  };

  const responseData = maybeAxiosError.response?.data;
  if (typeof responseData === "string" && responseData.trim().length > 0) {
    return responseData;
  }

  const apiMessage =
    typeof responseData === "object" && responseData !== null ? responseData.message : undefined;
  if (typeof apiMessage === "string" && apiMessage.trim().length > 0) {
    return apiMessage;
  }

  const apiError =
    typeof responseData === "object" && responseData !== null ? responseData.error : undefined;
  if (typeof apiError === "string" && apiError.trim().length > 0) {
    return apiError;
  }

  if (typeof maybeAxiosError.message === "string" && maybeAxiosError.message.trim().length > 0) {
    return maybeAxiosError.message;
  }

  return MODEL_SUBMISSION_ERROR_FALLBACK;
};
