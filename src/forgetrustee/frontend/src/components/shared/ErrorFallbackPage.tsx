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

import { Anchor, Box, Button, Text } from "grommet";
import type { FallbackProps } from "react-error-boundary";
import ErrorPageShell from "./ErrorPageShell";
import { routes } from "../../config/routes";

const ErrorFallbackPage = ({ resetErrorBoundary }: FallbackProps) => {
  return (
    <ErrorPageShell>
      <Box
        background="background-front"
        pad="large"
        round="small"
        elevation="small"
        gap="medium"
        width={{ max: "large" }}
        align="center"
      >
        <Text
          as="h1"
          size="xlarge"
          weight="bold"
          textAlign="center"
        >
          Da ist etwas schiefgelaufen
        </Text>
        <Text
          textAlign="center"
          size="medium"
        >
          Bitte laden Sie die Seite neu. Falls der Fehler bleibt, starten Sie die Anwendung erneut.
        </Text>
        <Text
          textAlign="center"
          size="medium"
        >
          Wenn das Problem bestehen bleibt, kontaktieren Sie uns unter{" "}
          <Anchor href="mailto:forgetrustee@iese.fraunhofer.de">
            forgetrustee@iese.fraunhofer.de
          </Anchor>
          .
        </Text>
        <Button
          label="Zur Startseite"
          primary
          onClick={() => {
            resetErrorBoundary();
            window.location.assign(routes.startpage.path);
          }}
        />
      </Box>
    </ErrorPageShell>
  );
};

export default ErrorFallbackPage;
