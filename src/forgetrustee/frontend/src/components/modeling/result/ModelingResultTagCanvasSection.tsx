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

import { Box, Stack, Text } from "grommet";
import CustomTag from "../common/CustomTag";

interface ModelingResultTagCanvasSectionProps {
  headlineTag?: string;
  headlinTagPosition?: "left" | "center" | "right";
  tagCategory: "core" | "data" | "legal" | "objectives" | "business";
  selectedTagId?: string | null;
  onTagClick?: (
    tag: string,
    category: "core" | "data" | "legal" | "objectives" | "business",
  ) => void;
  subSections: {
    title: string;
    groups: {
      title: string;
      tags: string[];
    }[];
  }[];
}

/**
 * Component for displaying a section of tags on the modeling result canvas. It can optionally
 * display a headline tag prominently, with sub-sections of additional tags below it.
 *
 * @param param0 The props for the component
 *
 * @returns A React element representing the section of tags.
 */
const ModelingResultTagCanvasSection = ({
  headlineTag,
  headlinTagPosition = "left",
  tagCategory,
  selectedTagId = null,
  onTagClick,
  subSections,
}: ModelingResultTagCanvasSectionProps) => {
  const tagPositionMap = {
    left: "top-left",
    center: "top",
    right: "top-right",
  } as const;

  const padBasedOnPosition = {
    left: { top: "small", left: "medium" },
    center: { top: "small" },
    right: { top: "small", right: "medium" },
  } as const;

  /**
   * Formats the label for a given tag.
   *
   * @param tag The tag to format.
   *
   * @returns The formatted label.
   */
  const formatTagLabel = (tag: string) => {
    if (tag === "NONE") {
      return "Keine Auswahl";
    }

    if (tag === "AMBIGUOUS") {
      return "Unklar";
    }

    return tag;
  };

  /**
   * Determines if there is any tag data available in the provided sub-sections.
   *
   * @returns `true` if there is at least one tag in the sub-sections, otherwise `false`.
   */
  const hasTagData = subSections.some((subSection) =>
    subSection.groups.some((group) => group.tags.length > 0),
  );

  const noDataHint = (
    <Text
      size="small"
      color="text-weak"
    >
      Keine Daten vorhanden
    </Text>
  );

  return (
    <Box
      pad={{ top: "medium" }}
      fill="horizontal"
    >
      {headlineTag ? (
        <Stack anchor={tagPositionMap[headlinTagPosition]}>
          <Box margin={padBasedOnPosition[headlinTagPosition]}>
            <Box
              border
              round="xsmall"
              pad={{ top: "large", bottom: "small", horizontal: "small" }}
              background="background"
            >
              {hasTagData
                ? subSections.map((subSection, index) => (
                    <Box
                      key={index}
                      margin={{
                        bottom: index < subSections.length - 1 ? "medium" : "none",
                      }}
                    >
                      <Text>{subSection.title}</Text>
                      <Box
                        direction="column"
                        gap="small"
                      >
                        {subSection.groups.map((group, groupIndex) => (
                          <Box
                            key={`${group.title}-${groupIndex}`}
                            direction="column"
                            gap="xsmall"
                          >
                            <Text
                              size="small"
                              color="text-weak"
                            >
                              {group.title}
                            </Text>
                            <Box
                              direction="column"
                              gap="xsmall"
                            >
                              {group.tags.map((tag, tagIndex) => (
                                <CustomTag
                                  key={tagIndex}
                                  label={formatTagLabel(tag)}
                                  selected={selectedTagId === `${tagCategory}:${tag}`}
                                  onClick={
                                    onTagClick ? () => onTagClick(tag, tagCategory) : undefined
                                  }
                                />
                              ))}
                            </Box>
                          </Box>
                        ))}
                      </Box>
                    </Box>
                  ))
                : noDataHint}
            </Box>
          </Box>
          <CustomTag
            label={formatTagLabel(headlineTag)}
            selected={selectedTagId === `${tagCategory}:${headlineTag}`}
            onClick={onTagClick ? () => onTagClick(headlineTag, tagCategory) : undefined}
          />
        </Stack>
      ) : (
        <Box>
          <Box
            border
            round="xsmall"
            pad={{ top: "small", bottom: "small", horizontal: "small" }}
            background="background"
          >
            {hasTagData
              ? subSections.map((subSection, index) => (
                  <Box
                    key={index}
                    margin={{
                      bottom: index < subSections.length - 1 ? "medium" : "none",
                    }}
                    height={{ min: "auto" }}
                  >
                    <Text>{subSection.title}</Text>
                    <Box
                      direction="column"
                      gap="small"
                    >
                      {subSection.groups.map((group, groupIndex) => (
                        <Box
                          key={`${group.title}-${groupIndex}`}
                          direction="column"
                          gap="xsmall"
                        >
                          <Text
                            size="small"
                            color="text-weak"
                          >
                            {group.title}
                          </Text>
                          <Box
                            direction="column"
                            gap="xsmall"
                          >
                            {group.tags.map((tag, tagIndex) => (
                              <CustomTag
                                key={tagIndex}
                                label={formatTagLabel(tag)}
                                selected={selectedTagId === `${tagCategory}:${tag}`}
                                onClick={
                                  onTagClick ? () => onTagClick(tag, tagCategory) : undefined
                                }
                              />
                            ))}
                          </Box>
                        </Box>
                      ))}
                    </Box>
                  </Box>
                ))
              : noDataHint}
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default ModelingResultTagCanvasSection;
