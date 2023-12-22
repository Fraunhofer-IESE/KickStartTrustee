import SentimentVeryDissatisfiedIcon from "@mui/icons-material/SentimentVeryDissatisfied";
import { Alert, AlertTitle, Box, Container } from "@mui/material";
import { Fragment, useCallback, useState } from "react";
import { FormattedMessage, IntlProvider } from "react-intl";
import {
  Navigate,
  isRouteErrorResponse,
  useRouteError,
} from "react-router-dom";
import Navigation from "../components/navigation/Navigation";
import LOCALES from "../i18n/locales";
import messages from "../i18n/messages";

const ErrorView = () => {
  const error = useRouteError();

  const [locale, setLocale] = useState<string>(() =>
    navigator.language === LOCALES.GERMAN ? LOCALES.GERMAN : LOCALES.ENGLISH
  );

  const onLocaleChange = useCallback(
    (locale: string) => {
      setLocale(locale);
    },
    [setLocale]
  );

  if (isRouteErrorResponse(error) && error.status === 401) {
    return <Navigate to="/login" />;
  }

  return (
    <IntlProvider
      messages={messages[locale]}
      locale={locale}
      defaultLocale={LOCALES.DEFAULT}
    >
      <Navigation locale={locale} onLocaleChange={onLocaleChange} />
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
