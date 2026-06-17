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

import { Box, Button, Grid, Heading, Text } from "grommet";
import SimpleModal from "../../shared/SimpleModal";
import type { ShaclReportDTO, ShaclResultDTO } from "../../../types/generated";

type ModelingResultShaclReportModalProps = {
  open: boolean;
  report?: ShaclReportDTO;
  onClose: () => void;
};

const formatValue = (value: unknown): string => {
  if (value === null || value === undefined || value === "") {
    return "—";
  }

  if (Array.isArray(value)) {
    return value.length > 0 ? value.join(", ") : "—";
  }

  if (typeof value === "boolean") {
    return value ? "Ja" : "Nein";
  }

  return String(value);
};

const summarizeResult = (result: ShaclResultDTO, index: number) => ({
  id: `${result.focusNode ?? "focus"}-${result.path ?? "path"}-${index}`,
  severity: formatValue(result.severity),
  message: formatValue(result.message),
  path: formatValue(result.path),
  focusNode: formatValue(result.focusNode),
  value: formatValue(result.value),
  sourceConstraintComponent: formatValue(result.sourceConstraintComponent),
  sourceShape: formatValue(result.sourceShape),
});

const ModelingResultShaclReportModal = ({
  open,
  report,
  onClose,
}: ModelingResultShaclReportModalProps) => {
  const results = (report?.results ?? []).map(summarizeResult);

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
              SHACL-Report
            </Heading>
            <Text color="text-weak">Validierungsergebnisse aus dem Feld shaclReport</Text>
          </Box>
          <Button
            label="Schließen"
            onClick={onClose}
          />
        </Box>

        <Grid
          columns={{ count: 2, size: "auto" }}
          gap="small"
        >
          <Text color="text-weak">SHACL-konform</Text>
          <Text weight="bold">{formatValue(report?.conforms)}</Text>
          <Text color="text-weak">Anzahl Ergebnisse</Text>
          <Text>{formatValue(report?.resultsCount ?? results.length)}</Text>
        </Grid>

        {results.length === 0 ? (
          <Text color="text-weak">Keine SHACL-Detailergebnisse vorhanden.</Text>
        ) : (
          <Box gap="small">
            {results.map((item) => (
              <Box
                key={item.id}
                pad="medium"
                round="small"
                background="background-contrast"
                gap="xsmall"
                height={{ min: "auto" }}
              >
                {item.severity !== "—" ? (
                  <Text
                    weight="bold"
                    color="status-warning"
                  >
                    Severity: {item.severity}
                  </Text>
                ) : null}
                <Text>{item.message}</Text>
                <Text
                  size="small"
                  color="text-weak"
                >
                  Pfad: {item.path}
                </Text>
                <Text
                  size="small"
                  color="text-weak"
                >
                  Fokus: {item.focusNode}
                </Text>
                <Text
                  size="small"
                  color="text-weak"
                >
                  Wert: {item.value}
                </Text>
                <Text
                  size="small"
                  color="text-weak"
                >
                  Constraint: {item.sourceConstraintComponent}
                </Text>
                <Text
                  size="small"
                  color="text-weak"
                >
                  Shape: {item.sourceShape}
                </Text>
              </Box>
            ))}
          </Box>
        )}
      </Box>
    </SimpleModal>
  );
};

export default ModelingResultShaclReportModal;
