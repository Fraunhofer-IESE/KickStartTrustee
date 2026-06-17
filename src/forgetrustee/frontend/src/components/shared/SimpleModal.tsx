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

import { type ReactNode } from "react";
import { Layer, Box, type LayerExtendedProps } from "grommet";
// import { useInstanceRef } from "../../context/InstanceRefContext";

type SimpleModalProps = {
  open: boolean;
  onClose: () => void;
  children: ReactNode;
  width?: string;
} & Omit<LayerExtendedProps, "children">;

const SimpleModal = ({
  open,
  onClose,
  children,
  width,
  ...rest
}: SimpleModalProps) => {
  if (!open) return null;

  return (
    <Layer
      onClickOutside={onClose}
      onEsc={onClose}
      {...rest}
      animation="fadeIn"
      background="transparent"
    >
      <Box
        pad="large"
        overflow="auto"
        fill={false}
        width={width || "560px"}
        round="small"
        background="background-front"
      >
        {children}
      </Box>
    </Layer>
  );
};

export default SimpleModal;
