import { DiseaseWarningSettingsDTO, ResponseError } from "owner-api";
import { useCallback, useEffect, useMemo, useState } from "react";
import { FormattedMessage } from "react-intl";
import {
  ActionFunction,
  Form,
  LoaderFunction,
  isRouteErrorResponse,
  redirect,
  useLoaderData,
  useLocation,
  useNavigate,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import ErrorDialog from "../../../components/dialogs/ErrorDialog";
import PageHeader from "../../../components/header/PageHeader";
import Loader from "../../../components/loader/loader";
import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  FormGroup,
  Stack,
  Typography,
} from "@mui/material";
import { authCheck } from "../../../hooks/useAuthCheck";

export type TDiseaseWarningData = {
  loaded: boolean;
  diseaseWarningSettings?: DiseaseWarningSettingsDTO;
};

export const diseaseWarningLoader: LoaderFunction = async (
  args
): Promise<TDiseaseWarningData> => {
  await authCheck();
  try {
    const { request } = args;

    const serviceDiseaseWarningApi =
      await APIFactory.createServiceDiseaseWarningApi();

    const diseaseWarningSettings =
      await serviceDiseaseWarningApi.getOwnSettings({
        signal: request.signal,
      });
    return { loaded: true, diseaseWarningSettings };
  } catch (e) {
    if (e instanceof ResponseError) {
      if (e.response.status !== 404) {
        throw e.response;
      }
    }
    return {
      loaded: true,
    };
  }
};

export const diseaseWarningAction: ActionFunction = async (args) => {
  const { request } = args;

  const formData = await request.formData();

  const settingsId = formData.get("settingsId")?.toString();
  const diseaseWarningSettings = {
    isDiseaseReportProcessingEnabled:
      formData.get("isDiseaseReportProcessingEnabled") === "on",
    isDiseaseWarningCreationEnabled:
      formData.get("isDiseaseWarningCreationEnabled") === "on",
    isEmailNotificationEnabled:
      formData.get("isEmailNotificationEnabled") === "on",
  };

  const serviceDiseaseWarningApi =
    await APIFactory.createServiceDiseaseWarningApi();

  if (!settingsId) {
    await serviceDiseaseWarningApi.createOwnSettings(diseaseWarningSettings, {
      signal: request.signal,
    });
  } else {
    await serviceDiseaseWarningApi.updateSettings(
      settingsId,
      diseaseWarningSettings,
      {
        signal: request.signal,
      }
    );
  }

  return redirect("/");
};

const DiseaseWarningView = () => {
  const { diseaseWarningSettings, loaded } =
    useLoaderData() as TDiseaseWarningData;
  const settingsId = diseaseWarningSettings?.id;
  const location = useLocation();
  const navigate = useNavigate();

  const error = useMemo(() => location.state?.error, [location.state]);
  const [isErrorDialogOpen, setIsErrorDialogOpen] = useState<boolean>(
    () => error !== undefined && error !== null
  );

  const handleClose = useCallback(() => {
    const { pathname, state } = location;
    const newState = { ...state };
    delete newState.error;
    navigate(pathname, { replace: true, state: newState });
  }, [location, navigate]);

  useEffect(() => {
    setIsErrorDialogOpen(error !== undefined && error !== null);
  }, [error]);

  const [isDiseaseWarningCreationEnabled, setIsDiseaseWarningCreationEnabled] =
    useState(diseaseWarningSettings?.isDiseaseWarningCreationEnabled === true);

  const [isEmailNotificationEnabled, setIsEmailNotificationEnabled] = useState(
    diseaseWarningSettings?.isEmailNotificationEnabled === true
  );

  const handleIsDiseaseWarningCreationEnabledChange = useCallback(
    (_event: React.SyntheticEvent, checked: boolean) => {
      setIsDiseaseWarningCreationEnabled(checked);
    },
    []
  );

  const handleIsEmailNotificationEnabledChange = useCallback(
    (_event: React.SyntheticEvent, checked: boolean) => {
      setIsEmailNotificationEnabled(checked);
    },
    []
  );

  useEffect(() => {
    if (!isDiseaseWarningCreationEnabled && isEmailNotificationEnabled) {
      setIsEmailNotificationEnabled(false);
    }
  }, [isDiseaseWarningCreationEnabled, isEmailNotificationEnabled]);

  return (
    <Stack spacing={2}>
      <PageHeader>
        <FormattedMessage id="service_disease_warning_name" />
      </PageHeader>
      {loaded ? (
        <Stack spacing={2}>
          <Typography align="justify" variant="body2">
            <FormattedMessage id="service_disease_warning_description" />
          </Typography>
          <Form method="post" noValidate={true}>
            <Stack spacing={2}>
              {settingsId && (
                <input type="hidden" name="settingsId" value={settingsId} />
              )}
              <FormGroup>
                <FormControlLabel
                  control={
                    <Checkbox
                      name="isDiseaseReportProcessingEnabled"
                      defaultChecked={
                        diseaseWarningSettings?.isDiseaseReportProcessingEnabled ===
                        true
                      }
                    />
                  }
                  label={
                    <FormattedMessage id="service_disease_warning_consentToContributeDiseaseReports_label" />
                  }
                />
                <FormControlLabel
                  control={<Checkbox name="isDiseaseWarningCreationEnabled" />}
                  label={
                    <FormattedMessage id="service_disease_warning_consentToReceiveDiseaseWarnings_label" />
                  }
                  onChange={handleIsDiseaseWarningCreationEnabledChange}
                  checked={isDiseaseWarningCreationEnabled}
                />
                <FormControlLabel
                  control={<Checkbox name="isEmailNotificationEnabled" />}
                  label={
                    <FormattedMessage id="service_disease_warning_emailNotificationOnNewDiseaseWarnings_label" />
                  }
                  disabled={!isDiseaseWarningCreationEnabled}
                  onChange={handleIsEmailNotificationEnabledChange}
                  checked={isEmailNotificationEnabled}
                />
              </FormGroup>
              <Box>
                <Button
                  color="primary"
                  size="large"
                  variant="contained"
                  type="submit"
                >
                  <FormattedMessage id="save" />
                </Button>
              </Box>
            </Stack>
          </Form>
        </Stack>
      ) : (
        <Loader />
      )}
      <ErrorDialog
        open={isErrorDialogOpen}
        onClose={handleClose}
        message={
          isRouteErrorResponse(error) ? (
            `${error.data}`
          ) : (
            <FormattedMessage id="error_unknown" />
          )
        }
      />
    </Stack>
  );
};

export default DiseaseWarningView;
