import { Grid, Typography } from "@mui/material";
import { Fragment, useMemo } from "react";
import { FormattedMessage } from "react-intl";
import { Navigate } from "react-router-dom";
import Loader from "../../../components/loader/loader";
import useAuthContext from "../../../hooks/useAuthContext";

const LoginView = () => {
  const { initialized, keycloakService } = useAuthContext();

  const isAuthenticated = useMemo(
    () => keycloakService.authenticated,
    [keycloakService.authenticated]
  );

  if (!initialized) {
    return <Loader />;
  }

  if (isAuthenticated) {
    return <Navigate to="/" />;
  }

  return (
    <Fragment>
      <h1>
        <FormattedMessage id="welcome_header" />
      </h1>
      <Grid container spacing={2}>
        <Grid item xs={12} md={6} container spacing={16}>
          <Grid item>
            <Typography>
              <FormattedMessage id="welcome_text" />
            </Typography>
          </Grid>
        </Grid>
      </Grid>
    </Fragment>
  );
};

export default LoginView;
