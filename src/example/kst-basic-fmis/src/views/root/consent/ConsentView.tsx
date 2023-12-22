import {
  Box,
  Button,
  FormControl,
  FormControlLabel,
  FormHelperText,
  Radio,
  RadioGroup,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import {
  DataItemTypeDTO,
  ProsumerConsentDTOPurposeEnum,
  ResponseError,
} from "kst-api";
import React, { FC, Fragment, useCallback } from "react";
import { FormattedMessage } from "react-intl";
import {
  LoaderFunction,
  useLoaderData,
  useRouteLoaderData,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import Loader from "../../../components/loader/loader";
import useAppConfig from "../../../hooks/useAppConfig";
import useDataItemTypeFormatter from "../../../hooks/useDataItemTypeFormatter";
import useDateTimeFormatter from "../../../hooks/useDateTimeFormatter";
import { TRootLoaderData } from "../RootView";

const convertPurpose = (purpose: ProsumerConsentDTOPurposeEnum) => {
  switch (purpose) {
    case ProsumerConsentDTOPurposeEnum.Research:
      return "Research";
    case ProsumerConsentDTOPurposeEnum.Service:
      return "Service";
    case ProsumerConsentDTOPurposeEnum.Other:
      return "Other";
    default:
      break;
  }
  return "Unknown";
};

export type TConsentViewData = {
  loaded: boolean;
  dataItemTypes?: Array<DataItemTypeDTO>;
};

export const consentViewLoader: LoaderFunction = async (
  args
): Promise<TConsentViewData> => {
  const { request } = args;
  try {
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    return { loaded: true, dataItemTypes };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true };
  }
};

const ConsentView: FC = () => {
  const { appConfig } = useAppConfig();
  const {
    loaded: consentLoaded,
    activeConsent,
    isConsentLevelFull,
  } = useRouteLoaderData("root") as TRootLoaderData;
  const { loaded, dataItemTypes = [] } = useLoaderData() as TConsentViewData;
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);
  const dateTimeFormatter = useDateTimeFormatter();

  const handleRevokeClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      if (!activeConsent) {
        return;
      }
      const kstOwnerUI = appConfig?.kstOwnerUI;
      const consentUrl = new URL(`consent/${activeConsent.id}`, kstOwnerUI);
      consentUrl.searchParams.set("redirect_uri", window.location.origin);
      window.location.assign(consentUrl);
    },
    [activeConsent, appConfig?.kstOwnerUI]
  );

  if (!loaded || !consentLoaded) {
    return <Loader />;
  }

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="my_consent" />
      </PageHeader>
      {activeConsent && (
        <Fragment>
          <Stack spacing={2}>
            <Fragment>
              <Typography>
                <FormattedMessage id="current_consent_message" />
              </Typography>
              <FormControl variant="standard">
                <RadioGroup value={!isConsentLevelFull ? "basic" : "full"}>
                  <FormControlLabel
                    value="basic"
                    control={<Radio />}
                    label={
                      <FormattedMessage id="consent_dialog_basic_consent" />
                    }
                    disabled={true}
                  />
                  <FormHelperText sx={{ marginLeft: "2rem" }}>
                    <FormattedMessage id="consent_dialog_basic_explain" />
                  </FormHelperText>
                  <FormControlLabel
                    value="full"
                    control={<Radio />}
                    label={
                      <FormattedMessage id="consent_dialog_full_consent" />
                    }
                    disabled={true}
                  />
                  <FormHelperText sx={{ marginLeft: "2rem" }}>
                    <FormattedMessage id="consent_dialog_full_explain" />
                  </FormHelperText>
                </RadioGroup>
              </FormControl>
            </Fragment>
            <Typography>
              <FormattedMessage id="consent_details" />
            </Typography>
            <TextField
              disabled={true}
              label={<FormattedMessage id="consent_id" />}
              value={activeConsent.id}
              InputLabelProps={{ shrink: true }}
              variant="standard"
            />
            <TextField
              disabled={true}
              label={<FormattedMessage id="consent_consumed_data_items" />}
              value={dataItemTypeFormatter(
                activeConsent?.consumedDataItemTypes
              )}
              InputLabelProps={{ shrink: true }}
              variant="standard"
            />
            <TextField
              disabled={true}
              label={<FormattedMessage id="consent_provided_data_items" />}
              value={dataItemTypeFormatter(
                activeConsent?.providedDataItemTypes
              )}
              InputLabelProps={{ shrink: true }}
              variant="standard"
            />
            <TextField
              disabled={true}
              label={<FormattedMessage id="consent_created_at" />}
              value={dateTimeFormatter(activeConsent?.createdAt)}
              InputLabelProps={{ shrink: true }}
              variant="standard"
            />
            {activeConsent.dataUsageStatement && (
              <TextField
                disabled={true}
                label={<FormattedMessage id="consent_data_usage_statement" />}
                multiline={true}
                value={activeConsent.dataUsageStatement}
                InputLabelProps={{ shrink: true }}
                variant="standard"
              />
            )}
            {activeConsent.purpose && (
              <TextField
                disabled={true}
                label={<FormattedMessage id="consent_purpose" />}
                value={convertPurpose(activeConsent.purpose)}
                InputLabelProps={{ shrink: true }}
                variant="standard"
              />
            )}
            <Typography variant="subtitle2">
              <FormattedMessage id="consents_immutable" />
            </Typography>
            <Box>
              <Button onClick={handleRevokeClicked} variant="contained">
                <FormattedMessage id="consent_revoke" />
              </Button>
            </Box>
          </Stack>
        </Fragment>
      )}
    </Fragment>
  );
};

export default ConsentView;
