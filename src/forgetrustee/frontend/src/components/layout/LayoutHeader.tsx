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
import { Link, useLocation } from "react-router-dom";
import HeaderNavItem from "./HeaderNavItem";
import { routes } from "../../config/routes";

import logoSmall from "../../assets/images/kickstarttrustee-logo-small.svg";

const LayoutHeader = () => {
  const location = useLocation();

  return (
    <Box
      fill
      direction="row"
      gap="large"
    >
      <Box
        direction="row"
        pad={{ vertical: "small", left: "medium" }}
      >
        <Link to={routes.startpage.path}>
          <Image
            src={logoSmall}
            alt="Kickstart Trustee Logo Small"
            height="64px"
          />
        </Link>
      </Box>
      <Box
        direction="row"
        gap="medium"
        align="end"
      >
        <HeaderNavItem
          label="Modellieren"
          active={location.pathname.startsWith(routes.dataTrusteeModeling.path)}
          to={routes.dataTrusteeModeling.path}
        />
        <HeaderNavItem
          label="Überblick & Vergleich"
          active={location.pathname.startsWith(routes.dataTrusteeCompare.path)}
          to={routes.dataTrusteeCompare.path}
        />
        <HeaderNavItem
          label="Informieren"
          active={location.pathname.startsWith(routes.inform.path)}
          href={routes.inform.path}
        />
      </Box>
    </Box>
  );
};

export default LayoutHeader;
