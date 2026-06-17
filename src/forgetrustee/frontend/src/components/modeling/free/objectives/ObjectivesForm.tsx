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

import { useCallback, useEffect } from "react";
import { Box, Form, Text } from "grommet";
import { useGetModuleMetadata } from "../../../../api/generated/metadata-controller/metadata-controller";
import { getMetadataSections } from "../../../shared/metadata/metadataHelpers";
import type { FieldValue } from "../../../../types/fieldValue";
import LoadingSpinner from "../../../shared/LoadingSpinner";
import MetadataSection from "../MetadataSection";
import { isFieldVisible } from "../visibility";
import MetadataFieldRenderer from "../../../shared/metadata/MetadataFieldRenderer";
import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import MetadataHints from "../MetadataHints";
import { buildVisibilityValues } from "../visibilityValues";

interface ObjectivesFormProps {
  moduleId: string;
  allFormValues: DataTrusteeModelDraft;
  onFieldChange: (name: string, value: FieldValue | undefined) => void;
  onStepValidityChange?: (isValid: boolean) => void;
  showValidationErrors?: boolean;
  missingRequiredFieldNames?: string[];
}

const ObjectivesForm = ({
  moduleId,
  allFormValues,
  onFieldChange: onFormValueChange,
  onStepValidityChange,
  showValidationErrors,
  missingRequiredFieldNames,
}: ObjectivesFormProps) => {
  const {
    data: metadata,
    isLoading: isLoadingMetadata,
    error: metadataError,
  } = useGetModuleMetadata(moduleId);

  useEffect(() => {
    onStepValidityChange?.(true);
  }, [onStepValidityChange]);

  // For simplicity pass full formValues to each section so visibility rules
  // can reference any other field across sections.
  const sectionValues = buildVisibilityValues(allFormValues);

  const handleFormValueChange = useCallback(
    (name: string, value: FieldValue | undefined) => {
      onFormValueChange(name, value);
    },
    [onFormValueChange],
  );

  // Retrieve metadata sections (dynamic)
  const sectionsArray = getMetadataSections(metadata);

  const fields = metadata?.fields;

  useEffect(() => {
    if (!fields) {
      return;
    }

    Object.entries(fields).forEach(([fieldId, field]) => {
      const fieldName = field.name || fieldId;
      const visible = isFieldVisible(field, sectionValues ?? {});
      if (!visible && sectionValues?.[fieldName] !== undefined) {
        handleFormValueChange(fieldName, undefined);
      }
    });
  }, [fields, handleFormValueChange, sectionValues]);

  if (metadataError) {
    return <div>Error loading metadata: {metadataError.message}</div>;
  }

  if (isLoadingMetadata || !metadata) {
    return <LoadingSpinner />;
  }

  return (
    <Form style={{ width: "100%" }}>
      <Box
        direction="column"
        gap="medium"
        flex
        pad={{ bottom: "medium" }}
        height={{ min: "auto" }}
        animation="fadeIn"
      >
        <MetadataHints
          hints={metadata.hints}
          values={sectionValues}
        />
        <Box
          direction="column"
          gap="medium"
          flex
        >
          {/* Render fields first */}
          {fields &&
            Object.entries(fields).map(([fieldId, field]) => {
              const fieldName = field.name || fieldId;
              const visible = isFieldVisible(field, sectionValues ?? {});
              if (!visible) return null;
              return (
                <Box
                  key={fieldId}
                  data-metadata-field-name={fieldName}
                  gap="xsmall"
                  background={
                    showValidationErrors && missingRequiredFieldNames?.includes(fieldName)
                      ? { color: "status-critical", opacity: "weak" }
                      : undefined
                  }
                  round={
                    showValidationErrors && missingRequiredFieldNames?.includes(fieldName)
                      ? "xsmall"
                      : undefined
                  }
                  pad={
                    showValidationErrors && missingRequiredFieldNames?.includes(fieldName)
                      ? { horizontal: "xsmall", vertical: "xxsmall" }
                      : undefined
                  }
                >
                  <MetadataFieldRenderer
                    field={field}
                    value={sectionValues ? sectionValues[fieldName] : undefined}
                    onChange={(value) => handleFormValueChange(fieldName, value)}
                  />
                  {showValidationErrors && missingRequiredFieldNames?.includes(fieldName) && (
                    <Text
                      size="small"
                      color="status-critical"
                    >
                      Pflichtfeld bitte ausfüllen.
                    </Text>
                  )}
                </Box>
              );
            })}

          {/* Then render sections */}
          {sectionsArray.map((section, idx) => (
            <MetadataSection
              key={section.id ?? section.label ?? String(idx)}
              sectionDto={section}
              onChange={handleFormValueChange}
              values={sectionValues}
              showValidationErrors={showValidationErrors}
              missingRequiredFieldNames={missingRequiredFieldNames}
            />
          ))}
        </Box>
      </Box>
    </Form>
  );
};

export default ObjectivesForm;
