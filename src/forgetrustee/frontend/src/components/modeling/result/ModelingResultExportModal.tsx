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

import { Box, Button, Heading, List, Text } from "grommet";
import { MODEL_EXPORT_FILE_NAMES } from "../common/modelExportZip";
import SimpleModal from "../../shared/SimpleModal";

type ModelingResultExportModalProps = {
  open: boolean;
  onClose: () => void;
  onConfirm: () => void;
  isProcessing?: boolean;
};

const ModelingResultExportModal = ({
  open,
  onClose,
  onConfirm,
  isProcessing = false,
}: ModelingResultExportModalProps) => {
  const exportItems = [
    {
      primary: "Formulardaten",
      secondary: MODEL_EXPORT_FILE_NAMES.formData,
    },
    {
      primary: "Datentreuhandmodell im JSON-Format",
      secondary: MODEL_EXPORT_FILE_NAMES.resultData,
    },
    {
      primary: "Executive Summary im HTML-Format",
      secondary: MODEL_EXPORT_FILE_NAMES.executiveSummary,
    },
    {
      primary: "TTL-Datei des Modells",
      secondary: MODEL_EXPORT_FILE_NAMES.ttl,
    },
    {
      primary: "SHACL-Validierung im JSON-Format",
      secondary: MODEL_EXPORT_FILE_NAMES.validation,
    },
  ];

  return (
    <SimpleModal
      open={open}
      onClose={onClose}
      modal={true}
    >
      <Box gap="medium">
        <Heading
          level={3}
          margin="none"
        >
          Hinweis zum Export
        </Heading>
        <Text>Der Export enthält diese Dateien als ZIP:</Text>
        <Box>
          <List
            data={exportItems}
            primaryKey="primary"
            secondaryKey="secondary"
          />
        </Box>
        <Box
          direction="row"
          justify="between"
          gap="small"
          margin={{ top: "small" }}
        >
          <Button
            label="Abbrechen"
            onClick={onClose}
            disabled={isProcessing}
          />
          <Button
            primary
            label="Export starten"
            disabled={isProcessing}
            onClick={onConfirm}
            busy={isProcessing}
          />
        </Box>
      </Box>
    </SimpleModal>
  );
};

export default ModelingResultExportModal;
