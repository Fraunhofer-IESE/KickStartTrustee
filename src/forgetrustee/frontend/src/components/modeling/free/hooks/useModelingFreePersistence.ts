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

import { useCallback, useEffect, useRef, useState } from "react";
import type { Dispatch, SetStateAction } from "react";
import type { DataTrusteeWizardBuildResponseDto } from "../../../../types/generated";
import type { DataTrusteeModelDraft } from "../../../../types/dataTrusteeModelDraft";
import type { LocalStoragePayload } from "../../../../types/localStoragePayload";
import {
  clearModelingFreePayload,
  loadModelingFreePayload,
  saveModelingFreePayload,
} from "../../common/modelingStorage";
import {
  clearModelingFreeSessionState,
  getModelingFreeSessionState,
  markModelingFreeSessionResolved,
  setModelingFreeSessionPayload,
} from "../../common/modelingSession";
import { createModelExportZip, downloadBlob } from "../../common/modelExportZip";
import { createEmptyDataTrusteeModelDraft } from "../modelingFreeUtils";
import {
  normalizeBusinessFinancingTypesForUi,
  normalizeDataProcessingBasesForUi,
} from "../legal/legalProcessingBases";

type UseModelingFreePersistenceParams = {
  formValues: DataTrusteeModelDraft;
  avatarDataUrl: string | null;
  avatarFile: File | null;
  avatarFileName: string | null;
  modelBuildResult: DataTrusteeWizardBuildResponseDto | null;
  isDirty: boolean;
  initialFormValues: DataTrusteeModelDraft;
  setFormValues: Dispatch<SetStateAction<DataTrusteeModelDraft>>;
  setIsDirty: Dispatch<SetStateAction<boolean>>;
  setAvatarDataUrl: Dispatch<SetStateAction<string | null>>;
  setAvatarFileName: Dispatch<SetStateAction<string | null>>;
  setModelBuildResult: Dispatch<SetStateAction<DataTrusteeWizardBuildResponseDto | null>>;
};

