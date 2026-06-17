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

import MetadataFieldText from "./MetadataFieldText";
import MetadataFieldTextarea from "./MetadataFieldTextarea";
import MetadataFieldSelect from "./MetadataFieldSelect";
import { MetadataFieldDtoType, type MetadataFieldDto } from "../../../types/generated";
import { createLogger } from "../../util/log";
import MetadataFieldTextWithDescription from "./MetadataFieldTextWithDescription";
import type { FieldValue } from "../../../types/fieldValue";
import MetadataFieldTextWithDescriptionList from "./MetadataFieldTextWithDescriptionList";
import MetadataFieldContainer from "./MetadataFieldContainer";
import MetadataFieldTagSelectWithDescriptionList from "./MetadataFieldTagSelectWithDescriptionList";
import MetadataFieldTagSelectWithTwoFieldsList from "./MetadataFieldTagSelectWithTwoFieldsList";
import MetadataFieldTagSelectWithTextAndDescriptionList from "./MetadataFieldTagSelectWithTextAndDescriptionList";

export interface MetadataFieldComponentProps {
  field: MetadataFieldDto;
  value?: FieldValue;
  allValues?: Record<string, FieldValue | undefined>;
  onChange?: (value: FieldValue | undefined) => void;
  embedded?: boolean;
  error?: boolean | string;
  missingRequiredFieldNames?: string[];
}

type MetadataFieldRendererProps = MetadataFieldComponentProps;

const MetadataFieldRenderer = ({
  field,
  value,
  allValues,
  onChange,
  embedded = false,
  error,
  missingRequiredFieldNames,
}: MetadataFieldRendererProps) => {
  const fieldType = field.type as string;
  const log = createLogger("MetadataFieldRenderer");
  log.debug(`Rendering field "${field.name}" of type "${field.type}" and value:`, value);

  switch (fieldType) {
    case MetadataFieldDtoType.LONGTEXT:
      return (
        <MetadataFieldContainer>
          <MetadataFieldTextarea
            field={field}
            value={value as string | undefined}
            allValues={allValues}
            onChange={onChange}
            error={error}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.SELECT:
    case MetadataFieldDtoType.TAGS:
      return (
        <MetadataFieldContainer>
          <MetadataFieldSelect
            field={field}
            value={value}
            allValues={allValues}
            onChange={onChange}
            embedded={embedded}
            error={error}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.TEXT_WITH_DESCRIPTION:
      return (
        <MetadataFieldContainer>
          <MetadataFieldTextWithDescription
            field={field}
            value={value as Record<string, string> | undefined}
            onChange={onChange}
            error={error}
            missingRequiredFieldNames={missingRequiredFieldNames}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.TEXT_WITH_DESCRIPTION_LIST:
      return (
        <MetadataFieldContainer>
          <MetadataFieldTextWithDescriptionList
            field={field}
            value={value as Record<string, string>[] | undefined}
            onChange={onChange}
            error={error}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.TAGS_WITH_DESCRIPTION_LIST:
      return (
        <MetadataFieldContainer>
          <MetadataFieldTagSelectWithDescriptionList
            field={field}
            value={value as Record<string, string>[] | undefined}
            allValues={allValues}
            onChange={onChange}
            error={error}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.TAGS_WITH_TWO_FIELDS:
      return (
        <MetadataFieldContainer>
          <MetadataFieldTagSelectWithTwoFieldsList
            field={field}
            value={value as Record<string, string>[] | undefined}
            allValues={allValues}
            onChange={onChange}
            error={error}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.TAGS_WITH_TEXT_AND_DESCRIPTION_LIST:
      return (
        <MetadataFieldContainer>
          <MetadataFieldTagSelectWithTextAndDescriptionList
            field={field}
            value={value as Record<string, FieldValue | undefined>[] | undefined}
            allValues={allValues}
            onChange={onChange}
            error={error}
          />
        </MetadataFieldContainer>
      );
    case MetadataFieldDtoType.TEXT:
    default:
      return (
        <MetadataFieldContainer>
          <MetadataFieldText
            field={field}
            value={value as string}
            allValues={allValues}
            onChange={onChange}
            embedded={embedded}
            error={error}
          />
        </MetadataFieldContainer>
      );
  }
};

export default MetadataFieldRenderer;
