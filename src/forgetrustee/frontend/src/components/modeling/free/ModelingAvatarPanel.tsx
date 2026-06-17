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

import { Box, Image, Stack, Text } from "grommet";
import DynamicIcon from "../../shared/DynamicIcon";
import placeholder from "../../../assets/placeholder.svg";
import { useRef } from "react";

type ModelingAvatarPanelProps = {
  avatarDataUrl: string | null;
  avatarFile: File | null;
  avatarFileName: string | null;
  onAvatarUpload: () => void;
};

const ModelingAvatarPanel = ({
  avatarDataUrl,
  avatarFile,
  avatarFileName,
  onAvatarUpload,
}: ModelingAvatarPanelProps) => {
  const stickyRef = useRef<HTMLDivElement | null>(null);

  return (
    <Box
      ref={stickyRef}
      alignSelf="start"
      width="small"
      direction="column"
      gap="medium"
      style={{ position: "sticky", top: "1rem", zIndex: 10 }}
    >
      <Box
        direction="column"
        gap="xsmall"
        height={{ min: "auto" }}
      >
        <Box>
          <Stack anchor="bottom-right">
            <Image
              src={avatarDataUrl || placeholder}
              fit="contain"
              fill="horizontal"
            />
            <Box
              round
              background="white"
              pad="xsmall"
              margin="small"
              border={{ color: "text-weak", size: "small" }}
              onClick={onAvatarUpload}
            >
              <DynamicIcon
                iconName="Edit"
                color="text"
                size="small"
              />
            </Box>
          </Stack>
        </Box>
        <Text size="small">
          {avatarFile?.name || avatarFileName
            ? `Ausgewählt: ${avatarFile?.name || avatarFileName}`
            : "Noch keine Datei ausgewählt. Mögliche Dateiformate: png, jpg"}
        </Text>
      </Box>
    </Box>
  );
};

export default ModelingAvatarPanel;
