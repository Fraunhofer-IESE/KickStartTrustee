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

import { Box, Button, Spinner, Text } from "grommet";
import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import {
  getDataTrusteeModelAvatar,
  useGetDataTrusteeModelPackageById,
} from "../../../api/generated/data-trustee-model-controller/data-trustee-model-controller";
import { routes } from "../../../config/routes";
import Breadcrumb from "../../shared/Breadcrumb";
import ModelingResultTagCanvasView from "./ModelingResultTagCanvasView";
import { createEmptyDataTrusteeModelDraft } from "../free/modelingFreeUtils";
import {
  normalizeBusinessFinancingTypesForUi,
  normalizeDataProcessingBasesForUi,
} from "../free/legal/legalProcessingBases";
import {
  getModelingFreeSessionState,
  markModelingFreeSessionResolved,
  setModelingFreeSessionPayload,
} from "../common/modelingSession";
import { loadModelingFreePayload } from "../common/modelingStorage";
import type {
  DataTrusteeModelResponseDto,
  DataTrusteeWizardBuildResponseDto,
} from "../../../types/generated";

type ModelingResultLocationState = {
  modelBuildResult?: DataTrusteeWizardBuildResponseDto | null;
  dataTrusteeModel?: DataTrusteeModelResponseDto | null;
  modelId?: string | null;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
  preserveModelingSession?: boolean;
};

const ModelingResultDetailPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { modelId: routeModelId } = useParams();
  const detailBasePath = routes.dataTrusteeModelingDetail.path.replace("/:modelId?", "");

  const locationState = (location.state as ModelingResultLocationState | null) ?? null;
  const sessionPayload = getModelingFreeSessionState().payload;
  const shouldPreserveModelingSession = Boolean(locationState?.preserveModelingSession);
  const stateModelId = locationState?.modelId ?? null;
  const effectiveModelId = routeModelId ?? stateModelId ?? "";
  const shouldLoadFromBackend = Boolean(effectiveModelId);
  const [backendAvatarDataUrl, setBackendAvatarDataUrl] = useState<string | null>(null);
  const detailBreadcrumbRoute = {
    ...routes.dataTrusteeModelingDetail,
    path: effectiveModelId
      ? `${detailBasePath}/${encodeURIComponent(effectiveModelId)}`
      : detailBasePath,
  };

  const {
    data: fetchedDataTrusteeModelPackage,
    isPending,
    error,
  } = useGetDataTrusteeModelPackageById(effectiveModelId, {
    lang: "de",
  }, {
    query: {
      enabled: shouldLoadFromBackend,
    },
  });

  useEffect(() => {
    if (!shouldLoadFromBackend || locationState?.avatarDataUrl || !effectiveModelId) {
      setBackendAvatarDataUrl(null);
      return;
    }

    let isActive = true;

    const loadAvatar = async () => {
      try {
        const avatarBlob = await getDataTrusteeModelAvatar(effectiveModelId);
        const dataUrl = await new Promise<string>((resolve, reject) => {
          const reader = new FileReader();
          reader.onload = () => resolve(String(reader.result ?? ""));
          reader.onerror = () => reject(new Error("Das Avatar-Bild konnte nicht geladen werden."));
          reader.readAsDataURL(avatarBlob);
        });

        if (isActive) {
          setBackendAvatarDataUrl(dataUrl);
        }
      } catch {
        if (isActive) {
          setBackendAvatarDataUrl(null);
        }
      }
    };

    void loadAvatar();

    return () => {
      isActive = false;
    };
  }, [effectiveModelId, locationState?.avatarDataUrl, shouldLoadFromBackend]);

  /**
   * Determine the source of truth for the model details to display. This can come from:
   *
   * 1. The backend, if we have a modelId from the route or state.
   * 2. The location state, if it contains a modelBuildResult or dataTrusteeModel (e.g. after coming
   *    from the modeling page).
   * 3. The session payload, if it contains a modelBuildResult or dataTrusteeModel (e.g. after coming
   *    from the modeling page).
   */
  const detailState = useMemo(
    () =>
      shouldLoadFromBackend
        ? fetchedDataTrusteeModelPackage
          ? {
              modelBuildResult: fetchedDataTrusteeModelPackage,
              avatarDataUrl: backendAvatarDataUrl,
              avatarFileName: null,
            }
          : locationState?.dataTrusteeModel
            ? {
                modelBuildResult: {
                  dataTrusteeModel: locationState.dataTrusteeModel,
                } as DataTrusteeWizardBuildResponseDto,
                avatarDataUrl: locationState.avatarDataUrl ?? null,
                avatarFileName: locationState.avatarFileName ?? null,
              }
            : {
                modelBuildResult: null,
                avatarDataUrl: null,
                avatarFileName: null,
              }
        : locationState?.modelBuildResult
          ? locationState
          : locationState?.dataTrusteeModel
            ? {
                modelBuildResult: {
                  dataTrusteeModel: locationState.dataTrusteeModel,
                } as DataTrusteeWizardBuildResponseDto,
                avatarDataUrl: locationState.avatarDataUrl ?? null,
                avatarFileName: locationState.avatarFileName ?? null,
              }
            : {
                modelBuildResult: sessionPayload?.modelBuildResult ?? null,
                avatarDataUrl: sessionPayload?.avatarDataUrl ?? null,
                avatarFileName: sessionPayload?.avatarFileName ?? null,
              },
    [
      backendAvatarDataUrl,
      fetchedDataTrusteeModelPackage,
      locationState,
      sessionPayload,
      shouldLoadFromBackend,
    ],
  );

  /**
   * On component mount, check if there's a saved model in localStorage. If so, store it in a ref
   * and show the import modal. We use a ref to avoid triggering re-renders and to have the data
   * available for the import handler. We also track whether the initial load has been done to avoid
   * overwriting localStorage on the first render.
   */
  useEffect(() => {
    if (!detailState.modelBuildResult) {
      return;
    }

    if (sessionPayload?.modelBuildResult && sessionPayload?.formValues) {
      markModelingFreeSessionResolved();
      return;
    }

    if (!shouldPreserveModelingSession) {
      return;
    }

    const persistedPayload = loadModelingFreePayload();

    const restoredFormValues =
      sessionPayload?.formValues ??
      detailState.modelBuildResult.request ??
      persistedPayload?.formValues ??
      createEmptyDataTrusteeModelDraft();
    const restoredModelBuildResult =
      sessionPayload?.modelBuildResult ??
      persistedPayload?.modelBuildResult ??
      detailState.modelBuildResult;

    setModelingFreeSessionPayload({
      formValues: normalizeBusinessFinancingTypesForUi(
        normalizeDataProcessingBasesForUi(restoredFormValues),
      ),
      avatarDataUrl:
        detailState.avatarDataUrl ??
        persistedPayload?.avatarDataUrl ??
        sessionPayload?.avatarDataUrl ??
        null,
      avatarFileName:
        detailState.avatarFileName ??
        persistedPayload?.avatarFileName ??
        sessionPayload?.avatarFileName ??
        null,
      modelBuildResult: restoredModelBuildResult,
      hasPendingChanges: sessionPayload?.hasPendingChanges ?? false,
      createdAt: sessionPayload?.createdAt,
      savedAt: sessionPayload?.savedAt,
    });
  }, [detailState, sessionPayload, shouldPreserveModelingSession]);

  if (!detailState.modelBuildResult) {
    if (isPending) {
      return (
        <Box
          fill="horizontal"
          gap="medium"
        >
          <Breadcrumb
            items={[routes.startpage, routes.dataTrusteeModeling, detailBreadcrumbRoute]}
          />
          <Box
            direction="row"
            gap="small"
            pad="small"
          >
            <Spinner />
            <Text>Modell wird geladen.</Text>
          </Box>
        </Box>
      );
    }

    return (
      <Box
        fill="horizontal"
        gap="medium"
      >
        <Breadcrumb items={[routes.startpage, routes.dataTrusteeModeling, detailBreadcrumbRoute]} />
        <Box gap="small">
          <Text>
            {error
              ? "Das Modell konnte nicht vom Backend geladen werden."
              : "Keine Modellierungsdetails gefunden."}
          </Text>
          <Button
            label="Zur Modellierung"
            onClick={() => navigate(routes.dataTrusteeModeling.path)}
          />
        </Box>
      </Box>
    );
  }

  return (
    <Box fill>
      <Breadcrumb items={[routes.startpage, routes.dataTrusteeModeling, detailBreadcrumbRoute]} />
      <ModelingResultTagCanvasView
        result={detailState.modelBuildResult}
        allowExportAndSubmission={!shouldLoadFromBackend}
        avatarDataUrl={detailState.avatarDataUrl}
        avatarFileName={detailState.avatarFileName}
        onBack={() => navigate(routes.dataTrusteeModeling.path)}
      />
    </Box>
  );
};

export default ModelingResultDetailPage;
