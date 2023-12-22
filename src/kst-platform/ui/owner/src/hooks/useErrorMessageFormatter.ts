import { useCallback } from "react";
import { IntlShape, useIntl } from "react-intl";
import { isRouteErrorResponse } from "react-router-dom";

export const formatErrorMessage = (intl: IntlShape, error?: any): string => {
  if (!error) {
    return "";
  } else {
    if (!isRouteErrorResponse(error)) {
      return intl.formatMessage({ id: "error_unknown" });
    } else {
      if (error?.data?.message?.id) {
        return intl.formatMessage(
          {
            id: error.data.message.id,
          },
          error.data.message.values
        );
      } else {
        return error.data;
      }
    }
  }
};

const useErrorMessageFormatter = () => {
  const intl = useIntl();

  const formatErrorMessageWithIntl = useCallback(
    (error?: any) => {
      return formatErrorMessage(intl, error);
    },
    [intl]
  );

  return formatErrorMessageWithIntl;
};

export default useErrorMessageFormatter;
