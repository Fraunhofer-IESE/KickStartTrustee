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

import { Box, Text, TextInput } from "grommet";
import MetadataFieldInput from "../MetadataFieldInput";
import type { MetadataFieldDto } from "../../../types/generated";
import { useEffect, useState } from "react";
import type { FieldValue } from "../../../types/fieldValue";
import { isFieldRequired } from "../../modeling/free/visibility";

type Props = {
  field: MetadataFieldDto;
  value?: string;
  allValues?: Record<string, FieldValue | undefined>;
  onChange?: (value: string) => void;
  embedded?: boolean;
  error?: boolean | string;
};

const MetadataFieldText = ({
  field,
  value,
  allValues,
  onChange,
  embedded = false,
  error,
}: Props) => {
  const normalizedValue = value ?? "";
  const required = isFieldRequired(field, allValues ?? {});

  const [currentLength, setCurrentLength] = useState<number>(normalizedValue.length);

  useEffect(() => {
    setCurrentLength(normalizedValue.length);
  }, [normalizedValue]);

  const handleOnChange = (newValue: string) => {
    setCurrentLength(newValue.length);
    onChange?.(newValue);
  };

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
        sectionWithInfoProps={{ reserveSpaceWhenNoTooltip: !embedded }}
        error={error}
      >
        <TextInput
          name={field.name}
          plain
          focusIndicator={false}
          placeholder={field.placeholder}
          maxLength={field.maxLength}
          value={normalizedValue}
          onChange={(event) => handleOnChange(event.target.value)}
          required={required}
        />
      </MetadataFieldInput>
    </Box>
  );
};

export default MetadataFieldText;
