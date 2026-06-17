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

import { Box, Notification } from "grommet";
import type { MetadataHintDto } from "../../../types/generated";
import type { FieldValue } from "../../../types/fieldValue";
import { isFieldVisible } from "./visibility";

type MetadataHintsProps = {
  hints?: MetadataHintDto[];
  values?: Record<string, FieldValue | undefined>;
};

const MetadataHints = ({ hints, values = {} }: MetadataHintsProps) => {
  const normalizedHints = (hints ?? []).filter(
    (hint) =>
      Boolean(hint?.text?.trim()) &&
      isFieldVisible({ visibleBy: hint.visibleBy }, values),
  );

  if (normalizedHints.length === 0) {
    return null;
  }

  return (
    <Box gap="small">
      {normalizedHints.map((hint) => (
        <Notification
          key={hint.name || hint.text}
          status="info"
          title="Hinweis"
          message={hint.text}
        />
      ))}
    </Box>
  );
};

export default MetadataHints;
