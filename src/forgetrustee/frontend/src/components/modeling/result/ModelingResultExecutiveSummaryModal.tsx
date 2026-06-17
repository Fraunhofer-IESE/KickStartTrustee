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
import SimpleModal from "../../shared/SimpleModal";
import HtmlContent from "../../shared/HtmlContent";

type ModelingResultExecutiveSummaryModalProps = {
  open: boolean;
  executiveSummary?: string | null;
  onClose: () => void;
};

const ModelingResultExecutiveSummaryModal = ({
  open,
  executiveSummary,
  onClose,
}: ModelingResultExecutiveSummaryModalProps) => {
  return (
    <SimpleModal
      open={open}
      onClose={onClose}
      modal={true}
      width="960px"
      margin="large"
    >
      <Box gap="medium">
        <Box
          direction="row"
          justify="between"
          gap="medium"
          align="center"
        >
          <Box gap="xsmall">
            <Heading
              level={3}
              margin="none"
            >
              Executive Summary
            </Heading>
            <Text color="text-weak">Die Zusammenfassung des Modells im HTML-Format.</Text>
          </Box>
          <Button
            label="Schließen"
            onClick={onClose}
          />
        </Box>

        {executiveSummary ? (
          <Box
            background="background"
            pad="medium"
            round="small"
            overflow="auto"
          >
            <HtmlContent sanitize={false}>{executiveSummary}</HtmlContent>
          </Box>
        ) : (
          <Text
            color="text-weak"
            size="small"
          >
            Keine Executive Summary vorhanden.
          </Text>
        )}
      </Box>
    </SimpleModal>
  );
};

export default ModelingResultExecutiveSummaryModal;
