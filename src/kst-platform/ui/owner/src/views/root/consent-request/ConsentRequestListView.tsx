import { GridColDef } from "@mui/x-data-grid";
import {
  DataItemTypeDTO,
  GetOwnedConsentRequestsStatusEnum,
  OwnerConsentRequestDTO,
  OwnerConsentRequestDTOStatusEnum,
  ResponseError,
} from "owner-api";
import { Fragment, useCallback, useEffect, useMemo, useState } from "react";
import { FormattedMessage, IntlShape, useIntl } from "react-intl";
import {
  LoaderFunction,
  SubmitOptions,
  isRouteErrorResponse,
  useLoaderData,
  useLocation,
  useNavigate,
  useFetcher,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import ErrorDialog from "../../../components/dialogs/ErrorDialog";
import PageHeader from "../../../components/header/PageHeader";
import FilterableList from "../../../components/list/FilterableList";
import Loader from "../../../components/loader/loader";
import {
  convertStatusText,
  convertStatusColor,
} from "../../../hooks/useConsentRequestConverter";
import useDataItemTypeFormatter, {
  TDataItemTypeFormatter,
} from "../../../hooks/useDataItemTypeFormatter";
import { dateTimeFormatter } from "../../../hooks/useDateTimeFormatter";
import { Button, Stack, Typography } from "@mui/material";
import CircleIcon from "@mui/icons-material/Circle";
import ActionButton from "../../../components/list/ActionButton";
import ConsentRequestAcceptConfirmationDialog from "./ConsentRequestAcceptConfirmationDialog";
import ConsentRequestRejectConfirmationDialog from "./ConsentRequestRejectConfirmationDialog";
import ConsentRequestAcceptedNotificationDialog from "./ConsentRequestAcceptedNotificationDialog";

export type TConsentRequestListData = {
  loaded: boolean;
  consentRequests?: Array<OwnerConsentRequestDTO>;
  dataItemTypes?: Array<DataItemTypeDTO>;
};

export const defaultFilterQueryParams = `status=${GetOwnedConsentRequestsStatusEnum.Pending}`;

export const consentRequestListLoader: LoaderFunction = async (
  args
): Promise<TConsentRequestListData> => {
  try {
    const { request } = args;
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const url = new URL(request.url);
    const status = url.searchParams.get("status") ?? undefined;
    const consentApi = await APIFactory.createConsentApi();
    const consentRequests = await consentApi.getOwnedConsentRequests(
      undefined,
      status as GetOwnedConsentRequestsStatusEnum,
      { signal: request.signal }
    );
    return { loaded: true, consentRequests, dataItemTypes };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true };
  }
};

const createColumns = <V,>(
  dataItemTypeFormatter: TDataItemTypeFormatter,
  intl: IntlShape,
  handleViewDetailsButtonClicked: (value: V) => void,
  handleAcceptButtonClicked: (value: V) => void,
  handleRejectButtonClicked: (value: V) => void
): GridColDef[] => [
  {
    field: "requester",
    headerName: intl.formatMessage({ id: "consent_requester" }),
    width: 200,
    valueGetter: ({ value }) => value.name,
  },
  {
    field: "consumedDataItemTypes",
    headerName: intl.formatMessage({ id: "consent_consumed_data_items" }),
    width: 230,
    type: "string",
    valueGetter: ({ value }) => dataItemTypeFormatter(value),
  },
  {
    field: "providedDataItemTypes",
    headerName: intl.formatMessage({ id: "consent_provided_data_items" }),
    width: 230,
    type: "string",
    valueGetter: ({ value }) => dataItemTypeFormatter(value),
  },
  {
    field: "createdAt",
    headerName: intl.formatMessage({ id: "consent_request_created_at" }),
    width: 200,
    valueFormatter: ({ value }) => dateTimeFormatter(intl, value),
  },
  {
    field: "status",
    headerName: intl.formatMessage({ id: "consent_status" }),
    width: 160,
    valueGetter: ({ value }) => convertStatusText(intl, value),
    renderCell: ({ value, row }) => (
      <Stack direction="row" spacing={0.5} alignItems="center">
        <CircleIcon fontSize="small" color={convertStatusColor(row.status)} />
        <Typography>{value}</Typography>
      </Stack>
    ),
  },
  {
    field: "actions",
    headerName: intl.formatMessage({ id: "actions" }),
    sortable: false,
    minWidth: 440,
    renderCell: ({ row }) => (
      <Stack direction="row" spacing={3}>
        <ActionButton
          variant="contained"
          data={row}
          onClick={handleViewDetailsButtonClicked}
        >
          <FormattedMessage id="view_details" />
        </ActionButton>
        {row.status === OwnerConsentRequestDTOStatusEnum.Pending ? (
          <Stack direction="row" spacing={1}>
            <ActionButton
              variant="outlined"
              color="success"
              data={row}
              onClick={handleAcceptButtonClicked}
            >
              <FormattedMessage id="consent_accept" />
            </ActionButton>
            <ActionButton
              variant="outlined"
              color="error"
              data={row}
              onClick={handleRejectButtonClicked}
            >
              <FormattedMessage id="consent_reject" />
            </ActionButton>
          </Stack>
        ) : (
          <Stack direction="row" spacing={1}>
            <Button disabled={true}>
              {convertStatusText(intl, row.status)}
            </Button>
          </Stack>
        )}
      </Stack>
    ),
  },
];

