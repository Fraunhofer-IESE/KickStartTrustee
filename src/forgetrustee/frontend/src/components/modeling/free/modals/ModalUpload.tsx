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

import { Box, Button, FileInput, Form, FormField, Heading, Select, Text } from "grommet";
import { useEffect, useMemo, useState } from "react";
import JSZip from "jszip";
import {
  getDataTrusteeModelAvatar,
  getDataTrusteeModelPackageById,
  useGetAllDataTrusteeModels,
} from "../../../../api/generated/data-trustee-model-controller/data-trustee-model-controller";
import Divider from "../../../shared/Divider";
import SimpleModal from "../../../shared/SimpleModal";
import LabeledInputBox from "../../../shared/LabeledInputBox";
import { createEmptyDataTrusteeModelDraft } from "../modelingFreeUtils";
import {
  normalizeBusinessFinancingTypesForUi,
  normalizeDataProcessingBasesForUi,
} from "../legal/legalProcessingBases";
import { MODEL_EXPORT_FILE_NAMES } from "../../common/modelExportZip";
import type { LocalStoragePayload } from "../../../../types/localStoragePayload";
import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import type {
  DataTrusteeModelResponseDto,
  DataTrusteeWizardBuildResponseDto,
  DataTrusteeModelSummaryDto,
  DataTrusteeWizardRequestDto,
  ShaclReportDTO,
} from "../../../../types/generated";

type ModalUploadProps = {
  open: boolean;
  onClose: () => void;
  onConfirm?: (payload: LocalStoragePayload) => void;
};

const MAX_FILE_SIZE = 1000 * 1024;

const findZipEntry = (zip: JSZip, fileName: string) => {
  const target = fileName.toLowerCase();
  return (
    zip.file(fileName) ??
    Object.values(zip.files).find(
      (entry) => !entry.dir && entry.name.toLowerCase().endsWith(target),
    )
  );
};

const getEntryFileName = (entryName: string): string => {
  const normalized = entryName.replace(/\\/g, "/");
  const parts = normalized.split("/");
  return parts[parts.length - 1] || entryName;
};

const isImageFileName = (fileName: string): boolean =>
  /\.(png|jpe?g|webp|gif|svg)$/i.test(fileName);

const mimeTypeForImageFile = (fileName: string): string => {
  if (/\.png$/i.test(fileName)) return "image/png";
  if (/\.jpe?g$/i.test(fileName)) return "image/jpeg";
  if (/\.webp$/i.test(fileName)) return "image/webp";
  if (/\.gif$/i.test(fileName)) return "image/gif";
  if (/\.svg$/i.test(fileName)) return "image/svg+xml";
  return "application/octet-stream";
};

const blobToDataUrl = (blob: Blob): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(String(reader.result ?? ""));
    reader.onerror = () => reject(new Error("Das Avatar-Bild konnte nicht gelesen werden."));
    reader.readAsDataURL(blob);
  });

type CatalogModelOption = {
  label: string;
  modelId: string;
  description: string;
};

const toCatalogImportPayload = async (
  modelId: string,
  packageData: DataTrusteeWizardBuildResponseDto,
): Promise<LocalStoragePayload> => {
  const requestDraft = (packageData.request ??
    createEmptyDataTrusteeModelDraft()) as DataTrusteeModelDraft;
  const avatarBlob = await getDataTrusteeModelAvatar(modelId);

  return {
    formValues: normalizeBusinessFinancingTypesForUi(
      normalizeDataProcessingBasesForUi(requestDraft),
    ),
    avatarDataUrl: avatarBlob ? await blobToDataUrl(avatarBlob) : null,
    avatarFileName: null,
    modelBuildResult: packageData,
    hasPendingChanges: false,
  };
};

const toImportPayload = (parsed: unknown): LocalStoragePayload => {
  if (typeof parsed !== "object" || parsed === null) {
    throw new Error("Ungültiges Dateiformat");
  }

  const maybePayload = parsed as Partial<LocalStoragePayload>;
  if (
    maybePayload.formValues &&
    typeof maybePayload.formValues === "object" &&
    maybePayload.formValues !== null
  ) {
    return {
      formValues: maybePayload.formValues,
      avatarDataUrl: maybePayload.avatarDataUrl ?? null,
      avatarFileName: maybePayload.avatarFileName ?? null,
      modelBuildResult: maybePayload.modelBuildResult ?? null,
      hasPendingChanges: maybePayload.hasPendingChanges ?? true,
      createdAt: maybePayload.createdAt,
      savedAt: maybePayload.savedAt,
    };
  }

  // Supports plain exported form data (e.g. formData.json from ZIP export)
  return {
    formValues: parsed as LocalStoragePayload["formValues"],
    avatarDataUrl: null,
    avatarFileName: null,
    modelBuildResult: null,
    hasPendingChanges: true,
  };
};

