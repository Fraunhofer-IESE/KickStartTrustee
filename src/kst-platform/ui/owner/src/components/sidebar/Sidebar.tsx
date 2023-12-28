import { Drawer, List, ListItem } from "@mui/material";
import { FC } from "react";
import { FormattedMessage } from "react-intl";
import { NavLink } from "react-router-dom";
import useStyles from "../../hooks/useStyles";
import { defaultFilterQueryParams as consentDefaultFilterQueryParams } from "../../views/root/consent/ConsentListView";
import { defaultFilterQueryParams as consentRequestDefaultFilterQueryParams } from "../../views/root/consent-request/ConsentRequestListView";
import { defaultFilterQueryParams as auditLogDefaultFilterQueryParams } from "../../views/root/audit/AuditLogView";

const styleCreator = () => ({
  linkStyle: {
    textDecoration: "none",
    color: "grey",
  },
  linkStyleActive: {
    textDecoration: "none",
    color: "var(--mui-palette-primary-main)",
    fontWeight: "bold",
  },
});

const Sidebar: FC = () => {
  const styles = useStyles(styleCreator);

  return (
    <Drawer
      component="nav"
      variant="permanent"
      anchor="left"
      PaperProps={{
        sx: {
          position: "relative",
          top: "unset",
          left: "unset",
          paddingLeft: 1,
        },
      }}
      sx={{ flex: 1 }}
    >
      <nav>
        <List>
          <ListItem>
            <NavLink
              to={`/consent-request?${consentRequestDefaultFilterQueryParams}`}
              style={({ isActive }) =>
                isActive ? styles.linkStyleActive : styles.linkStyle
              }
            >
              <FormattedMessage id="my_consent_requests" />
            </NavLink>
          </ListItem>
          <ListItem>
            <NavLink
              to={`/consent?${consentDefaultFilterQueryParams}`}
              style={({ isActive }) =>
                isActive ? styles.linkStyleActive : styles.linkStyle
              }
            >
              <FormattedMessage id="my_consents" />
            </NavLink>
          </ListItem>
          <ListItem>
            <NavLink
              to="/data"
              style={({ isActive }) =>
                isActive ? styles.linkStyleActive : styles.linkStyle
              }
            >
              <FormattedMessage id="my_data_items" />
            </NavLink>
          </ListItem>
          <ListItem>
            <NavLink
              to={`/audit?${auditLogDefaultFilterQueryParams}`}
              style={({ isActive }) =>
                isActive ? styles.linkStyleActive : styles.linkStyle
              }
            >
              <FormattedMessage id="all_events" />
            </NavLink>
          </ListItem>
        </List>
      </nav>
    </Drawer>
  );
};

export default Sidebar;
