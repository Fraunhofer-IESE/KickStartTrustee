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

import { Box } from "grommet";
import Step from "./Step";
import type { MetadataModuleIndexSummaryDto } from "../../../types/generated";

export interface ModelingStepNavigationProps {
  modulesSummary: MetadataModuleIndexSummaryDto;
  activeStep: number;
  onStepClick: (step: number) => void;
  isStepEdited?: (step: number) => boolean;
  isStepValid?: (step: number) => boolean;
}

const modelingSteps = [
  {
    number: 1,
    title: "Akteure",
    description: "Beteiligte Akteure",
    help: "onHelpStep1",
    completed: true,
  },
  {
    number: 2,
    title: "Rechtsgrundlage",
    description: "DTM Ziele",
    help: "onHelpStep2",
  },
  {
    number: 3,
    title: "Schritt 3",
    description: "Schritt 3",
    help: "onHelpStep2",
  },
  {
    number: 4,
    title: "Schritt 4",
    description: "Schritt 4",
    help: "onHelpStep2",
  },
  {
    number: 5,
    title: "Schritt 5",
    description: "Schritt 5",
    help: "onHelpStep2",
  },
  {
    number: 6,
    title: "Schritt 6",
    description: "Schritt 6",
    help: "onHelpStep2",
    showLine: false,
  },
];

const ModelingStepNavigation = ({
  activeStep,
  onStepClick,
  isStepValid,
}: ModelingStepNavigationProps) => (
  <Box
    direction="column"
    gap="medium"
    style={{ position: "sticky", top: "1em", zIndex: 10 }}
  >
    {modelingSteps.map((step) => (
      <Step
        key={step.number}
        number={step.number}
        title={step.title}
        description={step.description}
        showLine={step.showLine !== false}
        active={activeStep === step.number}
        valid={isStepValid?.(step.number) ?? false}
        onClick={() => onStepClick(step.number)}
        completed={step.completed}
      />
    ))}
  </Box>
);

export default ModelingStepNavigation;
