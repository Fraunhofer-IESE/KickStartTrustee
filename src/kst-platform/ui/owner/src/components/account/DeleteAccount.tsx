import { Button } from "@mui/material";
import { Fragment, useCallback, useState } from "react";
import { FormattedMessage } from "react-intl";
import { ActionFunction, redirect, useSubmit } from "react-router-dom";
import APIFactory from "../../api/APIFactory";
import KeycloakService from "../../services/KeycloakService";
import ConfirmationDialog from "../dialogs/ConfirmationDialog";

export const deleteAccountAction: ActionFunction =
  async (): Promise<Response> => {
    const id = KeycloakService.getInstance().getUserId();
    if (!id) {
      return redirect("/logout");
    }
    const ownerApi = await APIFactory.createOwnerApi();
    await ownerApi.deleteAccount(id);
    return redirect("/logout");
  };

const DeleteAccount = () => {
  const submit = useSubmit();
  const [isConfirmationDialogOpen, setIsConfirmationDialogOpen] =
    useState<boolean>(false);

  const handleDeleteButtonClick = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      setIsConfirmationDialogOpen(true);
    },
    []
  );

  const handleConfirmationDialogClose = useCallback(() => {
    setIsConfirmationDialogOpen(false);
  }, []);

  const handleConfirmationDialogConfirmed = useCallback(() => {
    setIsConfirmationDialogOpen(false);
    submit(null, {
      action: `/account/delete`,
      method: "post",
    });
  }, [submit]);

  return (
    <Fragment>
      <Button
        color="error"
        size="large"
        variant="outlined"
        onClick={handleDeleteButtonClick}
      >
        <FormattedMessage id="delete_account" />
      </Button>
      <ConfirmationDialog
        open={isConfirmationDialogOpen}
        title={<FormattedMessage id="delete_account" />}
        message={<FormattedMessage id="delete_account_question" />}
        onClose={handleConfirmationDialogClose}
        onConfirmed={handleConfirmationDialogConfirmed}
      />
    </Fragment>
  );
};

export default DeleteAccount;