const parseZipPayload = async (file: File): Promise<LocalStoragePayload> => {
  const zip = await JSZip.loadAsync(file);

  const formDataEntry =
    findZipEntry(zip, MODEL_EXPORT_FILE_NAMES.formData) ??
    findZipEntry(zip, MODEL_EXPORT_FILE_NAMES.legacyFormData);

  if (!formDataEntry) {
    throw new Error("ZIP enthält keine formData.json oder wizard.json");
  }

  const formDataText = await formDataEntry.async("string");
  const parsedFormData = JSON.parse(formDataText);
  const payload = toImportPayload(parsedFormData);

  const resultDataEntry =
    findZipEntry(zip, MODEL_EXPORT_FILE_NAMES.resultData) ??
    findZipEntry(zip, MODEL_EXPORT_FILE_NAMES.legacyResultData);
  let parsedResultData: unknown;
  if (resultDataEntry) {
    const resultDataText = await resultDataEntry.async("string");
    parsedResultData = JSON.parse(resultDataText);
  }

  const ttlEntry = findZipEntry(zip, MODEL_EXPORT_FILE_NAMES.ttl);
  const validationEntry = findZipEntry(zip, MODEL_EXPORT_FILE_NAMES.validation);
  const imageEntries = Object.values(zip.files).filter(
    (entry) => !entry.dir && isImageFileName(getEntryFileName(entry.name)),
  );
  const avatarEntry =
    imageEntries.find((entry) => /^avatar(\.|$)/i.test(getEntryFileName(entry.name))) ??
    imageEntries[0];

  let ttl: string | undefined;
  if (ttlEntry) {
    ttl = await ttlEntry.async("string");
  }

  let shaclReport: ShaclReportDTO | undefined;
  if (validationEntry) {
    const validationText = await validationEntry.async("string");
    shaclReport = JSON.parse(validationText) as ShaclReportDTO;
  }

  if (ttl || shaclReport) {
    const modelBuildResult: DataTrusteeWizardBuildResponseDto = {
      dataTrusteeModel: (parsedResultData ?? payload.formValues) as DataTrusteeModelResponseDto,
      request: payload.formValues as unknown as DataTrusteeWizardRequestDto,
      ttl,
      shaclReport,
    };
    payload.modelBuildResult = modelBuildResult;
  }

  if (avatarEntry) {
    const avatarFileName = getEntryFileName(avatarEntry.name);
    const mimeType = mimeTypeForImageFile(avatarFileName);
    const avatarBase64 = await avatarEntry.async("base64");
    payload.avatarDataUrl = `data:${mimeType};base64,${avatarBase64}`;
    payload.avatarFileName = avatarFileName;
  }

  return payload;
};

