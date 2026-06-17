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

import type { MetadataModuleDto } from "../../../types/generated/metadataModuleDto";
import type { MetadataFieldDto } from "../../../types/generated/metadataFieldDto";
import type { MetadataSectionDto } from "../../../types/generated/metadataSectionDto";

export const getMetadataSection = (
  metadata: MetadataModuleDto | undefined,
  sectionId: string,
): MetadataSectionDto | undefined => metadata?.sections?.[sectionId];

export const getMetadataField = (
  metadata: MetadataModuleDto | undefined,
  sectionId: string,
  fieldId: string,
): MetadataFieldDto | undefined => metadata?.sections?.[sectionId]?.fields?.[fieldId];

/**
 * Return metadata sections as an ordered array. Works for object- or array-shaped `sections`.
 */
export const getMetadataSections = (
  metadata: MetadataModuleDto | undefined,
): MetadataSectionDto[] => {
  if (!metadata?.sections) return [];

  // If sections is already an array, return it as-is
  if (Array.isArray(metadata.sections)) {
    return metadata.sections as MetadataSectionDto[];
  }

  // Otherwise assume an object keyed by section id — preserve insertion order
  return Object.entries(metadata.sections as Record<string, MetadataSectionDto>).map(
    ([id, sec]) => ({ ...(sec as MetadataSectionDto), id: sec.id ?? id }),
  );
};
