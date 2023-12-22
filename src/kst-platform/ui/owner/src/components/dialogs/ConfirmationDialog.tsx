import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import React, { FC, ReactNode, useCallback } from "react";
import { FormattedMessage } from "react-intl";

type ConfirmationDialogProps = {
  open: boolean;
  title: ReactNode;
  message: ReactNode;
  onClose: () => void;
  onConfirmed: () => void;
};

const ConfirmationDialog: FC<ConfirmationDialogProps> = (props) => {
  const { open, title, message, onClose, onConfirmed } = props;

  const handleClose = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      onClose();
    },
    [onClose]
  );

  const handleConfirmed = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      onConfirmed();
    },
    [onConfirmed]
  );

  return (
    <Dialog maxWidth="lg" open={open} onClose={onClose}>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText>{message}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button variant="contained" onClick={handleClose}>
          <FormattedMessage id="no" />
        </Button>
        <Button variant="outlined" onClick={handleConfirmed}>
          <FormattedMessage id="yes" />
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmationDialog;
