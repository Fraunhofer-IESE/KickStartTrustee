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
import type { ReactNode } from "react";
import CustomThemeSection from "../util/CustomThemeSection";
import startPageOverride from "../../themes/overrides/startPageOverride";
import ScrollToTop from "./ScrollToTop";
import Header from "../startpage/StartPageHeader";
import Footer from "../startpage/StartPageFooter";

type ErrorPageShellProps = {
  children: ReactNode;
};

const ErrorPageShell = ({ children }: ErrorPageShellProps) => {
  return (
    <CustomThemeSection customTheme={startPageOverride}>
      <ScrollToTop />
      <Box
        fill="horizontal"
        align="center"
      >
        <Grid
          rows={["auto", "flex", "auto"]}
          columns={["flex"]}
          areas={[
            { name: "header", start: [0, 0], end: [0, 0] },
            { name: "main", start: [0, 1], end: [0, 1] },
            { name: "footer", start: [0, 2], end: [0, 2] },
          ]}
          height={{ min: "100vh" }}
          style={{ overflow: "hidden" }}
          fill="horizontal"
        >
          <Header />
          <Box
            gridArea="main"
            align="center"
            justify="center"
            fill="horizontal"
            pad="large"
          >
            {children}
          </Box>
          <Footer />
        </Grid>
      </Box>
    </CustomThemeSection>
  );
};

export default ErrorPageShell;
