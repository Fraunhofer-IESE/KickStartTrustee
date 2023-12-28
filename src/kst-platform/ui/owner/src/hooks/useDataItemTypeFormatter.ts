import { DataItemTypeDTO } from "owner-api";
import { useCallback, useMemo } from "react";

type TDataItemTypes = Set<string> | Array<string>;

const DEFAULT_SEPERATOR = ", ";

export const dataItemTypeFormatter = (
  dataItemTypesMap: Record<string, string>,
  seperator = DEFAULT_SEPERATOR,
  value?: TDataItemTypes
): string | null => {
  return Array.from(value ?? [])
    .map((v) => dataItemTypesMap[v] ?? v)
    .join(seperator);
};

export type TDataItemTypeFormatter = (value?: TDataItemTypes) => string | null;

const useDataItemTypeFormatter = (
  dataItemTypes: Array<DataItemTypeDTO>,
  seperator = DEFAULT_SEPERATOR
): TDataItemTypeFormatter => {

  const dataItemTypesMap: Record<string, string> = useMemo(() => {
    return Object.fromEntries(dataItemTypes.map((v) => [v.id, v.name]));
  }, [dataItemTypes]);

  const formatter = useCallback(
    (value?: TDataItemTypes) => {
      return dataItemTypeFormatter(dataItemTypesMap, seperator, value);
    },
    [dataItemTypesMap, seperator]
  );

  return formatter;
};

export default useDataItemTypeFormatter;
