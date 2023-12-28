import { Link, Paper, Stack } from "@mui/material";
import { FormattedMessage } from "react-intl";
import './Footer.css'

const style = {
    display: 'flex',
    justifyContent: 'center',
    paddingTop: '0.25rem',
    paddingBottom: '0.25rem',
};

const Footer = () => {
    return (
        <Paper component="footer" sx={style}>
            <Stack direction="row" spacing={2}>
                <Link href="https://www.iese.fraunhofer.de/de/impressum.html">
                    <FormattedMessage id="imprint" />
                </Link>
                <Link href="https://www.kickstarttrustee.de/datenschutzerklaerung/">
                    <FormattedMessage id="privacy_policy" />
                </Link>
            </Stack>
        </Paper>
    )
};

export default Footer;
