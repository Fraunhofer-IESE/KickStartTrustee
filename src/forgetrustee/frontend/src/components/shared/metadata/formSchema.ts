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

export type MetadataFormFieldType = "text" | "textarea" | "select" | "taggroup";

export type MetadataFormFieldOption = {
  label: string;
  value: string;
};

export type MetadataFormFieldSchema<TValueKey extends string = string> = {
  type: MetadataFormFieldType;
  label: string;
  placeholder?: string;
  required?: boolean;
  maxLengthFallback?: number;
  values?: MetadataFormFieldOption[];
  includeUnknownOption?: boolean;
  multiple?: boolean;
  valueKey?: TValueKey;
};

export type MetadataFormSectionSchema<TValueKey extends string = string> = {
  fields: Record<string, MetadataFormFieldSchema<TValueKey>>;
};
