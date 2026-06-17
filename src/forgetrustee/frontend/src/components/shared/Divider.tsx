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

import { Box } from "grommet";
import type { BackgroundType } from "grommet/utils";
import React from "react";

type DividerProps = {
  background?: BackgroundType | undefined;
};

const Divider: React.FC<DividerProps> = ({ background }) => (
  <Box
    width="100%"
    height="1px"
    background={background ? background : "border-light"}
    margin={{ vertical: "xsmall" }}
    style={{ minHeight: 1 }}
  />
);

export default Divider;
