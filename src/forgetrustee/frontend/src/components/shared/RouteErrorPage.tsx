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
import { isRouteErrorResponse, useRouteError, type ErrorResponse } from "react-router-dom";
import { routes } from "../../config/routes";
import ErrorPageShell from "./ErrorPageShell";

const getErrorText = (error: unknown): string => {
  if (isRouteErrorResponse(error)) {
    const routeError = error as ErrorResponse;
    return routeError.statusText || routeError.status.toString();
  }

  if (error instanceof Error) {
    return error.message;
  }

  return "Unbekannter Fehler";
};

const RouteErrorPage = () => {
  const error = useRouteError();
  const errorText = getErrorText(error);

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
          Die Seite konnte nicht geladen werden
        </Text>
        <Text
          textAlign="center"
          size="medium"
        >
          Es ist ein Fehler innerhalb der Anwendung aufgetreten.
        </Text>
        <Text
          textAlign="center"
          size="small"
          color="text-weak"
        >
          {errorText}
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
          onClick={() => window.location.assign(routes.startpage.path)}
        />
      </Box>
    </ErrorPageShell>
  );
};

export default RouteErrorPage;
