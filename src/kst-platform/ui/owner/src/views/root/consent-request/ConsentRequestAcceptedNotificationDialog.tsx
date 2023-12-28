import { FormattedMessage } from "react-intl";
import { FC } from "react";
import NotificationDialog from "../../../components/dialogs/NotificationDialog";

export type ConsentRequestAcceptedNotificationDialogProps = {
  open: boolean;
  onClose: () => void;
};

const ConsentRequestAcceptedNotificationDialog: FC<
  ConsentRequestAcceptedNotificationDialogProps
> = (props) => {
  const { open, onClose } = props;
  return (
    <NotificationDialog
      open={open}
      title={
        <FormattedMessage id="consent_request_accepted_notification_dialog_title" />
      }
      message={
        <FormattedMessage id="consent_request_accepted_notification_dialog_message" />
      }
      onClose={onClose}
    />
  );
};

export default ConsentRequestAcceptedNotificationDialog;
