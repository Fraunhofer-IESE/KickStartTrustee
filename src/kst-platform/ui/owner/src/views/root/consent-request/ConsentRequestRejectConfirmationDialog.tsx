import { FormattedMessage } from "react-intl";
import ConfirmationDialog from "../../../components/dialogs/ConfirmationDialog";
import { FC } from "react";

export type ConsentRequestRejectConfirmationDialogProps = {
  open: boolean;
  onClose: () => void;
  onConfirmed: () => void;
};

const ConsentRequestRejectConfirmationDialog: FC<
  ConsentRequestRejectConfirmationDialogProps
> = (props) => {
  const { open, onClose, onConfirmed } = props;
  return (
    <ConfirmationDialog
      open={open}
      title={<FormattedMessage id="consent_reject" />}
      message={<FormattedMessage id="consent_reject_question" />}
      onClose={onClose}
      onConfirmed={onConfirmed}
    />
  );
};

export default ConsentRequestRejectConfirmationDialog;
