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

import { Box, Button, Heading, Text } from "grommet";
import React from "react";

interface HeadingWithLineProps {
  level?: "1" | "2" | "3" | "4" | "5" | "6" | 1 | 2 | 3 | 4 | 5 | 6;
  children: React.ReactNode;
  id?: string;
  onHelpClick?: () => void;
  showBorder?: boolean;
  color?: string;
}

const HeadingWithLine: React.FC<HeadingWithLineProps> = ({
  level,
  children,
  id,
  onHelpClick,
  showBorder = true,
  color,
}) => (
  <Box
    direction="row"
    align="center"
    gap="small"
    border={showBorder ? { color: "#E8EDF0", side: "bottom" } : undefined}
  >
    <Box flex>
      <Heading
        level={level}
        id={id}
        margin="none"
        color={color}
      >
        {children}
      </Heading>
    </Box>
    {onHelpClick && (
      <Box pad={{ horizontal: "xsmall" }}>
        <Button
          plain
          onClick={onHelpClick}
          label={
            <Text
              size="xsmall"
              color="text-xweak"
              style={{ textDecoration: "underline", cursor: "pointer" }}
            >
              Hilfe
            </Text>
          }
        />
      </Box>
    )}
  </Box>
);

export default HeadingWithLine;
