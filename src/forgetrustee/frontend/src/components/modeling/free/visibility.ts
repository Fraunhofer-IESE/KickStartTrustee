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

import type { VisibilityRule } from "../../../types/generated/visibilityRule";
import type { RequiredRule } from "../../../types/generated/requiredRule";
import type { FieldValue } from "../../../types/fieldValue";

type ConditionalRule = {
  name?: string;
  operator?: string;
  value?: string;
};

const valueExists = (v: FieldValue): boolean => {
  return v !== undefined && v !== null;
};

const notEmpty = (v: FieldValue): boolean => {
  if (!valueExists(v)) return false;
  if (Array.isArray(v)) return v.length > 0;
  if (typeof v === "string") return v.trim().length > 0;
  if (typeof v === "object") return Object.keys(v).length > 0;
  return true;
};

const valueIncludes = (actual: FieldValue, expected: string | undefined): boolean => {
  if (expected === undefined) return false;
  if (Array.isArray(actual)) {
    return actual.some((item) => {
      if (item === expected) return true;
      if (typeof item === "string") return item === expected;
      if (typeof item === "number" || typeof item === "boolean") {
        return String(item) === expected;
      }
      if (item && typeof item === "object") {
        return Object.values(item).some((v) => String(v) === expected);
      }
      return false;
    });
  }
  if (typeof actual === "string") return actual === expected;
  if (typeof actual === "object" && actual !== null) {
    return Object.values(actual).some((v) => String(v) === expected);
  }
  return false;
};

const evaluateConditionalRule = (
  rule: ConditionalRule | undefined,
  values: Record<string, FieldValue | undefined>,
): boolean => {
  if (!rule || !rule.name) return true;
  const actual = values[rule.name];
  const op = rule.operator || "EQUALS";
  const val = rule.value;

  switch (op) {
    case "EQUALS":
      return valueIncludes(actual, val);
    case "NOT_EQUALS":
      return !valueIncludes(actual, val);
    case "IS_TRUE":
      return !!actual && actual !== "false" && actual !== "0";
    case "IS_FALSE":
      return !actual || actual === "false" || actual === "0";
    case "NOT_EMPTY":
      return notEmpty(actual);
    case "EXISTS":
      return Object.prototype.hasOwnProperty.call(values, rule.name);
    case "IN": {
      if (!val) return false;

      // allow comma-separated values in rule.value
      console.log(`Checking if actual value`, actual, `is in`, val);
      const expectedValues = val.split(",").map((s) => s.trim());

      return expectedValues.some((expected) => {
        if (Array.isArray(actual)) return valueIncludes(actual, expected);

        if (typeof actual === "string" && actual.includes(",")) {
          return actual
            .split(",")
            .map((s) => s.trim())
            .includes(expected);
        }

        return valueIncludes(actual, expected);
      });
    }
    case "MATCHES":
      if (!val || typeof actual !== "string") return false;
      try {
        const re = new RegExp(val);
        return re.test(actual);
      } catch (e) {
        console.warn(`Invalid regex in visibility rule: ${val}`, e);
        return false;
      }
    default:
      return true;
  }
};

export const evaluateVisibilityRule = (
  rule: VisibilityRule | undefined,
  values: Record<string, FieldValue | undefined>,
): boolean => evaluateConditionalRule(rule, values);

export const evaluateRequiredRule = (
  rule: RequiredRule | undefined,
  values: Record<string, FieldValue | undefined>,
): boolean => evaluateConditionalRule(rule, values);

export const isFieldVisible = (
  field: { visible?: boolean; visibleBy?: VisibilityRule[] },
  values: Record<string, FieldValue | undefined>,
) => {
  console.log("Evaluating visibility for field", field, "with values", values);
  if (field.visible === false) return false;
  if (field.visible === true && !field.visibleBy) return true;
  if (!field.visibleBy || field.visibleBy.length === 0) return true;

  // default: AND semantics for multiple rules
  return field.visibleBy.every((r) => evaluateVisibilityRule(r, values));
};

export const getVisibilityDependencies = (field: { visibleBy?: VisibilityRule[] }) => {
  console.log("Field", field, "has visibility rules", field.visibleBy);
  if (!field || !field.visibleBy) return [] as string[];
  const deps = new Set<string>();
  field.visibleBy.forEach((r) => {
    if (r && r.name) deps.add(r.name);
  });
  return Array.from(deps);
};

export const isFieldRequired = (
  field: { required?: boolean; requiredBy?: RequiredRule[] },
  values: Record<string, FieldValue | undefined>,
) => {
  if (field.required === true) return true;
  if (!field.requiredBy || field.requiredBy.length === 0) return false;

  // default: AND semantics for multiple rules
  return field.requiredBy.every((r) => evaluateRequiredRule(r, values));
};
