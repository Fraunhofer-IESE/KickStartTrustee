import FormatListBulletedIcon from "@mui/icons-material/FormatListBulleted";
import ReportIcon from "@mui/icons-material/Report";
import WarningIcon from "@mui/icons-material/Warning";
import { Badge, Box, Grid, Paper, Stack, Typography } from "@mui/material";
import { blueGrey } from "@mui/material/colors";
import { ResponseError } from "kst-api";
import { Fragment } from "react";
import { FormattedMessage } from "react-intl";
import {
  LoaderFunction,
  useLoaderData,
  useRouteLoaderData,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import DashboardTile from "../../../components/dashboard/DashboardTile";
import PageHeader from "../../../components/header/PageHeader";
import useStyles from "../../../hooks/useStyles";
import KeycloakService from "../../../services/KeycloakService";
import { TRootLoaderData } from "../RootView";

const styleCreator = () => ({
  paperStyle: {
    background: blueGrey[50],
    padding: "1rem",
  },
  rowHeadingStyle: {
    marginBottom: "0.65rem",
  },
});

const DISEASE_WARNING_TYPE = "disease_warning";
const SEVEN_DAYS = 7 * 24 * 3600 * 1000;

export type THomeLoaderData = {
  newDiseaseWarningCount: number;
};

export const homeLoader: LoaderFunction = async ({
  request,
}): Promise<THomeLoaderData> => {
  const { signal } = request;
  const keycloakService = KeycloakService.getInstance();
  try {
    const ownerId = keycloakService.getUserId();
    const prosumerDataApi = await APIFactory.createProsumerDataApi();
    const diseaseWarnings = await prosumerDataApi.getDataItems(
      DISEASE_WARNING_TYPE,
      ownerId,
      { signal }
    );
    const lastWeek = new Date().getTime() - SEVEN_DAYS;
    const newDiseaseWarningCount = diseaseWarnings.filter(
      (dataItem) => dataItem.metaData.createdAt.getTime() > lastWeek
    ).length;
    return {
      newDiseaseWarningCount,
    };
  } catch (e) {
    if (e instanceof ResponseError) {
      const { response } = e;
      // if we change the backend to return a 403 in case there is no consent we need to add this:
      // if (response.status === 403) {
      //   return { newDiseaseWarningCount: 0 };
      // }
      throw response;
    }
    throw e;
  }
};

const Home = () => {
  const styles = useStyles(styleCreator);
  const { newDiseaseWarningCount } = useLoaderData() as THomeLoaderData;
  const { isConsentLevelFull } = useRouteLoaderData("root") as TRootLoaderData;

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="dashboard" />
      </PageHeader>
      <Stack spacing={2}>
        <Paper sx={styles.paperStyle} variant="outlined">
          <Typography component="h2" variant="h5" sx={styles.rowHeadingStyle}>
            <FormattedMessage id="field_management" />
          </Typography>
          <Grid container spacing={2}>
            <Grid item>
              <DashboardTile
                to="/fields"
                icon={<FormatListBulletedIcon />}
                messageId="fields"
              />
            </Grid>
          </Grid>
        </Paper>
        <Paper sx={styles.paperStyle} variant="outlined">
          <Typography component="h2" variant="h5" sx={styles.rowHeadingStyle}>
            <FormattedMessage id="disease_features" />
          </Typography>
          <Stack spacing={2}>
            <Grid container spacing={2}>
              <Grid item>
                <DashboardTile
                  to="/disease-report"
                  icon={<ReportIcon />}
                  messageId="disease_reports"
                  disabled={!isConsentLevelFull}
                />
              </Grid>
              <Grid item>
                <Badge badgeContent={newDiseaseWarningCount} color="warning">
                  <DashboardTile
                    to="/disease-warning"
                    icon={<WarningIcon />}
                    messageId="disease_warnings"
                    disabled={!isConsentLevelFull}
                  />
                </Badge>
              </Grid>
            </Grid>
            {!isConsentLevelFull && (
              <Box>
                <FormattedMessage id="insufficient_consent_for_disease_features" />
              </Box>
            )}
          </Stack>
        </Paper>
      </Stack>
    </Fragment>
  );
};

export default Home;
