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

import { Box, Button, Grid, Paragraph, Tip } from "grommet";
import { CircleQuestion } from "grommet-icons";
import { type ComponentPropsWithoutRef, type ReactNode } from "react";

type SectionWithInfoProps = {
  children: ReactNode;
  helpText?: string;
  examples?: string[];
  iconLabel?: string;
  icon?: ReactNode;
  onPin?: (payload: { text?: string }) => void;
  pinLabel?: string;
  maxTextWidth?: number | string;
  maxLines?: number;
  fill?: boolean;
  reserveSpaceWhenNoTooltip?: boolean;
} & Omit<ComponentPropsWithoutRef<typeof Tip>, "children">;

const InfoTooltip = ({
  children,
  helpText,
  examples,
  iconLabel = "Hilfe",
  icon,
  content,
  onPin,
  pinLabel = "Anpinnen",
  maxTextWidth = "260px",
  maxLines = 0,
  fill = true,
  reserveSpaceWhenNoTooltip = false,
  ...tipProps
}: SectionWithInfoProps) => {
  const textMaxWidth = typeof maxTextWidth === "number" ? `${maxTextWidth}px` : maxTextWidth;
  const normalizedExamples = (examples ?? []).filter((example) => Boolean(example?.trim()));
  const hasExamples = normalizedExamples.length > 0;
  const showPinAction = Boolean(onPin && helpText);

  const tipContent =
    content ??
    (helpText || hasExamples ? (
      <Box
        direction="row"
        align="top"
        // width={{ max: "medium" }}
        gap="none"
      >
        <Box
          // margin={{ top: "medium" }}
          // align="center"
          justify="center"
        >
          <svg
            viewBox="0 0 22 22"
            version="1.1"
            width="22px"
            height="22px"
          >
            <polygon
              fill="black"
              points="6 2 18 12 6 22"
              transform="matrix(-1 0 0 1 30 0)"
            />
          </svg>
        </Box>
        <Box
          background="black"
          direction="column"
          pad="small"
          flex
          round="small"
          gap="small"
        >
          <Box
            gap="small"
            direction="row"
            align="center"
          >
            {helpText ? (
              <Paragraph
                size="small"
                margin="none"
                maxLines={maxLines}
                style={{ maxWidth: textMaxWidth }}
              >
                {helpText}
              </Paragraph>
            ) : null}
            {showPinAction ? (
              <Button
                size="small"
                label={pinLabel}
                onClick={() => onPin?.({ text: helpText })}
              />
            ) : null}
          </Box>
          {hasExamples ? (
            <Box gap="xsmall">
              <Paragraph
                size="small"
                margin="none"
                style={{ maxWidth: textMaxWidth, fontWeight: 600 }}
              >
                Beispiele
              </Paragraph>
              <Box gap="xxsmall">
                <ul>
                  {normalizedExamples.map((example) => (
                    <Paragraph
                      key={example}
                      size="small"
                      margin="none"
                      style={{ maxWidth: textMaxWidth }}
                    >
                      <li>{example}</li>
                    </Paragraph>
                  ))}
                </ul>
              </Box>
            </Box>
          ) : null}
        </Box>
      </Box>
    ) : undefined);

  const showTooltipIcon = Boolean(tipContent);
  const showReservedTooltipSlot = !showTooltipIcon && reserveSpaceWhenNoTooltip;
  const showTooltipColumn = showTooltipIcon || showReservedTooltipSlot;

  return (
    <Grid
      fill="horizontal"
      columns={
        showTooltipColumn
          ? fill
            ? ["minmax(0, 1fr)", "auto"]
            : ["auto", "auto"]
          : ["minmax(0, 1fr)"]
      }
      rows={["auto"]}
      gap="small"
      align="center"
    >
      <Box>{children}</Box>

      {showTooltipIcon ? (
        <Tip
          content={tipContent}
          dropProps={{
            align: { left: "right" },
            elevation: "none",
            // target: container ?? undefined, // FIXME: does not work
            margin: { right: "medium" },
          }}
          plain
          {...tipProps}
        >
          <Box
            height="32px"
            width="32px"
            flex={false}
            align="center"
            justify="center"
            hoverIndicator
            aria-label={iconLabel}
          >
            {icon ?? <CircleQuestion color="brand" />}
          </Box>
        </Tip>
      ) : showReservedTooltipSlot ? (
        <Box
          height="32px"
          width="32px"
          flex={false}
        />
      ) : null}
    </Grid>
  );
};

export default InfoTooltip;
