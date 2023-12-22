import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";
import { FC, ReactNode, useCallback } from "react";
import { FormattedMessage } from "react-intl";

type ErrorDialogProps = {
    open: boolean;
    title?: ReactNode;
    message: ReactNode;
    onClose: () => void;
  };

const ErrorDialog: FC<ErrorDialogProps> = (props) => {
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
        <DialogTitle>{title ?? <FormattedMessage id="error_dialog_title" />}</DialogTitle>
        <DialogContent>
          <DialogContentText>{message}</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>
            <FormattedMessage id="ok" />
          </Button>
        </DialogActions>
      </Dialog>  
    );
}

export default ErrorDialog;
