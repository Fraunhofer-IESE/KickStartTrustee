import { GridColDef } from "@mui/x-data-grid";
import {
  DataItemTypeDTO,
  OwnerAuditLogEntryDTO,
  ResponseError,
} from "owner-api";
import { Fragment, useCallback, useEffect, useMemo, useState } from "react";
import { FormattedMessage, IntlShape, useIntl } from "react-intl";
import {
  LoaderFunction,
  isRouteErrorResponse,
  useLoaderData,
  useLocation,
  useNavigate,
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
import KeycloakService from "../../../services/KeycloakService";
import AppConfig from "../../../config/AppConfig";
import { eventTypeFormatter } from "../../../hooks/useEventTypeFormatter";
import AuditLogEntryActionButtons from "./AuditLogEntryActionButtons";
import ProfileCacheService from "../../../services/ProfileCacheService";

export type TAuditLogData = {
  loaded: boolean;
  eventTypes: Array<string>;
  auditLogEntries: Array<OwnerAuditLogEntryDTO>;
  dataItemTypes?: Array<DataItemTypeDTO>;
  clientId: string;
};

const getUrlSearchParams = (request: Request): URLSearchParams => {
  const url = new URL(request.url);
  return url.searchParams;
};

export const auditLogLoader: LoaderFunction = async (
  args
): Promise<TAuditLogData | Response> => {
  try {
    const config = await AppConfig.getInstance().getConfig();
    const { request } = args;
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const auditApi = await APIFactory.createAuditApi();
    const eventTypes = await auditApi.getEventTypes({ signal: request.signal });
    const searchParams = getUrlSearchParams(request);
    const eventType = searchParams.get("event-type") ?? undefined;
    const timeRange = searchParams.get("time-range");
    const begin = await getBegin(timeRange, request);
    const end = new Date();
    const auditLogEntries = await auditApi.getAuditLogEntries(
      begin,
      end,
      eventType,
      { signal: request.signal }
    );
    return {
      loaded: true,
      eventTypes,
      auditLogEntries,
      dataItemTypes,
      clientId: config.keycloakClientId,
    };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true, eventTypes: [], auditLogEntries: [], clientId: "" };
  }
};

export const TimeFilter = {
  LastMinute: "LAST_MINUTE",
  LastHour: "LAST_HOUR",
  LastHalfDay: "LAST_12_HOURS",
  LastDay: "LAST_24_HOURS",
} as const;

export const defaultFilterQueryParams = `time-range=${TimeFilter.LastDay}`;

export const getBegin = async (
  filter: string | null,
  request: Request
): Promise<Date> => {
  const result = new Date();
  switch (filter) {
    case TimeFilter.LastMinute:
      result.setTime(result.getTime() - 60 * 1000);
      break;
    case TimeFilter.LastHour:
      result.setTime(result.getTime() - 3600 * 1000);
      break;
    case TimeFilter.LastHalfDay:
      result.setTime(result.getTime() - 12 * 3600 * 1000);
      break;
    case TimeFilter.LastDay:
      result.setTime(result.getTime() - 24 * 3600 * 1000);
      break;
    default:
      break;
  }
  if (!filter) {
    const profile = await ProfileCacheService.getInstance().getProfile(request);
    const { createdAt } = profile;
    const noFilterTime = createdAt
      ? createdAt.getTime()
      : result.getTime() - 365 * 3600 * 1000;
    result.setTime(noFilterTime);
  }
  return result;
};

const createColumns = (
  clientId: string,
  dataItemTypeFormatter: TDataItemTypeFormatter,
  intl: IntlShape
): GridColDef[] => [
  {
    field: "timestamp",
    headerName: intl.formatMessage({ id: "event_timestamp" }),
    width: 200,
    valueFormatter: ({ value }) => dateTimeFormatter(intl, value),
  },
  {
    field: "eventType",
    headerName: intl.formatMessage({ id: "event_typ" }),
    width: 200,
    valueGetter: ({ value }) => eventTypeFormatter(value),
  },
  {
    field: "actorId",
    headerName: intl.formatMessage({ id: "event_user_id" }),
    width: 200,
    valueGetter: ({ value }) =>
      value !== clientId
        ? value
        : `${KeycloakService.getInstance().getUserName()} (You)`,
  },
  {
    field: "dataItemTypes",
    headerName: intl.formatMessage({ id: "event_data_items" }),
    width: 320,
    type: "string",
    valueGetter: ({ value }) => dataItemTypeFormatter(value),
  },
  {
    field: "actions",
    headerName: intl.formatMessage({ id: "actions" }),
    width: 400,
    sortable: false,
    renderCell: ({ row }) => <AuditLogEntryActionButtons auditLogEntry={row} />,
  },
];

const AuditLogView = () => {
  const {
    auditLogEntries,
    eventTypes,
    dataItemTypes = [],
    clientId,
    loaded,
  } = useLoaderData() as TAuditLogData;
  const intl = useIntl();
  const location = useLocation();
  const navigate = useNavigate();
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);

  const columns = useMemo(
    () => createColumns(clientId, dataItemTypeFormatter, intl),
    [clientId, dataItemTypeFormatter, intl]
  );
  const filters = useMemo(
    () => [
      {
        name: "event-type",
        label: intl.formatMessage({ id: "event_typ" }),
        values: Object.fromEntries(
          eventTypes.map((v) => [eventTypeFormatter(v), v])
        ),
      },
      {
        name: "time-range",
        label: intl.formatMessage({ id: "time_range" }),
        values: TimeFilter,
      },
    ],
    [eventTypes, intl]
  );
  const error = useMemo(() => location.state?.error, [location.state]);
  const [isErrorDialogOpen, setIsErrorDialogOpen] = useState<boolean>(
    () => error !== undefined && error !== null
  );

  const handleClose = useCallback(() => {
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
        <FormattedMessage id="all_events" />
      </PageHeader>
      {loaded ? (
        <FilterableList
          filters={filters}
          columns={columns}
          rows={auditLogEntries}
          disableRowClick={true}
        />
      ) : (
        <Loader />
      )}
      <ErrorDialog
        open={isErrorDialogOpen}
        onClose={handleClose}
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

export default AuditLogView;
