import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import React, { FC, useCallback } from "react";
import { FormattedMessage } from "react-intl";

export type NoAccessDialogProps = {
  open: boolean;
  onSignoutClick: () => void;
};

const NoAccessDialog: FC<NoAccessDialogProps> = (props) => {
  const { open, onSignoutClick } = props;

  const handleSignoutClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      onSignoutClick();
    },
    [onSignoutClick]
  );

  return (
    <Dialog maxWidth="md" open={open}>
      <DialogTitle>
        <FormattedMessage id="wrong_role_title" />
      </DialogTitle>
      <DialogContent>
        <DialogContentText>
          <FormattedMessage id="wrong_role_text" />
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleSignoutClicked} variant="contained">
          <FormattedMessage id="sign_out" />
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default NoAccessDialog;
