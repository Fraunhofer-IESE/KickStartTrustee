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

import { useEffect, useState } from "react";
import { Box, Button, FileInput, Form, FormField, Heading, Image, Text } from "grommet";
import SimpleModal from "../../../shared/SimpleModal";
import LabeledInputBox from "../../../shared/LabeledInputBox";

type ModalAvatarUploadProps = {
  open: boolean;
  avatarDataUrl: string | null;
  avatarFileName?: string | null;
  onClose: () => void;
  onConfirm?: (payload: { file: File | null; dataUrl: string | null; cleared?: boolean }) => void;
};

const formatFileSizeKb = (bytes: number) => {
  const kb = bytes / 1024;
  return `${kb.toFixed(kb >= 100 ? 0 : 1)} kB`;
};

const ModalAvatarUpload = (props: ModalAvatarUploadProps) => {
  const MAX_FILE_SIZE = 500 * 1024;
  const ACCEPTED_MIME_TYPES = ["image/png", "image/jpeg", "image/jpg", "image/webp"];
  const { open, avatarDataUrl, avatarFileName, onClose, onConfirm } = props;
  const [file, setFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [dataUrl, setDataUrl] = useState<string | null>(null);
  const [removeRequested, setRemoveRequested] = useState(false);
  const [validationError, setValidationError] = useState<string | null>(null);

  const resetState = () => {
    setFile(null);
    setPreviewUrl(null);
    setDataUrl(null);
    setRemoveRequested(false);
    setValidationError(null);
  };

  const validateFile = (pickedFile: File | null) => {
    if (!pickedFile) {
      return null;
    }
    if (pickedFile.size > MAX_FILE_SIZE) {
      return `Die Datei überschreitet die maximale Größe von ${formatFileSizeKb(MAX_FILE_SIZE)}.`;
    }
    if (!ACCEPTED_MIME_TYPES.includes(pickedFile.type)) {
      return "Ungültiges Dateiformat. Erlaubt: png, jpg, jpeg, webp.";
    }
    return null;
  };

  useEffect(() => {
    if (!file) return undefined;

    const objectUrl = URL.createObjectURL(file);
    setPreviewUrl(objectUrl);
    const reader = new FileReader();
    reader.onload = () => setDataUrl(typeof reader.result === "string" ? reader.result : null);
    reader.readAsDataURL(file);
    return () => URL.revokeObjectURL(objectUrl);
  }, [file]);

  useEffect(() => {
    if (open) {
      resetState();
      setPreviewUrl(avatarDataUrl ?? null);
      setDataUrl(avatarDataUrl ?? null);
    }
  }, [avatarDataUrl, open]);

  const handleConfirm = () => {
    if (removeRequested) {
      onConfirm?.({ file: null, dataUrl: null, cleared: true });
    } else {
      onConfirm?.({ file, dataUrl });
    }
    onClose();
  };

  const handleClear = () => {
    setFile(null);
    setPreviewUrl(null);
    setDataUrl(null);
    setValidationError(null);
    setRemoveRequested(true);
  };

  const hasPendingChange = removeRequested || !!file;

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
            Bild hochladen
          </Heading>
          <Box>
            <Text>
              Wählen Sie ein Bild für das Profil aus. Unterstützte Formate: png, jpg, jpeg, webp.
            </Text>
          </Box>

          <FormField
            name="avatarUploadFile"
            required
            error={validationError ?? undefined}
          >
            <LabeledInputBox label="Bild hochladen">
              <FileInput
                id="avatarUploadFile"
                name="avatarUploadFile"
                accept="image/png,image/jpeg,image/jpg,image/webp"
                messages={{
                  browse: "Datei auswählen",
                  dropPrompt: "Datei hierhin ziehen",
                  files: "Dateien",
                  alert: {
                    maxSize: `Die Datei überschreitet die maximale Größe von {maxSize}.`,
                  },
                }}
                multiple={false}
                plain
                onChange={(event) => {
                  const picked = event?.target?.files?.[0] ?? null;
                  const error = validateFile(picked);
                  setValidationError(error);
                  if (error) {
                    setFile(null);
                    return;
                  }
                  setFile(picked);
                  setRemoveRequested(false);
                }}
                maxSize={MAX_FILE_SIZE}
              />
              {file && (
                <Text
                  size="small"
                  color="text-weak"
                  margin={{ top: "xsmall" }}
                >
                  Ausgewählt: {file.name} ({formatFileSizeKb(file.size)})
                </Text>
              )}
              {!file && avatarFileName && previewUrl && (
                <Text
                  size="small"
                  color="text-weak"
                  margin={{ top: "xsmall" }}
                >
                  Aktuell gesetzt: {avatarFileName}
                </Text>
              )}
            </LabeledInputBox>
          </FormField>

          {previewUrl && (
            <Box
              pad="small"
              border={{ color: "border" }}
              round="xsmall"
              background="background-front"
              height={{ max: "small" }}
            >
              <Image
                fit="contain"
                src={previewUrl}
                height="small"
              />
            </Box>
          )}

          {removeRequested && (
            <Text
              size="small"
              color="status-warning"
            >
              Das aktuelle Bild wird entfernt, sobald Sie auf Übernehmen klicken.
            </Text>
          )}

          <Box
            justify="center"
            margin={{ top: "medium" }}
            direction="row"
            gap="small"
          >
            <Button
              label="Übernehmen"
              primary
              onClick={handleConfirm}
              disabled={!hasPendingChange}
            />
            <Button
              label="Bild entfernen"
              onClick={handleClear}
              secondary
              disabled={!previewUrl}
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

export default ModalAvatarUpload;
