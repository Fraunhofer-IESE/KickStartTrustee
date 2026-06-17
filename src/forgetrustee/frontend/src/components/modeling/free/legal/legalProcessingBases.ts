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

import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import type {
  DataTrusteeWizardDataRequestDtoProcessingBases,
  TitleDescriptionRestDto,
} from "../../../../types/generated";

type ProcessingBaseUiItem = {
  name?: string;
  entries?: TitleDescriptionRestDto[];
  [key: string]: unknown;
};

const toStringOrUndefined = (value: unknown): string | undefined => {
  if (typeof value !== "string") return undefined;
  const trimmed = value.trim();
  return trimmed.length > 0 ? trimmed : undefined;
};

const selectionKeyPattern = /name|bezeichnung|titel|title/i;

const getSelectionFieldKey = (entry: Record<string, unknown>): string | undefined => {
  if ("__selectedTag" in entry) return "__selectedTag";
  if ("name" in entry) return "name";

  const matchingKey = Object.keys(entry).find((key) => selectionKeyPattern.test(key));
  return matchingKey;
};

const getDynamicFieldValues = (entry: Record<string, unknown>): string[] => {
  const selectionFieldKey = getSelectionFieldKey(entry);

  return Object.entries(entry)
    .filter(
      ([key]) =>
        key !== selectionFieldKey &&
        key !== "__selectedTag" &&
        key !== "name" &&
        key !== "title" &&
        key !== "description",
    )
    .map(([, value]) => toStringOrUndefined(value))
    .filter((value): value is string => Boolean(value));
};

const normalizeTitleDescriptionList = (value: unknown): TitleDescriptionRestDto[] => {
  if (!Array.isArray(value)) {
    return [];
  }

  return value
    .filter(
      (entry): entry is Record<string, unknown> =>
        !!entry && typeof entry === "object" && !Array.isArray(entry),
    )
    .map((entry) => ({
      title: toStringOrUndefined(entry.title) ?? "",
      description: toStringOrUndefined(entry.description) ?? "",
    }));
};

const getNestedTitleDescriptionList = (
  entry: Record<string, unknown>,
): TitleDescriptionRestDto[] => {
  const selectionFieldKey = getSelectionFieldKey(entry);
  const nestedArrayEntry = Object.entries(entry).find(
    ([key, value]) => key !== selectionFieldKey && Array.isArray(value),
  );

  return normalizeTitleDescriptionList(nestedArrayEntry?.[1]);
};

const getDetailFromObject = (entry: Record<string, unknown>): string | undefined => {
  const selectionFieldKey = getSelectionFieldKey(entry);

  const explicitDescription =
    selectionFieldKey === "description" ? undefined : toStringOrUndefined(entry.description);
  if (explicitDescription) return explicitDescription;

  const dynamicValues = getDynamicFieldValues(entry);
  return dynamicValues[1];
};

const getTitleFromObject = (entry: Record<string, unknown>): string | undefined => {
  const selectionFieldKey = getSelectionFieldKey(entry);

  const explicitTitle =
    selectionFieldKey === "title" ? undefined : toStringOrUndefined(entry.title);
  if (explicitTitle) return explicitTitle;

  const dynamicValues = getDynamicFieldValues(entry);
  return dynamicValues[0];
};

const getNameFromObject = (entry: Record<string, unknown>): string | undefined => {
  const selectionFieldKey = getSelectionFieldKey(entry);
  if (selectionFieldKey) {
    const selectedValue = toStringOrUndefined(entry[selectionFieldKey]);
    if (selectedValue) return selectedValue;
  }

  const values = Object.values(entry)
    .map((value) => toStringOrUndefined(value))
    .filter((value): value is string => Boolean(value));

  return values[0];
};

