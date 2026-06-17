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

import type { ComponentPropsWithoutRef, ReactNode } from "react";
import type { MetadataFieldDto } from "../../types/generated/metadataFieldDto";
import LabeledInputBox from "./LabeledInputBox";
import InfoTooltip from "./InfoTooltip";

type LabeledInputBoxProps = Omit<
  ComponentPropsWithoutRef<typeof LabeledInputBox>,
  "label" | "maxLength" | "currentLength" | "children"
>;

type SectionWithInfoProps = Omit<
  ComponentPropsWithoutRef<typeof InfoTooltip>,
  "children" | "helpText" | "onPin"
>;

type MetadataFieldInputProps = {
  field?: MetadataFieldDto;
  label: string;
  currentLength?: number;
  maxLengthFallback?: number;
  helpTextFallback?: string;
  onPin?: (payload: { text?: string }) => void;
  error?: boolean | string;
  labeledInputBoxProps?: LabeledInputBoxProps;
  sectionWithInfoProps?: SectionWithInfoProps;
  children: ReactNode;
};

const MetadataFieldInput = ({
  field,
  label,
  currentLength,
  maxLengthFallback,
  helpTextFallback,
  onPin,
  error,
  labeledInputBoxProps,
  sectionWithInfoProps,
  children,
}: MetadataFieldInputProps) => {
  const helpText = field?.tooltip ?? helpTextFallback;
  const maxLength = field?.maxLength ?? maxLengthFallback;
  const resolvedLabel = field?.label ?? label;

  return (
    <InfoTooltip
      helpText={helpText}
      examples={field?.examples}
      onPin={onPin}
      {...sectionWithInfoProps}
    >
      <LabeledInputBox
        label={resolvedLabel}
        maxLength={maxLength}
        currentLength={currentLength}
        error={error}
        {...labeledInputBoxProps}
      >
        {children}
      </LabeledInputBox>
    </InfoTooltip>
  );
};

export default MetadataFieldInput;
