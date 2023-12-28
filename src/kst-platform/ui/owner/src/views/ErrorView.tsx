import SentimentVeryDissatisfiedIcon from "@mui/icons-material/SentimentVeryDissatisfied";
import { Alert, AlertTitle, Box, Container } from "@mui/material";
import { Fragment, useMemo } from "react";
import { FormattedMessage, IntlProvider } from "react-intl";
import {
  Navigate,
  isRouteErrorResponse,
  useRouteError,
  useRouteLoaderData,
} from "react-router-dom";
import Navigation from "../components/navigation/Navigation";
import LOCALES from "../i18n/locales";
import messages from "../i18n/messages";
import { TRootLoaderData } from "./root/RootView";

const ErrorView = () => {
  const error = useRouteError();
  const rootData = useRouteLoaderData("root");

  const locale = useMemo(() => {
    if (!rootData) {
      return LOCALES.DEFAULT;
    }
    const { profile } = rootData as TRootLoaderData;
    return profile?.preferredLanguage ?? LOCALES.DEFAULT;
  }, [rootData]);

  if (isRouteErrorResponse(error) && error.status === 401) {
    return <Navigate to="/login" />;
  }

  return (
    <IntlProvider
      messages={messages[locale]}
      locale={locale}
      defaultLocale={LOCALES.DEFAULT}
    >
      <Navigation />
      <Container component="main" maxWidth="lg">
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          paddingBottom={1}
        >
          <SentimentVeryDissatisfiedIcon
            color="action"
            sx={{ fontSize: "10rem" }}
          />
        </Box>
        <Alert severity="error">
          <AlertTitle>
            <FormattedMessage id="error_dialog_title" />
          </AlertTitle>
          {isRouteErrorResponse(error) ? (
            <Fragment>
              <strong>{error.status}</strong> {JSON.stringify(error.data)}
            </Fragment>
          ) : (
            <Fragment>
              <FormattedMessage id="error_unknown" />
            </Fragment>
          )}
        </Alert>
      </Container>
    </IntlProvider>
  );
};

export default ErrorView;
