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

import { Anchor, Box, Button, Grid, Text } from "grommet";
import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import DynamicIcon from "../shared/DynamicIcon";
import { routes } from "../../config/routes";
import Breadcrumb from "../shared/Breadcrumb";
import LoadingSpinner from "../shared/LoadingSpinner";
import { getModelingFreeSessionState } from "../modeling/common/modelingSession";
import CompareCard, { type DthData } from "./CompareCard";
import {
  getCompareSessionState,
  SESSION_COMPARE_ITEM_ID,
  setCompareSessionSelectedIds,
} from "./compareSession";
import { useGetAllDataTrusteeModels } from "../../api/generated/data-trustee-model-controller/data-trustee-model-controller";
import type { DataTrusteeModelResponseDto } from "../../types/generated/dataTrusteeModelResponseDto";
import type { DataTrusteeModelSummaryDto } from "../../types/generated/dataTrusteeModelSummaryDto";
import { unwrapCommentedString } from "../shared/commentedValue";

type CompareOverviewLocationState = {
  removeSelectedId?: string;
  removeSelectionIndex?: number;
};

const toCardData = (model: DataTrusteeModelSummaryDto): DthData | null => {
  if (!model.modelId) {
    return null;
  }

  return {
    id: model.modelId,
    modelId: model.modelId,
    imageDataUrl: null,
    name: model.name || "Unbenanntes Modell",
    description: model.description || "Keine Beschreibung",
  };
};

const toOwnSessionCardData = (model: DataTrusteeModelResponseDto): DthData => ({
  id: SESSION_COMPARE_ITEM_ID,
  name: unwrapCommentedString(model.core?.dataTrusteeName) || "Unbenanntes Modell",
  description: unwrapCommentedString(model.core?.dataTrusteeDescription) || "Keine Beschreibung",
  model,
  isOwnModel: true,
});

type CompareModelMeta = {
  modelId?: string;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
  isOwnModel?: boolean;
};

const toComparisonMeta = (item: DthData): CompareModelMeta => ({
  modelId: item.modelId,
  avatarDataUrl: item.imageDataUrl ?? null,
  avatarFileName: item.imageFileName ?? null,
  isOwnModel: item.isOwnModel,
});

/**
 * Übersichtsseite zum Vergleich von Datentreuhandmodellen
 *
 * @returns JSX.Element
 */
