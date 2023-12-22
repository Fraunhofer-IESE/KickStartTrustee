import { Button, Grid, Stack, Typography } from "@mui/material";
import React, { Fragment, useCallback, useEffect, useMemo } from "react";
import { FormattedMessage } from "react-intl";
import { useNavigate } from "react-router-dom";
import Loader from "../../../components/loader/loader";
import useAuthContext from "../../../hooks/useAuthContext";
import useStyles from "../../../hooks/useStyles";

const styleCreator = () => ({
  centerInline: {
    display: "inline-flex",
    justifyContent: "center",
    alignItems: "center",
  },
});

const LoginView = () => {
  const { initialized, keycloakService } = useAuthContext();
  const navigate = useNavigate();
  const styles = useStyles(styleCreator);

  const isAuthenticated = useMemo(
    () => keycloakService.authenticated,
    [keycloakService.authenticated]
  );

  const handleLoginClick = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      keycloakService.login();
    },
    [keycloakService]
  );

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  if (!initialized) {
    return <Loader />;
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
        <Grid item xs={12} md={6} style={styles.centerInline}>
          <Stack direction="row" spacing={2}>
            <Button onClick={handleLoginClick} size="large" variant="contained">
              <FormattedMessage id="login" />
            </Button>
          </Stack>
        </Grid>
      </Grid>
    </Fragment>
  );
};

export default LoginView;
