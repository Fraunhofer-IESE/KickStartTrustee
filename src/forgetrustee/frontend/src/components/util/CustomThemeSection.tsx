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

import { ThemeContext, type ThemeType } from "grommet";
import { deepMerge } from "grommet/utils";
import { useContext, useMemo, type PropsWithChildren } from "react";

type CustomThemeSectionProps = {
  customTheme: ThemeType;
};

/**
 * Helper component to apply a custom theme to its children by merging it with the parent theme.
 *
 * @param props
 * @returns
 */
const CustomThemeSection: React.FC<
  PropsWithChildren<CustomThemeSectionProps>
> = (props) => {
  const { customTheme, children } = props;
  const parentTheme = useContext(ThemeContext) as ThemeType;

  // Merge nur neu berechnen, wenn sich eins der Themes ändert
  const mergedTheme = useMemo(
    () => deepMerge(parentTheme ?? {}, customTheme),
    [parentTheme, customTheme],
  );

  return (
    <ThemeContext.Provider value={mergedTheme}>
      {children}
    </ThemeContext.Provider>
  );
};

export default CustomThemeSection;
