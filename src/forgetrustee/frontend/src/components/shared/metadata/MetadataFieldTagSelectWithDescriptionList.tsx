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

import { Box, Text } from "grommet";
import { useEffect, useMemo, useState } from "react";
import type { MetadataFieldDto, MetadataFieldOption } from "../../../types/generated";
import { MetadataFieldDtoType } from "../../../types/generated";
import type { FieldValue } from "../../../types/fieldValue";
import TagGroup from "../../modeling/common/TagGroup";
import InfoTooltip from "../InfoTooltip";
import MetadataFieldRenderer from "./MetadataFieldRenderer";
import {
  computeAllowedOptionValues,
  mapOptionsWithAllowedState,
} from "../../modeling/free/allowedValues";

type Props = {
  field: MetadataFieldDto;
  value?: FieldValue;
  allValues?: Record<string, FieldValue | undefined>;
  onChange?: (value: FieldValue | undefined) => void;
  error?: boolean | string;
};

const arePairListsEqual = (left: Record<string, string>[], right: Record<string, string>[]) => {
  if (left.length !== right.length) return false;

  for (let i = 0; i < left.length; i += 1) {
    const leftItem = left[i];
    const rightItem = right[i];
    const keys = new Set([...Object.keys(leftItem), ...Object.keys(rightItem)]);

    for (const key of keys) {
      if ((leftItem[key] ?? "") !== (rightItem[key] ?? "")) return false;
    }
  }

  return true;
};

const MetadataFieldTagSelectWithDescriptionList = ({
  field,
  value,
  allValues,
  onChange,
  error,
}: Props) => {
  const nestedFields = field.fields
    ? Object.values(field.fields as Record<string, MetadataFieldDto>)
    : [];

  const descriptionField =
    nestedFields.find(
      (subfield) =>
        /description|beschreibung/i.test(subfield.name) ||
        /description|beschreibung/i.test(subfield.label ?? ""),
    ) ?? nestedFields[1];

  const nameField = nestedFields.find((subfield) => subfield !== descriptionField);

  const nameKey = nameField?.name ?? "title";
  const descriptionKey = descriptionField?.name ?? "description";

  const resolvedDescriptionField: MetadataFieldDto = descriptionField ?? {
    name: descriptionKey,
    label: "Beschreibung",
    type: MetadataFieldDtoType.LONGTEXT,
    placeholder: "Beschreibung",
  };

  const normalizedValue = useMemo(() => {
    if (!Array.isArray(value)) return [];
    return value.filter(
      (item): item is Record<string, string> =>
        !!item && typeof item === "object" && !Array.isArray(item),
    );
  }, [value]);

  const [pairs, setPairs] = useState<Record<string, string>[]>(normalizedValue);

  useEffect(() => {
    setPairs((prev) => (arePairListsEqual(prev, normalizedValue) ? prev : normalizedValue));
  }, [normalizedValue]);

  const selectedTags = useMemo(
    () => pairs.map((pair) => pair[nameKey]).filter(Boolean),
    [pairs, nameKey],
  );

  const valueContext = useMemo(() => allValues ?? {}, [allValues]);

  const allowedValues = useMemo(
    () => computeAllowedOptionValues(field, valueContext),
    [field, valueContext],
  );

  const options = useMemo(
    () => mapOptionsWithAllowedState(field, valueContext),
    [field, valueContext],
  );

  const optionLabelByValue = useMemo(() => {
    const map = new Map<string, string>();
    (field.options ?? []).forEach((option: MetadataFieldOption) => {
      map.set(option.value, option.label);
    });
    return map;
  }, [field.options]);

  const notify = (next: Record<string, string>[]) => {
    setPairs(next);
    onChange?.(next);
  };

  useEffect(() => {
    if (!allowedValues) {
      return;
    }
    const filtered = pairs.filter((pair) => allowedValues.has(pair[nameKey] ?? ""));
    if (!arePairListsEqual(filtered, pairs)) {
      setPairs(filtered);
      onChange?.(filtered);
    }
  }, [allowedValues, nameKey, onChange, pairs]);

  const handleTagSelectionChange = (newValue: string | string[] | undefined) => {
    const nextSelected = Array.isArray(newValue) ? newValue : newValue ? [newValue] : [];

    const nextPairs = nextSelected.map((selectedTag) => {
      const existing = pairs.find((pair) => pair[nameKey] === selectedTag);
      if (existing) return existing;

      return {
        [nameKey]: selectedTag,
        [descriptionKey]: "",
      };
    });

    notify(nextPairs);
  };

  const handleDescriptionChange = (tagValue: string, description: string) => {
    const nextPairs = pairs.map((pair) =>
      pair[nameKey] === tagValue ? { ...pair, [descriptionKey]: description } : pair,
    );

    notify(nextPairs);
  };

  return (
    <Box
      direction="column"
      gap="small"
    >
      {field.heading && (
        <Box>
          <Text color={error ? "status-critical" : undefined}>{field.heading}</Text>
        </Box>
      )}

      {typeof error === "string" ? (
        <Text
          size="xsmall"
          color="status-critical"
        >
          {error}
        </Text>
      ) : null}

      <InfoTooltip
        helpText={field.helpText ?? field.tooltip}
        examples={field.examples}
      >
        <Box gap="medium">
          <TagGroup
            label={field.label}
            options={options.map((option: MetadataFieldOption & { disabled?: boolean }) => ({
              name: option.label,
              value: option.value,
              disabled: option.disabled,
              extraArea: option.extraArea,
            }))}
            multiple
            includeUnknownOption={field.allowUnknown}
            includeNoAnswerOption={field.allowNoAnswer}
            includeUnknownOptionLabel={field.allowUnknownLabel}
            includeNoAnswerOptionLabel={field.allowNoAnswerLabel}
            onChange={handleTagSelectionChange}
            value={selectedTags}
            error={error}
          />

          {pairs.length > 0 && (
            <Box gap="small">
              {pairs.map((pair, idx) => {
                const pairTagValue =
                  typeof pair[nameKey] === "string" ? pair[nameKey] : selectedTags[idx];
                const tagValue = typeof pairTagValue === "string" ? pairTagValue : "";

                const tagLabel = tagValue.trim() || "Eintrag";
                const tagText = optionLabelByValue.get(tagValue) ?? tagLabel;
                const descriptionFieldForTag: MetadataFieldDto = {
                  ...resolvedDescriptionField,
                  label: `${resolvedDescriptionField.label ?? "Beschreibung"}: ${tagText}`,
                  placeholder: resolvedDescriptionField.placeholder ?? `Beschreibung zu ${tagText}`,
                };

                return (
                  <Box
                    key={`${tagValue || "item"}-${idx}`}
                    gap="small"
                    pad={{ bottom: "small" }}
                  >
                    <MetadataFieldRenderer
                      field={
                        descriptionFieldForTag.type === field.type
                          ? {
                              ...descriptionFieldForTag,
                              type: MetadataFieldDtoType.LONGTEXT,
                            }
                          : descriptionFieldForTag
                      }
                      value={pair[descriptionKey]}
                      allValues={allValues}
                      embedded
                      onChange={(v) =>
                        handleDescriptionChange(
                          tagValue,
                          v === undefined || v === null ? "" : String(v),
                        )
                      }
                    />
                  </Box>
                );
              })}
            </Box>
          )}
        </Box>
      </InfoTooltip>
    </Box>
  );
};

export default MetadataFieldTagSelectWithDescriptionList;
