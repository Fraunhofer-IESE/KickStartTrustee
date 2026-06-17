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

import {
  Button,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Heading,
  Image,
  Text,
} from "grommet";

type ActionCardProps = {
  icon: string;
  title: string;
  text: string;
  buttonLabel: string;
  href?: string;
  onClick?: () => void;
};

const ActionCard = ({
  icon,
  title,
  text,
  buttonLabel,
  href,
  onClick,
}: ActionCardProps) => {
  return (
    <Card
      align="center"
      pad={{ horizontal: "medium", top: "large", bottom: "large" }}
      gap="medium"
      background="white"
      round="medium"
      border={{ color: "#48B8EF" }}
      elevation="small"
    >
      <CardHeader
        justify="center"
        align="center"
        height="xsmall"
      >
        <Image
          src={icon}
          alt={`${title} Icon`}
        />
      </CardHeader>
      <CardBody
        align="center"
        pad={{ horizontal: "medium" }}
      >
        <Heading
          level={3}
          margin={{ top: "small", bottom: "small" }}
        >
          {title}
        </Heading>
        <Text textAlign="center">{text}</Text>
      </CardBody>
      <CardFooter>
        {href ? (
          <Button
            primary
            label={buttonLabel}
            href={href}
            target="_blank"
            rel="noopener noreferrer"
          />
        ) : (
          <Button
            primary
            label={buttonLabel}
            onClick={onClick}
          />
        )}
      </CardFooter>
    </Card>
  );
};

export default ActionCard;
