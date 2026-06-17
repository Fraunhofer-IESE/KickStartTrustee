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

import { Box, Text, TextArea } from "grommet";
import MetadataFieldInput from "../MetadataFieldInput";
import type { MetadataFieldDto } from "../../../types/generated";
import { useCallback, useEffect, useState } from "react";
import type { FieldValue } from "../../../types/fieldValue";
import { isFieldRequired } from "../../modeling/free/visibility";

type Props = {
  field: MetadataFieldDto;
  value?: string;
  allValues?: Record<string, FieldValue | undefined>;
  onChange?: (value: string) => void;
  error?: boolean | string;
};

const MetadataFieldTextarea = ({ field, value, allValues, onChange, error }: Props) => {
  const normalizedValue = value ?? "";
  const required = isFieldRequired(field, allValues ?? {});

  const [currentLength, setCurrentLength] = useState<number>(normalizedValue.length);

  useEffect(() => {
    setCurrentLength(normalizedValue.length);
  }, [normalizedValue]);

  const handleOnChange = useCallback(
    (newValue: string) => {
      setCurrentLength(newValue.length);
      onChange?.(newValue);
    },
    [onChange],
  );

  return (
    <Box
      direction="column"
      gap="small"
    >
      {field.heading && (
        <Box>
          <Text>{field.heading}</Text>
        </Box>
      )}
      <MetadataFieldInput
        field={field}
        label={field.label}
        currentLength={currentLength}
        error={error}
      >
        <TextArea
          name={field.name}
          plain
          focusIndicator={false}
          placeholder={field.placeholder}
          maxLength={field.maxLength}
          value={normalizedValue}
          onChange={(event) => handleOnChange(event.target.value)}
          resize={false}
          rows={1}
          required={required}
          // fill
        />
      </MetadataFieldInput>
    </Box>
  );
};

export default MetadataFieldTextarea;
