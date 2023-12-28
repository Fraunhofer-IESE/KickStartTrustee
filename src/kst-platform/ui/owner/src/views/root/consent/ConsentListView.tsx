import { GridColDef } from "@mui/x-data-grid";
import {
  DataItemTypeDTO,
  GetOwnedConsentsStatusEnum,
  OwnerConsentDTO,
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
} from "../../../hooks/useConsentConverter";
import useDataItemTypeFormatter, {
  TDataItemTypeFormatter,
} from "../../../hooks/useDataItemTypeFormatter";
import { dateTimeFormatter } from "../../../hooks/useDateTimeFormatter";
import { Button, Stack, Typography } from "@mui/material";
import CircleIcon from "@mui/icons-material/Circle";
import ActionButton from "../../../components/list/ActionButton";
import ConsentRevokeConfirmationDialog from "./ConsentRevokeConfirmationDialog";

export type TConsentListData = {
  loaded: boolean;
  consents?: Array<OwnerConsentDTO>;
  dataItemTypes?: Array<DataItemTypeDTO>;
};

export const defaultFilterQueryParams = `status=${GetOwnedConsentsStatusEnum.Active}`;

export const consentListLoader: LoaderFunction = async (
  args
): Promise<TConsentListData> => {
  try {
    const { request } = args;
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const url = new URL(request.url);
    const status = url.searchParams.get("status") ?? undefined;
    const consentApi = await APIFactory.createConsentApi();
    const consents = await consentApi.getOwnedConsents(
      status as GetOwnedConsentsStatusEnum,
      { signal: request.signal }
    );
    return { loaded: true, consents, dataItemTypes };
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
  handleViewDetailsButtonClicked: (entry: V) => void,
  handleRevokeButtonClicked: (entry: V) => void
): GridColDef[] => [
  {
    field: "actor",
    headerName: intl.formatMessage({ id: "consent_consumer" }),
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
    headerName: intl.formatMessage({ id: "consent_created_at" }),
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
        {row.status === GetOwnedConsentsStatusEnum.Active ? (
          <Stack direction="row" spacing={1}>
            <ActionButton
              variant="outlined"
              color="error"
              data={row}
              onClick={handleRevokeButtonClicked}
            >
              <FormattedMessage id="consent_revoke" />
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

const ConsentListView = () => {
  const {
    consents = [],
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TConsentListData;
  const intl = useIntl();
  const location = useLocation();
  const navigate = useNavigate();
  const fetcher = useFetcher();
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);

  const [isRevokeConfirmationDialogOpen, setIsRevokeConfirmationDialogOpen] =
    useState<boolean>(false);

  const [selectedConsent, setSelectedConsent] = useState<OwnerConsentDTO>();

  const handleViewDetailsButtonClicked = useCallback(
    (entry: OwnerConsentDTO) => {
      const { id } = entry;
      navigate(`${id}`);
    },
    [navigate]
  );

  const handleRevokeButtonClicked = useCallback((entry: OwnerConsentDTO) => {
    setSelectedConsent(entry);
    setIsRevokeConfirmationDialogOpen(true);
  }, []);

  const handleRevokeConfirmationDialogClose = useCallback(() => {
    setIsRevokeConfirmationDialogOpen(false);
    setSelectedConsent(undefined);
  }, []);

  const handleRevokeConfirmationDialogConfirmed = useCallback(() => {
    const id = selectedConsent?.id;
    if (!id) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/consent/${id}/revoke`,
    };
    fetcher.submit(null, submitOptions);
    setIsRevokeConfirmationDialogOpen(false);
    setSelectedConsent(undefined);
  }, [selectedConsent?.id, fetcher]);

  const columns = useMemo(
    () =>
      createColumns(
        dataItemTypeFormatter,
        intl,
        handleViewDetailsButtonClicked,
        handleRevokeButtonClicked
      ),
    [
      dataItemTypeFormatter,
      intl,
      handleViewDetailsButtonClicked,
      handleRevokeButtonClicked,
    ]
  );
  const filters = useMemo(
    () => [
      {
        name: "status",
        label: intl.formatMessage({ id: "consent_status" }),
        values: GetOwnedConsentsStatusEnum,
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
        <FormattedMessage id="my_consents" />
      </PageHeader>
      {loaded ? (
        <FilterableList
          filters={filters}
          columns={columns}
          rows={consents}
          disableRowClick={true}
        />
      ) : (
        <Loader />
      )}
      <ConsentRevokeConfirmationDialog
        open={isRevokeConfirmationDialogOpen}
        onClose={handleRevokeConfirmationDialogClose}
        onConfirmed={handleRevokeConfirmationDialogConfirmed}
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

export default ConsentListView;
