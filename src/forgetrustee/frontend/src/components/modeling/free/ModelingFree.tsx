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

import { Box, Text } from "grommet";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useQueries } from "@tanstack/react-query";
import Breadcrumb from "../../shared/Breadcrumb";
import ScrollToTop from "../../shared/ScrollToTop";
import { routes } from "../../../config/routes";
import { usePinnedTips } from "../../../hooks/usePinnedTips";
import { default as ModelingStepNavigationVertical } from "../common/ModelingStepNavigationVertical";
import CustomThemeSection from "../../util/CustomThemeSection";
import modelingOverride from "../../../themes/overrides/modellingOverride";
import ModelingAvatarPanel from "./ModelingAvatarPanel";
import ModelingFreeActions from "./ModelingFreeActions";
import ModelingFreeModals from "./ModelingFreeModals";
import ModelingFreeSidebar from "./ModelingFreeSidebar";
import ModelingStepContent from "./ModelingStepContent";
import useModelingFreeModals from "./hooks/useModelingFreeModals";
import useModelingFreePersistence from "./hooks/useModelingFreePersistence";
import useModelingFreeState, { DEFAULT_STEP_VALIDITY } from "./hooks/useModelingFreeState";
import useModelingFreeStepValidation from "./hooks/useModelingFreeStepValidation";
import {
  extractModelSubmissionErrorMessage,
  getStepFormConfig,
  toDataTrusteeModelPayload,
  toFieldValueMap,
} from "./modelingFreeUtils";
import { collectMissingRequiredFields, validateRequiredFields } from "./requiredValidation";
import { useBuildDataTrusteeModel } from "../../../api/generated/ontology-wizard-controller/ontology-wizard-controller";
import {
  getGetModuleMetadataQueryOptions,
  useGetModuleMetadata,
  useGetModulesSummary,
} from "../../../api/generated/metadata-controller/metadata-controller";
import type { DataTrusteeWizardBuildResponseDto } from "../../../types/generated";

/**
 * Hauptkomponente für die freie Modellierung eines Data Trustees. Verwaltet den Zustand des
 * Formulars, die Navigation zwischen den Schritten, die Modaldialoge für Aktionen wie Zurücksetzen,
 * Starten der Modellierung, Hochladen und Avatar-Upload sowie die Integration mit der API zum Bauen
 * des Data Trustee Modells.
 *
 * @returns JSX.Element, die die Benutzeroberfläche für die freie Modellierung darstellt.
 */
