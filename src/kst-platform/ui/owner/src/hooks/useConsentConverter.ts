import { OwnerConsentDTOStatusEnum } from "owner-api";
import { useCallback } from "react";
import { IntlShape, useIntl } from "react-intl";

export const convertStatusText = (
  intl: IntlShape,
  status?: OwnerConsentDTOStatusEnum
): string | undefined => {
  switch (status) {
    case OwnerConsentDTOStatusEnum.Active:
      return intl.formatMessage({ id: "consent_status_active" });
    case OwnerConsentDTOStatusEnum.Revoked:
      return intl.formatMessage({ id: "consent_status_revoked" });
    default:
      break;
  }
};

export const convertStatusColor = (status?: OwnerConsentDTOStatusEnum) => {
  switch (status) {
    case OwnerConsentDTOStatusEnum.Active:
      return "success";
    case OwnerConsentDTOStatusEnum.Revoked:
      return "error";
    default:
      return undefined;
  }
};

const useConsentConverter = () => {
  const intl = useIntl();

  const convertStatusTextWithIntl = useCallback(
    (status?: OwnerConsentDTOStatusEnum) => convertStatusText(intl, status),
    [intl]
  );

  return {
    convertStatusText: convertStatusTextWithIntl,
    convertStatusColor,
  };
};

export default useConsentConverter;
