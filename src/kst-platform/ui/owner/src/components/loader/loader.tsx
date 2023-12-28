import { Box, CircularProgress } from "@mui/material";

const Loader = () => {
    return (
        <Box role="presentation" display="flex" justifyContent="center" alignItems="center" height="100vh">
            <CircularProgress />
        </Box>
    );
}

export default Loader;
