import { useCallback } from "react";
import { IntlShape, useIntl } from "react-intl";

export type DateT = string | number | Date;

export const dateFormatter = (intl: IntlShape, value?: Date | string): string | null => {
  if (!value) {
    return null;
  }
  return `${intl.formatDate(value)}`;
};

const useDateFormatter = () => {
  const intl = useIntl();

  const dateFormatterWithIntl = useCallback(
    (value?: Date) => {
      return dateFormatter(intl, value);
    },
    [intl]
  );

  return dateFormatterWithIntl;
};

export default useDateFormatter;
