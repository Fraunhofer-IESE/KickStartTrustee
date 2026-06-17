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

import { Box, Text, Tip } from "grommet";

type InfoTipProps = {
  message: string;
};

/**
 * A component that displays an info tip icon with additional information when hovered over or clicked.
 * @returns
 */
const InfoTip = ({ message }: InfoTipProps) => {
  return (
    <Tip
      content={
        <Box
          pad={{ left: "small" }}
          align="center"
          justify="center"
        >
          <Text>{message}</Text>
        </Box>
      }
    />
  );
};

export default InfoTip;
