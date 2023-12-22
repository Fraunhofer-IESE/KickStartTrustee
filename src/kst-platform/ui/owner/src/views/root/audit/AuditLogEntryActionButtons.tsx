import { Button, Stack } from "@mui/material";
import { OwnerAuditLogEntryDTO } from "owner-api";
import { FC, useCallback, useMemo } from "react";
import { FormattedMessage } from "react-intl";
import { useNavigate } from "react-router-dom";

const ConsentCreatedEventType = "ConsentCreated";
const ConsentRevokedEventType = "ConsentRevoked";
const ConsentEventTypes = [ConsentCreatedEventType, ConsentRevokedEventType];

const DataItemCreatedEventType = "DataItemCreated";
const DataItemConsumedEventType = "DataItemConsumed";
const DataItemDeletedEventType = "DataItemDeleted";
const DataItemEventTypes = [
  DataItemCreatedEventType,
  DataItemConsumedEventType,
  DataItemDeletedEventType,
];

export type AuditLogEntryActionButtonsProps = {
  auditLogEntry?: OwnerAuditLogEntryDTO;
};

const AuditLogEntryActionButtons: FC<AuditLogEntryActionButtonsProps> = (
  props
) => {
  const { auditLogEntry } = props;
  const navigate = useNavigate();

  const isConsentEvent = useMemo(() => {
    if (!auditLogEntry) {
      return false;
    }
    const { eventType } = auditLogEntry;
    return ConsentEventTypes.includes(eventType);
  }, [auditLogEntry]);

  const isDataItemEvent = useMemo(() => {
    if (!auditLogEntry) {
      return false;
    }
    const { eventType } = auditLogEntry;
    return DataItemEventTypes.includes(eventType);
  }, [auditLogEntry]);

  const isDataItemDeleteEvent = useMemo(() => {
    if (!auditLogEntry) {
      return false;
    }
    const { eventType } = auditLogEntry;
    return eventType === DataItemDeletedEventType;
  }, [auditLogEntry]);

  const handleConsentDetailsClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      const consentId = auditLogEntry?.entityId;
      if (!consentId) {
        return;
      }
      navigate(`/consent/${consentId}`);
    },
    [auditLogEntry?.entityId, navigate]
  );

  const handleDataItemDetailsClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      const dataItemId = auditLogEntry?.entityId;
      if (!dataItemId) {
        return;
      }
      navigate(`/data/${dataItemId}`);
    },
    [auditLogEntry?.entityId, navigate]
  );

  return (
    <Stack direction="row" spacing={1}>
      {isConsentEvent && (
        <Button
          disabled={!auditLogEntry?.entityId}
          variant="contained"
          onClick={handleConsentDetailsClicked}
        >
          <FormattedMessage id="event_view_consent_details" />
        </Button>
      )}
      {isDataItemEvent && !isDataItemDeleteEvent && (
        <Button
          disabled={!auditLogEntry?.entityId}
          variant="contained"
          onClick={handleDataItemDetailsClicked}
        >
          <FormattedMessage id="event_view_data_item_details" />
        </Button>
      )}
      {isDataItemEvent && isDataItemDeleteEvent && (
        <Button disabled={true}>
          <FormattedMessage id="event_data_item_deleted" />
        </Button>
      )}
    </Stack>
  );
};

export default AuditLogEntryActionButtons;