const ModalUpload = (props: ModalUploadProps) => {
  const { open, onClose, onConfirm } = props;

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [selectedCatalogModelId, setSelectedCatalogModelId] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const [isCatalogLoading, setIsCatalogLoading] = useState(false);

  const {
    data: catalogModels,
    isPending: isCatalogModelsPending,
    error: catalogModelsError,
  } = useGetAllDataTrusteeModels({
    query: {
      enabled: open,
    },
  });

  useEffect(() => {
    if (!open) {
      return;
    }

    setSelectedFile(null);
    setSelectedCatalogModelId("");
    setError(null);
  }, [open]);

  const catalogOptions = useMemo<CatalogModelOption[]>(
    () =>
      (catalogModels ?? [])
        .filter((model): model is DataTrusteeModelSummaryDto & { modelId: string } =>
          Boolean(model.modelId),
        )
        .map((model) => ({
          modelId: model.modelId,
          label: model.name?.trim() || model.modelId,
          description: model.description?.trim() || "Keine Beschreibung",
        })),
    [catalogModels],
  );

  const selectedCatalogModel =
    catalogOptions.find((model) => model.modelId === selectedCatalogModelId) ?? null;

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files: FileList | null = event?.target?.files ?? null;
    if (!files || files.length === 0) {
      setSelectedFile(null);
      return;
    }
    setSelectedFile(files[0]);
    setSelectedCatalogModelId("");
    setError(null);
  };

  const handleCatalogChange = (nextOption: CatalogModelOption | null) => {
    setSelectedCatalogModelId(nextOption?.modelId ?? "");
    setSelectedFile(null);
    setError(null);
  };

  const handleCatalogImport = async () => {
    if (!selectedCatalogModelId) {
      setError("Bitte wählen Sie ein Modell aus dem Katalog aus.");
      return;
    }

    setIsCatalogLoading(true);
    try {
      const packageData = await getDataTrusteeModelPackageById(selectedCatalogModelId);
      const payload = await toCatalogImportPayload(selectedCatalogModelId, packageData);
      if (onConfirm) onConfirm(payload);
      else onClose();
    } catch (err) {
      console.error("Fehler beim Laden des Katalog-Modells", err);
      setError("Das Modell konnte nicht aus dem Katalog geladen werden.");
    } finally {
      setIsCatalogLoading(false);
    }
  };

  const handlePrimaryImport = async () => {
    if (selectedCatalogModelId) {
      await handleCatalogImport();
      return;
    }

    if (selectedFile) {
      await handleImport();
      return;
    }

    setError("Bitte wählen Sie eine Datei oder ein Katalogmodell aus.");
  };

  const handleImport = async () => {
    if (!selectedFile) {
      setError("Bitte wählen Sie eine Datei aus.");
      return;
    }
    if (selectedFile.size > MAX_FILE_SIZE) {
      setError("Die Datei überschreitet die maximale Größe.");
      return;
    }

    const isZip = selectedFile.name.toLowerCase().endsWith(".zip");

    if (isZip) {
      try {
        const payload = await parseZipPayload(selectedFile);
        if (onConfirm) onConfirm(payload);
        else onClose();
      } catch (err) {
        console.error("Fehler beim Einlesen der Datei", err);
        setError("Die ZIP-Datei konnte nicht gelesen werden.");
      }
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      try {
        const text = String(reader.result ?? "");
        const parsed = JSON.parse(text);
        const payload = toImportPayload(parsed);
        if (onConfirm) onConfirm(payload);
        else onClose();
      } catch (err) {
        console.error("Fehler beim Einlesen der Datei", err);
        setError("Die JSON-Datei konnte nicht gelesen oder geparst werden.");
      }
    };
    reader.onerror = (err) => {
      console.error("FileReader error", err);
      setError("Fehler beim Lesen der Datei.");
    };
    reader.readAsText(selectedFile);
  };

  return (
    <SimpleModal
      open={open}
      onClose={onClose}
      background="background-back"
      modal={true}
    >
      <Form validate="change">
        <Box
          direction="column"
          gap="medium"
        >
          <Heading
            level={3}
            margin="none"
          >
            Modell hochladen
          </Heading>
          <Box>
            <Text>
              Bitte importieren Sie ein bereits erstelltes Datentreuhandmodell (Format .json oder
              .zip) oder verwenden Sie ein Modell aus dem Katalog als Vorlage.
            </Text>
            <Text
              size="small"
              color="text-weak"
              margin={{ top: "xsmall" }}
            >
              Für ZIP-Import werden {MODEL_EXPORT_FILE_NAMES.formData} oder (legacy){" "}
              {MODEL_EXPORT_FILE_NAMES.legacyFormData} unterstützt. Optional:{" "}
              {MODEL_EXPORT_FILE_NAMES.resultData}/{MODEL_EXPORT_FILE_NAMES.legacyResultData},{" "}
              {MODEL_EXPORT_FILE_NAMES.ttl}, {MODEL_EXPORT_FILE_NAMES.validation} sowie ein
              Avatar-Bild.
            </Text>
          </Box>

          <FormField
            name="dtfile"
            required
          >
            <LabeledInputBox label="Modell hochladen">
              <FileInput
                name="dtfile"
                id="dtfile"
                disabled={Boolean(selectedCatalogModelId)}
                messages={{
                  browse: "Datei auswählen",
                  dropPrompt: "Datei hierhin ziehen",
                  alert: {
                    maxSize: "Die Datei überschreitet die maximale Größe von 300 KB.",
                  },
                }}
                multiple={false}
                plain
                maxSize={MAX_FILE_SIZE}
                onChange={handleFileChange}
              />
            </LabeledInputBox>
          </FormField>

          <Box
            align="center"
            gap="xsmall"
          >
            <Divider />
            <Text
              size="small"
              color="text-weak"
            >
              oder
            </Text>
            <Divider />
          </Box>

          {error && (
            <Box pad={{ horizontal: "small" }}>
              <Text color="status-error">{error}</Text>
            </Box>
          )}

          {catalogModelsError && (
            <Box pad={{ horizontal: "small" }}>
              <Text color="status-error">Der Katalog konnte nicht geladen werden.</Text>
            </Box>
          )}

          <Box
            direction="row"
            gap="small"
          >
            <Box flex>
              <LabeledInputBox label="Katalog durchsuchen">
                <Select
                  options={catalogOptions}
                  value={selectedCatalogModel}
                  labelKey="label"
                  placeholder={isCatalogModelsPending ? "Katalog wird geladen..." : "Bitte wählen"}
                  disabled={Boolean(selectedFile)}
                  plain
                  onChange={({ option }) =>
                    handleCatalogChange((option as CatalogModelOption) ?? null)
                  }
                />
              </LabeledInputBox>
            </Box>
          </Box>

          {selectedCatalogModel && (
            <Box pad={{ horizontal: "small" }}>
              <Text
                size="small"
                color="text-weak"
              >
                {selectedCatalogModel.description}
              </Text>
            </Box>
          )}

          <Box
            justify="center"
            margin={{ top: "medium" }}
            direction="row"
            gap="small"
          >
            <Button
              label={isCatalogLoading ? "Lädt..." : "Modell importieren"}
              primary
              onClick={handlePrimaryImport}
              disabled={isCatalogLoading || (!selectedCatalogModelId && !selectedFile)}
            />
            <Button
              label="Abbrechen"
              onClick={onClose}
              secondary
            />
          </Box>
        </Box>
      </Form>
    </SimpleModal>
  );
};

export default ModalUpload;
