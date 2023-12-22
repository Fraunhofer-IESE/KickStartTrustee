import {
  Box,
  Button,
  FormControl,
  FormLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  Stack,
} from "@mui/material";
import React, { useCallback } from "react";
import { FormattedMessage } from "react-intl";
import { Form } from "react-router-dom";
import LOCALES from "../../i18n/locales";
import DeleteAccount from "../account/DeleteAccount";

type ProfileFormProps = {
  id?: string;
  preferedLanguage?: string;
  onPreferedLanguageChange: (preferedLanguage: string) => void;
  fullWidth?: boolean;
  showDeleteButton?: boolean;
};

const ProfileForm = (props: ProfileFormProps) => {
  const { id, preferedLanguage, onPreferedLanguageChange, fullWidth = false, showDeleteButton = false } = props;

  const handlePreferredLanguageChange = useCallback(
    (event: SelectChangeEvent<string>) => {
      event.preventDefault();
      event.stopPropagation();
      onPreferedLanguageChange(event.target.value);
    },
    [onPreferedLanguageChange]
  );

  return (
    <Form method="post" action="/">
      {id && <input type="hidden" name="id" value={id} />}
      <Stack spacing={2}>
        <Box width="100%">
          <FormControl fullWidth={fullWidth}>
            <FormLabel>
              <FormattedMessage id="choose_language" />
            </FormLabel>
            <Select
              name="preferedLanguage"
              onChange={handlePreferredLanguageChange}
              value={preferedLanguage}
              defaultValue={LOCALES.ENGLISH}
              variant="standard"
            >
              <MenuItem value={LOCALES.ENGLISH}><FormattedMessage id="english" /></MenuItem>
              <MenuItem value={LOCALES.GERMAN}><FormattedMessage id="german" /></MenuItem>
            </Select>
          </FormControl>
        </Box>
        <Stack direction={fullWidth ? "row-reverse" : "row"} spacing={1}>
          <Button color="primary" size="large" variant="contained" type="submit">
            <FormattedMessage id="save" />
          </Button>
          {showDeleteButton && (
            <DeleteAccount />
          )}
        </Stack>
      </Stack>
    </Form>
  );
};

export default ProfileForm;
