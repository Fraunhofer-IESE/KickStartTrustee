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

import { useEffect, useState } from "react";
import { Box, Button, Paragraph, Text } from "grommet";
import HtmlContent from "../../shared/HtmlContent";

export type PinnedTip = {
  fieldName: string;
  text?: string;
  callToAction?: string;
};

type PinnedTipsPanelProps = {
  tips: PinnedTip[];
  onUnpin: (fieldName: string) => void;
  anchorEl?: HTMLElement | null;
  bottomOffset?: number;
};

type PanelPosition = { right: number; width: number; bottomAdjust: number };

const PinnedTipsPanel = ({ tips, onUnpin, anchorEl, bottomOffset = 24 }: PinnedTipsPanelProps) => {
  const [pos, setPos] = useState<PanelPosition | null>(null);

  useEffect(() => {
    if (!anchorEl) {
      setPos(null);
      return undefined;
    }

    const update = () => {
      const rect = anchorEl.getBoundingClientRect();
      setPos({
        right: Math.max(window.innerWidth - rect.right, 0),
        width: rect.width,
        bottomAdjust: Math.max(window.innerHeight - rect.bottom, 0),
      });
    };

    update();
    window.addEventListener("resize", update, { passive: true });
    window.addEventListener("scroll", update, { passive: true });
    const resizeObserver = new ResizeObserver(() => update());
    resizeObserver.observe(anchorEl);
    return () => {
      window.removeEventListener("resize", update);
      window.removeEventListener("scroll", update);
      resizeObserver.disconnect();
    };
  }, [anchorEl]);

  if (!tips.length || !pos) {
    return null;
  }

  return (
    <Box
      background="background-front"
      pad="medium"
      round="small"
      elevation="medium"
      style={{
        position: "fixed",
        maxHeight: "60vh",
        overflowY: "auto",
        bottom: `${bottomOffset + pos.bottomAdjust}px`,
        right: `${pos.right}px`,
        zIndex: 20,
      }}
      animation="fadeIn"
      overflow="auto"
      border={{ color: "brand" }}
      height={{ min: "auto" }}
    >
      <Box
        gap="small"
        height={{ min: "auto" }}
      >
        {tips.map((tip) => (
          <Box
            key={tip.fieldName}
            direction="column"
            gap="large"
            align="start"
            height={{ min: "auto" }}
          >
            <Box
              flex
              height={{ min: "auto" }}
              width="medium"
            >
              <Text
                size="small"
                color="text-weak"
                wordBreak="break-word"
              >
                {tip.fieldName}
              </Text>
              <Box height={{ min: "auto" }}>
                <HtmlContent sanitize={false}>{tip.text ?? "(kein Text)"}</HtmlContent>
              </Box>
              <Box>
                {tip.callToAction && (
                  <Paragraph
                    size="small"
                    margin={{ top: "xxsmall", bottom: "none" }}
                    color="brand"
                  >
                    {tip.callToAction}
                  </Paragraph>
                )}
              </Box>
            </Box>
            <Box
              justify="end"
              direction="row"
              align="center"
              fill="horizontal"
            >
              <Button
                primary
                label="Schließen"
                onClick={() => onUnpin(tip.fieldName)}
              />
            </Box>
          </Box>
        ))}
      </Box>
    </Box>
  );
};

export default PinnedTipsPanel;
