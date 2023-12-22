import { AccountCircle } from "@mui/icons-material";
import {
  AppBar,
  Button,
  IconButton,
  Menu,
  MenuItem,
  Stack,
  Toolbar,
  Typography,
} from "@mui/material";
import React, { Fragment, useCallback, useMemo, useState } from "react";
import { FormattedMessage } from "react-intl";
import { useNavigate } from "react-router-dom";
import KSTLogo from "../../assets/KST-Logo.svg";
import useAuthContext from "../../hooks/useAuthContext";
import useStyles, { TStyleCreator } from "../../hooks/useStyles";
import ProfileCacheService from "../../services/ProfileCacheService";
import AppLogo from "./AppLogo";
import HomeLink from "./HomeLink";

const styleCreator: TStyleCreator = () => ({
  emptyDivStyle: {
    flexGrow: 1,
  },
  appBarStyle: {
    borderBottomWidth: "2px",
    borderBottomStyle: "solid",
    borderBottomColor: "var(--mui-palette-primary-main)",
  },
});



const Navigation = () => {
  const navigate = useNavigate();
  const styles = useStyles(styleCreator);
  const { keycloakService } = useAuthContext();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const authenticated = useMemo(
    () => keycloakService.authenticated,
    [keycloakService.authenticated]
  );

  const handleLoginClick = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      ProfileCacheService.getInstance().invalidate();
      keycloakService.login();
    },
    [keycloakService]
  );

  const handleRegisterClick = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      keycloakService.register();
    },
    [keycloakService]
  );

  const handleMenu = useCallback((event: React.MouseEvent<HTMLElement>) => {
    event.preventDefault();
    event.stopPropagation();
    setAnchorEl(event.currentTarget);
  }, []);

  const handleClose = useCallback(() => {
    setAnchorEl(null);
  }, []);

  const handleProfile = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      navigate("/profile");
      setAnchorEl(null);
    },
    [navigate]
  );

  const handleLogout = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      navigate("/logout");
      setAnchorEl(null);
    },
    [navigate]
  );

  return (
    <AppBar
      color="transparent"
      position="static"
      elevation={0}
      sx={styles.appBarStyle}
    >
      <Toolbar>
        <HomeLink to="/">
          <AppLogo
            src={KSTLogo}
            alt="KickStartTrustee"
          />
        </HomeLink>
        <div style={styles.emptyDivStyle} />
        {authenticated ? (
          <Fragment>
            <Typography variant="body2" color="inherit" component="div">
              <FormattedMessage
                id="hello_name"
                values={{ name: keycloakService.getUserName() }}
              />
            </Typography>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <AccountCircle />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: "bottom",
                horizontal: "right",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem onClick={handleProfile}>
                <FormattedMessage id="my_profile" />
              </MenuItem>
              <MenuItem onClick={handleLogout}>
                <FormattedMessage id="sign_out" />
              </MenuItem>
            </Menu>
          </Fragment>
        ) : (
          <Stack direction="row" spacing={2}>
            <Button onClick={handleLoginClick} size="large" variant="contained">
              <FormattedMessage id="login" />
            </Button>
            <Button
              onClick={handleRegisterClick}
              size="large"
              variant="outlined"
            >
              <FormattedMessage id="register" />
            </Button>
          </Stack>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navigation;
