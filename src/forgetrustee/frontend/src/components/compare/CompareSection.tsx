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

import { Box, Grid, Heading, Text } from "grommet";
import { Checkmark, Close } from "grommet-icons";
import { COMPARE_GRID_COLUMNS } from "./compareColumns";

export type CompareField = {
  key: string;
  label: string;
  value1: {
    text: string;
    lines: string[];
    items: CompareValueItem[];
  };
  value2: {
    text: string;
    lines: string[];
    items: CompareValueItem[];
  };
  isDifferent: boolean;
};

type CompareValueItem = {
  text: string;
  children?: CompareValueItem[];
};

const renderValue = (value: CompareField["value1"], shouldEmphasize: boolean) => {
  const renderLine = (line: string, key: string) => {
    const normalized = line.trim().toLowerCase();
    const isYes = normalized === "ja";
    const isNo = normalized === "nein";

    if (!isYes && !isNo) {
      return (
        <Text
          key={key}
          size="small"
          weight={shouldEmphasize ? "bold" : undefined}
        >
          {line}
        </Text>
      );
    }

    return (
      <Box
        key={key}
        direction="row"
        gap="xsmall"
        align="center"
      >
        {isYes ? (
          <Checkmark
            color="status-ok"
            size="14px"
          />
        ) : (
          <Close
            color="status-critical"
            size="14px"
          />
        )}
        <Text
          size="small"
          weight={shouldEmphasize ? "bold" : undefined}
        >
          {isYes ? "Ja" : "Nein"}
        </Text>
      </Box>
    );
  };

  const renderItems = (items: CompareValueItem[], level = 0) => {
    return (
      <Box
        as="ul"
        margin={{ top: "none", bottom: "none" }}
        pad="none"
        style={{ listStyleType: level === 0 ? "disc" : "circle", paddingLeft: `${16 + level * 12}px` }}
      >
        {items.map((item, index) => (
          <Box
            key={`${item.text}-${index}-${level}`}
            as="li"
            margin={{ bottom: "xsmall" }}
          >
            {renderLine(item.text, `${item.text}-${index}-${level}-line`)}
            {item.children && item.children.length > 0 ? renderItems(item.children, level + 1) : null}
          </Box>
        ))}
      </Box>
    );
  };

  if (value.items.length > 0) {
    return renderItems(value.items);
  }

  if (value.lines.length === 0) {
    return (
      <Text
        size="small"
        color="text-weak"
      >
        —
      </Text>
    );
  }

  if (value.lines.length === 1) {
    return renderLine(value.lines[0], value.lines[0]);
  }

  return (
    <Box gap="xsmall">
      {value.lines.map((line, index) => renderLine(line, `${line}-${index}`))}
    </Box>
  );
};

type CompareSectionProps = {
  title: string;
  fields: CompareField[];
  highlightDifferences: boolean;
};

const CompareSection = ({ title, fields, highlightDifferences }: CompareSectionProps) => {
  return (
    <Box margin={{ bottom: "medium" }}>
      <Box
        pad={{ horizontal: "small", vertical: "xsmall" }}
        border={{ color: "brand", size: "small", side: "bottom" }}
      >
        <Heading
          level={4}
          margin="none"
        >
          {title}
        </Heading>
      </Box>

      {fields.length === 0 ? (
        <Text
          size="small"
          color="text-weak"
        >
          Keine Angaben
        </Text>
      ) : (
        <Box background="background-front">
          {fields.map((field) => {
            const shouldHighlight = highlightDifferences && field.isDifferent;

            return (
              <Box
                key={field.key}
                overflow="hidden"
                border={{
                  color: "border-light",
                  size: "1px",
                  side: "bottom",
                }}
                background={shouldHighlight ? "#EBF6FC" : "background-front"}
              >
                <Grid
                  columns={[...COMPARE_GRID_COLUMNS]}
                  gap="none"
                >
                  <Box
                    pad={{ horizontal: "small", vertical: "xsmall" }}
                    border={{
                      color: "border-light",
                      size: "1px",
                      side: "right",
                    }}
                    justify="center"
                  >
                    <Text
                      size="small"
                      weight="bold"
                    >
                      {field.label}
                    </Text>
                  </Box>

                  <Box
                    pad={{ horizontal: "small", vertical: "xsmall" }}
                    border={{
                      color: "border-light",
                      size: "xsmall",
                      side: "right",
                    }}
                  >
                    {renderValue(field.value1, shouldHighlight)}
                  </Box>

                  <Box pad={{ horizontal: "small", vertical: "xsmall" }}>
                    {renderValue(field.value2, shouldHighlight)}
                  </Box>
                </Grid>
              </Box>
            );
          })}
        </Box>
      )}
    </Box>
  );
};

export default CompareSection;
