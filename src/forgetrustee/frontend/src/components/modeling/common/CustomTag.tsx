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
import React from "react";

interface CustomTagProps {
  label: string;
  selected?: boolean;
  onClick?: () => void;
  disabled?: boolean;
  warning?: boolean;
}

const CustomTag: React.FC<CustomTagProps> = ({
  label,
  selected = false,
  onClick,
  disabled = false,warning = false,
}) => {
  const border = disabled
    ? { color: "border", size: "xsmall" }
    : warning
      ? selected
        ? { color: "button-secondary", size: "xsmall" }
        : {
            color: "button-secondary",
            size: "xsmall",
            style: "dashed" as const,
          }
      : { color: "button-primary", size: "xsmall" };

  return (
    <Box
      pad={{ horizontal: "18px", vertical: "xsmall" }}
      background={
        disabled
          ? "light-4"
          : warning && selected
            ? "button-secondary"
            : warning
              ? "#e6f0f7"
              : selected
                ? "brand"
                : "white"
      }
      round="medium"
      border={border}
      style={{
        cursor: disabled ? "not-allowed" : "pointer",
        userSelect: "none",
        opacity: disabled ? 0.5 : 1,
        transition: "all 0.2s",
      }}
      onClick={disabled ? undefined : onClick}
      focusIndicator={false}
      hoverIndicator={disabled ? undefined : selected ? undefined : "light-2"}
      width={{ min: "120px" }}
      align="center"
    >
      <Text
        color={
          disabled
            ? "text-weak"
            : warning && selected
              ? "white"
              : warning
                ? "#16425b"
                : selected
                  ? "white"
                  : undefined
        }
        wordBreak="normal"
        style={{
          overflowWrap: "break-word",
          whiteSpace: "normal",
          textAlign: "center",
          display: "block",
          maxWidth: "100%",
          padding: "0 2px",
          boxSizing: "border-box",
          lineHeight: 1.2,
        }}
      >
        {label}
      </Text>
    </Box>
  );
};

export default CustomTag;
