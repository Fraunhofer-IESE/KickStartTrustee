import { Typography } from "@mui/material";
import { FC, PropsWithChildren } from "react";
import useStyles from "../../hooks/useStyles";

const styleCreator = () => ({
  headerStyle: {
    marginTop: "0.15em",
  },
});

type PageHeaderProps = {};

const PageHeader: FC<PropsWithChildren<PageHeaderProps>> = (props) => {
  const { children } = props;
  const styles = useStyles(styleCreator);

  return (
    <Typography
      component="h1"
      variant="h4"
      gutterBottom={true}
      sx={styles.headerStyle}
    >
      {children}
    </Typography>
  );
};

export default PageHeader;
