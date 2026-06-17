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

import { type ThemeType } from "grommet/themes";
import { deepMerge } from "grommet/utils";
import { grommet } from "grommet/themes";
import { Info } from "grommet-icons";
// import { hpe } from 'grommet-theme-hpe'
// import DynamicIcon from '../components/shared/DynamicIcon';

// Custom theme extending Grommet's default theme
const defaultTheme: ThemeType = deepMerge(grommet, {
  global: {
    colors: {
      brand: "#245A7C",
      "brand-secondary": "#48B8EF",
      "off-black": "#06090C",
      "off-white": "#FDFDFD",
      "button-primary": "#16425b",
      "button-secondary": "#3c7ca5",
      "border-light": "#e0e0e0",
      "text-weak": "#4A4A4A",
      focus: "transparent",
    },
    font: {
      family: "Roboto Variable, sans-serif",
      size: "16px",
      height: "20px",
      weight: 300,
    },
    input: {
      font: {
        weight: "normal",
      },
    },
    breakpoints: {
      small: {
        value: 1000,
      },
      medium: {
        value: 1000,
      },
      large: {
        value: 1200,
      },
    },
    focus: {
      border: {
        color: "transparent",
      },
      outline: {
        color: "transparent",
        size: "0px",
        offset: "0px",
      },
      shadow: "none",
    },
  },
  grommet: {
    extend: `
      overflow: visible;
    `,
  },
  text: {
    xsmall: { size: "10px", height: "14px" },
    small: { size: "12px", height: "16px" },
    medium: { size: "16px", height: "20px" }, // Standardgröße für <Text />
    large: { size: "24px", height: "28px" },
  },
  button: {
    border: {
      radius: "4px", // kleinerer Border-Radius
      width: "2px",
      color: "brand",
    },
    primary: {
      color: "brand", // custom primary button Farbe
      extend: `
        text-decoration: none !important;
      `,
    },
    secondary: {
      color: "#3c7ca5", // custom secondary button Farbe
      extend: `
        text-decoration: none !important
      `,
    },
    padding: {
      vertical: "8px",
      horizontal: "22px",
    },
    extend: `
      font-size: 0.9rem;
      font-weight: 500;
      // text-transform: uppercase;
      // text-decoration: underline;
    `,
  },
  formField: {
    font: {
      size: "1em",
    },
    border: {
      color: "transparent",
      side: "all" as const,
    },
    label: {
      requiredIndicator: true,
      size: "medium",
    },
    info: {
      icon: <Info />,
    },
  },
  layer: {
    overlay: {
      backdropFilter: "blur(4px)",
    },
  },
  heading: {
    level: {
      2: {
        font: {
          family: "Roboto Variable, sans-serif",
          size: "2rem",
          weight: 400,
        },
        extend: `
          letter-spacing: 0.5px;
        `,
      },
      3: {
        font: {
          family: "Roboto Variable, sans-serif",
          size: "1rem",
          weight: 400,
        },
        extend: `
          letter-spacing: 0.5px;
        `,
      },
    },
  },
  notification: {
    container: {
      round: "none",
      border: {
        side: "left",
        color: "brand",
      },
    },
    iconContainer: {
      align: "center",
      justify: "center",
    },
    message: {
      fill: true,
    },
    info: {
      color: "brand",
      background: "#F4F9FC",
      title: {
        color: "brand",
      },
    },
  },
  tip: {
    content: {
      elevation: "none",
      round: "medium",
      background: "transparent",
    },
    drop: {
      // background: "black",
      elevation: "medium",
      intelligentMargin: true,
    },
  },
  checkBox: {
    // border: {
    //   width: "0",
    // },
    // hover: {
    //   border: {
    //     color: "brand-secondary",
    //   },
    // },
    toggle: {
      background: "brand-secondary",
      color: "white",
      // extend: `
      //   padding: 2px;
      // `,
      knob: {
        color: "white",
        extend: `
          border: 2px solid #48B8EF;
        `,
      },
    },
  },
});

export default defaultTheme;
