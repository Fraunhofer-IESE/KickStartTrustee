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

import { Box, Button, CheckBox, Form, FormField, Heading, Text, TextInput } from "grommet";
import { useCallback, useEffect, useState } from "react";
import type {
  DataTrusteeModelUploadRequestDto,
  DataTrusteeWizardBuildResponseDto,
  UploadDataTrusteeModelBody,
} from "../../../types/generated";
import SimpleModal from "../../shared/SimpleModal";
import placeholder from "../../../assets/placeholder.svg";
import { useUploadDataTrusteeModel } from "../../../api/generated/data-trustee-model-controller/data-trustee-model-controller";
import LabeledInputBox from "../../shared/LabeledInputBox";
import { unwrapCommentedString } from "../../shared/commentedValue";

type ModelingResultSubmissionModalProps = {
  open: boolean;
  result: DataTrusteeWizardBuildResponseDto;
  onClose: () => void;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
};

const ModelingResultSubmissionModal = ({
  open,
  result,
  onClose,
  avatarDataUrl = null,
  avatarFileName = null,
}: ModelingResultSubmissionModalProps) => {
  const [isProcessing, setIsProcessing] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [honeypot, setHoneypot] = useState("");
  const [consentGiven, setConsentGiven] = useState(false);
  const [submissionError, setSubmissionError] = useState<string | null>(null);
  const [submissionSuccess, setSubmissionSuccess] = useState<string | null>(null);

  const { mutateAsync: uploadDataTrusteeModel } = useUploadDataTrusteeModel();

  const modelName =
    unwrapCommentedString(result.dataTrusteeModel?.core?.dataTrusteeName) || "Datentreuhandmodell";
  const trimmedName = name.trim();
  const trimmedEmail = email.trim();
  const submissionReady = consentGiven && trimmedName.length > 0 && trimmedEmail.length > 0;

  const resetForm = useCallback(() => {
    setName("");
    setEmail("");
    setHoneypot("");
    setConsentGiven(false);
    setSubmissionError(null);
    setSubmissionSuccess(null);
  }, []);

  useEffect(() => {
    if (!open) {
      resetForm();
    }
  }, [open, resetForm]);

  const resolveImageBlob = useCallback(async (): Promise<Blob> => {
    const source = avatarDataUrl ?? placeholder;
    return fetch(source).then((response) => response.blob());
  }, [avatarDataUrl]);

  const handleClose = useCallback(() => {
    if (isProcessing) {
      return;
    }

    resetForm();
    onClose();
  }, [isProcessing, onClose, resetForm]);

  const handlePrepareSubmission = async () => {
    if (isProcessing || !consentGiven) {
      return;
    }

    if (!trimmedName || !trimmedEmail) {
      setSubmissionError("Name und E-Mail-Adresse sind für die Einreichung erforderlich.");
      return;
    }

    if (!result.request) {
      setSubmissionError("Die Modelldaten konnten nicht geladen werden. Bitte versuche es erneut.");
      return;
    }

    setIsProcessing(true);
    setSubmissionError(null);
    setSubmissionSuccess(null);

    try {
      const request: DataTrusteeModelUploadRequestDto = {
        wizardData: result.request,
        name: trimmedName,
        email: trimmedEmail,
        honeypot,
      };

      const imageBlob = await resolveImageBlob();
      const imageFile = new File([imageBlob], avatarFileName ?? "avatar.svg", {
        type: imageBlob.type || "image/svg+xml",
      });

      const body: UploadDataTrusteeModelBody = {
        request,
        image: imageFile,
      };

      await uploadDataTrusteeModel({ data: body });
      setSubmissionSuccess(
        "Ihr Modell wurde erfolgreich eingereicht. Die Einpflege der Daten wird schnellstmöglich vorgenommen.",
      );
      setName("");
      setEmail("");
      setHoneypot("");
      setConsentGiven(false);
    } catch (error) {
      console.error("Fehler beim Einreichen des Modells", error);
      setSubmissionError("Das Modell konnte nicht an das Backend übermittelt werden.");
    } finally {
      setIsProcessing(false);
    }
  };

  return (
    <SimpleModal
      open={open}
      onClose={handleClose}
      modal={true}
      width="680px"
      margin="large"
    >
      <Form>
        <Box gap="medium">
          <Box gap="xsmall">
            <Heading
              level={3}
              margin="none"
            >
              Zur Freigabe einreichen
            </Heading>
            <Text color="text-weak">{modelName}</Text>
          </Box>

          <Text>
            Für die Einreichung werden das Modell und das Vorschaubild mit übermittelt. Ihren Namen
            und Ihre E-Mail-Adresse sind erforderlich. Die Einwilligung ist ebenfalls erforderlich.
          </Text>

          <Box
            pad="medium"
            round="small"
            background="background-contrast"
          >
            <Text size="small">
              Nach der Prüfung kann das Modell öffentlich angezeigt und zugänglich gemacht werden.
            </Text>
          </Box>

          {submissionSuccess ? (
            <Box
              pad="medium"
              round="small"
              background="status-ok"
            >
              <Text color="white">{submissionSuccess}</Text>
            </Box>
          ) : null}

          <FormField
            name="name"
            required
          >
            <LabeledInputBox label="Name *">
              <TextInput
                name="name"
                value={name}
                onChange={(event) => setName(event.target.value)}
                placeholder="Pflichtfeld"
                required
                plain
                disabled={Boolean(submissionSuccess)}
              />
            </LabeledInputBox>
          </FormField>

          <FormField
            name="email"
            required
          >
            <LabeledInputBox label="E-Mail *">
              <TextInput
                name="email"
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="Pflichtfeld"
                required
                plain
                disabled={Boolean(submissionSuccess)}
              />
            </LabeledInputBox>
          </FormField>

          <Box
            aria-hidden
            height="1px"
            overflow="hidden"
            width="1px"
            style={{ position: "absolute", left: "-10000px", top: "auto" }}
          >
            <TextInput
              name="honeypot"
              value={honeypot}
              tabIndex={-1}
              autoComplete="off"
              onChange={(event) => setHoneypot(event.target.value)}
              disabled={Boolean(submissionSuccess)}
            />
          </Box>

          <CheckBox
            name="submissionConsent"
            checked={consentGiven}
            onChange={(event) => setConsentGiven(event.target.checked)}
            label="Ich bestätige, zur Einreichung berechtigt zu sein, und stimme zu, dass das Modell nach der Prüfung öffentlich angezeigt und zugänglich gemacht werden darf."
            disabled={Boolean(submissionSuccess)}
          />

          {submissionError ? <Text color="status-critical">{submissionError}</Text> : null}

          <Box
            direction="row"
            justify="between"
            gap="small"
            margin={{ top: "small" }}
          >
            <Button
              label={submissionSuccess ? "Schließen" : "Abbrechen"}
              onClick={handleClose}
              disabled={isProcessing}
            />
            {submissionSuccess ? null : (
              <Button
                primary
                label="Modell einreichen"
                onClick={handlePrepareSubmission}
                disabled={isProcessing || !submissionReady}
                busy={isProcessing}
              />
            )}
          </Box>
        </Box>
      </Form>
    </SimpleModal>
  );
};

export default ModelingResultSubmissionModal;
