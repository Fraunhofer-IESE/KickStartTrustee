/*
 Copyright 2026 Fraunhofer IESE

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import { Anchor, Box, Grid, Image } from "grommet";
import { routes } from "../../config/routes";
import AnchorLink from "../modeling/common/AnchorLink";

import kickstartLogo from "../../assets/images/kickstarttrustee-logo-small.svg";
import foerderhinweisLogo from "../../assets/BMFTR_de_Web_RGB_gef_durch.png";

const LayoutFooter = () => {
  return (
    <Box
      pad={{ vertical: "medium" }}
      fill
    >
      <Grid
        columns={{ count: 3, size: "auto" }}
        gap="medium"
        fill="horizontal"
      >
        <Box
          justify="center"
          align="start"
        >
          <Box
            gap="small"
            justify="center"
          >
            <Anchor
              href="https://www.iese.fraunhofer.de/"
              target="_blank"
              rel="noopener noreferrer"
            >
              IESE
            </Anchor>
            <AnchorLink to={routes.imprint.path}>Impressum</AnchorLink>
            <AnchorLink to={routes.privacypolicy.path}>Datenschutzerklärung</AnchorLink>
          </Box>
        </Box>
        <Box
          align="center"
          justify="center"
        >
          <Image
            src={kickstartLogo}
            alt="Kickstart Trustee Logo"
            height="150px"
          />
        </Box>

        <Box
          justify="end"
          align="end"
        >
          <Box>
            <Image
              src={foerderhinweisLogo}
              alt="Förderhinweis Logo"
              height="150px"
            />
          </Box>
        </Box>
      </Grid>
    </Box>
  );
};

export default LayoutFooter;
