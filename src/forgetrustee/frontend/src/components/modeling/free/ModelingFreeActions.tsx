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

import { Box, Button, Text, Tip } from "grommet";

type ModelingFreeActionsProps = {
  activeStep: number;
  totalSteps: number;
  activeStepIsValid: boolean;
  missingRequiredFieldLabels: string[];
  allStepsValid: boolean;
  showModelingHint?: boolean;
  onPrevStep: () => void;
  onNextStep: () => void;
  onRevealMissingRequiredFields: () => void;
  onStartModeling: () => void;
};

const ModelingFreeActions = ({
  activeStep,
  totalSteps,
  activeStepIsValid,
  missingRequiredFieldLabels,
  allStepsValid,
  showModelingHint = false,
  onPrevStep,
  onNextStep,
  onRevealMissingRequiredFields,
  onStartModeling,
}: ModelingFreeActionsProps) => (
  <Box
    height={{ min: "auto" }}
    pad={{ top: "xlarge", bottom: "medium" }}
    gap="small"
  >
    <Box
      direction="row"
      gap="small"
      alignSelf="end"
    >
      {activeStep > 1 && (
        <Button
          label="Zurück"
          secondary
          onClick={onPrevStep}
          pad={{ horizontal: "large" }}
        />
      )}

      {activeStep < totalSteps &&
        (!activeStepIsValid && missingRequiredFieldLabels.length > 0 ? (
          <Tip
            content={
              <Box
                pad="small"
                gap="xxsmall"
                width={{ max: "medium" }}
              >
                <Text
                  size="small"
                  weight="bold"
                >
                  Pflichtfelder fehlen:
                </Text>
                {missingRequiredFieldLabels.map((label) => (
                  <Text
                    key={label}
                    size="small"
                  >
                    {`- ${label}`}
                  </Text>
                ))}
                <Box align="end">
                  <Button
                    label="Felder anzeigen"
                    plain
                    size="small"
                    onClick={onRevealMissingRequiredFields}
                  />
                </Box>
              </Box>
            }
          >
            <Box onClick={onRevealMissingRequiredFields}>
              <Button
                label="Weiter"
                primary
                onClick={onNextStep}
                pad={{ horizontal: "large" }}
              />
            </Box>
          </Tip>
        ) : (
          <Button
            label="Weiter"
            primary
            onClick={onNextStep}
            pad={{ horizontal: "large" }}
          />
        ))}

      {activeStep === totalSteps &&
        (showModelingHint ? (
          <Tip
            content={
              <Box
                pad="small"
                gap="xxsmall"
                width={{ max: "medium" }}
                background="background"
              >
                <Text
                  size="small"
                  weight="bold"
                >
                  Noch nichts bearbeitet
                </Text>
                <Text size="small">
                  Bitte zuerst einen oder mehrere Schritte ausfüllen. Danach kann das Modell
                  erstellt werden.
                </Text>
              </Box>
            }
            dropProps={{
              background: "background",
            }}
          >
            <Box>
              <Button
                label="Modellieren"
                primary
                onClick={onStartModeling}
                disabled={!allStepsValid}
                pad={{ horizontal: "large" }}
              />
            </Box>
          </Tip>
        ) : (
          <Button
            label="Modellieren"
            primary
            onClick={onStartModeling}
            disabled={!allStepsValid}
            pad={{ horizontal: "large" }}
          />
        ))}
    </Box>
  </Box>
);

export default ModelingFreeActions;
