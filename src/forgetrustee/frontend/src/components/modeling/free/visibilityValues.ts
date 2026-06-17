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

import type { DataTrusteeModelDraft } from "../../../types/dataTrusteeModelDraft";
import type { FieldValue, FieldValueMap } from "../../../types/fieldValue";

const isFieldValueObject = (value: unknown): value is FieldValueMap => {
  if (!value || typeof value !== "object" || Array.isArray(value)) {
    return false;
  }

  return Object.values(value).every((entry) => isFieldValue(entry));
};

const isFieldValueObjectArray = (value: unknown): value is FieldValueMap[] => {
  return (
    Array.isArray(value) && value.every((entry) => isFieldValueObject(entry))
  );
};

const isFieldValue = (value: unknown): value is FieldValue => {
  if (value === undefined) return true;
  if (typeof value === "string") return true;
  if (Array.isArray(value)) {
    return (
      value.every((entry) => typeof entry === "string") ||
      isFieldValueObjectArray(value)
    );
  }
  return isFieldValueObject(value);
};

const flattenToVisibilityValues = (
  value: unknown,
  target: Record<string, FieldValue | undefined>,
) => {
  if (!value || typeof value !== "object" || Array.isArray(value)) {
    return;
  }

  Object.entries(value).forEach(([key, entry]) => {
    if (isFieldValue(entry)) {
      target[key] = entry;
    }

    if (entry && typeof entry === "object" && !Array.isArray(entry)) {
      flattenToVisibilityValues(entry, target);
    }
  });
};

export const buildVisibilityValues = (formValues: DataTrusteeModelDraft) => {
  const visibilityValues: Record<string, FieldValue | undefined> = {};
  flattenToVisibilityValues(formValues, visibilityValues);
  return visibilityValues;
};
