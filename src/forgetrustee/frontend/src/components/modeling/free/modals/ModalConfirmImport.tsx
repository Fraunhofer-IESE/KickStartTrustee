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

type ModalConfirmImportProps = {
  open: boolean;
  onClose: () => void;
  onAccept: () => void;
};

const ModalConfirmImport = (props: ModalConfirmImportProps) => {
  const { open, onClose, onAccept } = props;

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
          Modellierung zurücksetzen?
        </Heading>
        <Box>
          <Text>
            Durch Zurücksetzen werden Ihre bisherigen Auswahlen aufgehoben. Sollten Sie ihre
            bisherigen Auswahlen speichern wollen, können Sie diese herunterladen.
          </Text>
        </Box>
        <Box
          direction="row"
          gap="medium"
          justify="between"
          margin={{ top: "medium" }}
        >
          <Button
            label="Zurücksetzen"
            primary
            onClick={() => {
              onAccept();
              onClose();
            }}
            fill="horizontal"
          />
          <Button
            label="Abbrechen"
            onClick={onClose}
            fill="horizontal"
          />
        </Box>
      </Box>
    </SimpleModal>
  );
};

export default ModalConfirmImport;