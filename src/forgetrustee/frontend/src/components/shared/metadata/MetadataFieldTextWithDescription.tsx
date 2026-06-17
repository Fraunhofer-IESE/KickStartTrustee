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
import { useCallback, useEffect, useMemo, useState } from "react";
import type { MetadataFieldDto } from "../../../types/generated";
import { createLogger } from "../../util/log";
import InfoTooltip from "../InfoTooltip";
import MetadataFieldRenderer from "./MetadataFieldRenderer";

type Props = {
  field: MetadataFieldDto;
  /**
   * Initial values for the two nested fields (by name)
   */
  value?: Record<string, string>;
  /**
   * Callback receiving exactly two string entries keyed by field name
   */
  onChange?: (pair: Record<string, string>) => void;
  error?: boolean | string;
  missingRequiredFieldNames?: string[];
};

const MetadataFieldTextWithDescription = ({
  field,
  value,
  onChange,
  error,
  missingRequiredFieldNames,
}: Props) => {
  const log = createLogger("MetadataFieldTextWithDescription");
  log.debug(`Rendering MetadataFieldTextWithDescription for field "${field.name}"`);
  const [pair, setPair] = useState<Record<string, string>>({});

  const nestedFields = useMemo(
    () => (field.fields ? Object.values(field.fields as Record<string, MetadataFieldDto>) : []),
    [field.fields],
  );

  const toRender = useMemo(() => nestedFields.slice(0, 2), [nestedFields]);
  const parentHelpText = field.helpText ?? field.tooltip;
  const hasParentTooltip = Boolean(parentHelpText?.trim()) || Boolean(field.examples?.length);
  const secondFieldHasOwnTooltip = Boolean(toRender[1]?.tooltip?.trim());
  const reserveTooltipSlot = !secondFieldHasOwnTooltip;
  const hasError = Boolean(error);
  const errorMessage = typeof error === "string" ? error : undefined;

  useEffect(() => {
    const next: Record<string, string> = {};
    toRender.forEach((sf) => {
      next[sf.name] = value?.[sf.name] ?? "";
    });

    setPair((prev) => {
      const changed = toRender.some((sf) => (prev[sf.name] ?? "") !== (next[sf.name] ?? ""));
      return changed ? next : prev;
    });
  }, [value, toRender]);

  const handleSubfieldChange = useCallback(
    (name: string, raw: string) => {
      const str = raw;
      setPair((prev) => {
        const next = { ...prev, [name]: str };
        const result: Record<string, string> = {};
        toRender.forEach((sf) => {
          result[sf.name] = next[sf.name] ?? "";
        });
        onChange?.(result);
        return next;
      });
    },
    [toRender, onChange],
  );

  const subfields = (
    <Box
      direction="row"
      gap="small"
    >
      {toRender.map((subfield) => (
        <Box
          fill
          key={subfield.name}
        >
          <MetadataFieldRenderer
            field={subfield}
            value={pair[subfield.name]}
            embedded
            error={
              missingRequiredFieldNames?.includes(subfield.name)
                ? "Pflichtfeld bitte ausfüllen."
                : undefined
            }
            onChange={(v) => {
              if (typeof v === "string") handleSubfieldChange(subfield.name, v);
            }}
          />
        </Box>
      ))}
    </Box>
  );

  return (
    <Box
      direction="column"
      gap="small"
    >
      {field.heading && (
        <Box>
          <Text color={hasError ? "status-critical" : undefined}>{field.heading}</Text>
        </Box>
      )}

      {errorMessage ? (
        <Text
          size="xsmall"
          color="status-critical"
        >
          {errorMessage}
        </Text>
      ) : null}

      {hasParentTooltip ? (
        <InfoTooltip
          helpText={parentHelpText}
          examples={field.examples}
        >
          {subfields}
        </InfoTooltip>
      ) : reserveTooltipSlot ? (
        <Box
          direction="row"
          gap="small"
          align="start"
        >
          <Box flex>{subfields}</Box>
          <Box
            width="32px"
            height="32px"
            flex={false}
          />
        </Box>
      ) : (
        subfields
      )}
    </Box>
  );
};

export default MetadataFieldTextWithDescription;
