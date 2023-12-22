import { Typography } from "@mui/material";
import { useEffect } from "react";
import { FormattedMessage } from "react-intl";
import useAuthContext from "../../../hooks/useAuthContext";

const LogoutView = () => {
    const { keycloakService } = useAuthContext();

    useEffect(() => {
        keycloakService.logout();
    }, [keycloakService]);

    return (
        <Typography align="center">
            <FormattedMessage id="logout" />
        </Typography>
    );
};

export default LogoutView;
