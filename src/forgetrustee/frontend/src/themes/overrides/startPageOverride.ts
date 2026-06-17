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

const startPageOverride = {
  heading: {
    color: "brand",
    level: {
      2: {
        font: {
          // family: "Roboto Variable, sans-serif",
          size: "20px",
          weight: 700,
        },
        small: {
          size: "20px",
        },
        medium: {
          size: "20px",
        },
        extend: `
          letter-spacing: 0.5px;
        `,
      },
      3: {
        font: {
          weight: 300,
        },
        small: {
          size: "30px",
        },
        medium: {
          size: "30px",
        },
        large: {
          size: "40px",
        },
        xlarge: {
          size: "40px",
        },
      },
    },
  },
  button: {
    extend: `
      font-size: 0.9rem;
      font-weight: 500;
      text-transform: uppercase;
    `,
  },
};

export default startPageOverride;
