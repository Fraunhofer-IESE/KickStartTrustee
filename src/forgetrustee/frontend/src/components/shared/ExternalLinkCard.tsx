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

import { Box, Card, CardBody, CardHeader, Text } from "grommet";
import DynamicIcon from "./DynamicIcon";
import { useCallback } from "react";

type ExternalLinkCardProps = {
  title: string;
  href: string;
  description?: string;
};

const ExternalLinkCard = ({
  title,
  href,
  description,
}: ExternalLinkCardProps) => {
  const handleOnClick = useCallback(() => {
    window.open(href, "_blank", "noopener,noreferrer");
  }, [href]);

  return (
    <Card
      pad="medium"
      gap="small"
      background="white"
      round="small"
      elevation="small"
      onClick={handleOnClick}
      focusIndicator={false}
      hoverIndicator={true}
    >
      <CardHeader>
        <Box
          direction="row"
          justify="between"
          align="center"
          gap="small"
          fill="horizontal"
        >
          <Text
            size="small"
            color="brand"
            weight="bold"
          >
            {title}
          </Text>
          <DynamicIcon
            iconName="Share"
            size="small"
          />
        </Box>
      </CardHeader>
      {description && (
        <CardBody>
          <Text size="small">{description}</Text>
        </CardBody>
      )}
    </Card>
  );
};

export default ExternalLinkCard;
