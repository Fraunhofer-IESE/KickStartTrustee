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

import { Box } from "grommet";
import React from "react";
import { Link } from "react-router-dom";
import DynamicIcon from "../shared/DynamicIcon";

interface HeaderNavItemProps {
  label: string;
  active: boolean;
  to?: string;
  href?: string;
  onClick?: () => void;
}

const HeaderNavItem: React.FC<HeaderNavItemProps> = ({ label, active, to, href, onClick }) => {
  // External link: render as anchor to open in new tab
  if (href) {
    return (
      <a
        href={href}
        target="_blank"
        rel="noopener noreferrer"
        style={{ textDecoration: "none", color: "inherit" }}
      >
        <Box
          direction="row"
          align="center"
          gap="xsmall"
          pad={{ horizontal: "small", vertical: "xsmall" }}
          border={active ? { side: "bottom", color: "brand", size: "small" } : undefined}
          onClick={onClick}
          style={{ cursor: "pointer" }}
        >
          {label}
          <DynamicIcon
            iconName="Share"
            size="small"
          />
        </Box>
      </a>
    );
  }
  if (to) {
    return (
      <Link
        to={to}
        style={{ textDecoration: "none", color: "inherit" }}
      >
        <Box
          pad={{ horizontal: "small", vertical: "xsmall" }}
          border={active ? { side: "bottom", color: "brand", size: "small" } : undefined}
          style={{ cursor: "pointer" }}
        >
          {label}
        </Box>
      </Link>
    );
  }
  return (
    <Box
      pad={{ horizontal: "small", vertical: "xsmall" }}
      border={active ? { side: "bottom", color: "brand", size: "small" } : undefined}
      onClick={onClick}
      style={{ cursor: onClick ? "pointer" : undefined }}
    >
      {label}
    </Box>
  );
};

export default HeaderNavItem;
