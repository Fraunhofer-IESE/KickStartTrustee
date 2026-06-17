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

import React from "react";
import * as Icons from "grommet-icons";
import { type IconProps } from "grommet-icons";

const filterIcons = (
  icons: typeof Icons,
): Record<string, React.ComponentType<IconProps>> => {
  return Object.fromEntries(
    Object.entries(icons).filter(
      ([, icon]) => typeof icon === "function" || typeof icon === "object", // Filters only components
    ),
  ) as Record<string, React.ComponentType<IconProps>>;
};

// Creating the icon mapping object by filtering only valid icon components
const iconMapping = filterIcons(Icons);

// Define types for icon names based on the filtered icon mapping object
type IconName = keyof typeof iconMapping;

// Define the props for the DynamicIcon component
interface DynamicIconProps extends IconProps {
  iconName: IconName; // Icon name must match the keys from iconMapping
}

// The DynamicIcon component renders the specified icon based on the iconName prop
const DynamicIcon: React.FC<DynamicIconProps> = ({ iconName, ...props }) => {
  const IconComponent = iconMapping[iconName];

  // Render a fallback if the icon is not found (should be caught by TypeScript)
  if (!IconComponent) {
    return <div>Icon not found</div>; // or return null or a default icon
  }

  // Render the dynamically loaded icon with passed props like size, color, etc.
  return <IconComponent {...props} />;
};

export default DynamicIcon;
