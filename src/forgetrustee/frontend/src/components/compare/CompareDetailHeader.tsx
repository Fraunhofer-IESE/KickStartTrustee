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

import { Box, Button, CheckBox, Grid, Heading, Image, Text } from "grommet";
import { useNavigate } from "react-router-dom";
import placeholder from "../../assets/placeholder.svg";
import { routes } from "../../config/routes";
import { getModelingFreeSessionState } from "../modeling/common/modelingSession";
import type { DataTrusteeModelResponseDto } from "../../types/generated/dataTrusteeModelResponseDto";
import { COMPARE_GRID_COLUMNS } from "./compareColumns";
import { SESSION_COMPARE_ITEM_ID } from "./compareSession";
import { toYesNo, unwrapCommentedString } from "../shared/commentedValue";
import useAvatarDataUrl from "../shared/useAvatarDataUrl";

type CompareModelMeta = {
  modelId?: string;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
  isOwnModel?: boolean;
};

type CompareDetailHeaderProps = {
  item1: DataTrusteeModelResponseDto;
  item2: DataTrusteeModelResponseDto;
  meta1?: CompareModelMeta;
  meta2?: CompareModelMeta;
  highlightDifferences: boolean;
  onToggleHighlight: (nextValue: boolean) => void;
};

const getModelTitle = (model: DataTrusteeModelResponseDto, fallback: string) =>
  unwrapCommentedString(model.core?.dataTrusteeName) || fallback;

const getRecordField = (obj: unknown, fieldName: string): unknown => {
  if (!obj || typeof obj !== "object") {
    return undefined;
  }

  const value = (obj as Record<string, unknown>)[fieldName];
  return value;
};

const formatMotivationLabel = (value: string | undefined): string => {
  if (!value) {
    return "—";
  }

  if (value === "AMBIGUOUS") {
    return "Unklar";
  }

  if (value === "NONE") {
    return "Keine Auswahl";
  }

  return value
    .replaceAll("_", " ")
    .toLocaleLowerCase("de-DE")
    .replace(/(^|[\s-])(\p{L})/gu, (_match, prefix: string, letter: string) => {
      return `${prefix}${letter.toLocaleUpperCase("de-DE")}`;
    });
};

const getMotivationPills = (
  label: string,
  values: Array<{ label?: string; value?: string }> | undefined,
) =>
  (values ?? []).flatMap((entry) => {
    const entryLabel =
      formatMotivationLabel(entry.label?.trim()) || formatMotivationLabel(entry.value?.trim());

    if (!entryLabel || entryLabel === "—") {
      return [];
    }

    return [{ label, value: entryLabel }];
  });

const getDataCategory = (model: DataTrusteeModelResponseDto): string => {
  const category = model.core?.dataCategoryName ?? getRecordField(model.legal, "dataCategoryName");
  const normalizedCategory = unwrapCommentedString(category);
  if (normalizedCategory) {
    return normalizedCategory;
  }
  return "—";
};

const getTagPills = (
  model: DataTrusteeModelResponseDto,
): Array<{ label: string; value: string }> => {
  const rightsHolder = unwrapCommentedString(model.core?.rightsHolderName) || "—";
  const dataConsumer = unwrapCommentedString(model.core?.dataConsumerName) || "—";
  const dataOwner =
    toYesNo(model.core?.rightsHolderIsRepresented) === "Nein"
      ? unwrapCommentedString(model.core?.rightsHolderName) || "—"
      : unwrapCommentedString(model.core?.dataOwnerName) || "—";
  const dataTrustee = unwrapCommentedString(model.core?.dataTrusteeName) || "—";

  return [
    {
      label: "Rechteinhaber",
      value: unwrapCommentedString(model.core?.rightsHolderName) || rightsHolder,
    },
    ...getMotivationPills("Motivation Rechteinhaber", model.objectives?.motivationRightsHolder),
    { label: "Datennutzer", value: dataConsumer },
    ...getMotivationPills("Motivation Datennutzer", model.objectives?.motivationDataConsumer),
    { label: "Dateninhaber", value: dataOwner },
    ...getMotivationPills("Motivation Dateninhaber", model.objectives?.motivationDataHolder),
    { label: "Datentreuhänder", value: dataTrustee },
  ];
};