const ConsentRequestListView = () => {
  const {
    consentRequests = [],
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TConsentRequestListData;
  const intl = useIntl();
  const location = useLocation();
  const navigate = useNavigate();
  const fetcher = useFetcher();
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);

  const [isAcceptConfirmationDialogOpen, setIsAcceptConfirmationDialogOpen] =
    useState<boolean>(false);
  const [isRejectConfirmationDialogOpen, setIsRejectConfirmationDialogOpen] =
    useState<boolean>(false);
  const [
    isAcceptedNotificationDialogOpen,
    setIsAcceptedNotificationDialogOpen,
  ] = useState<boolean>(false);
  const [selectedConsentRequest, setSelectedConsentRequest] =
    useState<OwnerConsentRequestDTO>();

  const handleViewDetailsButtonClicked = useCallback(
    (entry: OwnerConsentRequestDTO) => {
      const { id } = entry;
      navigate(`${id}`);
    },
    [navigate]
  );

  const handleAcceptButtonClicked = useCallback(
    (entry: OwnerConsentRequestDTO) => {
      setSelectedConsentRequest(entry);
      setIsAcceptConfirmationDialogOpen(true);
    },
    []
  );

  const handleAcceptConfirmationDialogClose = useCallback(() => {
    setIsAcceptConfirmationDialogOpen(false);
    setSelectedConsentRequest(undefined);
  }, []);

  const handleAcceptConfirmationDialogConfirmed = useCallback(() => {
    const id = selectedConsentRequest?.id;
    if (!id) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/consent-request/${id}/accept`,
    };
    fetcher.submit(null, submitOptions);
    setIsAcceptConfirmationDialogOpen(false);
    setSelectedConsentRequest(undefined);
    setIsAcceptedNotificationDialogOpen(true);
  }, [selectedConsentRequest?.id, fetcher]);

  const handleRejectButtonClicked = useCallback(
    (entry: OwnerConsentRequestDTO) => {
      setSelectedConsentRequest(entry);
      setIsRejectConfirmationDialogOpen(true);
    },
    []
  );

  const handleRejectConfirmationDialogClose = useCallback(() => {
    setIsRejectConfirmationDialogOpen(false);
    setSelectedConsentRequest(undefined);
  }, []);

  const handleRejectConfirmationDialogConfirmed = useCallback(() => {
    const id = selectedConsentRequest?.id;
    if (!id) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/consent-request/${id}/reject`,
    };
    fetcher.submit(null, submitOptions);
    setIsRejectConfirmationDialogOpen(false);
    setSelectedConsentRequest(undefined);
  }, [selectedConsentRequest?.id, fetcher]);

  const handleAcceptedNotificationDialogClose = useCallback(() => {
    setIsAcceptedNotificationDialogOpen(false);
  }, []);

  const columns = useMemo(
    () =>
      createColumns(
        dataItemTypeFormatter,
        intl,
        handleViewDetailsButtonClicked,
        handleAcceptButtonClicked,
        handleRejectButtonClicked
      ),
    [
      dataItemTypeFormatter,
      intl,
      handleViewDetailsButtonClicked,
      handleAcceptButtonClicked,
      handleRejectButtonClicked,
    ]
  );
  const filters = useMemo(
    () => [
      {
        name: "status",
        label: intl.formatMessage({ id: "consent_status" }),
        values: GetOwnedConsentRequestsStatusEnum,
      },
    ],
    [intl]
  );

  const error = useMemo(() => location.state?.error, [location.state]);
  const [isErrorDialogOpen, setIsErrorDialogOpen] = useState<boolean>(
    () => error !== undefined && error !== null
  );

  const handleErrorDialogClose = useCallback(() => {
    const { pathname, state, search } = location;
    const newState = { ...state };
    delete newState.error;
    navigate(pathname + search, { replace: true, state: newState });
  }, [location, navigate]);

  useEffect(() => {
    setIsErrorDialogOpen(error !== undefined && error !== null);
  }, [error]);

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="my_consent_requests" />
      </PageHeader>
      {loaded ? (
        <FilterableList
          filters={filters}
          columns={columns}
          rows={consentRequests}
          disableRowClick={true}
        />
      ) : (
        <Loader />
      )}
      <ConsentRequestAcceptConfirmationDialog
        open={isAcceptConfirmationDialogOpen}
        onClose={handleAcceptConfirmationDialogClose}
        onConfirmed={handleAcceptConfirmationDialogConfirmed}
      />
      <ConsentRequestRejectConfirmationDialog
        open={isRejectConfirmationDialogOpen}
        onClose={handleRejectConfirmationDialogClose}
        onConfirmed={handleRejectConfirmationDialogConfirmed}
      />
      <ConsentRequestAcceptedNotificationDialog
        open={isAcceptedNotificationDialogOpen}
        onClose={handleAcceptedNotificationDialogClose}
      />
      <ErrorDialog
        open={isErrorDialogOpen}
        onClose={handleErrorDialogClose}
        message={
          isRouteErrorResponse(error) ? (
            `${error.data}`
          ) : (
            <FormattedMessage id="error_unknown" />
          )
        }
      />
    </Fragment>
  );
};

export default ConsentRequestListView;
