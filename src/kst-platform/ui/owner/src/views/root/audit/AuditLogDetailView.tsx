import { Stack } from "@mui/material";
import {
  DataItemTypeDTO,
  OwnerAuditLogEntryDTO,
  ResponseError,
} from "owner-api";
import { Fragment } from "react";
import { FormattedMessage } from "react-intl";
import { LoaderFunction, useLoaderData } from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import useDateTimeFormatter from "../../../hooks/useDateTimeFormatter";
import useDataItemTypeFormatter from "../../../hooks/useDataItemTypeFormatter";
import DataField from "../../../components/data-field/DataField";
import useEventTypeFormatter from "../../../hooks/useEventTypeFormatter";
import AuditLogEntryActionButtons from "./AuditLogEntryActionButtons";

export type TAuditLogDetailData = {
  loaded: boolean;
  auditLogEntry?: OwnerAuditLogEntryDTO;
  dataItemTypes?: Array<DataItemTypeDTO>;
};

const handleResponseError = (id: string, e: ResponseError) => {
  const response = e.response;
  switch (response.status) {
    case 404:
      throw new Response(`Audit log entry with id ${id} not found!`, {
        status: 404,
      });
    default:
      break;
  }
  throw response;
};

export const auditLogDetailLoader: LoaderFunction = async (
  args
): Promise<TAuditLogDetailData> => {
  const { params, request } = args;
  const { auditLogEntryId } = params;
  if (!auditLogEntryId) {
    // This is an internal bug, therefore don't handle that graceful because it should never happen
    throw new Error("No id given!");
  }
  try {
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const auditApi = await APIFactory.createAuditApi();
    const auditLogEntry = await auditApi.getAuditLogEntryById(auditLogEntryId, {
      signal: request.signal,
    });
    return { loaded: true, auditLogEntry, dataItemTypes };
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(auditLogEntryId, e);
    }
    return { loaded: true };
  }
};

const AuditLogDetailView = () => {
  const {
    auditLogEntry,
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TAuditLogDetailData;
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);
  const dateTimeFormatter = useDateTimeFormatter();
  const eventTypeFormatter = useEventTypeFormatter();

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="event_details" />
      </PageHeader>
      {loaded && (
        <Fragment>
          <Stack spacing={1}>
            <DataField
              name="identifier"
              label={<FormattedMessage id="event_id" />}
              value={auditLogEntry?.id}
            />
            <DataField
              name="event-type"
              label={<FormattedMessage id="event_typ" />}
              value={eventTypeFormatter(auditLogEntry?.eventType)}
            />
            <DataField
              name="timestamp"
              label={<FormattedMessage id="event_timestamp" />}
              value={dateTimeFormatter(auditLogEntry?.timestamp)}
            />
            <DataField
              name="user-id"
              label={<FormattedMessage id="event_user_id" />}
              value={auditLogEntry?.actorId}
            />
            <DataField
              name="operation"
              label={<FormattedMessage id="event_operation" />}
              value={auditLogEntry?.operation}
            />
            <DataField
              name="data-item-types"
              label={<FormattedMessage id="event_data_items" />}
              value={dataItemTypeFormatter(auditLogEntry?.dataItemTypes)}
            />
            <AuditLogEntryActionButtons auditLogEntry={auditLogEntry} />
          </Stack>
        </Fragment>
      )}
    </Fragment>
  );
};

export default AuditLogDetailView;
