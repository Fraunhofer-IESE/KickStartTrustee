import { GridColDef } from "@mui/x-data-grid";
import { DataItemDTO, DataItemTypeDTO, ResponseError } from "owner-api";
import {
  Fragment,
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import { FormattedMessage, IntlShape, useIntl } from "react-intl";
import {
  LoaderFunction,
  SubmitOptions,
  useLoaderData,
  useLocation,
  useNavigate,
  useSubmit,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import ErrorDialog from "../../../components/dialogs/ErrorDialog";
import PageHeader from "../../../components/header/PageHeader";
import FilterableList from "../../../components/list/FilterableList";
import Loader from "../../../components/loader/loader";
import { dateTimeFormatter } from "../../../hooks/useDateTimeFormatter";
import useDataItemTypeFormatter, {
  TDataItemTypeFormatter,
} from "../../../hooks/useDataItemTypeFormatter";
import ActionButton from "../../../components/list/ActionButton";
import { Stack } from "@mui/material";
import DataDeleteConfirmationDialog from "./DataDeleteConfirmationDialog";
import useDataDownload from "../../../hooks/useDataDowload";
import useErrorMessageFormatter from "../../../hooks/useErrorMessageFormatter";

export type TDataListData = {
  loaded: boolean;
  dataItems: Array<DataItemDTO>;
  dataItemTypes: Array<DataItemTypeDTO>;
};

const getUrlSearchParams = (request: Request): URLSearchParams => {
  const url = new URL(request.url);
  return url.searchParams;
};

export const dataListLoader: LoaderFunction = async (
  args
): Promise<TDataListData | Response> => {
  try {
    const { request } = args;
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const searchParams = getUrlSearchParams(request);
    const dataItemType = searchParams.get("data-item-type") ?? undefined;
    const provider = searchParams.get("provider") ?? undefined;
    const ownerDataApi = await APIFactory.createDataApi();
    const dataItems = await ownerDataApi.getDataItems1(dataItemType, provider, {
      signal: request.signal,
    });
    return {
      loaded: true,
      dataItems,
      dataItemTypes,
    };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true, dataItems: [], dataItemTypes: [] };
  }
};

const createColumns = <V,>(
  dataItemTypeFormatter: TDataItemTypeFormatter,
  intl: IntlShape,
  onViewDetails: (value: V) => void,
  onDownload: (value: V) => void,
  onDeleteItem: (value: V) => void
): GridColDef[] => [
  {
    field: "id",
    headerName: intl.formatMessage({ id: "data_item_id" }),
    width: 260,
  },
  {
    field: "createdAt",
    headerName: intl.formatMessage({ id: "data_item_created_at" }),
    width: 200,
    valueFormatter: ({ value }) => dateTimeFormatter(intl, value),
    valueGetter: ({ row }: { row: DataItemDTO }) => row.metaData.createdAt,
  },
  {
    field: "provider",
    headerName: intl.formatMessage({ id: "data_item_provider" }),
    width: 200,
    valueGetter: ({ row }: { row: DataItemDTO }) => row.metaData.providerId,
  },
  {
    field: "dataItemType",
    headerName: intl.formatMessage({ id: "data_item_type" }),
    width: 200,
    type: "string",
    valueFormatter: ({ value }) => dataItemTypeFormatter([value]),
    valueGetter: ({ row }: { row: DataItemDTO }) => row.metaData.dataItemType,
  },
  {
    field: "actions",
    headerName: intl.formatMessage({ id: "actions" }),
    width: 610,
    sortable: false,
    renderCell: ({ row }) => (
      <Stack direction="row" spacing={3}>
        <ActionButton variant="contained" onClick={onViewDetails} data={row}>
          <FormattedMessage id="data_item_view_details" />
        </ActionButton>
        <Stack direction="row" spacing={1}>
          <ActionButton variant="outlined" onClick={onDownload} data={row}>
            <FormattedMessage id="data_item_data_download" />
          </ActionButton>
          <ActionButton
            color="error"
            variant="outlined"
            onClick={onDeleteItem}
            data={row}
          >
            <FormattedMessage id="data_item_delete" />
          </ActionButton>
        </Stack>
      </Stack>
    ),
  },
];

const DataListView = () => {
  const {
    dataItems,
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TDataListData;
  const { downloadData } = useDataDownload();
  const intl = useIntl();
  const location = useLocation();
  const navigate = useNavigate();
  const submit = useSubmit();
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);
  const errorMessageFormatter = useErrorMessageFormatter();
  const error = useMemo(() => location.state?.error, [location.state]);

  const [isDeleteConfirmationDialogOpen, setIsDeleteConfirmationDialogOpen] =
    useState<boolean>(false);
  const [selectedDataItem, setSelectedDataItem] = useState<DataItemDTO>();
  const [isErrorDialogOpen, setIsErrorDialogOpen] = useState<boolean>(
    () => error !== undefined && error !== null
  );

  const downloadLinkRef = useRef<HTMLAnchorElement>(null);

  const handleViewDetails = useCallback(
    (dataItem: DataItemDTO) => {
      const { id } = dataItem;
      navigate(`/data/${id}`);
    },
    [navigate]
  );

  const handleDownloadClicked = useCallback(
    (dataItem: DataItemDTO) => downloadData(downloadLinkRef, dataItem),
    [downloadData]
  );

  const handleItemDeletion = useCallback((dataItem: DataItemDTO) => {
    setSelectedDataItem(dataItem);
    setIsDeleteConfirmationDialogOpen(true);
  }, []);

  const handleDeleteConfirmationDialogClose = useCallback(() => {
    setSelectedDataItem(undefined);
    setIsDeleteConfirmationDialogOpen(false);
  }, []);

  const handleDeleteConfirmed = useCallback(() => {
    const dataItemId = selectedDataItem?.id;
    if (!dataItemId) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/data/${dataItemId}/delete`,
    };
    submit(null, submitOptions);
    setSelectedDataItem(undefined);
    setIsDeleteConfirmationDialogOpen(false);
  }, [selectedDataItem?.id, submit]);

  const handleErrorDialogClose = useCallback(() => {
    const { pathname, state } = location;
    const newState = { ...state };
    delete newState.error;
    navigate(pathname, { replace: true, state: newState });
  }, [location, navigate]);

  const columns = useMemo(
    () =>
      createColumns(
        dataItemTypeFormatter,
        intl,
        handleViewDetails,
        handleDownloadClicked,
        handleItemDeletion
      ),
    [
      dataItemTypeFormatter,
      intl,
      handleViewDetails,
      handleDownloadClicked,
      handleItemDeletion,
    ]
  );

  const providers = useMemo(() => {
    return dataItems
      .map((d) => d.metaData.providerId)
      .filter((p, index, array) => {
        return p && array.indexOf(p) === index;
      });
  }, [dataItems]);

  const filters = useMemo(
    () => [
      {
        name: "data-item-type",
        label: intl.formatMessage({ id: "data_item_type" }),
        values: Object.fromEntries(dataItemTypes.map((v) => [v.name, v.id])),
      },
      {
        name: "provider",
        label: intl.formatMessage({ id: "data_item_provider" }),
        values: Object.fromEntries(providers.map((p) => [p, p])),
      },
    ],
    [dataItemTypes, intl, providers]
  );

  useEffect(() => {
    setIsErrorDialogOpen(error !== undefined && error !== null);
  }, [error]);

  const errorMessage = useMemo(
    () => errorMessageFormatter(error),
    [errorMessageFormatter, error]
  );

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="my_data_items" />
      </PageHeader>
      {loaded ? (
        <FilterableList
          filters={filters}
          columns={columns}
          rows={dataItems}
          disableRowClick={true}
        />
      ) : (
        <Loader />
      )}
      <a
        ref={downloadLinkRef}
        hidden={true}
        aria-hidden={true}
        href="dataItem.json"
        download="dataItem.json"
      >
        Download
      </a>
      <DataDeleteConfirmationDialog
        dataItemId={selectedDataItem?.id}
        open={isDeleteConfirmationDialogOpen}
        onClose={handleDeleteConfirmationDialogClose}
        onConfirmed={handleDeleteConfirmed}
      />
      <ErrorDialog
        open={isErrorDialogOpen}
        onClose={handleErrorDialogClose}
        message={errorMessage}
      />
    </Fragment>
  );
};

export default DataListView;
