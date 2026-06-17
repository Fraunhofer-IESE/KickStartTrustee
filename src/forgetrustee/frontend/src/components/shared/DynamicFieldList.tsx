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

import { Box, Button, TextInput, Text } from "grommet";
import { useState } from "react";
import LabeledInputBox from "./LabeledInputBox";
import { Trash } from "grommet-icons";

export interface DynamicFieldListItem {
  [key: string]: string;
}

export interface DynamicFieldListField {
  label: string;
  maxLength?: number;
}

interface DynamicFieldListProps {
  title: string;
  fieldLabels: [DynamicFieldListField, DynamicFieldListField];
  addLabel?: string;
  initialItems?: DynamicFieldListItem[];
  onChange?: (items: DynamicFieldListItem[]) => void;
}

const DynamicFieldList = ({
  title,
  fieldLabels,
  addLabel = "Hinzufügen",
  initialItems = [],
  onChange,
}: DynamicFieldListProps) => {
  const [items, setItems] = useState<DynamicFieldListItem[]>(initialItems);
  const [input, setInput] = useState<DynamicFieldListItem>({
    [fieldLabels[0].label]: "",
    [fieldLabels[1].label]: "",
  });

  const handleInputChange = (key: string, value: string) => {
    setInput((prev) => ({ ...prev, [key]: value }));
  };

  const handleAdd = () => {
    if (!input[fieldLabels[0].label] && !input[fieldLabels[1].label]) return;
    const newItems = [...items, input];
    setItems(newItems);
    setInput({ [fieldLabels[0].label]: "", [fieldLabels[1].label]: "" });
    onChange?.(newItems);
  };

  const handleDelete = (idx: number) => {
    const newItems = items.filter((_, i) => i !== idx);
    setItems(newItems);
    onChange?.(newItems);
  };

  return (
    <Box gap="medium">
      <Box border={{ side: "bottom", color: "border-light", size: "xsmall" }}>
        <Text weight="bold" size="medium" color="brand">
          {title}
        </Text>
      </Box>
      {/* Gespeicherte Einträge */}
      {items.length > 0 && (
        <Box gap="xsmall">
          <Box
            direction="row"
            gap="small"
            align="center"
            pad={{ vertical: "xsmall" }}
          >
            <Box flex>
              <Text size="xsmall" weight="bold" color="dark-3">
                {fieldLabels[0].label}
              </Text>
            </Box>
            <Box flex>
              <Text size="xsmall" weight="bold" color="dark-3">
                {fieldLabels[1].label}
              </Text>
            </Box>
            <Box width="32px" />
          </Box>
          {items.map((item, idx) => (
            <Box
              key={idx}
              direction="row"
              gap="small"
              align="center"
              pad={{ vertical: "xsmall" }}
              border={{ side: "bottom", color: "border-light", size: "hair" }}
            >
              <Box flex>
                <Text size="small">{item[fieldLabels[0].label]}</Text>
              </Box>
              <Box flex>
                <Text size="small">{item[fieldLabels[1].label]}</Text>
              </Box>
              <Button
                icon={<Trash />}
                onClick={() => handleDelete(idx)}
                plain
              />
            </Box>
          ))}
        </Box>
      )}
      {/* Eingabefelder und Hinzufügen-Button */}
      <Box>
        <Box direction="row" gap="small" align="center">
          <LabeledInputBox
            label={fieldLabels[0].label}
            maxLength={fieldLabels[0].maxLength}
            currentLength={input[fieldLabels[0].label]?.length || 0}
          >
            <TextInput
              placeholder={fieldLabels[0].label}
              value={input[fieldLabels[0].label] || ""}
              onChange={(e) =>
                handleInputChange(fieldLabels[0].label, e.target.value)
              }
              maxLength={fieldLabels[0].maxLength}
              plain
              focusIndicator={false}
            />
          </LabeledInputBox>
          <LabeledInputBox
            label={fieldLabels[1].label}
            maxLength={fieldLabels[1].maxLength}
            currentLength={input[fieldLabels[1].label]?.length || 0}
          >
            <TextInput
              placeholder={fieldLabels[1].label}
              value={input[fieldLabels[1].label] || ""}
              onChange={(e) =>
                handleInputChange(fieldLabels[1].label, e.target.value)
              }
              maxLength={fieldLabels[1].maxLength}
              plain
              focusIndicator={false}
            />
          </LabeledInputBox>
        </Box>
        <Box direction="row" justify="end" margin={{ top: "xsmall" }}>
          <Button label={addLabel} onClick={handleAdd} primary />
        </Box>
      </Box>
    </Box>
  );
};

export default DynamicFieldList;
