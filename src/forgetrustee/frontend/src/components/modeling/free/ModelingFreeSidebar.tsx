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

import { Box, Button } from "grommet";
import { Download, Upload } from "grommet-icons";
import type { RefObject } from "react";
import PinnedTipsPanel, { type PinnedTip } from "../common/PinnedTipsPanel";

type ModelingFreeSidebarProps = {
  sidebarRef: RefObject<HTMLDivElement>;
  tips: PinnedTip[];
  onUnpin: (fieldName: string) => void;
  onExport: () => void;
  onUpload: () => void;
  onReset: () => void;
};

const ModelingFreeSidebar = ({
  sidebarRef,
  tips,
  onUnpin,
  onExport,
  onUpload,
  onReset,
}: ModelingFreeSidebarProps) => (
  <Box
    // style={{ transition: "flex-basis 0.1s ease" }}
    fill="vertical"
    ref={sidebarRef}
    animation="fadeIn"
  >
    <PinnedTipsPanel
      tips={tips}
      onUnpin={onUnpin}
      anchorEl={sidebarRef.current}
    />
    <Box
      direction="column"
      gap="small"
      fill="vertical"
    >
      <Button
        label={<Box flex>Exportieren</Box>}
        icon={<Download size="small" />}
        onClick={onExport}
        primary
      />

      <Button
        label={<Box flex>Modell importieren</Box>}
        icon={<Upload size="small" />}
        onClick={onUpload}
        primary
      />
      <Button
        label={<Box flex>Zurücksetzen</Box>}
        onClick={onReset}
        secondary
      />
    </Box>
  </Box>
);

export default ModelingFreeSidebar;
