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

import { Box, Button, Text } from "grommet";
import React, { useCallback, useState } from "react";
import CustomTag from "./CustomTag";
import { usePinnedTips } from "../../../hooks/usePinnedTips";
import InfoTooltip from "../../shared/InfoTooltip";

export interface TagOption {
  name: string;
  value: string;
  disabled?: boolean;
  warning?: boolean;
  extraArea?: boolean;
}

interface TagGroupProps {
  label: string;
  options: TagOption[];
  value?: string | string[];
  onChange?: (selected: string | string[] | undefined) => void;
  multiple?: boolean;
  includeUnknownOption?: boolean;
  includeNoAnswerOption?: boolean;
  includeUnknownOptionLabel?: string;
  includeNoAnswerOptionLabel?: string;
  helpText?: string;
  tooltipHelpText?: string;
  tooltipExamples?: string[];
  reserveTooltipSpaceOnTags?: boolean;
  error?: boolean | string;
}

const TagGroup: React.FC<TagGroupProps> = ({
  label,
  options,
  value,
  onChange,
  multiple = true,
  includeUnknownOption = false,
  includeNoAnswerOption = false,
  includeUnknownOptionLabel,
  includeNoAnswerOptionLabel,
  helpText,
  tooltipHelpText,
  tooltipExamples,
  reserveTooltipSpaceOnTags = false,
  error,
}) => {
  const { pinTip: pinTipMetadata } = usePinnedTips();
  const normalizedValue = React.useMemo(
    () => (Array.isArray(value) ? value : value ? [value] : []),
    [value],
  );

  const [selected, setSelected] = useState<string[]>(normalizedValue);

  const [sortedPrimaryOptions, sortedExtraAreaOptions] = React.useMemo(() => {
    const sorted = [...options].sort((a, b) => {
      const leftName =
        typeof a.name === "string" && a.name.trim().length > 0
          ? a.name
          : typeof a.value === "string"
            ? a.value
            : "";
      const rightName =
        typeof b.name === "string" && b.name.trim().length > 0
          ? b.name
          : typeof b.value === "string"
            ? b.value
            : "";

      return leftName.localeCompare(rightName, "de", { sensitivity: "base" });
    });

    return [
      sorted.filter((option) => option.extraArea !== true),
      sorted.filter((option) => option.extraArea === true),
    ];
  }, [options]);

  const handleOnHelpClick = () => {
    if (helpText) {
      pinTipMetadata({
        fieldName: label,
        text: helpText,
      });
    }
  };

  React.useEffect(() => {
    setSelected(normalizedValue);
  }, [normalizedValue]);
  const UNKNOWN_VALUE = "AMBIGUOUS";
  const NO_ANSWER_VALUE = "NONE";

  const stripSpecial = useCallback(
    (values: string[]) => {
      let filtered = values;
      if (includeUnknownOption) filtered = filtered.filter((v) => v !== UNKNOWN_VALUE);
      if (includeNoAnswerOption) filtered = filtered.filter((v) => v !== NO_ANSWER_VALUE);
      return filtered;
    },
    [includeUnknownOption, includeNoAnswerOption],
  );

  const handleClick = useCallback(
    (val: string) => {
      let newSelected: string[];
      const isAlreadySelected = selected.includes(val);
      const isUnknown = includeUnknownOption && val === UNKNOWN_VALUE;
      const isNoAnswer = includeNoAnswerOption && val === NO_ANSWER_VALUE;

      if (isUnknown || isNoAnswer) {
        newSelected = isAlreadySelected ? [] : [val];
      } else if (multiple) {
        const cleanedSelection = stripSpecial(selected);
        if (cleanedSelection.includes(val)) {
          newSelected = cleanedSelection.filter((v) => v !== val);
        } else {
          newSelected = [...cleanedSelection, val];
        }
      } else {
        newSelected = isAlreadySelected ? [] : [val];
      }

      const emittedValue = multiple
        ? newSelected
        : newSelected.length > 0
          ? newSelected[0]
          : undefined;

      setSelected(newSelected);
      onChange?.(emittedValue);
    },
    [selected, multiple, includeUnknownOption, includeNoAnswerOption, onChange, stripSpecial],
  );

  const hasPrimaryRow =
    sortedPrimaryOptions.length > 0 || includeNoAnswerOption || includeUnknownOption;

  const tagsContent = (
    <Box gap="small">
      {hasPrimaryRow ? (
        <Box
          direction="row"
          gap={{ row: "small", column: "small" }}
          cssGap={true}
          wrap
        >
          {sortedPrimaryOptions.map((opt) => (
            <CustomTag
              key={opt.value}
              label={opt.name}
              selected={selected.includes(opt.value)}
              onClick={() => handleClick(opt.value)}
              disabled={opt.disabled}
              warning={opt.warning}
            />
          ))}
          {includeNoAnswerOption && (
            <CustomTag
              key={NO_ANSWER_VALUE}
              label={includeNoAnswerOptionLabel?.trim() || "Keine Angaben"}
              selected={selected.includes(NO_ANSWER_VALUE)}
              onClick={() => handleClick(NO_ANSWER_VALUE)}
            />
          )}
          {includeUnknownOption && (
            <CustomTag
              key={UNKNOWN_VALUE}
              label={includeUnknownOptionLabel?.trim() || "Unklar"}
              selected={selected.includes(UNKNOWN_VALUE)}
              onClick={() => handleClick(UNKNOWN_VALUE)}
            />
          )}
        </Box>
      ) : null}

      {sortedExtraAreaOptions.length > 0 ? (
        <Box
          direction="row"
          gap={{ row: "small", column: "small" }}
          cssGap={true}
          wrap
        >
          {sortedExtraAreaOptions.map((opt) => (
            <CustomTag
              key={opt.value}
              label={opt.name}
              selected={selected.includes(opt.value)}
              onClick={() => handleClick(opt.value)}
              disabled={opt.disabled}
              warning={opt.warning}
            />
          ))}
        </Box>
      ) : null}
    </Box>
  );

  const showTagsTooltipWrapper =
    Boolean(tooltipHelpText?.trim()) ||
    Boolean(tooltipExamples?.length) ||
    reserveTooltipSpaceOnTags;
  const hasError = Boolean(error);
  const errorMessage = typeof error === "string" ? error : undefined;

  return (
    <Box gap="xsmall">
      <Box pad={{ vertical: "xsmall" }}>
        <Box
          direction="row"
          gap="small"
        >
          <Box flex>
            <Text color={hasError ? "status-critical" : undefined}>{label}</Text>
          </Box>
          {helpText && (
            <Box pad={{ horizontal: "xsmall" }}>
              <Button
                plain
                onClick={handleOnHelpClick}
                label={
                  <Text
                    size="xsmall"
                    color="text-xweak"
                    style={{ textDecoration: "underline", cursor: "pointer" }}
                  >
                    Hilfe
                  </Text>
                }
              />
            </Box>
          )}
        </Box>
        {multiple && (
          <Text
            size="small"
            color="#a4a4a4ff"
          >
            Mehrfachauswahl möglich
          </Text>
        )}
      </Box>

      {showTagsTooltipWrapper ? (
        <InfoTooltip
          helpText={tooltipHelpText}
          examples={tooltipExamples}
          reserveSpaceWhenNoTooltip={reserveTooltipSpaceOnTags}
        >
          {tagsContent}
        </InfoTooltip>
      ) : (
        tagsContent
      )}
      {errorMessage ? (
        <Text
          size="xsmall"
          color="status-critical"
        >
          {errorMessage}
        </Text>
      ) : null}
    </Box>
  );
};

export default TagGroup;
