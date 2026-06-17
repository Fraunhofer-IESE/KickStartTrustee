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

import { Box, Grid } from "grommet";
import { Outlet } from "react-router-dom";
import LayoutHeader from "./LayoutHeader";
import ScrollToTop from "../shared/ScrollToTop";
import LayoutFooter from "./LayoutFooter";
import { useRef } from "react";
import { LayoutContainerContext } from "./LayoutContainerContext";

const Layout = () => {
  const containerRef = useRef<HTMLDivElement | null>(null);

  return (
    <>
      <ScrollToTop />
      <Grid
        rows={["auto", "flex", "auto"]}
        columns={["auto", "flex", "auto"]}
        areas={[
          { name: "header", start: [0, 0], end: [2, 0] },
          { name: "leftSidebar", start: [0, 1], end: [0, 1] },
          { name: "main", start: [1, 1], end: [1, 1] },
          { name: "rightSidebar", start: [2, 1], end: [2, 1] },
          { name: "footer", start: [0, 2], end: [2, 2] },
        ]}
        height={{ min: "100vh" }}
        fill="horizontal"
      >
        <Box
          gridArea="header"
          direction="row"
          justify="center"
        >
          <Box
            fill
            align="center"
            border={{ side: "bottom", color: "border", size: "xsmall" }}
            // width={{ max: "xxlarge" }}
          >
            <Box
              width={{ max: "xxlarge" }}
              fill="horizontal"
            >
              <LayoutHeader />
            </Box>
          </Box>
        </Box>
        <Box
          gridArea="main"
          pad={{ vertical: "medium", horizontal: "large" }}
          fill
          align="center"
        >
          <LayoutContainerContext.Provider value={containerRef}>
            <Box
              width={{ max: "xxlarge" }}
              fill
              ref={containerRef}
            >
              <Outlet />
            </Box>
          </LayoutContainerContext.Provider>
        </Box>
        <Box gridArea="rightSidebar"></Box>
        <Box
          fill="horizontal"
          gridArea="footer"
          align="center"
          border={{ side: "top", color: "border", size: "xsmall" }}
        >
          <Box
            width={{ max: "xxlarge" }}
            fill="horizontal"
          >
            <LayoutFooter />
          </Box>
        </Box>
      </Grid>
    </>
  );
};

export default Layout;
