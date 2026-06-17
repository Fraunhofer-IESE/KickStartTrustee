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

import { Box, Grid, Heading, Image, ResponsiveContext, Text } from "grommet";
import { useNavigate } from "react-router";
import { routes } from "../../config/routes";
import ActionCard from "../shared/ActionCard";

import informierenIcon from "../../assets/images/icon-informieren.svg";
import modellierenIcon from "../../assets/images/icon-modellieren.svg";
import vergleichenIcon from "../../assets/images/icon-vergleichen.svg";
import projects from "../../assets/images/projects.svg";
import ExternalLinkCard from "../shared/ExternalLinkCard";
import { useContext } from "react";
import hand from "../../assets/hand.svg";
import logo from "../../assets/images/kickstarttrustee.svg";

const StartPageContent = () => {
  const navigate = useNavigate();
  const size = useContext(ResponsiveContext);
  const isCompact = size === "small" || size === "xsmall" || size === "xxsmall";

  return (
    <Box align="center">
      <Box
        pad={{ bottom: "large" }}
        width={{ max: "xxlarge" }}
        fill="horizontal"
      >
        <Box
          direction="row"
          height="medium"
        >
          <Box
            width="50%"
            align="start"
            justify="start"
          >
            <Image
              src={hand}
              height="100%"
            />
          </Box>
          <Box
            width="50%"
            align="end"
            justify="center"
            pad={{ right: "xlarge" }}
          >
            <Box>
              <Image
                src={logo}
                margin={{ bottom: "medium" }}
              />
              <Text
                size="medium"
                alignSelf="stretch"
                textAlign="center"
              >
                Datentreuhandmodelle modellieren und vergleichen
              </Text>
            </Box>
          </Box>
        </Box>
      </Box>

      <Box
        align="center"
        background="#EBF6FC"
        fill="horizontal"
        pad="large"
      >
        <Box
          direction="column"
          width={{ max: "xxlarge" }}
          pad={{ horizontal: "xlarge" }}
          background="#EBF6FC"
          fill
          gap="large"
        >
          <Box
            align="center"
            gap="xsmall"
          >
            <Heading
              level={2}
              margin="none"
            >
              Datentreuhandmodelle
            </Heading>
            <Text
              style={{
                fontWeight: "300",
                fontSize: "40px",
                fontStyle: "light",
              }}
            >
              Ihre Möglichkeiten
            </Text>
          </Box>
          <Grid
            gap="large"
            columns={{ count: isCompact ? 1 : 3, size: "auto" }}
            justify="center"
            pad={{ top: "medium" }}
            fill="horizontal"
          >
            <ActionCard
              icon={informierenIcon}
              title="Informieren"
              text="Erfahren Sie mehr über Datentreuhandmodelle und deren Einsatzmöglichkeiten. Unser Git Repository bietet umfassende Informationen und Ressourcen."
              buttonLabel="Zum Git Repository"
              href={routes.inform.path}
            />
            <ActionCard
              icon={vergleichenIcon}
              title="Vergleichen"
              text="Verschaffen Sie sich einen Überblick, über selbst erstellte und bestehende Modelle und vergleichen Sie diese miteinander."
              buttonLabel="Zu Übersicht & Vergleich"
              onClick={() => navigate(routes.dataTrusteeCompare.path)}
            />
            <ActionCard
              icon={modellierenIcon}
              title="Modellieren"
              text="Modellieren Sie ihr eigenes Datentreuhandmodell. Sie können bestehende Modelle als  Vorlage verwenden oder vorherige Eigenmodellierungen importieren."
              buttonLabel="Zur Modellierung"
              onClick={() => navigate(routes.dataTrusteeModeling.path)}
            />
          </Grid>
        </Box>
      </Box>
      <Box
        as="section"
        align="center"
        justify="center"
        background="white"
        fill="horizontal"
        pad={{ top: "large", bottom: "none" }}
        flex
        width={{ max: "xxlarge" }}
      >
        <Box
          direction="column"
          width={{ max: "xxlarge" }}
          fill
          gap="large"
        >
          <Box
            align="center"
            pad={{ horizontal: "xlarge" }}
            gap="xsmall"
          >
            <Heading
              level={2}
              margin="none"
            >
              Forschung rund um Datentreuhand
            </Heading>
            <Text
              style={{
                fontWeight: "300",
                fontSize: "40px",
                fontStyle: "light",
              }}
            >
              Unsere Projekte
            </Text>
          </Box>
          <Box
            direction="row"
            gap="xlarge"
          >
            <Box
              justify="start"
              pad={{ bottom: "large", left: "xlarge", right: "xlarge" }}
            >
              <Grid
                gap="medium"
                columns={{ count: 2, size: "auto" }}
                pad={{ top: "medium" }}
              >
                <ExternalLinkCard
                  title="BridgeUp"
                  href="https://www.iese.fraunhofer.de/de/referenz/bridge-up.html"
                  description="Projekt zur Brückenbildung in Datenökosystemen."
                />
                <ExternalLinkCard
                  title="TransitOn"
                  href="https://infai.org/project/transit-on/"
                  description="Initiative für vertrauenswürdige Datenübertragung."
                />
                <ExternalLinkCard
                  title="KickstartTrustee"
                  href="https://www.kickstarttrustee.de/"
                  description="Vorgängerprojekt mit grundlegenden Konzepten und Beispielen."
                />
                <ExternalLinkCard
                  title="ForgeTrustee"
                  href="https://forgetrustee.kickstarttrustee.de/"
                  description="Weiterentwicklung und Demonstrator für Datentreuhandmodelle."
                />
              </Grid>
            </Box>
            <Box
              justify="end"
              align="end"
            >
              <Image
                src={projects}
                alt="Projects Illustration"
                fill="horizontal"
              />
            </Box>
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default StartPageContent;
