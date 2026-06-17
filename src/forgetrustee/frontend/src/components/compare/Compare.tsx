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
import { useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { routes } from "../../config/routes";
import { getModelingFreeSessionState } from "../modeling/common/modelingSession";
import { useGetDataTrusteeModelById } from "../../api/generated/data-trustee-model-controller/data-trustee-model-controller";
import type { DataTrusteeModelResponseDto } from "../../types/generated/dataTrusteeModelResponseDto";
import Breadcrumb from "../shared/Breadcrumb";
import CompareDetailHeader from "./CompareDetailHeader.tsx";
import type { CompareField } from "./CompareSection.tsx";
import CompareSection from "./CompareSection.tsx";

type CompareLocationState = {
  dthItem1?: DataTrusteeModelResponseDto;
  dthItem2?: DataTrusteeModelResponseDto;
  dthMeta1?: {
    modelId?: string;
    avatarDataUrl?: string | null;
    avatarFileName?: string | null;
    isOwnModel?: boolean;
  };
  dthMeta2?: {
    modelId?: string;
    avatarDataUrl?: string | null;
    avatarFileName?: string | null;
    isOwnModel?: boolean;
  };
};

type ModelSection = {
  key: ModelSectionKey;
  title: string;
};

type ModelSectionKey = "core" | "legal" | "data" | "objectives" | "business";

type CompareValue = {
  text: string;
  lines: string[];
  items: CompareValueItem[];
};

type CompareValueItem = {
  text: string;
  children?: CompareValueItem[];
};

const SECTION_CONFIG: ModelSection[] = [
  { key: "core", title: "Core" },
  { key: "legal", title: "Legal" },
  { key: "data", title: "Data" },
  { key: "objectives", title: "Objectives" },
  { key: "business", title: "Business" },
];

const DISPLAY_LANG = "de";

const EXCLUDED_KEYS = new Set(["id"]);

const toYesNoText = (value: unknown): string | null => {
  if (value === true) {
    return "Ja";
  }

  if (value === false) {
    return "Nein";
  }

  if (typeof value === "string") {
    const normalized = value.trim().toLowerCase();
    if (normalized === "true") {
      return "Ja";
    }
    if (normalized === "false") {
      return "Nein";
    }
  }

  return null;
};

const looksLikeEnumToken = (value: string) => /^[A-Z0-9_]+$/.test(value.trim());

const shouldPreferLabelOverValue = (value: unknown, label: unknown): boolean => {
  if (typeof label !== "string" || !label.trim()) {
    return false;
  }

  // For free text we want the actual entered value, not a generic concept label.
  if (typeof value === "string") {
    const normalizedValue = value.trim();
    if (!normalizedValue) {
      return true;
    }

    return looksLikeEnumToken(normalizedValue);
  }

  return false;
};

const withPlaceholder = (): CompareValue => ({ text: "—", lines: [], items: [] });

const toLeafValue = (text: string): CompareValue => ({ text, lines: [text], items: [{ text }] });

const flattenItemTexts = (items: CompareValueItem[]): string[] => {
  const lines: string[] = [];

  items.forEach((item) => {
    lines.push(item.text);
    if (item.children?.length) {
      lines.push(...flattenItemTexts(item.children));
    }
  });

  return lines;
};

const toLabeledItem = (label: string, value: CompareValue): CompareValueItem => {
  if (value.items.length > 0) {
    return {
      text: label,
      children: value.items,
    };
  }

  return { text: `${label}: ${value.text}` };
};

const formatValue = (value: unknown): CompareValue => {
  if (value === undefined || value === null) {
    return withPlaceholder();
  }

  const yesNoText = toYesNoText(value);
  if (yesNoText) {
    return toLeafValue(yesNoText);
  }

  if (Array.isArray(value)) {
    if (value.length === 0) {
      return withPlaceholder();
    }

    const formattedEntries = value.map((item) => formatValue(item)).filter((entry) => entry.text !== "—");

    if (formattedEntries.length === 0) {
      return withPlaceholder();
    }

    const items = formattedEntries.flatMap((entry) => {
      if (entry.items.length > 0) {
        return entry.items;
      }

      return [{ text: entry.text }];
    });

    const lines = flattenItemTexts(items);

    return {
      text: formattedEntries.map((entry) => entry.text).join(" | "),
      lines,
      items,
    };
  }

  if (typeof value === "object") {
    const objectValue = value as Record<string, unknown>;
    const objectEntries = Object.entries(objectValue).filter(
      ([entryKey]) => entryKey !== "comment",
    );

    if (objectEntries.length === 0) {
      return withPlaceholder();
    }

    if (
      objectEntries.length <= 2 &&
      objectEntries.every(([entryKey]) => entryKey === "title" || entryKey === "description")
    ) {
      const title = formatValue(objectValue.title).text;
      const description = formatValue(objectValue.description).text;

      if (title !== "—") {
        const children = description !== "—" ? [{ text: description }] : undefined;
        const item: CompareValueItem = { text: title, children };
        return {
          text: description !== "—" ? `${title}: ${description}` : title,
          lines: flattenItemTexts([item]),
          items: [item],
        };
      }
    }

    if (
      objectEntries.length === 1 &&
      (objectEntries[0][0] === "value" || objectEntries[0][0] === "label")
    ) {
      return formatValue(objectEntries[0][1]);
    }

    if (
      objectEntries.every(
        ([entryKey]) => entryKey === "value" || entryKey === "label" || entryKey === "comment",
      )
    ) {
      const valueEntry = objectEntries.find(([entryKey]) => entryKey === "value");
      const labelEntry = objectEntries.find(([entryKey]) => entryKey === "label");

      if (valueEntry && labelEntry && shouldPreferLabelOverValue(valueEntry[1], labelEntry[1])) {
        const formattedLabel = formatValue(labelEntry[1]);
        if (formattedLabel.text !== "—") {
          return formattedLabel;
        }
      }

      if (valueEntry) {
        return formatValue(valueEntry[1]);
      }

      if (labelEntry) {
        return formatValue(labelEntry[1]);
      }
    }

    const valueEntry = objectEntries.find(([entryKey]) => entryKey === "value");
    const labelEntry = objectEntries.find(([entryKey]) => entryKey === "label");
    const shouldHideRawValue = Boolean(
      valueEntry &&
        labelEntry &&
        shouldPreferLabelOverValue(valueEntry[1], labelEntry[1]),
    );

    const lines = objectEntries
      .filter(([entryKey]) => !(shouldHideRawValue && entryKey === "value"))
      .map(([entryKey, entryValue]) => {
      if (entryKey === "value" || entryKey === "label") {
        return formatValue(entryValue).text;
      }

      const formattedValue = formatValue(entryValue).text;

      return `${toLabel(entryKey)}: ${formattedValue}`.trim();
      });

    const items = objectEntries
      .filter(([entryKey]) => !(shouldHideRawValue && entryKey === "value"))
      .map(([entryKey, entryValue]) => {
        if (entryKey === "value" || entryKey === "label") {
          return formatValue(entryValue).items;
        }

        return [toLabeledItem(toLabel(entryKey), formatValue(entryValue))];
      })
      .flat();

    return { text: lines.join(" | "), lines, items };
  }

  const text = String(value).trim();

  if (text.length === 0) {
    return withPlaceholder();
  }

  return toLeafValue(text);
};

const toLabel = (key: string) => {
  return key
    .replace(/([a-z0-9])([A-Z])/g, "$1 $2")
    .replace(/_/g, " ")
    .replace(/\s+/g, " ")
    .trim()
    .replace(/^./, (character) => character.toUpperCase());
};

const getPresentationSection = (
  model: DataTrusteeModelResponseDto,
  sectionKey: ModelSectionKey,
) => model.presentation?.sections?.find((section) => section.key === sectionKey);

const resolveSectionLabel = (
  sectionKey: ModelSectionKey,
  item1: DataTrusteeModelResponseDto,
  item2: DataTrusteeModelResponseDto,
  fallback: string,
) => {
  const leftLabel = getPresentationSection(item1, sectionKey)?.label?.trim();
  if (leftLabel) {
    return leftLabel;
  }

  const rightLabel = getPresentationSection(item2, sectionKey)?.label?.trim();
  return rightLabel || fallback;
};

const resolveFieldLabel = (
  sectionKey: ModelSectionKey,
  fieldKey: string,
  item1: DataTrusteeModelResponseDto,
  item2: DataTrusteeModelResponseDto,
) => {
  const leftLabel = getPresentationSection(item1, sectionKey)?.fields?.[fieldKey]?.trim();
  if (leftLabel) {
    return leftLabel;
  }

  const rightLabel = getPresentationSection(item2, sectionKey)?.fields?.[fieldKey]?.trim();
  return rightLabel || toLabel(fieldKey);
};

const getSectionFields = (
  sectionKey: ModelSectionKey,
  item1: DataTrusteeModelResponseDto,
  item2: DataTrusteeModelResponseDto,
): CompareField[] => {
  const section1 = (item1[sectionKey] ?? {}) as Record<string, unknown>;
  const section2 = (item2[sectionKey] ?? {}) as Record<string, unknown>;

  const allKeys = new Set<string>([...Object.keys(section1), ...Object.keys(section2)]);

  const filteredKeys = Array.from(allKeys).filter((key) => !EXCLUDED_KEYS.has(key));

  if (filteredKeys.length === 0) {
    return [];
  }

  return filteredKeys.map((key) => {
    const value1 = formatValue(section1[key]);
    const value2 = formatValue(section2[key]);

    return {
      key,
      label: resolveFieldLabel(sectionKey, key, item1, item2),
      value1,
      value2,
      isDifferent: value1.text !== value2.text,
    };
  });
};

const Compare: React.FC = () => {
  const { state } = useLocation();
  const navigate = useNavigate();
  const [highlightDifferences, setHighlightDifferences] = useState(true);
  const { dthItem1, dthItem2, dthMeta1, dthMeta2 } = (state as CompareLocationState) ?? {};
  const sessionModelInput =
    getModelingFreeSessionState().payload?.modelBuildResult?.dataTrusteeModel;

  const item1ModelId = dthMeta1?.isOwnModel ? undefined : dthMeta1?.modelId;
  const item2ModelId = dthMeta2?.isOwnModel ? undefined : dthMeta2?.modelId;

  const { data: fetchedItem1, isPending: isItem1Pending } = useGetDataTrusteeModelById(
    item1ModelId ?? "",
    { lang: DISPLAY_LANG },
    {
      query: {
        enabled: Boolean(item1ModelId),
      },
    },
  );
  const { data: fetchedItem2, isPending: isItem2Pending } = useGetDataTrusteeModelById(
    item2ModelId ?? "",
    { lang: DISPLAY_LANG },
    {
      query: {
        enabled: Boolean(item2ModelId),
      },
    },
  );

  const resolvedItem1 = dthMeta1?.isOwnModel
    ? (dthItem1 ?? sessionModelInput)
    : (fetchedItem1 ?? dthItem1);
  const resolvedItem2 = dthMeta2?.isOwnModel
    ? (dthItem2 ?? sessionModelInput)
    : (fetchedItem2 ?? dthItem2 ?? sessionModelInput);

  const comparisonItem1 = resolvedItem1 ?? dthItem1 ?? sessionModelInput;
  const comparisonItem2 = resolvedItem2 ?? dthItem2 ?? sessionModelInput;

  const hasSelection = Boolean(comparisonItem1 && comparisonItem2);
  const isLoadingComparison =
    (!dthMeta1?.isOwnModel && Boolean(item1ModelId) && isItem1Pending) ||
    (!dthMeta2?.isOwnModel && Boolean(item2ModelId) && isItem2Pending);

  const comparisonSections = useMemo(
    () =>
      SECTION_CONFIG.map((section) => ({
        key: section.key,
        title:
          resolvedItem1 && resolvedItem2
            ? resolveSectionLabel(section.key, resolvedItem1, resolvedItem2, section.title)
            : section.title,
        fields:
          resolvedItem1 && resolvedItem2
            ? getSectionFields(section.key, resolvedItem1, resolvedItem2)
            : [],
      })),
    [resolvedItem1, resolvedItem2],
  );

  return (
    <Box fill="horizontal">
      <Box
        direction="row"
        justify="between"
      >
        <Breadcrumb
          items={[routes.startpage, routes.dataTrusteeCompare, routes.dataTrusteeCompareResult]}
        />
      </Box>

      {/* Content */}
      <Box
        animation={{ type: "fadeIn", duration: 300 }}
        fill
      >
        {isLoadingComparison ? (
          <Box gap="small">
            <Text>Vergleichsdaten werden geladen.</Text>
          </Box>
        ) : !hasSelection ? (
          <Box gap="small">
            <Text>Keine Vergleichsdaten gefunden.</Text>
            <Button
              label="Zur Übersicht"
              onClick={() => navigate(routes.dataTrusteeCompare.path)}
            />
          </Box>
        ) : (
          <Box fill>
            <CompareDetailHeader
              item1={comparisonItem1 as DataTrusteeModelResponseDto}
              item2={comparisonItem2 as DataTrusteeModelResponseDto}
              meta1={dthMeta1}
              meta2={dthMeta2}
              highlightDifferences={highlightDifferences}
              onToggleHighlight={setHighlightDifferences}
            />

            <Box
              gap="small"
              pad={{ vertical: "small" }}
            >
              {comparisonSections.map((section) => (
                <CompareSection
                  key={section.key}
                  title={section.title}
                  fields={section.fields}
                  highlightDifferences={highlightDifferences}
                />
              ))}
            </Box>
          </Box>
        )}
      </Box>
    </Box>
  );
};

export default Compare;
