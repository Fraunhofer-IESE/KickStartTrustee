import { FormattedMessage } from "react-intl";
import ConfirmationDialog from "../../../components/dialogs/ConfirmationDialog";
import { FC } from "react";

export type DataDeleteConfirmationDialogProps = {
  dataItemId?: string;
  open: boolean;
  onClose: () => void;
  onConfirmed: () => void;
};

const DataDeleteConfirmationDialog: FC<DataDeleteConfirmationDialogProps> = (props) => {
  const { dataItemId, open, onClose, onConfirmed } = props;
  return (
    <ConfirmationDialog
      open={open}
      title={<FormattedMessage id="data_item_delete" />}
      message={<FormattedMessage id="data_item_delete_question" values={{ dataItemId }} />}
      onClose={onClose}
      onConfirmed={onConfirmed}
    />
  );
};

export default DataDeleteConfirmationDialog;
