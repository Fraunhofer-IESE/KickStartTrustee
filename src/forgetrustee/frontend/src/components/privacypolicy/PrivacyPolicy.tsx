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

import React, { useEffect, useState } from "react";
import { Box } from "grommet";
import Breadcrumb from "../shared/Breadcrumb";
import { routes } from "../../config/routes";

const generatorUrl =
  "https://dsi-generator.fraunhofer.de/dsi/view/de/0d8c8be4-59ae-4bd7-8d0d-d127955cb999/";

const PrivacyPolicy: React.FC = () => {
  const [content, setContent] = useState<string | null>(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    fetch(generatorUrl)
      .then((res) => res.text())
      .then(setContent)
      .catch(() => setError(true));
  }, []);

  if (error) {
    return (
      <Box
        background="status-critical"
        width={{ max: "xxlarge" }}
        alignSelf="center"
        pad={{ bottom: "large" }}
      >
        <strong>
          Fehler beim Abrufen des Inhalts.
          <br />
          &nbsp;
        </strong>
        <div>
          <strong>
            Die Datenschutzinformationen können Sie später unter{" "}
            <em>
              <a href={generatorUrl}>{generatorUrl}</a>
            </em>{" "}
            herunterladen.
          </strong>
        </div>
      </Box>
    );
  }

  if (!content) {
    return (
      <Box
        background="background-contrast"
        width={{ max: "xxlarge" }}
        alignSelf="center"
        pad={{ bottom: "large" }}
      >
        <strong>
          Die Datenschutzinformationen können Sie{" "}
          <em>
            <a href={generatorUrl}>hier</a>
          </em>{" "}
          herunterladen.
        </strong>
      </Box>
    );
  }

  return (
    <Box
      background="background"
      width={{ max: "xxlarge" }}
      alignSelf="center"
      pad={{ bottom: "large" }}
    >
      {/* Breadcrumb */}
      <Box
        direction="row"
        justify="between"
        height={{ min: "auto" }}
        width={{ max: "xxlarge" }}
      >
        <Breadcrumb items={[routes.startpage, routes.privacypolicy]} />
      </Box>
      <div dangerouslySetInnerHTML={{ __html: content }} />
    </Box>
  );
};

export default PrivacyPolicy;
