import { styled } from "@mui/material";
import { Link } from "react-router-dom";

const HomeLink = styled(Link)(() => ({
    color: "inherit",
    textDecoration: "none",
    display: "flex",
    alignItems: "center",
}));

export default HomeLink;