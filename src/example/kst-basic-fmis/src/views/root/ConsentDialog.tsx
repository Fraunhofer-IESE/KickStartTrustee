import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  FormControlLabel,
  FormHelperText,
  Radio,
  RadioGroup,
  Typography,
} from "@mui/material";
import { FC, useCallback, useState } from "react";
import { FormattedMessage } from "react-intl";

export type ConsentDialogProps = {
  open: boolean;
  onClose: () => void;
  onContinue: (consentType: string) => void;
};

const ConsentDialog: FC<ConsentDialogProps> = (props) => {
  const { open, onClose, onContinue } = props;
  const [consentType, setConsentType] = useState<string>("basic");

  const handleConsentTypeChange = useCallback(
    (_event: React.ChangeEvent<HTMLInputElement>, value: string) => {
      setConsentType(value);
    },
    []
  );

  const handleClose = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      onClose();
      setConsentType("basic");
    },
    [onClose]
  );

  const handleContinue = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      onContinue(consentType);
    },
    [consentType, onContinue]
  );

  return (
    <Dialog maxWidth="md" open={open} onClose={onClose}>
      <DialogTitle>
        <FormattedMessage id="consent_dialog_title" />
      </DialogTitle>
      <DialogContent>
        <Typography>
          <FormattedMessage id="consent_dialog_message" />
        </Typography>
        <FormControl variant="standard">
          <RadioGroup value={consentType} onChange={handleConsentTypeChange}>
            <FormControlLabel
              value="basic"
              control={<Radio />}
              label={<FormattedMessage id="consent_dialog_basic_consent" />}
            />
            <FormHelperText sx={{ marginLeft: "2rem" }}>
              <FormattedMessage id="consent_dialog_basic_explain" />
            </FormHelperText>
            <FormControlLabel
              value="full"
              control={<Radio />}
              label={<FormattedMessage id="consent_dialog_full_consent" />}
            />
            <FormHelperText sx={{ marginLeft: "2rem" }}>
              <FormattedMessage id="consent_dialog_full_explain" />
            </FormHelperText>
          </RadioGroup>
        </FormControl>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>
          <FormattedMessage id="consent_dialog_cancel" />
        </Button>
        <Button variant="contained" onClick={handleContinue}>
          <FormattedMessage id="consent_dialog_continue" />
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConsentDialog;
