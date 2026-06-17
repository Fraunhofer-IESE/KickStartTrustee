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
import { useCallback } from "react";
import { CircleQuestion } from "grommet-icons";
import DynamicIcon from "../../shared/DynamicIcon";

interface StepVerticalProps {
  number: number;
  title: string;
  active?: boolean;
  edited?: boolean;
  valid?: boolean;
  onClick?: () => void;
}

const StepVertical: React.FC<StepVerticalProps> = ({
  number,
  title,
  active = false,
  edited = false,
  valid = false,
  onClick,
}) => {
  const handleOnClick = useCallback(() => {
    onClick?.();
  }, [onClick]);

  const showValidState = edited && valid;
  const showInvalidState = edited && !valid;
  const background = showValidState ? "brand" : showInvalidState ? "status-critical" : "#e0e0e0";
  const titleColor = showValidState ? "brand" : showInvalidState ? "status-critical" : "text-weak";

  return (
    <Box
      direction="row"
      fill="horizontal"
      gap="medium"
      onClick={handleOnClick}
    >
      <Box
        direction="column"
        fill="horizontal"
        gap="small"
        style={{ cursor: onClick ? "pointer" : undefined }}
      >
        <Box
          align="center"
          justify="center"
          fill
          height={{ min: "auto" }}
        >
          <Box
            round="full"
            background={background}
            width="32px"
            height="32px"
            align="center"
            justify="center"
            border={active ? { color: "off-black", size: "xsmall" } : undefined}
          >
            {showValidState ? (
              <DynamicIcon
                iconName="Checkmark"
                size="1em"
                color="white"
              />
            ) : showInvalidState ? (
              <CircleQuestion
                size="1em"
                color="white"
              />
            ) : (
              <Text color="black">{number}</Text>
            )}
          </Box>
        </Box>
        <Box
          fill
          justify="center"
          align="center"
        >
          <Text
            weight="bold"
            size="14px"
            color={titleColor}
            textAlign="center"
          >
            {title}
          </Text>
        </Box>
      </Box>
    </Box>
  );
};

export default StepVertical;
