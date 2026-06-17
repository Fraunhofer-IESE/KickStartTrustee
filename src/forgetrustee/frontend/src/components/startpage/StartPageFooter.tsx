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

import { Anchor, Box, Grid, Image, Text } from "grommet";
import AnchorLink from "../modeling/common/AnchorLink";

import kickstartLogo from "../../assets/images/kickstarttrustee-logo-small.svg";
import foerderhinweisLogo from "../../assets/BMFTR_de_Web_RGB_gef_durch.png";
import { routes } from "../../config/routes";

const StartPageFooter = () => {
  return (
    <Box
      gridArea="footer"
      align="center"
      justify="center"
      background="#245A7C"
      pad={{ top: "large", bottom: "large", horizontal: "medium" }}
    >
      <Box
        direction="row"
        gap="large"
        fill="horizontal"
        width={{ max: "xxlarge" }}
        justify="center"
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
                color="white"
                href="https://www.iese.fraunhofer.de/"
                target="_blank"
                rel="noopener noreferrer"
              >
                IESE
              </Anchor>
              <AnchorLink
                color="white"
                to={routes.imprint.path}
              >
                Impressum
              </AnchorLink>
              <AnchorLink
                color="white"
                to={routes.privacypolicy.path}
              >
                Datenschutzerklärung
              </AnchorLink>
              <Text>Bildquelle: iStock.com/artis777 / FraunhoferIESE</Text>
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
    </Box>
  );
};

export default StartPageFooter;
