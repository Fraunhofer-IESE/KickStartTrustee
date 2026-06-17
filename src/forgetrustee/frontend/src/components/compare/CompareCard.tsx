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

import { Box, Button, Card, CardBody, CardFooter, CardHeader, Text } from "grommet";
import DynamicIcon from "../shared/DynamicIcon";
import CustomThemeSection from "../util/CustomThemeSection";
import compareCardButtonOverride from "../../themes/overrides/compareCardButtonOverride";
import placeholder from "../../assets/placeholder.svg";
import type { DataTrusteeModelResponseDto } from "../../types/generated/dataTrusteeModelResponseDto";
import useAvatarDataUrl from "../shared/useAvatarDataUrl";

export type DthData = {
  id: string;
  modelId?: string;
  imageDataUrl?: string | null;
  imageFileName?: string | null;
  name: string;
  description: string;
  model?: DataTrusteeModelResponseDto;
  isOwnModel?: boolean;
};

interface CompareCardProps {
  item: DthData;
  selected?: boolean;
  disabled?: boolean;
  onSelect?: (id: string) => void;
  onDetail?: () => void;
  showSelectButton?: boolean;
}

const CompareCard = ({
  item,
  selected = false,
  disabled = false,
  onSelect,
  onDetail,
  showSelectButton = true,
}: CompareCardProps) => {
  const isOwnModel = Boolean(item.isOwnModel);
  const avatarDataUrl = useAvatarDataUrl(item.modelId, item.imageDataUrl ?? null);

  return (
    <Card
      pad="large"
      gap="none"
      background={isOwnModel ? "background-back" : "background-front"}
      round="medium"
      elevation={isOwnModel ? "medium" : "small"}
      border={{
        color: "border-light",
        size: "xsmall",
      }}
    >
      <CardHeader pad="none">
        <Box
          direction="row"
          fill="horizontal"
          justify="between"
        >
          <Box
            direction="column"
            gap="xsmall"
            flex
          >
            <Text size="xlarge">{item.name}</Text>
            {isOwnModel ? (
              <Box
                alignSelf="start"
                background="brand"
                round="xsmall"
                pad={{ horizontal: "small", vertical: "xxsmall" }}
              >
                <Text
                  size="xsmall"
                  color="white"
                >
                  Eigenes Modell
                </Text>
              </Box>
            ) : null}
          </Box>
          <Box>
            <img
              src={avatarDataUrl || placeholder}
              alt={item.imageFileName || item.name || "Datentreuhandmodell"}
              style={{
                width: "60px",
                height: "60px",
                objectFit: "contain",
              }}
            />
          </Box>
        </Box>
      </CardHeader>
      <CardBody pad={{ vertical: "small", horizontal: "none" }}>
        <Box gap="xsmall">
          <Text
            size="small"
            color="text"
          >
            {item.description}
          </Text>
        </Box>
      </CardBody>
      <CardFooter
        pad={{ horizontal: "none", vertical: "medium" }}
        justify="center"
      >
        <Box
          fill="horizontal"
          direction="row"
          justify="between"
        >
          <Box>
            {showSelectButton && (
              <CustomThemeSection customTheme={compareCardButtonOverride}>
                <Button
                  icon={
                    <DynamicIcon
                      iconName="Add"
                      size="small"
                      color={selected ? "white" : "brand"}
                    />
                  }
                  disabled={disabled}
                  onClick={() => onSelect && onSelect(item.id)}
                  a11yTitle="Zum Vergleich hinzufügen"
                  active={selected}
                  color="brand"
                />
              </CustomThemeSection>
            )}
          </Box>
          <Button
            plain
            label="Detailansicht"
            onClick={onDetail}
            disabled={!onDetail}
          />
        </Box>
      </CardFooter>
    </Card>
  );
};

export default CompareCard;
