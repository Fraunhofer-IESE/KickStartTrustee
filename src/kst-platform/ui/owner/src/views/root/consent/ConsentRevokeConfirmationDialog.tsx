import { FormattedMessage } from "react-intl";
import ConfirmationDialog from "../../../components/dialogs/ConfirmationDialog";
import { FC } from "react";

export type ConsentRevokeConfirmationDialogProps = {
  open: boolean;
  onClose: () => void;
  onConfirmed: () => void;
};

const ConsentRevokeConfirmationDialog: FC<
  ConsentRevokeConfirmationDialogProps
> = (props) => {
  const { open, onClose, onConfirmed } = props;
  return (
    <ConfirmationDialog
      open={open}
      title={<FormattedMessage id="consent_revoke" />}
      message={<FormattedMessage id="consent_revoke_question" />}
      onClose={onClose}
      onConfirmed={onConfirmed}
    />
  );
};

export default ConsentRevokeConfirmationDialog;
