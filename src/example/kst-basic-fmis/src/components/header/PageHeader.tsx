import { Typography } from "@mui/material";
import { FC, PropsWithChildren } from "react";

type PageHeaderProps = {};

const PageHeader: FC<PropsWithChildren<PageHeaderProps>> = (props) => {
  const { children } = props;

  return (
    <Typography component="h1" variant="h4" gutterBottom={true}>
      {children}
    </Typography>
  );
};

export default PageHeader;
