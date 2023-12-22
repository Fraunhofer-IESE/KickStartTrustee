import { AccountCircle } from "@mui/icons-material";
import {
  AppBar,
  IconButton,
  Menu,
  MenuItem,
  ToggleButton,
  ToggleButtonGroup,
  Toolbar,
  Typography,
} from "@mui/material";
import { Container } from "@mui/system";
import React, { Fragment, useCallback, useState } from "react";
import { FormattedMessage } from "react-intl";
import { Link, useNavigate } from "react-router-dom";
import useAuthContext from "../../hooks/useAuthContext";
import useStyles, { TStyleCreator } from "../../hooks/useStyles";
import LOCALES from "../../i18n/locales";

const styleCreator: TStyleCreator = () => ({
  toggleButtonStyle: {
    color: "inherit",
  },
  homeLinkStyle: {
    color: "inherit",
    textDecoration: "none",
  },
  emptyDivStyle: {
    flexGrow: 1,
  },
});

export type NavigationProps = {
  locale: string;
  onLocaleChange: (locale: string) => void;
};

const Navigation: React.FC<NavigationProps> = (props: NavigationProps) => {
  const { locale, onLocaleChange } = props;
  const navigate = useNavigate();
  const styles = useStyles(styleCreator);
  const { keycloakService } = useAuthContext();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleMenu = useCallback((event: React.MouseEvent<HTMLElement>) => {
    event.preventDefault();
    event.stopPropagation();
    setAnchorEl(event.currentTarget);
  }, []);

  const handleClose = useCallback(() => {
    setAnchorEl(null);
  }, []);

  const handleConsent = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      navigate("/consent");
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

  const handleLocaleChange = useCallback(
    (event: React.MouseEvent<HTMLElement>, value: string) => {
      event.preventDefault();
      event.stopPropagation();
      onLocaleChange(value);
    },
    [onLocaleChange]
  );

  const handleToggleLocale = useCallback(
    (event: React.MouseEvent<HTMLElement>) => {
      event.preventDefault();
      event.stopPropagation();
      const newLocale =
        locale === LOCALES.ENGLISH ? LOCALES.GERMAN : LOCALES.ENGLISH;
      onLocaleChange(newLocale);
      setAnchorEl(null);
    },
    [locale, onLocaleChange]
  );

  return (
    <AppBar position="static" elevation={0}>
      <Container maxWidth="lg">
        <Toolbar
          sx={{ paddingLeft: { xs: "0px", sm: "0px", md: "0px", lg: "0px" } }}
        >
          <Link to="/" style={styles.homeLinkStyle}>
            <Typography variant="h6" color="inherit" component="div">
              <FormattedMessage id="navigation_header" />
            </Typography>
          </Link>
          <div style={styles.emptyDivStyle} />
          {!keycloakService.authenticated && (
            <ToggleButtonGroup
              value={locale}
              exclusive={true}
              onChange={handleLocaleChange}
              aria-label="Locale"
            >
              <ToggleButton
                style={styles.toggleButtonStyle}
                value={LOCALES.GERMAN}
              >
                <FormattedMessage id="de_caps" />
              </ToggleButton>
              <ToggleButton
                style={styles.toggleButtonStyle}
                value={LOCALES.ENGLISH}
              >
                <FormattedMessage id="en_caps" />
              </ToggleButton>
            </ToggleButtonGroup>
          )}
          {keycloakService.authenticated && (
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
                <MenuItem onClick={handleConsent}>
                  <FormattedMessage id="my_consent" />
                </MenuItem>
                <MenuItem onClick={handleToggleLocale}>
                  <FormattedMessage id="toggle_locale" />
                </MenuItem>
                <MenuItem onClick={handleLogout}>
                  <FormattedMessage id="sign_out" />
                </MenuItem>
              </Menu>
            </Fragment>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Navigation;
