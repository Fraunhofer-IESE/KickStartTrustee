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
import type {
  MetadataFieldDto,
  MetadataFieldOption,
  MetadataOptionAdditionalDetailsDto,
} from "../../../types/generated";
import { MetadataFieldDtoType } from "../../../types/generated";
import type { FieldValue, FieldValueMap } from "../../../types/fieldValue";
import TagGroup from "../../modeling/common/TagGroup";
import {
  computeAllowedOptionValues,
  mapOptionsWithAllowedState,
} from "../../modeling/free/allowedValues";
import InfoTooltip from "../InfoTooltip";
import MetadataFieldTextWithDescriptionList from "./MetadataFieldTextWithDescriptionList";

type Props = {
  field: MetadataFieldDto;
  value?: FieldValue;
  allValues?: Record<string, FieldValue | undefined>;
  onChange?: (value: FieldValue | undefined) => void;
  error?: boolean | string;
};

const areEntryListsEqual = (left: FieldValueMap[], right: FieldValueMap[]) =>
  JSON.stringify(left) === JSON.stringify(right);

const SELECTION_KEY = "__selectedTag";
const ENTRIES_KEY = "entries";
const UNKNOWN_VALUE = "AMBIGUOUS";
const NO_ANSWER_VALUE = "NONE";

const createFallbackListField = (): MetadataFieldDto => ({
  name: "entries",
  label: "Eintraege",
  type: MetadataFieldDtoType.TEXT_WITH_DESCRIPTION_LIST,
  fields: {
    title: {
      name: "title",
      label: "Text",
      type: MetadataFieldDtoType.TEXT,
    },
    description: {
      name: "description",
      label: "Beschreibung",
      type: MetadataFieldDtoType.LONGTEXT,
    },
  },
});

const normalizeListValue = (value: unknown): Record<string, string>[] => {
  if (!Array.isArray(value)) {
    return [];
  }

  return value
    .filter(
      (entry): entry is Record<string, unknown> =>
        !!entry && typeof entry === "object" && !Array.isArray(entry),
    )
    .map((entry) =>
      Object.fromEntries(
        Object.entries(entry).map(([key, itemValue]) => [
          key,
          typeof itemValue === "string" ? itemValue : "",
        ]),
      ),
    );
};

const toCanonicalTitleDescriptionRows = (
  rows: Record<string, string>[],
  titleSourceKey?: string,
  descriptionSourceKey?: string,
): Record<string, string>[] => {
  const firstNonEmpty = (...candidates: Array<string | undefined>): string => {
    for (const candidate of candidates) {
      if (typeof candidate === "string" && candidate.trim().length > 0) {
        return candidate;
      }
    }
    return "";
  };

  return rows.map((row) => {
    const values = Object.values(row).filter((value) => value.trim().length > 0);
    const title = firstNonEmpty(
      row.title,
      titleSourceKey ? row[titleSourceKey] : undefined,
      values[0],
    );
    const description = firstNonEmpty(
      row.description,
      descriptionSourceKey ? row[descriptionSourceKey] : undefined,
      values[1],
    );

    return {
      title,
      description,
    };
  });
};

const getNestedListValue = (entry: FieldValueMap, listKey: string): Record<string, string>[] => {
  const directValue = normalizeListValue(entry[listKey]);
  if (directValue.length > 0) {
    return directValue;
  }

  const fallbackValue = normalizeListValue(entry.entries);
  if (fallbackValue.length > 0) {
    return fallbackValue;
  }

  const nestedArrayValue = Object.entries(entry).find(
    ([key, itemValue]) => key !== listKey && Array.isArray(itemValue),
  )?.[1];

  return normalizeListValue(nestedArrayValue);
};

const getSelectedTagValue = (entry: FieldValueMap): string => {
  const knownCandidates = [entry[SELECTION_KEY], entry.name, entry.title, entry.value, entry.label];

  for (const candidate of knownCandidates) {
    if (typeof candidate === "string" && candidate.trim().length > 0) {
      return candidate;
    }
  }

  const genericCandidate = Object.entries(entry).find(
    ([key, value]) =>
      key !== ENTRIES_KEY &&
      key !== "description" &&
      key !== "details" &&
      typeof value === "string" &&
      value.trim().length > 0,
  );

  return genericCandidate && typeof genericCandidate[1] === "string" ? genericCandidate[1] : "";
};

