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

import { Box, Button, Heading, Notification, Spinner, Text } from "grommet";
import { useState } from "react";
import SimpleModal from "../../../shared/SimpleModal";

type ModalStartModelingProps = {
  open: boolean;
  onClose: () => void;
  onConfirm?: () => Promise<void>;
  isSubmitting?: boolean;
  errorMessage?: string | null;
};

const ModalStartModeling = (props: ModalStartModelingProps) => {
  const { open, onClose, onConfirm, isSubmitting = false, errorMessage } = props;
  const [localErrorMessage, setLocalErrorMessage] = useState<string | null>(null);

  const buttonsDisabled = isSubmitting;
  const resolvedErrorMessage = errorMessage?.trim() || localErrorMessage;

  return (
    <SimpleModal
      open={open}
      onClose={() => {
        setLocalErrorMessage(null);
        onClose();
      }}
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
          Datentreuhandmodell modellieren
        </Heading>
        <Text>
          Beim Abschicken werden das Modell, die Validierung und die Executive Summary berechnet.
          Danach werden Sie automatisch zur Detailansicht weitergeleitet.
        </Text>
        {isSubmitting && (
          <Box
            direction="row"
            gap="small"
            align="center"
          >
            <Spinner size="small" />
            <Text size="small">Berechnung läuft ...</Text>
          </Box>
        )}
        {resolvedErrorMessage && (
          <Notification
            status="critical"
            title="Fehler"
            message={resolvedErrorMessage}
          />
        )}
        <Box
          direction="row"
          gap="medium"
          margin={{ top: "medium" }}
        >
          <Button
            label={isSubmitting ? "Berechne ..." : "Bestätigen"}
            primary
            onClick={async () => {
              setLocalErrorMessage(null);
              if (onConfirm) {
                try {
                  await onConfirm();
                } catch (error) {
                  if (error instanceof Error && error.message.trim().length > 0) {
                    setLocalErrorMessage(error.message);
                  } else {
                    setLocalErrorMessage("Ein unbekannter Fehler ist aufgetreten.");
                  }
                }
                return;
              }
              onClose();
            }}
            fill="horizontal"
            disabled={buttonsDisabled}
          />
          <Button
            label="Abbrechen"
            onClick={() => {
              setLocalErrorMessage(null);
              onClose();
            }}
            fill="horizontal"
            disabled={buttonsDisabled}
          />
        </Box>
      </Box>
    </SimpleModal>
  );
};

export default ModalStartModeling;
