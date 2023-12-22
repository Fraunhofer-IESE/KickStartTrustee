import { useCallback } from "react";
import { IntlShape, useIntl } from "react-intl";

export type DateTime = string | number | Date;

export const dateTimeFormatter = (
  intl: IntlShape,
  value?: DateTime
): string | null => {
  if (!value) {
    return null;
  }
  return `${intl.formatDate(value)} ${intl.formatTime(value)}`;
};

const useDateTimeFormatter = () => {
  const intl = useIntl();

  const dateTimeFormatterWithIntl = useCallback(
    (value?: DateTime) => {
      return dateTimeFormatter(intl, value);
    },
    [intl]
  );

  return dateTimeFormatterWithIntl;
};

export default useDateTimeFormatter;
