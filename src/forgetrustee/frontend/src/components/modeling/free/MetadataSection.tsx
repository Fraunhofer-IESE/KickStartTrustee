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

import { Box } from "grommet";
import { useEffect, useMemo } from "react";
import HeadingWithLine from "../../shared/HeadingWithLine";
import { usePinnedTips } from "../../../hooks/usePinnedTips";
import MetadataFieldRenderer from "../../shared/metadata/MetadataFieldRenderer";
import type { MetadataSectionDto } from "../../../types/generated";
import type { FieldValue, FieldChangeCallback } from "../../../types/fieldValue";
import { isFieldVisible } from "./visibility";
import MetadataSubsection from "./MetadataSubsection";
import MetadataHints from "./MetadataHints";

const collectSectionFieldNames = (section: MetadataSectionDto): string[] => {
  const names: string[] = [];

  if (section.fields) {
    Object.entries(section.fields).forEach(([fieldId, field]) => {
      names.push(field.name || fieldId);
    });
  }

  if (section.subsections) {
    Object.entries(section.subsections).forEach(([subsectionId, subsection]) => {
      const resolvedSubsection: MetadataSectionDto = {
        ...subsection,
        id: subsection.id ?? subsectionId,
      };
      names.push(...collectSectionFieldNames(resolvedSubsection));
    });
  }

  return names;
};

interface MetadataSectionProps {
  sectionDto: MetadataSectionDto;
  /**
   * Callback when a field value changes. Returns the field `name` and the new `value`.
   */
  onChange?: FieldChangeCallback;
  /**
   * Optional current values for fields, keyed by field name.
   */
  values?: Record<string, FieldValue | undefined>;
  showValidationErrors?: boolean;
  missingRequiredFieldNames?: string[];
}

const MetadataSection = ({
  sectionDto,
  onChange,
  values,
  showValidationErrors,
  missingRequiredFieldNames,
}: MetadataSectionProps) => {
  const { pinTip: pinTipMetadata } = usePinnedTips();

  const title = sectionDto.heading;
  const fields = sectionDto.fields;
  const subsections = sectionDto.subsections;
  const sectionValues = useMemo(() => values ?? {}, [values]);
  const sectionVisible = isFieldVisible(sectionDto, sectionValues);
  const missingFieldNameSet = useMemo(
    () => new Set(missingRequiredFieldNames ?? []),
    [missingRequiredFieldNames],
  );

  useEffect(() => {
    if (!onChange || sectionVisible === true) {
      return;
    }

    const fieldNames = collectSectionFieldNames(sectionDto);
    fieldNames.forEach((fieldName) => {
      if (sectionValues[fieldName] !== undefined) {
        onChange(fieldName, undefined);
      }
    });
  }, [onChange, sectionDto, sectionVisible, sectionValues]);

  useEffect(() => {
    if (!fields || !onChange || !sectionVisible) {
      return;
    }

    Object.entries(fields).forEach(([fieldId, field]) => {
      const fieldName = field.name || fieldId;
      const visible = isFieldVisible(field, sectionValues);
      if (!visible && sectionValues[fieldName] !== undefined) {
        onChange(fieldName, undefined);
      }
    });
  }, [fields, onChange, sectionValues, sectionVisible]);

  const handleOnHelpClick = () => {
    if (sectionDto.helpText) {
      pinTipMetadata({
        fieldName: title || "",
        text: sectionDto.helpText,
        callToAction: sectionDto.callToAction,
      });
    }
  };

  if (!sectionVisible) return null;

  return (
    <Box
      height={{ min: "auto" }}
      gap="small"
      margin={{ bottom: "medium" }}
    >
      <Box>
        <HeadingWithLine
          id={sectionDto.id}
          level={2}
          onHelpClick={sectionDto.helpText ? handleOnHelpClick : undefined}
        >
          {title}
        </HeadingWithLine>
        <MetadataHints
          hints={sectionDto.hints}
          values={sectionValues}
        />
      </Box>
      <Box
        height={{ min: "auto" }}
        gap="large"
      >
        {fields &&
          Object.entries(fields).map(([fieldId, field]) => {
            const fieldName = field.name || fieldId;
            const visible = isFieldVisible(field, sectionValues);
            if (!visible) return null;
            return (
              <Box
                key={fieldId}
                data-metadata-field-name={fieldName}
                gap="xsmall"
                round={
                  showValidationErrors && missingFieldNameSet.has(fieldName) ? "xsmall" : undefined
                }
                pad={
                  showValidationErrors && missingFieldNameSet.has(fieldName)
                    ? { horizontal: "none", vertical: "xxsmall" }
                    : undefined
                }
              >
                <MetadataFieldRenderer
                  field={field}
                  value={sectionValues[fieldName]}
                  allValues={sectionValues}
                  onChange={(value) => onChange && onChange(fieldName, value)}
                  error={
                    showValidationErrors && missingFieldNameSet.has(fieldName)
                      ? "Pflichtfeld bitte ausfüllen."
                      : undefined
                  }
                  missingRequiredFieldNames={missingRequiredFieldNames}
                />
              </Box>
            );
          })}

        {subsections &&
          Object.entries(subsections).map(([subsectionId, subsection]) => (
            <MetadataSubsection
              key={subsection.id ?? subsectionId}
              subsectionDto={{
                ...subsection,
                id: subsection.id ?? subsectionId,
              }}
              onChange={onChange}
              values={sectionValues}
              showValidationErrors={showValidationErrors}
              missingRequiredFieldNames={missingRequiredFieldNames}
            />
          ))}
      </Box>
    </Box>
  );
};

export default MetadataSection;
