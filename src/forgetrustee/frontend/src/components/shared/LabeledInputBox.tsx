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

import { Box, Text, type BoxProps } from "grommet";
import type { ReactNode } from "react";

type LabeledInputBoxProps = BoxProps & {
  label: string; // Der Text, der innerhalb des Rahmens angezeigt wird
  children: ReactNode; // Das Eingabefeld (z. B. TextInput oder Select)
  backgroundColor?: string; // Hintergrundfarbe für die Box und den Text
  maxLength?: number;
  currentLength?: number;
  error?: boolean | string;
};

const LabeledInputBox = ({
  label,
  children,
  backgroundColor = "white",
  maxLength,
  currentLength,
  error,
  ...rest
}: LabeledInputBoxProps) => {
  const hasError = Boolean(error);
  const errorMessage = typeof error === "string" ? error : undefined;

  return (
    <Box
      direction="column"
      gap="xsmall"
    >
      <Box
        border={{ color: hasError ? "status-critical" : "border", size: "xsmall" }}
        round="xsmall"
        pad={{ top: "xsmall", bottom: "xsmall", horizontal: "none" }}
        style={{ position: "relative" }}
        {...rest}
        flex
        margin={{ top: "small" }}
      >
        <Text
          size="small"
          color={hasError ? "status-critical" : "rgba(0,0,0,0.6)"}
          style={{
            position: "absolute",
            top: "-9px",
            left: "10px",
            background: backgroundColor,
            padding: "0 4px",
            borderRadius: "4px",
            letterSpacing: "0.15px",
            maxWidth: "calc(100% - 30px)",
          }}
          truncate="tip"
        >
          {label}
        </Text>
        {children}

        {typeof maxLength === "number" && typeof currentLength === "number" && (
          <Text
            size="xsmall"
            color="rgba(0,0,0,0.4)"
            style={{ position: "absolute", bottom: "-2px", right: "10px" }}
          >
            {currentLength}/{maxLength} Zeichen
          </Text>
        )}
      </Box>
      {errorMessage ? (
        <Box pad={{ horizontal: "small" }}>
          <Text
            size="xsmall"
            color="status-critical"
          >
            {errorMessage}
          </Text>
        </Box>
      ) : null}
    </Box>
  );
};

export default LabeledInputBox;
