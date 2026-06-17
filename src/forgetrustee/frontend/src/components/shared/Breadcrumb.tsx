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

import { Box, Text } from "grommet";
import { Link } from "react-router-dom";
import type { RouteConfig } from "../../types/routeconfig";

interface BreadcrumbProps {
  items: RouteConfig[];
}

const Breadcrumb: React.FC<BreadcrumbProps> = ({ items }) => (
  <Box margin={{ bottom: "xsmall" }}>
    <Text
      size="small"
      color="text-xweak"
    >
      {items.map((item, idx) => (
        <>
          {item.path ? (
            <Link
              to={item.path}
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <Text
                color="brand"
                size="small"
              >
                {item.name}
              </Text>
            </Link>
          ) : (
            item.name
          )}
          {idx < items.length - 1 && " > "}
        </>
      ))}
    </Text>
  </Box>
);

export default Breadcrumb;
