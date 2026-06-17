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

import { Box, Grid } from "grommet";
import { Fragment, useEffect, useRef, useState } from "react";
import type { ModelingStepNavigationProps } from "./ModelingStepNavigation";
import StepVertical from "./StepVertical";

const buildStepGridColumns = (stepCount: number, stepToGapRatio = 2) => {
  if (stepCount <= 0) return [];
  const gapCount = Math.max(stepCount - 1, 0);
  const totalUnits = stepCount * stepToGapRatio + gapCount * 1;
  const unitPercent = 100 / totalUnits;

  const columns: string[] = [];
  for (let i = 0; i < stepCount; i++) {
    columns.push(`${(stepToGapRatio * unitPercent).toFixed(2)}%`);
    if (i < stepCount - 1) {
      columns.push(`${unitPercent.toFixed(2)}%`);
    }
  }
  return columns;
};

const ModelingStepNavigationVertical = ({
  modulesSummary,
  activeStep,
  onStepClick,
  isStepEdited,
  isStepValid,
}: ModelingStepNavigationProps) => {
  const columns = buildStepGridColumns(modulesSummary.modules.length, 4);
  const stickyRef = useRef<HTMLDivElement | null>(null);
  const [isStuck, setIsStuck] = useState(false);

  useEffect(() => {
    const updateStickyState = () => {
      if (!stickyRef.current) return;
      const { top } = stickyRef.current.getBoundingClientRect();
      const stuck = top <= 0.5 && window.scrollY > 0;
      setIsStuck(stuck);
    };

    updateStickyState();
    window.addEventListener("scroll", updateStickyState, { passive: true });
    window.addEventListener("resize", updateStickyState);

    return () => {
      window.removeEventListener("scroll", updateStickyState);
      window.removeEventListener("resize", updateStickyState);
    };
  }, []);

  return (
    <Box
      ref={stickyRef}
      background="white"
      style={{ position: "sticky", top: "0em", zIndex: 10 }}
      border={isStuck ? { side: "bottom", color: "border", size: "xsmall" } : undefined}
      pad={{ top: "small", bottom: "small" }}
    >
      <Grid
        columns={columns}
        width={{ max: "100%" }}
      >
        {modulesSummary.modules.map((step, idx) => {
          const edited = isStepEdited?.(idx + 1) ?? false;
          const valid = isStepValid?.(idx + 1) ?? false;

          return (
            <Fragment key={step.order}>
              <StepVertical
                number={idx + 1}
                title={step.menuPoint}
                active={activeStep === idx + 1}
                edited={edited}
                valid={valid}
                onClick={() => onStepClick(idx + 1)}
              />
              {idx < modulesSummary.modules.length - 1 && (
                <Box
                  align="center"
                  justify="center"
                  fill
                >
                  <Box
                    width="100%"
                    height="1px"
                    background="border"
                  />
                </Box>
              )}
            </Fragment>
          );
        })}
      </Grid>
    </Box>
  );
};

export default ModelingStepNavigationVertical;