const MetadataFieldTagSelectWithTextAndDescriptionList = ({
  field,
  value,
  allValues,
  onChange,
  error,
}: Props) => {
  const resolvedListField: MetadataFieldDto = useMemo(
    () => ({
      ...(field.fields ? field : createFallbackListField()),
      name: ENTRIES_KEY,
      label: field.label ?? "Eintraege",
      type: MetadataFieldDtoType.TEXT_WITH_DESCRIPTION_LIST,
      fields: field.fields ?? createFallbackListField().fields,
    }),
    [field],
  );

  const listSubfields = useMemo(
    () =>
      resolvedListField.fields
        ? Object.values(resolvedListField.fields as Record<string, MetadataFieldDto>)
        : [],
    [resolvedListField.fields],
  );

  const titleSourceKey = listSubfields[0]?.name;
  const descriptionSourceKey = listSubfields[1]?.name;

  const canonicalListField = useMemo<MetadataFieldDto>(
    () => ({
      ...resolvedListField,
      type: MetadataFieldDtoType.TEXT_WITH_DESCRIPTION_LIST,
      fields: {
        title: {
          ...(listSubfields[0] ?? {
            name: "title",
            label: "Text",
            type: MetadataFieldDtoType.TEXT,
          }),
          name: "title",
          type: MetadataFieldDtoType.TEXT,
        },
        description: {
          ...(listSubfields[1] ?? {
            name: "description",
            label: "Beschreibung",
            type: MetadataFieldDtoType.LONGTEXT,
          }),
          name: "description",
          type: MetadataFieldDtoType.LONGTEXT,
        },
      },
    }),
    [listSubfields, resolvedListField],
  );

  const normalizedValue = useMemo(() => {
    if (!Array.isArray(value)) {
      return [] as FieldValueMap[];
    }

    return value
      .filter(
        (item): item is FieldValueMap => !!item && typeof item === "object" && !Array.isArray(item),
      )
      .map((item) => ({
        [SELECTION_KEY]: getSelectedTagValue(item),
        [ENTRIES_KEY]: toCanonicalTitleDescriptionRows(
          getNestedListValue(item, ENTRIES_KEY),
          titleSourceKey,
          descriptionSourceKey,
        ),
      }));
  }, [descriptionSourceKey, titleSourceKey, value]);

  const [entries, setEntries] = useState<FieldValueMap[]>(normalizedValue);

  useEffect(() => {
    setEntries((prev) => (areEntryListsEqual(prev, normalizedValue) ? prev : normalizedValue));
  }, [normalizedValue]);

  const selectedTags = useMemo(
    () =>
      entries
        .map((entry) => (typeof entry[SELECTION_KEY] === "string" ? entry[SELECTION_KEY] : ""))
        .filter(Boolean) as string[],
    [entries],
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

  const optionDisableAdditionalDetailsByValue = useMemo(() => {
    const map = new Map<string, boolean>();
    (field.options ?? []).forEach((option: MetadataFieldOption) => {
      map.set(option.value, option.disableAdditionalDetails === true);
    });
    return map;
  }, [field.options]);

  const optionAdditionalDetailsByValue = useMemo(() => {
    const map = new Map<string, MetadataOptionAdditionalDetailsDto>();
    (field.options ?? []).forEach((option: MetadataFieldOption) => {
      if (option.additionalDetails) {
        map.set(option.value, option.additionalDetails);
      }
    });
    return map;
  }, [field.options]);

  const notify = (next: FieldValueMap[]) => {
    setEntries(next);
    onChange?.(next);
  };

  useEffect(() => {
    if (!allowedValues) {
      return;
    }

    const filtered = entries.filter((entry) => {
      const tagValue = typeof entry[SELECTION_KEY] === "string" ? entry[SELECTION_KEY] : "";
      return allowedValues.has(tagValue);
    });

    if (!areEntryListsEqual(filtered, entries)) {
      setEntries(filtered);
      onChange?.(filtered);
    }
  }, [allowedValues, entries, onChange]);

  useEffect(() => {
    const sanitized = entries.map((entry) => {
      const tagValue = typeof entry[SELECTION_KEY] === "string" ? entry[SELECTION_KEY] : "";
      const disableAdditionalDetails =
        tagValue === UNKNOWN_VALUE ||
        tagValue === NO_ANSWER_VALUE ||
        optionDisableAdditionalDetailsByValue.get(tagValue) === true;

      if (!disableAdditionalDetails) {
        return entry;
      }

      const currentList = getNestedListValue(entry, ENTRIES_KEY);
      if (currentList.length === 0) {
        return entry;
      }

      return {
        ...entry,
        [ENTRIES_KEY]: [],
      } satisfies FieldValueMap;
    });

    if (!areEntryListsEqual(sanitized, entries)) {
      setEntries(sanitized);
      onChange?.(sanitized);
    }
  }, [entries, onChange, optionDisableAdditionalDetailsByValue]);

  const handleTagSelectionChange = (newValue: string | string[] | undefined) => {
    const nextSelected = Array.isArray(newValue) ? newValue : newValue ? [newValue] : [];

    const nextEntries = nextSelected.map((selectedTag) => {
      const existing = entries.find((entry) => entry[SELECTION_KEY] === selectedTag);
      if (existing) {
        return existing;
      }

      return {
        [SELECTION_KEY]: selectedTag,
        [ENTRIES_KEY]: [],
      } satisfies FieldValueMap;
    });

    notify(nextEntries);
  };

  const handleListChange = (tagValue: string, nextList: Record<string, string>[]) => {
    const canonicalList = toCanonicalTitleDescriptionRows(
      nextList,
      titleSourceKey,
      descriptionSourceKey,
    );

    const nextEntries = entries.map((entry) =>
      entry[SELECTION_KEY] === tagValue ? { ...entry, [ENTRIES_KEY]: canonicalList } : entry,
    );

    notify(nextEntries);
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

      <Box gap="medium">
        <InfoTooltip
          helpText={field.helpText ?? field.tooltip}
          examples={field.examples}
        >
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
        </InfoTooltip>

        {entries.length > 0 && (
          <Box gap="small">
            {entries.map((entry, idx) => {
              const entryTagValue =
                typeof entry[SELECTION_KEY] === "string" ? entry[SELECTION_KEY] : selectedTags[idx];

              const tagValue = typeof entryTagValue === "string" ? entryTagValue : "";
              const tagLabel = tagValue.trim() || "Eintrag";
              const tagText = optionLabelByValue.get(tagValue) ?? tagLabel;
              const optionAdditionalDetails = optionAdditionalDetailsByValue.get(tagValue);
              const additionalDetailFields = optionAdditionalDetails?.fields ?? [];
              const titleDetailField = additionalDetailFields[0];
              const descriptionDetailField = additionalDetailFields[1];
              const optionDefaultTooltip = optionAdditionalDetails?.defaultTooltip?.trim()
                ? optionAdditionalDetails.defaultTooltip
                : undefined;
              const disableAdditionalDetails =
                tagValue === UNKNOWN_VALUE ||
                tagValue === NO_ANSWER_VALUE ||
                optionDisableAdditionalDetailsByValue.get(tagValue) === true;

              const baseFields =
                (canonicalListField.fields as Record<string, MetadataFieldDto> | undefined) ?? {};
              const baseTitleField = baseFields.title ?? {
                name: "title",
                label: "Text",
                type: MetadataFieldDtoType.TEXT,
              };
              const baseDescriptionField = baseFields.description ?? {
                name: "description",
                label: "Beschreibung",
                type: MetadataFieldDtoType.LONGTEXT,
              };

              const listFieldForTag: MetadataFieldDto = {
                ...canonicalListField,
                label: `${canonicalListField.label ?? "Eintraege"}: ${tagText}`,
                type: MetadataFieldDtoType.TEXT_WITH_DESCRIPTION_LIST,
                fields: {
                  ...baseFields,
                  title: {
                    ...baseTitleField,
                    label: titleDetailField?.label?.trim() || baseTitleField.label || "Text",
                    placeholder:
                      titleDetailField?.placeholder ??
                      optionAdditionalDetails?.defaultPlaceholder ??
                      baseTitleField.placeholder ??
                      undefined,
                    tooltip: titleDetailField?.tooltip ?? baseTitleField.tooltip,
                    required: titleDetailField?.required,
                    type: MetadataFieldDtoType.TEXT,
                    name: "title",
                  },
                  description: {
                    ...baseDescriptionField,
                    label:
                      descriptionDetailField?.label?.trim() ||
                      baseDescriptionField.label ||
                      "Beschreibung",
                    placeholder:
                      descriptionDetailField?.placeholder ??
                      optionAdditionalDetails?.defaultPlaceholder ??
                      baseDescriptionField.placeholder ??
                      undefined,
                    tooltip: descriptionDetailField?.tooltip ?? baseDescriptionField.tooltip,
                    required: descriptionDetailField?.required,
                    type: MetadataFieldDtoType.LONGTEXT,
                    name: "description",
                  },
                },
              };

              return (
                <Box
                  key={`${tagValue || "item"}-${idx}`}
                  gap="small"
                  pad={{ bottom: "small" }}
                >
                  {!disableAdditionalDetails ? (
                    <Box
                      direction="column"
                      gap="small"
                    >
                      <Text weight="bold">{tagText}</Text>
                      <MetadataFieldTextWithDescriptionList
                        field={listFieldForTag}
                        value={toCanonicalTitleDescriptionRows(
                          getNestedListValue(entry, ENTRIES_KEY),
                          titleSourceKey,
                          descriptionSourceKey,
                        )}
                        onChange={(nextList) => handleListChange(tagValue, nextList)}
                        inputRowTooltip={optionDefaultTooltip}
                        error={error}
                      />
                    </Box>
                  ) : null}
                </Box>
              );
            })}
          </Box>
        )}
      </Box>
    </Box>
  );
};

export default MetadataFieldTagSelectWithTextAndDescriptionList;
