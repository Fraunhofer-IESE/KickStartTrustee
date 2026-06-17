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

import { Box, Grid, Text } from "grommet";
import DynamicIcon from "../../shared/DynamicIcon";

interface StepProps {
  number: number;
  title: string;
  description: string;
  showLine?: boolean;
  active?: boolean;
  valid?: boolean;
  completed?: boolean;
  onClick?: () => void;
  onHelp?: () => void;
}

const Step: React.FC<StepProps> = ({
  number,
  title,
  description,
  showLine = false,
  active = false,
  valid = false,
  completed = false,
  onClick,
  onHelp,
}) => (
  <Grid
    columns={["40px", "auto"]}
    rows={showLine ? ["auto", "1fr"] : ["auto"]}
    areas={
      showLine
        ? [
            { name: "icon", start: [0, 0], end: [0, 0] },
            { name: "content", start: [1, 0], end: [1, 1] },
            { name: "line", start: [0, 1], end: [0, 1] },
          ]
        : [
            { name: "icon", start: [0, 0], end: [0, 0] },
            { name: "content", start: [1, 0], end: [1, 0] },
          ]
    }
    gap="none"
    style={{ ...(onClick ? { cursor: "pointer" } : {}) }}
    onClick={onClick}
    fill="vertical"
    height={{ height: "100%", min: "100px" }}
  >
    <Box
      gridArea="icon"
      align="center"
      justify="start"
      height="100%"
    >
      <Box
        round="full"
        background={active ? "button-primary" : valid ? "brand" : "#e0e0e0"}
        width="32px"
        height="32px"
        align="center"
        justify="center"
        border={active ? { color: "off-black", size: "xsmall" } : undefined}
      >
        {completed ? (
          <DynamicIcon
            iconName="Checkmark"
            size="1em"
          />
        ) : (
          <Text color={active || valid ? "white" : "black"}>{number}</Text>
        )}
      </Box>
    </Box>
    <Box
      gridArea="content"
      pad={{ left: "small", bottom: "xsmall" }}
    >
      <Text weight="bold">{title}</Text>
      <Text
        size="small"
        color="text-weak"
      >
        {description}
      </Text>
      {typeof onHelp === "function" && (
        <Text
          size="small"
          style={{
            cursor: "pointer",
            textDecoration: "underline",
            marginTop: 4,
          }}
          onClick={onHelp}
        >
          Hilfe
        </Text>
      )}
    </Box>
    {showLine && (
      <Box
        gridArea="line"
        align="center"
        justify="center"
        height="100%"
      >
        <Box
          width="1px"
          height="100%"
          background="border"
          margin={{ top: "medium" }}
        />
      </Box>
    )}
  </Grid>
);

export default Step;