const CompareOverview = () => {
  const [selectedIds, setSelectedIds] = useState<string[]>(() =>
    getCompareSessionState().selectedIds.slice(0, 2),
  );
  const location = useLocation();
  const navigate = useNavigate();
  const sessionPayload = getModelingFreeSessionState().payload;
  const locationState = (location.state as CompareOverviewLocationState | null) ?? null;
  const sessionModelInput = sessionPayload?.modelBuildResult?.dataTrusteeModel;

  const {
    data: listDataTrusteeModels,
    isPending: isCompareModelsPending,
  } = useGetAllDataTrusteeModels();

  const remoteItems = useMemo(
    () =>
      (listDataTrusteeModels || [])
        .map((item) => toCardData(item))
        .filter((item): item is DthData => item !== null),
    [listDataTrusteeModels],
  );

  const compareItems = useMemo(() => {
    if (!sessionModelInput) {
      return remoteItems;
    }

    const sessionItem: DthData = {
      ...toOwnSessionCardData(sessionModelInput),
      imageDataUrl: sessionPayload?.avatarDataUrl ?? null,
      imageFileName: sessionPayload?.avatarFileName ?? null,
      description:
        unwrapCommentedString(sessionModelInput.core?.dataTrusteeDescription) ||
        "Berechnetes Datenmodell aus der aktuellen Sitzung",
    };

    return [sessionItem, ...remoteItems];
  }, [remoteItems, sessionPayload, sessionModelInput]);

  const showLoading = isCompareModelsPending && remoteItems.length === 0;
  const showEmptyState = !isCompareModelsPending && compareItems.length === 0;

  const selectedItems = compareItems.filter((item) => selectedIds.includes(item.id));
  const selectionSlots: (DthData | undefined)[] = [selectedItems[0], selectedItems[1]];

  useEffect(() => {
    const removeSelectedId = locationState?.removeSelectedId;
    const removeSelectionIndex = locationState?.removeSelectionIndex;

    if (!removeSelectedId && !removeSelectionIndex) {
      return;
    }

    setSelectedIds((prev) => {
      if (removeSelectedId) {
        return prev.filter((id) => id !== removeSelectedId).slice(0, 2);
      }

      const index = (removeSelectionIndex ?? 0) - 1;
      if (index < 0 || index >= prev.length) {
        return prev;
      }

      return prev.filter((_, idx) => idx !== index).slice(0, 2);
    });
  }, [locationState?.removeSelectedId, locationState?.removeSelectionIndex]);

  useEffect(() => {
    if (isCompareModelsPending && !sessionModelInput) {
      return;
    }

    const validIds = new Set(compareItems.map((item) => item.id));
    setSelectedIds((prev) => {
      const sanitized = prev.filter((id) => validIds.has(id)).slice(0, 2);

      if (sanitized.length === prev.length && sanitized.every((id, index) => id === prev[index])) {
        return prev;
      }

      return sanitized;
    });
  }, [compareItems, isCompareModelsPending, sessionModelInput]);

  useEffect(() => {
    setCompareSessionSelectedIds(selectedIds);
  }, [selectedIds]);

  const toggleSelection = (id: string) => {
    setSelectedIds((prev) => {
      if (prev.includes(id)) {
        return prev.filter((existingId) => existingId !== id);
      }

      if (prev.length >= 2) {
        return prev; // limit to max 2
      }

      return [...prev, id];
    });
  };

  const handleCompare = () => {
    if (selectedItems.length === 0) {
      return;
    }

    navigate(routes.dataTrusteeCompareResult.path, {
      state: {
        dthMeta1: toComparisonMeta(selectedItems[0]),
        dthMeta2: selectedItems[1] ? toComparisonMeta(selectedItems[1]) : undefined,
        dthItem1: selectedItems[0].isOwnModel ? selectedItems[0].model : undefined,
        dthItem2: selectedItems[1]?.isOwnModel ? selectedItems[1].model : undefined,
      },
    });
  };

  const handleDetail = (item: DthData) => {
    const detailBasePath = routes.dataTrusteeModelingDetail.path.replace("/:modelId?", "");
    const targetModelId = item.modelId ?? (!item.isOwnModel ? item.id : undefined);

    if (targetModelId) {
      navigate(`${detailBasePath}/${encodeURIComponent(targetModelId)}`, {});
      return;
    }

    if (item.isOwnModel && sessionPayload?.modelBuildResult) {
      navigate(detailBasePath, {
        state: {
          modelBuildResult: sessionPayload.modelBuildResult,
          dataTrusteeModel: item.model,
          avatarDataUrl: item.imageDataUrl ?? sessionPayload.avatarDataUrl ?? null,
          avatarFileName: item.imageFileName ?? sessionPayload.avatarFileName ?? null,
        },
      });
      return;
    }

    if (item.model) {
      navigate(detailBasePath, {
        state: {
          dataTrusteeModel: item.model,
          avatarDataUrl: item.imageDataUrl ?? null,
          avatarFileName: item.imageFileName ?? null,
        },
      });
      return;
    }

    if (item.isOwnModel) {
      navigate(detailBasePath);
    }
  };

  const openDetail = (item: DthData) => {
    handleDetail(item);
  };

  return (
    <>
      {/* Breadcrumb */}
      <Box
        direction="row"
        justify="between"
      >
        <Breadcrumb items={[routes.startpage, routes.dataTrusteeCompare]} />
      </Box>
      <Box
        animation={{ type: "fadeIn", duration: 300 }}
        fill
      >
        <Box fill>
          {showLoading && (
            <Box
              fill="vertical"
              align="center"
              justify="center"
            >
              <LoadingSpinner />
            </Box>
          )}
          {showEmptyState && (
            <Box
              fill="vertical"
              align="center"
              justify="center"
              pad="medium"
              gap="medium"
            >
              <Text
                color="status-critical"
                size="large"
                textAlign="center"
              >
                Es konnten keine Datentreuhandmodelle vom Backend geladen werden.
              </Text>
              <Text
                color="text-weak"
                textAlign="center"
              >
                Bitte versuchen Sie es später erneut.
              </Text>
            </Box>
          )}
          {!showLoading && !showEmptyState && (
            <>
              <Grid
                columns={{ count: 3, size: "auto" }}
                gap="medium"
              >
                {compareItems.map((item: DthData) => (
                  <CompareCard
                    key={item.id}
                    item={item}
                    selected={selectedIds.includes(item.id)}
                    onSelect={toggleSelection}
                    onDetail={() => openDetail(item)}
                  />
                ))}
              </Grid>
              <Box
                background="background-front"
                pad={{ horizontal: "medium", vertical: "small" }}
                round="small"
                margin={{ top: "medium", bottom: "small", right: "small" }}
              >
                <Text
                  size="small"
                  color="text-weak"
                  textAlign="center"
                >
                  Sollten Sie Einwände gegen die hier dargestellten Daten haben, wenden Sie sich bitte
                  unter Angabe der betroffenen Daten an{" "}
                  <Anchor href="mailto:forgetrustee@iese.fraunhofer.de">
                    forgetrustee@iese.fraunhofer.de
                  </Anchor>
                  .
                </Text>
              </Box>
              <Box flex />
              <Box
                alignSelf="end"
                background="background-front"
                pad="medium"
                round="small"
                elevation="medium"
                width="medium"
                gap="small"
                // style={{
                //   position: "fixed",
                //   right: "24px",
                //   bottom: "24px",
                //   zIndex: 20,
                // }}
                style={{
                  position: "sticky",
                  bottom: "24px",
                  zIndex: 1,
                  alignSelf: "end",
                }}
                height={{ min: "auto" }}
                margin={{ right: "small" }}
              >
                <Text color="text-weak">Wählen Sie zwei Modellen zum Vergleich aus.</Text>

                <Box gap="xsmall">
                  {selectionSlots.map((item, idx) => (
                    <Box
                      key={idx}
                      direction="row"
                      align="center"
                      gap="small"
                      pad={{ vertical: "xsmall" }}
                    >
                      <Text>{`${idx + 1}.`}</Text>
                      <Box flex>
                        {item ? (
                          <>
                            <Text truncate>{item.name}</Text>
                          </>
                        ) : (
                          <Text color="text-weak">keine Auswahl</Text>
                        )}
                      </Box>
                      {item && (
                        <Button
                          plain
                          icon={
                            <DynamicIcon
                              iconName="FormClose"
                              size="small"
                              color="text"
                            />
                          }
                          onClick={() => toggleSelection(item.id)}
                          a11yTitle={`${item.name} aus Vergleich entfernen`}
                        />
                      )}
                    </Box>
                  ))}
                </Box>

                <Button
                  label="Vergleichen"
                  primary
                  disabled={selectedItems.length === 0}
                  onClick={handleCompare}
                />
              </Box>
            </>
          )}
        </Box>
      </Box>
      {/* Vergleichsauswahl */}
    </>
  );
};

export default CompareOverview;
