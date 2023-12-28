import { useCallback } from "react";

const toHumanEventName = (eventName: string): string => {
  switch (eventName) {
    case "DataItemCreated":
      return "DataProvided";
    case "DataItemConsumed":
      return "DataConsumed";
    case "DataItemDeleted":
      return "DataDeleted";
    default:
      return eventName;
  }
};

export const eventTypeFormatter = (value?: string): string | undefined => {
  return value ? toHumanEventName(value).replace(/([A-Z])/g, " $1").slice(1) : undefined;
};

const useEventTypeFormatter = () => {
  const formatter = useCallback((value?: string) => {
    return eventTypeFormatter(value);
  }, []);

  return formatter;
};

export default useEventTypeFormatter;