const useModelingFreePersistence = ({
  formValues,
  avatarDataUrl,
  avatarFile,
  avatarFileName,
  modelBuildResult,
  isDirty,
  initialFormValues,
  setFormValues,
  setIsDirty,
  setAvatarDataUrl,
  setAvatarFileName,
  setModelBuildResult,
}: UseModelingFreePersistenceParams) => {
  const [showLocalImportModal, setShowLocalImportModal] = useState(false);
  const savedLocalPayloadRef = useRef<LocalStoragePayload | null>(null);
  const initialLoadDoneRef = useRef<boolean>(false);

  const persistPayload = useCallback((payload: LocalStoragePayload) => {
    saveModelingFreePayload(payload);
    setModelingFreeSessionPayload(payload);
  }, []);

  const applyPayloadToState = useCallback(
    (payload: LocalStoragePayload | null) => {
      if (!payload) {
        return;
      }

      const parsed = payload ?? {};
      const pv =
        parsed.formValues ?? parsed.modelBuildResult?.request ?? createEmptyDataTrusteeModelDraft();

      if (pv) {
        setFormValues(
          normalizeBusinessFinancingTypesForUi(
            normalizeDataProcessingBasesForUi(pv as DataTrusteeModelDraft),
          ),
        );
        setIsDirty(parsed.hasPendingChanges ?? true);
      }

      setAvatarDataUrl(parsed.avatarDataUrl ?? null);
      setAvatarFileName(parsed.avatarFileName ?? null);
      setModelBuildResult(parsed.modelBuildResult ?? null);
      setModelingFreeSessionPayload({
        ...parsed,
        formValues: pv,
      });
    },
    [setAvatarDataUrl, setAvatarFileName, setFormValues, setIsDirty, setModelBuildResult],
  );

  /**
   * Recursively checks if the given object has any non-empty value.
   *
   * - For strings: checks if non-empty after trimming.
   * - For numbers: considers any number as having a value.
   */
  const hasAnyValue = useCallback((obj: unknown): boolean => {
    if (obj == null) return false;
    if (typeof obj === "string") return obj.trim() !== "";
    if (typeof obj === "number") return true;
    if (typeof obj === "boolean") return obj === true;
    if (Array.isArray(obj)) return obj.length > 0;
    if (typeof obj === "object") {
      if (obj && typeof obj === "object" && !Array.isArray(obj)) {
        const o = obj as { [key: string]: unknown };
        for (const k of Object.keys(o)) {
          if (hasAnyValue(o[k])) return true;
        }
        return false;
      }
    }
    return false;
  }, []);

  /**
   * On component mount, check if there's a saved model in localStorage. If so, store it in a ref
   * and show the import modal. We use a ref to avoid triggering re-renders and to have the data
   * available for the import handler. We also track whether the initial load has been done to avoid
   * overwriting localStorage on the first render.
   */
  useEffect(() => {
    const sessionState = getModelingFreeSessionState();

    if (sessionState.payload) {
      applyPayloadToState(sessionState.payload);
      initialLoadDoneRef.current = true;
      return;
    }

    if (sessionState.hasResolvedInitialLocalState) {
      initialLoadDoneRef.current = true;
      return;
    }

    const parsed = loadModelingFreePayload();
    if (!parsed) {
      markModelingFreeSessionResolved();
      initialLoadDoneRef.current = true;
      return;
    }
    try {
      const pv = parsed?.formValues ?? parsed ?? {};
      const hasData =
        hasAnyValue(pv) ||
        !!parsed?.avatarDataUrl ||
        !!parsed?.avatarFileName ||
        !!parsed?.modelBuildResult;
      if (hasData) {
        savedLocalPayloadRef.current = (parsed ?? null) as LocalStoragePayload | null;
        setShowLocalImportModal(true);
      } else {
        markModelingFreeSessionResolved();
      }
    } catch (e) {
      console.error("Konnte gespeicherte Modellierungsdaten nicht laden", e);
    } finally {
      initialLoadDoneRef.current = true;
    }
  }, [applyPayloadToState, hasAnyValue]);

  /**
   * Whenever formValues, avatarDataUrl, avatarFile, avatarFileName or isDirty changes, save the
   * current state to localStorage. We check isDirty to avoid saving an empty or initial state on
   * the first render before the user has made any changes. We also handle potential errors when
   * accessing localStorage and log them to the console.
   */
  useEffect(() => {
    if (!initialLoadDoneRef.current) return;
    if (!isDirty && !modelBuildResult) return;
    try {
      persistPayload({
        formValues,
        avatarDataUrl,
        avatarFileName: avatarFile?.name ?? avatarFileName,
        modelBuildResult: modelBuildResult ?? null,
        hasPendingChanges: isDirty,
      });
    } catch (e) {
      console.error("Error saving model to localStorage", e);
    }
  }, [
    formValues,
    avatarDataUrl,
    avatarFile,
    avatarFileName,
    modelBuildResult,
    isDirty,
    persistPayload,
  ]);

  /**
   * Import the saved model from localStorage. This function is called when the user chooses to
   * import the local model. It updates the form values and avatar data accordingly.
   */
  const handleImportFromLocal = useCallback(() => {
    const parsed = savedLocalPayloadRef.current;
    if (!parsed) return;

    try {
      applyPayloadToState(parsed);
    } catch (e) {
      console.error("Fehler beim Importieren des lokalen Modells", e);
    }
  }, [applyPayloadToState]);

  const handleDismissLocalImport = useCallback(() => {
    savedLocalPayloadRef.current = null;
    markModelingFreeSessionResolved();
    setShowLocalImportModal(false);
  }, []);

  /**
   * Export form data as ZIP with formData.json. Prefer persisted form data from local storage,
   * falling back to in-memory values if no saved payload exists.
   */
  const handleExport = useCallback(async () => {
    try {
      const persistedPayload = loadModelingFreePayload();
      const formDataToExport = persistedPayload?.formValues ?? formValues;
      const avatarDataUrlToExport = persistedPayload?.avatarDataUrl ?? avatarDataUrl;
      const avatarFileNameToExport = persistedPayload?.avatarFileName ?? avatarFileName;

      const exportBundle = await createModelExportZip({
        modelName: formDataToExport?.core?.dataTrusteeName,
        formData: formDataToExport,
        avatarDataUrl: avatarDataUrlToExport,
        avatarFileName: avatarFileNameToExport,
      });

      if (!exportBundle) {
        return;
      }

      downloadBlob(exportBundle.blob, exportBundle.fileName);
    } catch (e) {
      console.error("Fehler beim Exportieren der Formulardaten", e);
    }
  }, [avatarDataUrl, avatarFileName, formValues]);

  /**
   * Handle the confirmation of the upload/import process. This function is called when the user
   * confirms the import of a model (either from localStorage or from a file). It updates the form
   * values and avatar data accordingly, and also saves the imported model to localStorage. We
   * handle potential errors during this process and log them to the console.
   */
  const handleUploadConfirm = useCallback(
    (payload: LocalStoragePayload) => {
      const parsed = payload ?? {};

      try {
        applyPayloadToState(parsed);

        const aData = parsed.avatarDataUrl;
        const aName = parsed.avatarFileName;
        const result = parsed.modelBuildResult;
        const pv = parsed.formValues ?? initialFormValues;

        try {
          persistPayload({
            formValues: pv ?? initialFormValues,
            avatarDataUrl: aData ?? null,
            avatarFileName: aName ?? null,
            modelBuildResult: result ?? null,
            hasPendingChanges: false,
          });
        } catch (e) {
          console.error("Fehler beim Speichern des importierten Modells", e);
        }
      } catch (e) {
        console.error("Fehler beim Importieren des Modells", e);
      }
    },
    [initialFormValues, applyPayloadToState, persistPayload],
  );

  const persistModelBuildResult = useCallback(
    (result: DataTrusteeWizardBuildResponseDto | null) => {
      persistPayload({
        formValues,
        avatarDataUrl,
        avatarFileName: avatarFile?.name ?? avatarFileName,
        modelBuildResult: result ?? null,
        hasPendingChanges: false,
      });
    },
    [avatarDataUrl, avatarFile, avatarFileName, formValues, persistPayload],
  );

  const clearPersistedModel = useCallback(() => {
    clearModelingFreePayload();
    clearModelingFreeSessionState();
  }, []);

  return {
    clearPersistedModel,
    handleDismissLocalImport,
    handleExport,
    handleImportFromLocal,
    handleUploadConfirm,
    persistModelBuildResult,
    savedLocalPayloadRef,
    showLocalImportModal,
  };
};

export default useModelingFreePersistence;
