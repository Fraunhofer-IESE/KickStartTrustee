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

import TagGroup from "../../modeling/common/TagGroup";
import { useEffect, useMemo } from "react";
import type { MetadataFieldDto, MetadataFieldOption } from "../../../types/generated";
import { Box, Text } from "grommet";
import type { FieldValue } from "../../../types/fieldValue";
import {
  computeAllowedOptionValues,
  mapOptionsWithAllowedState,
  normalizeSelectionToAllowed,
} from "../../modeling/free/allowedValues";

const sameSelection = (
  left: string | string[] | undefined,
  right: string | string[] | undefined,
): boolean => {
  if (Array.isArray(left) && Array.isArray(right)) {
    return left.length === right.length && left.every((item, index) => item === right[index]);
  }
  return left === right;
};

type Props = {
  field: MetadataFieldDto;
  value?: FieldValue;
  allValues?: Record<string, FieldValue | undefined>;
  onChange?: (value: FieldValue | undefined) => void;
  embedded?: boolean;
  error?: boolean | string;
};

const MetadataFieldSelect = ({
  field,
  value,
  allValues,
  onChange,
  embedded = false,
  error,
}: Props) => {
  const normalizedValue =
    typeof value === "string"
      ? value
      : Array.isArray(value) && value.every((item) => typeof item === "string")
        ? value
        : undefined;

  const valueContext = useMemo(() => allValues ?? {}, [allValues]);

  const allowedValues = useMemo(
    () => computeAllowedOptionValues(field, valueContext),
    [field, valueContext],
  );

  const options = useMemo(
    () => mapOptionsWithAllowedState(field, valueContext),
    [field, valueContext],
  );

  useEffect(() => {
    const normalized = normalizeSelectionToAllowed(normalizedValue, allowedValues);
    if (!sameSelection(normalizedValue, normalized)) {
      onChange?.(normalized);
    }
  }, [allowedValues, normalizedValue, onChange]);

  const handleOnChange = (newValue: string | string[] | undefined) => {
    onChange?.(newValue);
  };

  return (
    <Box
      direction="column"
      gap="none"
    >
      {field.heading && (
        <Box>
          <Text>{field.heading}</Text>
        </Box>
      )}
      <TagGroup
        label={field.label}
        options={options.map((option: MetadataFieldOption & { disabled?: boolean }) => ({
          name: option.label,
          value: option.value,
          disabled: option.disabled,
          extraArea: option.extraArea,
        }))}
        multiple={field.multiple ?? false}
        includeUnknownOption={field.allowUnknown}
        includeNoAnswerOption={field.allowNoAnswer}
        includeUnknownOptionLabel={field.allowUnknownLabel}
        includeNoAnswerOptionLabel={field.allowNoAnswerLabel}
        onChange={handleOnChange}
        value={normalizedValue}
        helpText={field.helpText}
        tooltipHelpText={field.tooltip}
        tooltipExamples={field.examples}
        reserveTooltipSpaceOnTags={!embedded}
        error={error}
      />
    </Box>
  );
};

export default MetadataFieldSelect;
