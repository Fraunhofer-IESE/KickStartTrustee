import { Box, Paper } from "@mui/material";
import { FC, Fragment } from "react";
import { FormattedMessage } from "react-intl";
import { Link, To } from "react-router-dom";
import { blueGrey } from "@mui/material/colors";
import useStyles from "../../hooks/useStyles";

const styleCreator = () => ({
  linkStyle: {
    textDecoration: "none",
  },
});

type DashboardTileProps = {
  to: To;
  icon: React.ReactNode;
  messageId: string;
  disabled?: boolean;
};

const DashboardTile: FC<DashboardTileProps> = (props) => {
  const { to, icon, messageId, disabled } = props;
  const styles = useStyles(styleCreator);

  const content = (
    <Paper
      sx={{
        display: "inline-flex",
        flexWrap: "wrap",
        padding: "2rem",
        background: disabled ? blueGrey[100] : undefined,
      }}
    >
      <Box
        flex="100%"
        display="inline-flex"
        justifyContent="center"
        padding="0.25rem"
      >
        {icon}
      </Box>
      <Box flex="100%" display="inline-flex" justifyContent="center">
        <FormattedMessage id={messageId} />
      </Box>
    </Paper>
  );
  return disabled ? (
    <Fragment>{content}</Fragment>
  ) : (
    <Link to={to} style={styles.linkStyle}>
      {content}
    </Link>
  );
};

export default DashboardTile;
