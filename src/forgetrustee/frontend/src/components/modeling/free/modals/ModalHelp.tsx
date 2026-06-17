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

type ModalHelpStep1Props = {
  open: boolean;
  onClose: () => void;
};

const ModalHelp = (props: ModalHelpStep1Props) => {
  const { open, onClose } = props;

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
          Datentreuhandmodell modellieren
        </Heading>
        <Box>
          <Text>
            Die freie Modelierung empfielt sich, wenn sie bereits eine Vision
            ihres Datentreuhandmodels besitzen. Wählen Sie in den verschiedenen
            Kategorien ihre gewünschte Auswahl. Klicken Sie anschließend auf
            Modellieren, um Ihr Datentreuhandmodell im nächsten Schritt zu
            überprüfen, herunterzuladen oder zur Prüfung einzureichen.
          </Text>
        </Box>
        <Heading
          level={3}
          margin="none"
        >
          Modell und Grafik importieren
        </Heading>
        <Box>
          <Text>
            Sie können ein bereits erstelltes Datentreuhandmodell als Vorlage
            verwenden.
          </Text>
          <Text>
            Als Grafik können Sie z.B. ein Logo importieren, welches in der
            Übersicht angezeigt wird. Sollten Sie ihr Modell freigeben, wird
            diese Grafik für alle Nutzer sichtbar.
          </Text>
        </Box>
        <Box
          direction="row"
          gap="medium"
          justify="center"
          margin={{ top: "medium" }}
        >
          <Button
            label="OK"
            onClick={onClose}
            primary
          />
        </Box>
      </Box>
    </SimpleModal>
  );
};

export default ModalHelp;
