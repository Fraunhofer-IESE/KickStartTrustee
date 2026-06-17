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
import type { FieldChangeCallback, FieldValue } from "../../../types/fieldValue";
import { isFieldVisible } from "./visibility";

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

interface MetadataSubsectionProps {
  subsectionDto: MetadataSectionDto;
  onChange?: FieldChangeCallback;
  values?: Record<string, FieldValue | undefined>;
  headingLevel?: 3 | 4 | 5 | 6;
  showValidationErrors?: boolean;
  missingRequiredFieldNames?: string[];
}

const MetadataSubsection = ({
  subsectionDto,
  onChange,
  values,
  headingLevel = 3,
  showValidationErrors,
  missingRequiredFieldNames,
}: MetadataSubsectionProps) => {
  const { pinTip: pinTipMetadata } = usePinnedTips();

  const title = subsectionDto.heading;
  const fields = subsectionDto.fields;
  const subsections = subsectionDto.subsections;
  const nextHeadingLevel = headingLevel < 6 ? ((headingLevel + 1) as 4 | 5 | 6) : 6;
  const subsectionVisible = isFieldVisible(subsectionDto, values ?? {});
  const missingFieldNameSet = useMemo(
    () => new Set(missingRequiredFieldNames ?? []),
    [missingRequiredFieldNames],
  );

  useEffect(() => {
    if (!onChange || subsectionVisible === true) {
      return;
    }

    const fieldNames = collectSectionFieldNames(subsectionDto);
    fieldNames.forEach((fieldName) => {
      if (values?.[fieldName] !== undefined) {
        onChange(fieldName, undefined);
      }
    });
  }, [onChange, subsectionDto, subsectionVisible, values]);

  useEffect(() => {
    if (!fields || !onChange || !subsectionVisible) {
      return;
    }

    Object.entries(fields).forEach(([fieldId, field]) => {
      const fieldName = field.name || fieldId;
      const visible = isFieldVisible(field, values ?? {});
      if (!visible && values?.[fieldName] !== undefined) {
        onChange(fieldName, undefined);
      }
    });
  }, [fields, onChange, values, subsectionVisible]);

  const handleOnHelpClick = () => {
    if (subsectionDto.helpText) {
      pinTipMetadata({
        fieldName: title || "",
        text: subsectionDto.helpText,
        callToAction: subsectionDto.callToAction,
      });
    }
  };

  return subsectionVisible ? (
    <Box
      height={{ min: "auto" }}
      gap="small"
    >
      <HeadingWithLine
        id={subsectionDto.id}
        level={headingLevel}
        onHelpClick={subsectionDto.helpText ? handleOnHelpClick : undefined}
        showBorder={false}
        color="#48B8EF"
      >
        {title}
      </HeadingWithLine>
      <Box
        height={{ min: "auto" }}
        gap="medium"
      >
        {fields &&
          Object.entries(fields).map(([fieldId, field]) => {
            const fieldName = field.name || fieldId;
            const visible = isFieldVisible(field, values ?? {});
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
                    ? { horizontal: "xsmall", vertical: "xxsmall" }
                    : undefined
                }
              >
                <MetadataFieldRenderer
                  field={field}
                  value={values ? values[fieldName] : undefined}
                  allValues={values}
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
          Object.entries(subsections).map(([subsectionId, nestedSubsection]) => (
            <MetadataSubsection
              key={nestedSubsection.id ?? subsectionId}
              subsectionDto={{
                ...nestedSubsection,
                id: nestedSubsection.id ?? subsectionId,
              }}
              onChange={onChange}
              values={values}
              headingLevel={nextHeadingLevel}
              showValidationErrors={showValidationErrors}
              missingRequiredFieldNames={missingRequiredFieldNames}
            />
          ))}
      </Box>
    </Box>
  ) : null;
};

export default MetadataSubsection;
