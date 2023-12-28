import { Box, Paper } from "@mui/material";
import { CSSProperties, FC } from "react";
import { FormattedMessage } from "react-intl";
import { Link, To } from "react-router-dom";
import useStyles from "../../hooks/useStyles";

const styleCreator = () => ({
  linkStyle: { 
    textDecoration: "none" 
  },
  paperStyle: {
    display: "inline-flex",
    flexWrap: "wrap",
    padding: "2rem"
  } as CSSProperties,
});

type DashboardTileProps = {
  to: To;
  icon: React.ReactNode;
  messageId: string;
};

const DashboardTile: FC<DashboardTileProps> = (props) => {
  const { to, icon, messageId } = props;
  const styles = useStyles(styleCreator);

  return (
    <Link to={to} style={styles.linkStyle}>
      <Paper sx={styles.paperStyle}>
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
    </Link>
  );
};

export default DashboardTile;
