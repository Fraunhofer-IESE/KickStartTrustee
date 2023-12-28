import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import { FC, ReactNode, useCallback } from "react";
import { FormattedMessage } from "react-intl";

type NotificationDialogProps = {
  open: boolean;
  title: ReactNode;
  message: ReactNode;
  onClose: () => void;
};

const NotificationDialog: FC<NotificationDialogProps> = (props) => {
  const { open, title, message, onClose } = props;

  const handleClose = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      onClose();
    },
    [onClose]
  );

  return (
    <Dialog maxWidth="lg" open={open} onClose={onClose}>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText>{message}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button variant="contained" onClick={handleClose}>
          <FormattedMessage id="ok" />
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default NotificationDialog;
