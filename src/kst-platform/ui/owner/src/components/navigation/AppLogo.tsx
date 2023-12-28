import { styled } from "@mui/material";

const AppLogo = styled('img')(({ theme }) => ({
    minHeight: "4rem",
    width: "auto",
    padding: `${theme.spacing(0.5)} 0`,
}));

export default AppLogo;