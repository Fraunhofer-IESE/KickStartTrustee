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

import { Box, Image } from "grommet";

import logoSmall from "../../assets/images/kickstarttrustee-logo-small.svg";
import { Link } from "react-router";
import { routes } from "../../config/routes";

const StartPageHeader = () => {
  return (
    <Box
      align="center"
      gridArea="header"
      background="white"
    >
      <Box
        // align="bottom"
        pad={{ bottom: "large" }}
        width={{ max: "xxlarge" }}
        fill
      >
        <Box
          direction="row"
          pad={{ top: "small", left: "medium" }}
        >
          <Link to={routes.startpage.path}>
            <Image
              src={logoSmall}
              alt="Kickstart Trustee Logo Small"
              height="64px"
            />
          </Link>
        </Box>
        {/* <Box
          direction="row"
          height="medium"
        >
          <Box
            width="50%"
            align="start"
            justify="start"
          >
            <Image
              src={hand}
              height="100%"
            />
          </Box>
          <Box
            width="50%"
            align="end"
            justify="center"
            pad={{ right: "xlarge" }}
          >
            <Box>
              <Image
                src={logo}
                margin={{ bottom: "medium" }}
              />
              <Text
                size="medium"
                alignSelf="stretch"
                textAlign="center"
              >
                Datentreuhandmodelle modellieren und vergleichen
              </Text>
            </Box>
          </Box>
        </Box> */}
      </Box>
    </Box>
  );
};

export default StartPageHeader;
