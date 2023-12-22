import { Button, Stack } from "@mui/material";
import { DataItemDTO, DataItemTypeDTO, ResponseError } from "owner-api";
import { Fragment, useCallback, useMemo, useRef, useState } from "react";
import { FormattedMessage } from "react-intl";
import {
  ActionFunction,
  LoaderFunction,
  SubmitOptions,
  json,
  redirect,
  useLoaderData,
  useSubmit,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import useDateTimeFormatter from "../../../hooks/useDateTimeFormatter";
import useDataItemTypeFormatter from "../../../hooks/useDataItemTypeFormatter";
import DataField from "../../../components/data-field/DataField";
import DataDeleteConfirmationDialog from "./DataDeleteConfirmationDialog";
import DetailsContainer from "../../../components/details/DetailsContainer";
import DetailsContentContainer from "../../../components/details/DetailsContentContainer";
import useDataDownload from "../../../hooks/useDataDowload";

export type TDataDetailData = {
  loaded: boolean;
  dataItem?: DataItemDTO;
  dataItemTypes: Array<DataItemTypeDTO>;
};

const PREVIEW_LENGTH = 32;

const handleResponseError = (id: string, e: ResponseError) => {
  const response = e.response;
  switch (response.status) {
    case 404:
      throw json(
        {
          message: {
            id: "data_item_not_found_message",
            values: {
              dataItemId: id,
            },
          },
        },
        {
          status: 404,
        }
      );
    default:
      break;
  }
  throw response;
};

export const dataDetailLoader: LoaderFunction = async (
  args
): Promise<TDataDetailData> => {
  const { params, request } = args;
  const { dataItemId } = params;
  if (!dataItemId) {
    // This is an internal bug, therefore don't handle that graceful because it should never happen
    throw new Error("No id given!");
  }
  try {
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const ownerDataApi = await APIFactory.createDataApi();
    const dataItem = await ownerDataApi.getDataItemById1(dataItemId, {
      signal: request.signal,
    });
    return { loaded: true, dataItem, dataItemTypes };
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(dataItemId, e);
    }
    return { loaded: true, dataItemTypes: [] };
  }
};

export const dataDeleteAction: ActionFunction = async (args) => {
  const { params, request } = args;
  const { dataItemId } = params;
  if (!dataItemId) {
    // This is an internal bug, therefore don't handle that graceful because it should never happen
    throw new Error("No id given!");
  }
  try {
    const ownerDataApi = await APIFactory.createDataApi();
    await ownerDataApi.deleteDataItemById(dataItemId, {
      signal: request.signal,
    });
    return redirect(`/data`);
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(dataItemId, e);
    }
  }
};

const DataDetailView = () => {
  const {
    dataItem,
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TDataDetailData;
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);
  const dateTimeFormatter = useDateTimeFormatter();
  const { createDataBlob, downloadData } = useDataDownload();
  const submit = useSubmit();
  const [isDeleteConfirmationDialogOpen, setIsDeleteConfirmationDialogOpen] =
    useState<boolean>(false);

  const downloadLinkRef = useRef<HTMLAnchorElement>(null);

  const dataPreview = useMemo(() => {
    const jsonString = JSON.stringify(dataItem?.data, null, 2);
    if (jsonString.length > PREVIEW_LENGTH) {
      return jsonString.slice(0, PREVIEW_LENGTH - 3) + "...";
    }
    return jsonString;
  }, [dataItem?.data]);

  const handleViewClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      const viewData = async () => {
        const data = createDataBlob(dataItem?.data);
        const dataUrl = window.URL.createObjectURL(data);
        window.open(dataUrl, "_blank");
        window.URL.revokeObjectURL(dataUrl);
      };

      event.preventDefault();
      event.stopPropagation();
      viewData();
    },
    [createDataBlob, dataItem?.data]
  );

  const handleDownloadClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      downloadData(downloadLinkRef, dataItem);
    },
    [dataItem, downloadData]
  );

  const handleDeleteClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      setIsDeleteConfirmationDialogOpen(true);
    },
    []
  );

  const handleDeleteConfirmationDialogClose = useCallback(() => {
    setIsDeleteConfirmationDialogOpen(false);
  }, []);

  const handleDeleteConfirmationDialogConfirmed = useCallback(() => {
    const dataItemId = dataItem?.id;
    if (!dataItemId) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/data/${dataItemId}/delete`,
    };
    submit(null, submitOptions);
  }, [dataItem?.id, submit]);

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="data_item_details" />
      </PageHeader>
      <DetailsContainer>
        {loaded && (
          <Stack spacing={2}>
            <DetailsContentContainer variant="outlined">
              <Stack spacing={1}>
                <DataField
                  name="identifier"
                  label={<FormattedMessage id="data_item_id" />}
                  value={dataItem?.id}
                />
                <DataField
                  name="created-at"
                  label={<FormattedMessage id="data_item_created_at" />}
                  value={dateTimeFormatter(dataItem?.metaData.createdAt)}
                />
              </Stack>
            </DetailsContentContainer>
            <Stack spacing={4}>
              <DetailsContentContainer variant="outlined">
                <Stack spacing={1}>
                  <DataField
                    name="provider"
                    label={<FormattedMessage id="data_item_provider" />}
                    value={dataItem?.metaData.providerId}
                  />
                  <DataField
                    name="type"
                    label={<FormattedMessage id="data_item_type" />}
                    value={
                      dataItem?.metaData.dataItemType
                        ? dataItemTypeFormatter([
                            dataItem?.metaData.dataItemType,
                          ])
                        : ""
                    }
                  />
                </Stack>
              </DetailsContentContainer>
              <DetailsContentContainer variant="outlined">
                <Stack spacing={1}>
                  <DataField
                    name="data-preview"
                    label={<FormattedMessage id="data_item_data" />}
                    multiline={true}
                    value={dataPreview}
                  />
                </Stack>
              </DetailsContentContainer>
              <Stack direction="row" spacing={1}>
                <Button
                  disabled={!dataItem?.id}
                  variant="contained"
                  onClick={handleViewClicked}
                >
                  <FormattedMessage id="data_item_data_view" />
                </Button>
                <Button
                  disabled={!dataItem?.id}
                  variant="outlined"
                  onClick={handleDownloadClicked}
                >
                  <FormattedMessage id="data_item_data_download" />
                </Button>
                <Button
                  disabled={!dataItem?.id}
                  variant="outlined"
                  color="error"
                  onClick={handleDeleteClicked}
                >
                  <FormattedMessage id="data_item_delete" />
                </Button>
              </Stack>
            </Stack>
            <a
              ref={downloadLinkRef}
              hidden={true}
              aria-hidden={true}
              href={`${dataItem?.id}.json`}
            >
              Download
            </a>
            <DataDeleteConfirmationDialog
              dataItemId={dataItem?.id}
              open={isDeleteConfirmationDialogOpen}
              onClose={handleDeleteConfirmationDialogClose}
              onConfirmed={handleDeleteConfirmationDialogConfirmed}
            />
          </Stack>
        )}
      </DetailsContainer>
    </Fragment>
  );
};

export default DataDetailView;
