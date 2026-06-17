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

import { useEffect } from "react";
import { Box, Form } from "grommet";
import { useGetModuleMetadata } from "../../../../api/generated/metadata-controller/metadata-controller";
import { getMetadataSections } from "../../../shared/metadata/metadataHelpers";
import LoadingSpinner from "../../../shared/LoadingSpinner";
import MetadataSection from "../MetadataSection";
import type { FieldValue } from "../../../../types/fieldValue";
import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import MetadataHints from "../MetadataHints";
import { buildVisibilityValues } from "../visibilityValues";

interface DataFormProps {
  moduleId: string;
  allFormValues: DataTrusteeModelDraft;
  onFieldChange: (name: string, value: FieldValue | undefined) => void;
  onStepValidityChange?: (isValid: boolean) => void;
  showValidationErrors?: boolean;
  missingRequiredFieldNames?: string[];
}

const DataForm = ({
  moduleId,
  allFormValues,
  onFieldChange: onFormValueChange,
  onStepValidityChange,
  showValidationErrors,
  missingRequiredFieldNames,
}: DataFormProps) => {
  const {
    data: metadata,
    isLoading: isLoadingMetadata,
    error: metadataError,
  } = useGetModuleMetadata(moduleId);

  const handleFormValueChange = (name: string, value: FieldValue | undefined) => {
    onFormValueChange(name, value);
  };

  const sectionValues = buildVisibilityValues(allFormValues);

  // Retrieve metadata sections (dynamic)
  const sectionsArray = getMetadataSections(metadata);

  useEffect(() => {
    onStepValidityChange?.(true);
  }, [onStepValidityChange]);

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

export default DataForm;
