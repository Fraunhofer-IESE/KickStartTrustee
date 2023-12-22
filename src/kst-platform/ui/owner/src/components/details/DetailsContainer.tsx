import { Paper, PaperProps, styled } from "@mui/material";
import { blueGrey } from "@mui/material/colors";

const DetailsContainer = styled(Paper)<PaperProps>(() => ({
    background: blueGrey[50],
    padding: "1rem",
    width: "600px",
}));

export default DetailsContainer;