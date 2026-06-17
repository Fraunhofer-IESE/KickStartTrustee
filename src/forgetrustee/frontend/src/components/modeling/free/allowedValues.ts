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
  MetadataFieldDto,
  MetadataFieldOption,
  VisibilityRuleOperator,
} from "../../../types/generated";
import type { FieldValue } from "../../../types/fieldValue";
import { evaluateVisibilityRule } from "./visibility";

type AllowedValuesRuleLike = {
  name?: string;
  operator?: VisibilityRuleOperator;
  value?: string;
};

type AllowedValuesModeLike = "INTERSECTION" | "UNION";

type FieldAllowedValuesLike = MetadataFieldDto & {
  allowedValuesBy?: AllowedValuesRuleLike[];
  allowedValuesMode?: AllowedValuesModeLike;
};

const normalizeToValueSet = (input: FieldValue | undefined): Set<string> => {
  if (input === undefined || input === null) return new Set<string>();

  if (Array.isArray(input)) {
    const values = input
      .filter((item) => item !== undefined && item !== null)
      .flatMap((item) => {
        if (typeof item === "string" || typeof item === "number" || typeof item === "boolean") {
          return [String(item)];
        }
        if (item && typeof item === "object") {
          return Object.values(item)
            .filter((value) => value !== undefined && value !== null)
            .map((value) => String(value));
        }
        return [] as string[];
      });
    return new Set(values);
  }

  if (typeof input === "object") {
    return new Set(
      Object.values(input)
        .filter((value) => value !== undefined && value !== null)
        .map((value) => String(value)),
    );
  }

  return new Set([String(input)]);
};

const intersect = (left: Set<string>, right: Set<string>): Set<string> => {
  const out = new Set<string>();
  left.forEach((value) => {
    if (right.has(value)) out.add(value);
  });
  return out;
};

export const computeAllowedOptionValues = (
  field: MetadataFieldDto,
  values: Record<string, FieldValue | undefined>,
): Set<string> | undefined => {
  const typedField = field as FieldAllowedValuesLike;
  const rules = typedField.allowedValuesBy;

  if (!rules || rules.length === 0) return undefined;

  const validRules = rules.filter((rule) => rule?.name);
  if (validRules.length === 0) return undefined;

  const mode: AllowedValuesModeLike =
    typedField.allowedValuesMode === "UNION" ? "UNION" : "INTERSECTION";

  let allowed: Set<string> | undefined;

  for (const rule of validRules) {
    if (!evaluateVisibilityRule(rule, values)) {
      continue;
    }

    const current = normalizeToValueSet(values[rule.name as string]);
    if (!allowed) {
      allowed = current;
      continue;
    }

    allowed = mode === "UNION" ? new Set([...allowed, ...current]) : intersect(allowed, current);
  }

  return allowed ?? new Set<string>();
};

export const mapOptionsWithAllowedState = (
  field: MetadataFieldDto,
  values: Record<string, FieldValue | undefined>,
): Array<MetadataFieldOption & { disabled?: boolean }> => {
  const options = field.options ?? [];
  const allowed = computeAllowedOptionValues(field, values);

  if (!allowed) {
    return options;
  }

  return options.map((option) => ({
    ...option,
    disabled: !allowed.has(option.value),
  }));
};

export const normalizeSelectionToAllowed = (
  selection: string | string[] | undefined,
  allowedValues: Set<string> | undefined,
): string | string[] | undefined => {
  if (!allowedValues) return selection;

  if (Array.isArray(selection)) {
    return selection.filter((item) => allowedValues.has(item));
  }

  if (typeof selection === "string") {
    return allowedValues.has(selection) ? selection : undefined;
  }

  return selection;
};
