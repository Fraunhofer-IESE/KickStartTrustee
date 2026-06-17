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

export type CommentedStringValue = {
  value: string;
  comment?: string;
};

const isCommentedValue = (value: unknown): value is { value: unknown } =>
  typeof value === "object" && value !== null && !Array.isArray(value) && "value" in value;

export const unwrapCommentedValue = <T>(
  value: T | { value: T } | null | undefined,
): T | undefined => {
  if (value === null || value === undefined) {
    return undefined;
  }

  if (isCommentedValue(value)) {
    return value.value as T;
  }

  return value;
};

export const unwrapCommentedString = (value: unknown): string | undefined => {
  const candidate = unwrapCommentedValue(value);

  if (typeof candidate === "string") {
    const normalizedValue = candidate.trim();
    return normalizedValue || undefined;
  }

  if (typeof candidate === "number" || typeof candidate === "boolean") {
    return String(candidate);
  }

  return undefined;
};

export const wrapCommentedString = (value: string, comment?: string): CommentedStringValue =>
  comment ? { value, comment } : { value };

export const toYesNo = (value: unknown): string => {
  if (value === true) {
    return "Ja";
  }

  if (value === false) {
    return "Nein";
  }

  const normalizedValue = unwrapCommentedString(value)?.toLowerCase();

  if (normalizedValue === "true") {
    return "Ja";
  }

  if (normalizedValue === "false") {
    return "Nein";
  }

  return "—";
};
