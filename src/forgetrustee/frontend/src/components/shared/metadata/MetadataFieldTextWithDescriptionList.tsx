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

import { Box, Button, Text, Tip } from "grommet";
import { CircleQuestion, Trash } from "grommet-icons";
import { useEffect, useMemo, useState } from "react";
import type { MetadataFieldDto } from "../../../types/generated";
import MetadataFieldRenderer from "./MetadataFieldRenderer";
import DynamicIcon from "../DynamicIcon";

type MetadataFieldTextWithDescriptionListProps = {
  field: MetadataFieldDto;
  /**
   * Initial values for the two nested fields (by name)
   */
  value?: Record<string, string>[];
  /**
   * Callback receiving exactly two string entries keyed by field name
   */
  onChange?: (pairs: Record<string, string>[]) => void;
  error?: boolean | string;
  /**
   * Optional tooltip for the shared input row (both input fields together)
   */
  inputRowTooltip?: string;
};

const MetadataFieldTextWithDescriptionList = ({
  field,
  value,
  onChange,
  error,
  inputRowTooltip,
}: MetadataFieldTextWithDescriptionListProps) => {
  // we only support two nested fields, so we take the first two we find
  const nestedFields = useMemo(
    () => (field.fields ? Object.values(field.fields as Record<string, MetadataFieldDto>) : []),
    [field.fields],
  );

  // we try to be smart and identify which field is the description,
  // but if we fail, we just take the first two fields as they come
  const toRender = useMemo(() => nestedFields.slice(0, 2), [nestedFields]);

  // the list of entered pairs, each being an object keyed by field name
  const [pairs, setPairs] = useState<Record<string, string>[]>(() =>
    value && value.length > 0 ? value : [],
  );

  // input state for the currently edited pair, keyed by field name
  const [input, setInput] = useState<Record<string, string>>(() => {
    const empty: Record<string, string> = {};
    toRender.forEach((sf) => (empty[sf.name] = ""));
    return empty;
  });

  // initialize pairs from value, if provided
  useEffect(() => {
    setPairs(value ?? []);
  }, [value]);

  useEffect(() => {
    const reset: Record<string, string> = {};
    toRender.forEach((sf) => (reset[sf.name] = ""));
    setInput(reset);
  }, [toRender]);

  const notify = (next: Record<string, string>[]) => {
    setPairs(next);
    onChange?.(next);
  };

  const handleInputChange = (name: string, val: string) => setInput((p) => ({ ...p, [name]: val }));

  const canAdd = Boolean(input[toRender[0]?.name]?.trim());

  const handleAdd = () => {
    if (!canAdd) return;

    const next = [...pairs, input];
    notify(next);

    const reset: Record<string, string> = {};
    toRender.forEach((sf) => (reset[sf.name] = ""));
    setInput(reset);
  };

  const handleRemove = (idx: number) => {
    const next = pairs.filter((_, i) => i !== idx);
    notify(next);
  };

  const hasInputRowTooltip = Boolean(inputRowTooltip?.trim());
  const reserveDefaultTooltipSlot = true;
  const showDefaultTooltip = hasInputRowTooltip && reserveDefaultTooltipSlot;
  const hasError = Boolean(error);
  const errorMessage = typeof error === "string" ? error : undefined;

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
      <Box gap="small">
        {pairs.length > 0 && (
          <Box gap="xsmall">
            <Box
              direction="row"
              gap="small"
              align="center"
              pad={{ vertical: "xsmall" }}
            >
              <Box flex>
                <Text
                  size="xsmall"
                  weight="bold"
                  color="dark-3"
                >
                  {toRender[0].label}
                </Text>
              </Box>
              <Box flex>
                <Text
                  size="xsmall"
                  weight="bold"
                  color="dark-3"
                >
                  {toRender[1].label}
                </Text>
              </Box>
              <Box width="32px" />
            </Box>

            {pairs.map((p, idx) => (
              <Box
                key={idx}
                direction="row"
                align="center"
                pad={{ vertical: "xsmall" }}
                border={{ side: "bottom", color: "border-light", size: "hair" }}
                fill
              >
                <Box
                  fill="horizontal"
                  pad={{ horizontal: "small" }}
                >
                  <Text>{p[toRender[0].name]}</Text>
                </Box>
                <Box
                  fill="horizontal"
                  direction="row"
                >
                  <Box
                    fill="horizontal"
                    pad={{ horizontal: "small" }}
                  >
                    <Text>{p[toRender[1].name]}</Text>
                  </Box>
                  <Box pad={{ right: "xsmall" }}>
                    <Button
                      icon={<Trash size="1em" />}
                      onClick={() => handleRemove(idx)}
                      plain
                    />
                  </Box>
                </Box>
              </Box>
            ))}
          </Box>
        )}

        {/* input row */}
        <Box>
          <Box
            direction="row"
            align="center"
            gap="small"
          >
            {toRender.map((subfield) => (
              <Box
                key={subfield.name}
                flex
              >
                <MetadataFieldRenderer
                  field={subfield}
                  value={input[subfield.name]}
                  embedded
                  onChange={(v) =>
                    handleInputChange(subfield.name, v === undefined || v === null ? "" : String(v))
                  }
                />
              </Box>
            ))}

            {reserveDefaultTooltipSlot ? (
              <Box
                width="32px"
                height="32px"
                align="center"
                justify="center"
              >
                {showDefaultTooltip ? (
                  <Tip
                    content={
                      <Box
                        background="black"
                        pad="small"
                        round="small"
                      >
                        <Text
                          size="small"
                          color="white"
                        >
                          {inputRowTooltip}
                        </Text>
                      </Box>
                    }
                    dropProps={{
                      align: { left: "right" },
                      margin: { right: "medium" },
                    }}
                    plain
                  >
                    <Box
                      height="32px"
                      width="32px"
                      align="center"
                      justify="center"
                      hoverIndicator
                      aria-label="Hilfe"
                    >
                      <CircleQuestion color="brand" />
                    </Box>
                  </Tip>
                ) : null}
              </Box>
            ) : null}
          </Box>

          <Box
            direction="row"
            gap="small"
            align="center"
            fill="horizontal"
            margin={{ top: "xsmall" }}
          >
            <Box flex />
            <Box
              flex
              align="end"
            >
              <Button
                icon={
                  <DynamicIcon
                    iconName="Add"
                    size="1em"
                  />
                }
                label="Hinzufügen"
                onClick={handleAdd}
                disabled={!canAdd}
                primary
              />
            </Box>
            <Box width="32px" />
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default MetadataFieldTextWithDescriptionList;
