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

import { Box, Button, Grid, Heading, Image, Text } from "grommet";
import placeholder from "../../../assets/placeholder.svg";
import type { DataTrusteeWizardBuildResponseDto, ShaclResultDTO } from "../../../types/generated";
import { unwrapCommentedString } from "../../shared/commentedValue";

type ModelingResultDetailViewProps = {
  result: DataTrusteeWizardBuildResponseDto;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
  onBack: () => void;
};

const formatValue = (value: unknown): string => {
  if (value === null || value === undefined || value === "") {
    return "—";
  }

  if (Array.isArray(value)) {
    const items = value
      .map((item) => unwrapCommentedString(item) ?? String(item).trim())
      .filter((item) => Boolean(item));

    return items.length > 0 ? items.join(", ") : "—";
  }

  if (typeof value === "boolean") {
    return value ? "Ja" : "Nein";
  }

  return unwrapCommentedString(value) || String(value);
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

const ModelingResultDetailView = ({
  result,
  avatarDataUrl,
  avatarFileName,
  onBack,
}: ModelingResultDetailViewProps) => {
  const input = result.dataTrusteeModel;
  const core = input?.core;
  const shaclReport = result.shaclReport;
  const shaclResults = (shaclReport?.results ?? []).map(summarizeResult);
  const trusteeName = unwrapCommentedString(core?.dataTrusteeName) || "Modellierungsdetails";

  return (
    <Box
      gap="large"
      animation={{ type: "fadeIn", duration: 300 }}
    >
      <Box
        direction="row"
        justify="between"
        align="center"
        wrap
        gap="medium"
      >
        <Box gap="xsmall">
          <Heading
            level={2}
            margin="none"
          >
            {trusteeName}
          </Heading>
          <Text color="text-weak">
            Detailansicht der vom Backend erzeugten Modellierungsantwort
          </Text>
        </Box>
        <Button
          label="Zurück zur Modellierung"
          onClick={onBack}
        />
      </Box>

      <Grid
        columns={{ count: 2, size: ["small", "flex"] }}
        gap="medium"
      >
        <Box
          background="background-front"
          round="small"
          pad="medium"
          gap="small"
        >
          <Image
            src={avatarDataUrl || placeholder}
            fit="contain"
            fill="horizontal"
            style={{ borderRadius: 12 }}
          />
          <Text weight="bold">Avatar</Text>
          <Text
            size="small"
            color="text-weak"
          >
            {avatarFileName || "Kein Avatar gespeichert"}
          </Text>
        </Box>

        <Box
          background="background-front"
          round="small"
          pad="medium"
          gap="medium"
        >
          <Heading
            level={3}
            margin="none"
          >
            Stammdaten
          </Heading>
          <Grid
            columns={{ count: 2, size: "auto" }}
            gap="small"
          >
            <Text color="text-weak">Data Trustee</Text>
            <Text>{formatValue(core?.dataTrusteeName)}</Text>
            <Text color="text-weak">Betreiber</Text>
            <Text>{formatValue(core?.dataTrusteeOperator)}</Text>
            <Text color="text-weak">Rechteinhaber</Text>
            <Text>{formatValue(core?.rightsHolderName)}</Text>
            <Text color="text-weak">Datenkategorie</Text>
            <Text>{formatValue(core?.dataCategoryName)}</Text>
          </Grid>
          <Box gap="xsmall">
            <Text color="text-weak">Beschreibung</Text>
            <Text>{formatValue(core?.dataTrusteeDescription)}</Text>
          </Box>
        </Box>
      </Grid>

      <Box
        background="background-front"
        round="small"
        pad="medium"
        gap="medium"
      >
        <Heading
          level={3}
          margin="none"
        >
          SHACL-Report
        </Heading>
        <Text color="text-weak">Validierungsergebnisse aus dem Feld shaclReport</Text>
        <Grid
          columns={{ count: 2, size: "auto" }}
          gap="small"
        >
          <Text color="text-weak">SHACL-konform</Text>
          <Text weight="bold">{formatValue(shaclReport?.conforms)}</Text>
          <Text color="text-weak">Anzahl Ergebnisse</Text>
          <Text>{formatValue(shaclReport?.resultsCount ?? shaclResults.length)}</Text>
        </Grid>

        {shaclResults.length === 0 ? (
          <Text color="text-weak">Keine Detailergebnisse vorhanden.</Text>
        ) : (
          <Box gap="small">
            {shaclResults.map((item) => (
              <Box
                key={item.id}
                pad="medium"
                round="small"
                background="background-contrast"
                gap="xsmall"
              >
                <Text weight="bold">{item.severity}</Text>
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

      <Box
        background="background-front"
        round="small"
        pad="medium"
        gap="medium"
      >
        <Heading
          level={3}
          margin="none"
        >
          TTL-Ausgabe
        </Heading>
        <Box
          as="pre"
          margin="none"
          pad="medium"
          round="xsmall"
          background="background-contrast"
          style={{
            overflowX: "auto",
            whiteSpace: "pre-wrap",
            wordBreak: "break-word",
            maxHeight: "32rem",
          }}
        >
          <Text>{result.ttl || "Keine TTL-Ausgabe vorhanden."}</Text>
        </Box>
      </Box>
    </Box>
  );
};

export default ModelingResultDetailView;
