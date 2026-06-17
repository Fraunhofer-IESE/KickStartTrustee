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
  MetadataModuleDto,
  MetadataFieldDto,
  MetadataSectionDto,
} from "../../../types/generated";
import type { FieldValue } from "../../../types/fieldValue";
import { isFieldRequired, isFieldVisible } from "./visibility";

type FieldValueMap = Record<string, FieldValue | undefined>;

export type MissingRequiredField = {
  name: string;
  label: string;
};

const hasOwnKeys = (value: unknown): value is Record<string, unknown> =>
  typeof value === "object" && value !== null && Object.keys(value).length > 0;

const isFilledValue = (value: FieldValue | undefined): boolean => {
  if (value === undefined || value === null) return false;

  if (typeof value === "string") {
    return value.trim().length > 0;
  }

  if (Array.isArray(value)) {
    if (value.length === 0) return false;
    return value.some((entry) => {
      if (typeof entry === "string") return entry.trim().length > 0;
      return entry !== undefined && entry !== null;
    });
  }

  if (hasOwnKeys(value)) {
    return Object.values(value).some((entry) => {
      if (typeof entry === "string") return entry.trim().length > 0;
      return entry !== undefined && entry !== null;
    });
  }

  return true;
};

const normalizeSections = (
  sections: MetadataModuleDto["sections"] | MetadataSectionDto["subsections"],
): MetadataSectionDto[] => {
  if (!sections) return [];
  if (Array.isArray(sections)) return sections as MetadataSectionDto[];

  return Object.entries(sections as Record<string, MetadataSectionDto>).map(([id, section]) => ({
    ...section,
    id: section.id ?? id,
  }));
};

const normalizeFields = (
  fields: MetadataModuleDto["fields"] | MetadataSectionDto["fields"] | MetadataFieldDto["fields"],
): MetadataFieldDto[] => {
  if (!fields) return [];
  return Object.values(fields as Record<string, MetadataFieldDto>);
};

const collectMissingFromField = (
  field: MetadataFieldDto,
  values: FieldValueMap,
): MissingRequiredField[] => {
  const safeValues = values ?? {};

  if (!isFieldVisible(field, safeValues)) return [];

  const currentValue = safeValues[field.name];
  const fieldLabel = field.label || field.heading || field.name;
  const fieldIsRequired = isFieldRequired(field, safeValues);
  const nestedFields = normalizeFields(field.fields);

  const collectMissingFromNestedValue = (nestedValue: FieldValueMap): MissingRequiredField[] =>
    nestedFields.flatMap((nestedField) => collectMissingFromField(nestedField, nestedValue));

  if (Array.isArray(currentValue)) {
    const rows = currentValue as Array<Record<string, FieldValue | undefined>>;
    if (rows.length === 0) {
      return fieldIsRequired ? [{ name: field.name, label: fieldLabel }] : [];
    }

    const missingNestedFields = rows.flatMap((row) => collectMissingFromNestedValue(row));
    if (missingNestedFields.length > 0) {
      return missingNestedFields;
    }

    return fieldIsRequired && !isFilledValue(currentValue)
      ? [{ name: field.name, label: fieldLabel }]
      : [];
  }

  if (typeof currentValue === "object" && currentValue !== null) {
    const nestedValues = currentValue as Record<string, FieldValue | undefined>;
    const missingNestedFields = collectMissingFromNestedValue(nestedValues);
    if (missingNestedFields.length > 0) {
      return missingNestedFields;
    }
  }

  if (!isFilledValue(currentValue)) {
    if (nestedFields.length > 0) {
      const nestedValues =
        typeof currentValue === "object" && currentValue !== null
          ? (currentValue as Record<string, FieldValue | undefined>)
          : {};
      const nestedMissingFields = collectMissingFromNestedValue(nestedValues);

      if (nestedMissingFields.length > 0) {
        return nestedMissingFields;
      }
    }

    return fieldIsRequired ? [{ name: field.name, label: fieldLabel }] : [];
  }

  if (nestedFields.length === 0) {
    return [];
  }

  return [];
};

const collectMissingFromSection = (
  section: MetadataSectionDto,
  values: FieldValueMap,
): MissingRequiredField[] => {
  const safeValues = values ?? {};
  const fields = normalizeFields(section.fields);
  const sections = normalizeSections(section.subsections);

  const missingInFields = fields.flatMap((field) => collectMissingFromField(field, safeValues));
  const missingInSubsections = sections.flatMap((nestedSection) =>
    collectMissingFromSection(nestedSection, safeValues),
  );

  return [...missingInFields, ...missingInSubsections];
};

export const validateRequiredFields = (
  metadata: MetadataModuleDto | undefined,
  values: FieldValueMap,
): boolean => {
  return collectMissingRequiredFields(metadata, values).length === 0;
};

export const collectMissingRequiredFields = (
  metadata: MetadataModuleDto | undefined,
  values: FieldValueMap,
): MissingRequiredField[] => {
  if (!metadata) {
    return [];
  }

  const safeValues = values ?? {};

  const moduleFields = normalizeFields(metadata.fields);
  const sections = normalizeSections(metadata.sections);
  const missingModuleFields = moduleFields.flatMap((field) =>
    collectMissingFromField(field, safeValues),
  );
  const missingSectionFields = sections.flatMap((section) =>
    collectMissingFromSection(section, safeValues),
  );

  const uniqueMissingByName = new Map<string, MissingRequiredField>();
  [...missingModuleFields, ...missingSectionFields].forEach((missingField) => {
    if (!uniqueMissingByName.has(missingField.name)) {
      uniqueMissingByName.set(missingField.name, missingField);
    }
  });

  return Array.from(uniqueMissingByName.values());
};
