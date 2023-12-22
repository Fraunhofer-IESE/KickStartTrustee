import { FormattedMessage } from "react-intl";
import ConfirmationDialog from "../../../components/dialogs/ConfirmationDialog";
import { FC } from "react";

export type ConsentRequestAcceptConfirmationDialogProps = {
  open: boolean;
  onClose: () => void;
  onConfirmed: () => void;
};

const ConsentRequestAcceptConfirmationDialog: FC<
  ConsentRequestAcceptConfirmationDialogProps
> = (props) => {
  const { open, onClose, onConfirmed } = props;
  return (
    <ConfirmationDialog
      open={open}
      title={<FormattedMessage id="consent_accept" />}
      message={<FormattedMessage id="consent_accept_question" />}
      onClose={onClose}
      onConfirmed={onConfirmed}
    />
  );
};

export default ConsentRequestAcceptConfirmationDialog;
