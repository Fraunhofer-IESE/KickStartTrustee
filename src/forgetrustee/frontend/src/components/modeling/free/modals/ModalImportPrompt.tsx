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
import SimpleModal from "../../../shared/SimpleModal";
import type { LocalStoragePayload } from "../../../../types/localStoragePayload";

type ModalImportPromptProps = {
  open: boolean;
  onClose: () => void;
  onImport: () => void;
  payload: LocalStoragePayload | null;
};

const ModalImportPrompt = (props: ModalImportPromptProps) => {
  const { open, onClose, onImport, payload } = props;

  const fmt = (iso?: string) => {
    if (!iso) return "-";
    try {
      return new Date(iso).toLocaleString("de-DE");
    } catch {
      return iso;
    }
  };
  const name = payload?.formValues?.core?.dataTrusteeName ?? "";

  const created: string | undefined =
    payload?.createdAt ?? payload?.savedAt ?? undefined;
  const modified: string | undefined = payload?.savedAt ?? undefined;

  return (
    <SimpleModal
      open={open}
      onClose={onClose}
      background="background-back"
      modal={true}
    >
      <Box
        direction="column"
        gap="medium"
      >
        <Heading
          level={3}
          margin="none"
        >
          Gespeicherte Daten gefunden
        </Heading>
        <Box>
          <Text>
            Es wurden zuvor gespeicherte Modellierungsdaten im Browser gefunden.
            Möchten Sie diese jetzt importieren?
          </Text>
          <Box margin={{ top: "small" }}>
            <Text size="small">{`Name: ${name || "(nicht gesetzt)"}`}</Text>
            <Text size="small">{`Erstellt: ${fmt(created)}`}</Text>
            <Text size="small">{`Zuletzt geändert: ${fmt(modified)}`}</Text>
          </Box>
        </Box>
        <Box
          direction="row"
          gap="medium"
          justify="between"
          margin={{ top: "medium" }}
        >
          <Button
            label="Importieren"
            primary
            onClick={() => {
              onImport();
              onClose();
            }}
            fill="horizontal"
          />
          <Button
            label="Nicht importieren"
            onClick={onClose}
            fill="horizontal"
          />
        </Box>
      </Box>
    </SimpleModal>
  );
};

export default ModalImportPrompt;
