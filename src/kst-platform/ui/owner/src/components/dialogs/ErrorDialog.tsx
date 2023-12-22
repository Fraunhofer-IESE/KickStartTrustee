import { FC, ReactNode } from "react";
import { FormattedMessage } from "react-intl";
import NotificationDialog from "./NotificationDialog";

type ErrorDialogProps = {
  open: boolean;
  title?: ReactNode;
  message: ReactNode;
  onClose: () => void;
};

const ErrorDialog: FC<ErrorDialogProps> = (props) => {
  const { open, title, message, onClose } = props;
  return (
    <NotificationDialog
      open={open}
      title={title ?? <FormattedMessage id="error_dialog_title" />}
      message={message}
      onClose={onClose}
    />
  );
};

export default ErrorDialog;