const ModelingFree = () => {
  const detailBasePath = routes.dataTrusteeModelingDetail.path.replace("/:modelId?", "");

  const navigate = useNavigate();
  const [isSubmittingModel, setIsSubmittingModel] = useState(false);
  const [modelSubmissionError, setModelSubmissionError] = useState<string | null>(null);
  const [modelBuildResult, setModelBuildResult] =
    useState<DataTrusteeWizardBuildResponseDto | null>(null);
  // Gewaehlte Avatar-Datei
  const [avatarFile, setAvatarFile] = useState<File | null>(null);
  const [avatarFileName, setAvatarFileName] = useState<string | null>(null);
  // Vorschau als DataURL fuer Persistenz/Export
  const [avatarDataUrl, setAvatarDataUrl] = useState<string | null>(null);
  const [showStepValidationErrors, setShowStepValidationErrors] = useState(false);
  const [pendingRevealMissingRequiredFieldNames, setPendingRevealMissingRequiredFieldNames] =
    useState<string[] | null>(null);

  const {
    closeAvatarUploadModal,
    closeHelpModal,
    closeResetModal,
    closeStartModelingModal,
    closeUploadModal,
    forceCloseStartModelingModal,
    openAvatarUploadModal,
    openResetModal,
    openStartModelingModal,
    openUploadModal,
    showAvatarUploadModal,
    showHelp,
    showResetModal,
    showStartModelingModal,
    showUploadModal,
  } = useModelingFreeModals({
    isSubmittingModel,
    modelSubmissionError,
    onStartModelingModalClosed: () => setModelSubmissionError(null),
  });

  const {
    activeStep,
    allStepsValid,
    formValues,
    handleFormValueChangeFor,
    handleNextStep,
    handlePrevStep,
    handleStepClick,
    initialFormValues,
    isDirty,
    setFormValues,
    setIsDirty,
    setStepValidity,
    stepValidity,
    totalSteps,
  } = useModelingFreeState();

  const {
    data: modulesSummaryData,
    isPending: isModulesSummaryPending,
    error: modulesSummaryError,
  } = useGetModulesSummary();

  const moduleMetadataQueries = useQueries({
    queries:
      modulesSummaryData?.modules.map((module) =>
        getGetModuleMetadataQueryOptions(module.moduleId),
      ) ?? [],
  });

  const activeModuleId = modulesSummaryData?.modules[activeStep - 1]?.moduleId ?? "";

  const { data: activeStepMetadata } = useGetModuleMetadata(activeModuleId);

  const {
    activeStepIsValid,
    isStepEdited,
    isStepMarkedValid,
    isStepValid,
    missingRequiredFieldLabels,
    missingRequiredFieldNames,
  } = useModelingFreeStepValidation({
    activeStep,
    activeStepMetadata,
    formValues,
    modulesSummaryData,
    setStepValidity,
    stepValidity,
  });

  const hasAnyEditedStep = useMemo(
    () => (modulesSummaryData?.modules ?? []).some((_, idx) => isStepEdited(idx + 1)),
    [isStepEdited, modulesSummaryData?.modules],
  );

  useEffect(() => {
    if (!modulesSummaryData) {
      return;
    }

    const nextStepValidity = { ...stepValidity };

    modulesSummaryData.modules.forEach((_, idx) => {
      const stepNumber = idx + 1;
      const stepConfig = getStepFormConfig(modulesSummaryData, stepNumber);
      const moduleMetadata = moduleMetadataQueries[idx]?.data;

      if (!stepConfig || !moduleMetadata) {
        return;
      }

      nextStepValidity[stepConfig.validityKey] = validateRequiredFields(
        moduleMetadata,
        toFieldValueMap(formValues[stepConfig.formKey]),
      );
    });

    setStepValidity((prev) => {
      const hasChanges = Object.keys(nextStepValidity).some(
        (key) =>
          prev[key as keyof typeof prev] !== nextStepValidity[key as keyof typeof nextStepValidity],
      );

      return hasChanges ? nextStepValidity : prev;
    });
  }, [formValues, modulesSummaryData, moduleMetadataQueries, setStepValidity, stepValidity]);

  const scrollToFirstMissingRequiredField = useCallback((fieldNames: string[]) => {
    for (const missingFieldName of fieldNames) {
      const fieldContainers = Array.from(document.querySelectorAll("[data-metadata-field-name]"));
      const firstMatchingField = fieldContainers.find(
        (element) => element.getAttribute("data-metadata-field-name") === missingFieldName,
      );

      if (firstMatchingField) {
        firstMatchingField.scrollIntoView({ behavior: "smooth", block: "center" });
        const focusable = firstMatchingField.querySelector<HTMLElement>(
          "input, textarea, button, [tabindex]:not([tabindex='-1'])",
        );
        focusable?.focus();
        break;
      }
    }
  }, []);

  const getMissingRequiredFieldNamesForStep = useCallback(
    (stepNumber: number) => {
      const stepConfig = getStepFormConfig(modulesSummaryData, stepNumber);

      if (!stepConfig) {
        return [];
      }

      const moduleMetadata = moduleMetadataQueries[stepNumber - 1]?.data;

      if (!moduleMetadata) {
        return [];
      }

      return collectMissingRequiredFields(
        moduleMetadata,
        toFieldValueMap(formValues[stepConfig.formKey]),
      ).map((field) => field.name);
    },
    [formValues, moduleMetadataQueries, modulesSummaryData],
  );

  useEffect(() => {
    if (!pendingRevealMissingRequiredFieldNames || !showStepValidationErrors) {
      return;
    }

    scrollToFirstMissingRequiredField(pendingRevealMissingRequiredFieldNames);
    setPendingRevealMissingRequiredFieldNames(null);
  }, [
    pendingRevealMissingRequiredFieldNames,
    scrollToFirstMissingRequiredField,
    showStepValidationErrors,
  ]);

  const handleNextStepGuarded = useCallback(() => {
    if (!isStepValid(activeStep)) {
      const missingRequiredFieldNamesForStep = getMissingRequiredFieldNamesForStep(activeStep);
      setShowStepValidationErrors(true);
      setPendingRevealMissingRequiredFieldNames(missingRequiredFieldNamesForStep);
      return;
    }
    setShowStepValidationErrors(false);
    setPendingRevealMissingRequiredFieldNames(null);
    handleNextStep();
  }, [activeStep, getMissingRequiredFieldNamesForStep, handleNextStep, isStepValid]);

  const revealMissingRequiredFields = useCallback(
    (stepNumber = activeStep) => {
      const missingRequiredFieldNamesForStep = getMissingRequiredFieldNamesForStep(stepNumber);

      setShowStepValidationErrors(true);
      setPendingRevealMissingRequiredFieldNames(missingRequiredFieldNamesForStep);

      if (stepNumber !== activeStep) {
        handleStepClick(stepNumber);
      }
    },
    [activeStep, getMissingRequiredFieldNamesForStep, handleStepClick],
  );

  const handleStepNavigationClick = useCallback(
    (stepNumber: number) => {
      const stepIsMarkedInvalid = isStepEdited(stepNumber) && !isStepMarkedValid(stepNumber);

      if (stepIsMarkedInvalid) {
        revealMissingRequiredFields(stepNumber);
        return;
      }

      setShowStepValidationErrors(false);
      setPendingRevealMissingRequiredFieldNames(null);
      handleStepClick(stepNumber);
    },
    [handleStepClick, isStepEdited, isStepMarkedValid, revealMissingRequiredFields],
  );

  const {
    clearPersistedModel,
    handleDismissLocalImport,
    handleExport,
    handleImportFromLocal,
    handleUploadConfirm,
    persistModelBuildResult,
    savedLocalPayloadRef,
    showLocalImportModal,
  } = useModelingFreePersistence({
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
  });

  const { tips: pinnedTips, unpinTip, unpinAll } = usePinnedTips();
  const sidebarRef = useRef<HTMLDivElement>(null!);

  const {
    mutate: buildDataTrusteeModel,
    // isPending: buildDataTrusteeModelPending,
  } = useBuildDataTrusteeModel({
    mutation: {
      onSuccess: (data) => {
        persistModelBuildResult(data);
        setModelBuildResult(data);
        setIsDirty(false);
        setIsSubmittingModel(false);
        forceCloseStartModelingModal();
        navigate(detailBasePath, {
          state: {
            modelBuildResult: data,
            avatarDataUrl,
            avatarFileName: avatarFile?.name ?? avatarFileName,
            preserveModelingSession: true,
          },
        });
      },
      onError: (error) => {
        setIsSubmittingModel(false);
        console.error("Fehler beim Senden der Modellierung an das Backend", error);
        setModelSubmissionError(extractModelSubmissionErrorMessage(error));
      },
    },
  });

  // Reset-Funktion fuer das Formular
  const handleFormReset = () => {
    setFormValues(initialFormValues);
    setIsDirty(false);
    setModelBuildResult(null);
    setAvatarFile(null);
    setAvatarFileName(null);
    setAvatarDataUrl(null);
    clearPersistedModel();
  };

  // ModalReset Bestätigung
  // const handleShowResetModal = () => setShowResetModal(true);
  const handleConfirmReset = () => {
    // reset the from
    handleFormReset();

    // navigate back to the first step
    handleStepClick(1);
    setShowStepValidationErrors(false);
    setPendingRevealMissingRequiredFieldNames(null);

    // close the modal
    closeResetModal();

    // reset step validity
    setStepValidity(DEFAULT_STEP_VALIDITY);
  };

  const handleUpload = useCallback(() => {
    openUploadModal();
  }, [openUploadModal]);

  const handleUnpinTooltip = useCallback(
    (fieldName: string) => {
      unpinTip(fieldName);
    },
    [unpinTip],
  );

  const handleAvatarUpload = useCallback(() => {
    openAvatarUploadModal();
  }, [openAvatarUploadModal]);

  const handleStartModeling = useCallback(async () => {
    setModelSubmissionError(null);

    if (modelBuildResult && !isDirty) {
      forceCloseStartModelingModal();
      navigate(detailBasePath, {
        state: {
          modelBuildResult,
          avatarDataUrl,
          avatarFileName: avatarFile?.name ?? avatarFileName,
          preserveModelingSession: true,
        },
      });
      return;
    }

    setIsSubmittingModel(true);

    const payload = toDataTrusteeModelPayload(formValues);
    buildDataTrusteeModel({ data: payload });
  }, [
    avatarDataUrl,
    avatarFile,
    avatarFileName,
    buildDataTrusteeModel,
    detailBasePath,
    formValues,
    isDirty,
    modelBuildResult,
    navigate,
    forceCloseStartModelingModal,
  ]);

  const handleAvatarUploadConfirm = useCallback(
    (payload: { file: File | null; dataUrl: string | null; cleared?: boolean }) => {
      const { file, dataUrl, cleared } = payload;
      if (cleared || !file) {
        setAvatarFile(null);
        setAvatarFileName(null);
        setAvatarDataUrl(null);
        setIsDirty(true);
      } else if (file) {
        setAvatarFile(file);
        setAvatarFileName(file.name);
        setAvatarDataUrl(dataUrl ?? null);
        setIsDirty(true);
      }
      closeAvatarUploadModal();
    },
    [closeAvatarUploadModal, setIsDirty],
  );

  // Unpin all tips when changing steps to avoid showing irrelevant tips from previous steps
  useEffect(() => {
    unpinAll();
  }, [activeStep, unpinAll]);

  if (isModulesSummaryPending) {
    return (
      <Box
        fill="vertical"
        align="center"
        justify="center"
      >
        <Text>Loading...</Text>
      </Box>
    );
  }

  if (modulesSummaryError || !modulesSummaryData) {
    return (
      <Box
        fill="vertical"
        align="center"
        justify="center"
        pad="medium"
      >
        <Text
          color="status-critical"
          size="large"
          textAlign="center"
        >
          Fehler beim Laden der Modulsummaries. Bitte versuchen Sie es später erneut.
        </Text>
      </Box>
    );
  }

  if (modulesSummaryData.modules.length === 0) {
    return (
      <Box
        fill="vertical"
        align="center"
        justify="center"
        pad="medium"
      >
        <Text
          color="status-critical"
          size="large"
          textAlign="center"
        >
          Es sind keine Module verfügbar. Bitte versuchen Sie es später erneut.
        </Text>
      </Box>
    );
  }

  const activeStepTitle = modulesSummaryData.modules.find(
    (_, idx) => idx === activeStep - 1,
  )?.heading;

  return (
    <>
      <ModelingFreeModals
        help={{
          open: showHelp,
          onClose: closeHelpModal,
        }}
        localImport={{
          open: showLocalImportModal && !!savedLocalPayloadRef.current,
          onClose: handleDismissLocalImport,
          onImport: handleImportFromLocal,
          payload: savedLocalPayloadRef.current,
        }}
        reset={{
          open: showResetModal,
          onClose: closeResetModal,
          onConfirm: handleConfirmReset,
        }}
        startModeling={{
          open: showStartModelingModal,
          onClose: closeStartModelingModal,
          onConfirm: handleStartModeling,
          isSubmitting: isSubmittingModel,
          errorMessage: modelSubmissionError,
        }}
        upload={{
          open: showUploadModal,
          onClose: closeUploadModal,
          onConfirm: (payload) => {
            handleUploadConfirm(payload);
            closeUploadModal();
          },
        }}
        avatarUpload={{
          open: showAvatarUploadModal,
          avatarDataUrl,
          avatarFileName: avatarFile?.name ?? avatarFileName,
          onClose: closeAvatarUploadModal,
          onConfirm: handleAvatarUploadConfirm,
        }}
      />

      <CustomThemeSection customTheme={modelingOverride}>
        <ScrollToTop trigger={activeStep} />
        {/* Breadcrumb */}
        <Box
          direction="row"
          justify="between"
          height={{ min: "auto" }}
        >
          <Breadcrumb items={[routes.startpage, routes.dataTrusteeModeling]} />
        </Box>

        {/* Main Content */}
        <Box
          direction="row"
          fill="vertical"
          gap="large"
        >
          <Box
            direction="column"
            gap="medium"
            // pad={{ horizontal: "medium" }}
            flex
            animation={{ type: "fadeIn", duration: 300 }}
          >
            <Box margin={{ top: "none", bottom: "none", left: "small" }}>
              <Text size="20px">{`Schritt ${activeStep}: ${activeStepTitle ?? ""}`}</Text>
            </Box>
            <Box
              direction="row"
              align="start"
              gap="large"
            >
              <ModelingAvatarPanel
                avatarDataUrl={avatarDataUrl}
                avatarFile={avatarFile}
                avatarFileName={avatarFileName}
                onAvatarUpload={handleAvatarUpload}
              />
              <Box
                gap="medium"
                flex
                height={{ min: "auto" }}
              >
                <ModelingStepNavigationVertical
                  modulesSummary={modulesSummaryData}
                  activeStep={activeStep}
                  onStepClick={handleStepNavigationClick}
                  isStepEdited={isStepEdited}
                  isStepValid={isStepMarkedValid}
                />

                <ModelingStepContent
                  activeStep={activeStep}
                  activeModuleId={activeModuleId}
                  formValues={formValues}
                  showValidationErrors={showStepValidationErrors}
                  missingRequiredFieldNames={missingRequiredFieldNames}
                  onFieldChangeFor={handleFormValueChangeFor}
                />
              </Box>
            </Box>
          </Box>
          <ModelingFreeSidebar
            sidebarRef={sidebarRef}
            tips={pinnedTips}
            onUnpin={handleUnpinTooltip}
            onExport={handleExport}
            onUpload={handleUpload}
            onReset={openResetModal}
          />
        </Box>
        <ModelingFreeActions
          activeStep={activeStep}
          totalSteps={totalSteps}
          activeStepIsValid={activeStepIsValid}
          missingRequiredFieldLabels={missingRequiredFieldLabels}
          allStepsValid={allStepsValid}
          showModelingHint={activeStep === totalSteps && !hasAnyEditedStep}
          onPrevStep={handlePrevStep}
          onNextStep={handleNextStepGuarded}
          onRevealMissingRequiredFields={() => revealMissingRequiredFields(activeStep)}
          onStartModeling={openStartModelingModal}
        />
      </CustomThemeSection>
    </>
  );
};

export default ModelingFree;