const CompareDetailHeader = ({
  item1,
  item2,
  meta1,
  meta2,
  highlightDifferences,
  onToggleHighlight,
}: CompareDetailHeaderProps) => {
  const navigate = useNavigate();
  const sessionPayload = getModelingFreeSessionState().payload;
  const detailBasePath = routes.dataTrusteeModelingDetail.path.replace("/:modelId?", "");
  const item1AvatarDataUrl = useAvatarDataUrl(meta1?.modelId, meta1?.avatarDataUrl ?? null);
  const item2AvatarDataUrl = useAvatarDataUrl(meta2?.modelId, meta2?.avatarDataUrl ?? null);

  const summaries = [
    {
      key: "item1",
      title: getModelTitle(item1, "Modell 1"),
      model: item1,
      meta: meta1,
      avatarDataUrl: item1AvatarDataUrl,
    },
    {
      key: "item2",
      title: getModelTitle(item2, "Modell 2"),
      model: item2,
      meta: meta2,
      avatarDataUrl: item2AvatarDataUrl,
    },
  ];

  const openDetail = (model: DataTrusteeModelResponseDto, meta?: CompareModelMeta) => {
    if (meta?.isOwnModel && sessionPayload?.modelBuildResult) {
      navigate(detailBasePath, {
        state: {
          modelBuildResult: sessionPayload.modelBuildResult,
          dataTrusteeModel: model,
          avatarDataUrl: meta?.avatarDataUrl ?? null,
          avatarFileName: meta?.avatarFileName ?? null,
        },
      });
      return;
    }

    if (meta?.modelId) {
      navigate(`${detailBasePath}/${encodeURIComponent(meta.modelId)}`, {});
      return;
    }

    navigate(detailBasePath, {
      state: {
        dataTrusteeModel: model,
        avatarDataUrl: meta?.avatarDataUrl ?? null,
        avatarFileName: meta?.avatarFileName ?? null,
      },
    });
  };

  const openReplaceSelection = (meta: CompareModelMeta | undefined, index: number) => {
    const removeSelectedId =
      meta?.modelId ?? (meta?.isOwnModel ? SESSION_COMPARE_ITEM_ID : undefined);

    navigate(routes.dataTrusteeCompare.path, {
      state: {
        removeSelectedId,
        removeSelectionIndex: index + 1,
      },
    });
  };

  return (
    <Box
      fill="horizontal"
      gap="small"
    >
      <Grid
        columns={[...COMPARE_GRID_COLUMNS]}
        gap="small"
      >
        <Box
          pad={{ top: "xsmall" }}
          justify="end"
        >
          <CheckBox
            checked={highlightDifferences}
            label="Unterschiede hervorheben"
            onChange={(event) => onToggleHighlight(event.target.checked)}
            toggle
          />
        </Box>

        {summaries.map((summary, index) => {
          const category = getDataCategory(summary.model);
          const hasPersonalData = toYesNo(
            summary.model.core?.containPersonalInformation ??
              getRecordField(summary.model.legal, "containPersonalInformation"),
          );
          const hasBusinessSecrets = toYesNo(
            getRecordField(summary.model.core, "containBusinessSecrets") ??
              getRecordField(summary.model.legal, "containBusinessSecrets") ??
              getRecordField(summary.model.data, "containBusinessSecrets"),
          );
          const tags = getTagPills(summary.model);

          return (
            <Box
              key={summary.key}
              pad="small"
              round="small"
              border={{ color: "border-light", size: "xsmall" }}
              gap="small"
              elevation="small"
            >
              <Box
                direction="row"
                gap="medium"
              >
                <Button
                  plain
                  label={<Text size="small">Zur Detailansicht</Text>}
                  onClick={() => openDetail(summary.model, summary.meta)}
                />
                <Button
                  plain
                  label={<Text size="small">Modell austauschen</Text>}
                  onClick={() => openReplaceSelection(summary.meta, index)}
                />
              </Box>

              <Box
                direction="row"
                gap="small"
                align="center"
              >
                <Box
                  width="58px"
                  height="58px"
                  round="xsmall"
                  overflow="hidden"
                  border={{ color: "border", size: "xsmall" }}
                  background="background-front"
                  flex={false}
                >
                  <Image
                    src={summary.avatarDataUrl || placeholder}
                    fit="cover"
                  />
                </Box>

                <Box gap="xxsmall">
                  <Heading
                    margin="none"
                    level={4}
                  >
                    {unwrapCommentedString(summary.title) || summary.title}
                  </Heading>
                  <Text size="small">Datenkategorie: {category}</Text>
                  <Text size="small">Personenbezogene Daten: {hasPersonalData}</Text>
                  <Text size="small">Geschäftsgeheimnisse: {hasBusinessSecrets}</Text>
                </Box>
              </Box>

              <Box
                direction="row"
                gap="xsmall"
                wrap
                style={{ rowGap: "4px" }}
              >
                {tags.map((tag) => (
                  <Box
                    key={`${summary.key}-${tag.label}`}
                    pad={{ horizontal: "xsmall", vertical: "xxsmall" }}
                    round="xsmall"
                    border={{ color: "brand", size: "xsmall" }}
                    background="background-front"
                  >
                    <Text size="small">{`${tag.label}: ${tag.value}`}</Text>
                  </Box>
                ))}
              </Box>

              <Text
                size="small"
                color="text-weak"
              >
                {unwrapCommentedString(summary.model.core?.dataTrusteeDescription) ||
                  "Keine Beschreibung"}
              </Text>
            </Box>
          );
        })}
      </Grid>
    </Box>
  );
};

export default CompareDetailHeader;