export const toProcessingBasesMap = (
  value: unknown,
): DataTrusteeWizardDataRequestDtoProcessingBases => {
  if (!value) return {};

  if (Array.isArray(value)) {
    const out: DataTrusteeWizardDataRequestDtoProcessingBases = {};
    value.forEach((item) => {
      if (!item || typeof item !== "object" || Array.isArray(item)) {
        return;
      }
      const entry = item as Record<string, unknown>;
      const name = getNameFromObject(entry);
      if (!name) {
        return;
      }
      const nestedEntries = getNestedTitleDescriptionList(entry);
      if (nestedEntries.length > 0) {
        out[name] = nestedEntries;
        return;
      }
      const title = getTitleFromObject(entry) ?? "";
      const description = getDetailFromObject(entry) ?? "";
      out[name] = [
        {
          title,
          description,
        },
      ];
    });
    return out;
  }

  if (typeof value === "object") {
    const out: DataTrusteeWizardDataRequestDtoProcessingBases = {};
    Object.entries(value as Record<string, unknown>).forEach(([key, entry]) => {
      if (typeof entry === "string") {
        out[key] = [
          {
            title: key,
            description: entry,
          },
        ];
        return;
      }
      if (Array.isArray(entry)) {
        out[key] = normalizeTitleDescriptionList(entry);
        return;
      }
      if (entry && typeof entry === "object" && !Array.isArray(entry)) {
        const objectEntry = entry as Record<string, unknown>;
        const nestedEntries = getNestedTitleDescriptionList(objectEntry);
        if (nestedEntries.length > 0) {
          out[key] = nestedEntries;
          return;
        }
        const title = toStringOrUndefined(objectEntry.title) ?? key;
        const detail = getDetailFromObject(entry as Record<string, unknown>);
        out[key] = [
          {
            title,
            description: detail ?? "",
          },
        ];
        return;
      }
      out[key] = [
        {
          title: key,
          description: "",
        },
      ];
    });
    return out;
  }

  return {};
};

export const toProcessingBasesUiValue = (value: unknown): ProcessingBaseUiItem[] => {
  if (!value || Array.isArray(value)) {
    return Array.isArray(value) ? (value as ProcessingBaseUiItem[]) : [];
  }

  if (typeof value === "object") {
    return Object.entries(value as Record<string, unknown>).map(([key, entry]) => {
      if (Array.isArray(entry)) {
        return {
          name: key,
          entries: normalizeTitleDescriptionList(entry),
        };
      }

      if (typeof entry === "string") {
        return {
          name: key,
          entries: [
            {
              title: key,
              description: entry,
            },
          ],
        };
      }

      if (entry && typeof entry === "object" && !Array.isArray(entry)) {
        const nestedEntries = getNestedTitleDescriptionList(entry as Record<string, unknown>);
        if (nestedEntries.length > 0) {
          return {
            name: key,
            entries: nestedEntries,
          };
        }

        return {
          name: key,
          entries: [
            {
              title: toStringOrUndefined((entry as Record<string, unknown>).title) ?? key,
              description: getDetailFromObject(entry as Record<string, unknown>) ?? "",
            },
          ],
        };
      }

      return {
        name: key,
        entries: [],
      };
    });
  }

  return [];
};

export const normalizeDataProcessingBasesForUi = (
  draft: DataTrusteeModelDraft,
): DataTrusteeModelDraft => {
  const dataSection = draft.data;
  if (!dataSection || typeof dataSection !== "object") {
    return draft;
  }

  const current = (dataSection as Record<string, unknown>).processingBases;
  if (!current || Array.isArray(current)) {
    return draft;
  }

  const normalizedDataSection = {
    ...(dataSection as Record<string, unknown>),
    processingBases: toProcessingBasesUiValue(current),
  } as unknown as DataTrusteeModelDraft["data"];

  return {
    ...draft,
    data: normalizedDataSection,
  };
};

export const normalizeBusinessFinancingTypesForUi = (
  draft: DataTrusteeModelDraft,
): DataTrusteeModelDraft => {
  const businessSection = draft.business;
  if (!businessSection || typeof businessSection !== "object") {
    return draft;
  }

  const current = (businessSection as Record<string, unknown>).financingTypes;
  if (!current || Array.isArray(current)) {
    return draft;
  }

  const normalizedBusinessSection = {
    ...(businessSection as Record<string, unknown>),
    financingTypes: toProcessingBasesUiValue(current),
  } as unknown as DataTrusteeModelDraft["business"];

  return {
    ...draft,
    business: normalizedBusinessSection,
  };
};
