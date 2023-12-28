import { OwnerConsentRequestDTOStatusEnum } from "owner-api";
import { useCallback } from "react";
import { IntlShape, useIntl } from "react-intl";

export const convertStatusText = (
  intl: IntlShape,
  value?: OwnerConsentRequestDTOStatusEnum
): string | undefined => {
  switch (value) {
    case OwnerConsentRequestDTOStatusEnum.Accepted:
      return intl.formatMessage({ id: "consent_request_status_accepted" });
    case OwnerConsentRequestDTOStatusEnum.Rejected:
      return intl.formatMessage({ id: "consent_request_status_rejected" });
    case OwnerConsentRequestDTOStatusEnum.Retracted:
      return intl.formatMessage({ id: "consent_request_status_retracted" });
    case OwnerConsentRequestDTOStatusEnum.Pending:
      return intl.formatMessage({ id: "consent_request_status_pending" });
    default:
      break;
  }
};

export const convertStatusColor = (
  status?: OwnerConsentRequestDTOStatusEnum
) => {
  switch (status) {
    case OwnerConsentRequestDTOStatusEnum.Accepted:
      return "success";
    case OwnerConsentRequestDTOStatusEnum.Rejected:
      return "error";
    case OwnerConsentRequestDTOStatusEnum.Retracted:
      return "disabled";
    case OwnerConsentRequestDTOStatusEnum.Pending:
      return "warning";
    default:
      return undefined;
  }
};

const useConsentRequestConverter = () => {
  const intl = useIntl();

  const convertStatusTextWithIntl = useCallback(
    (value?: OwnerConsentRequestDTOStatusEnum) =>
      convertStatusText(intl, value),
    [intl]
  );

  return {
    convertStatusText: convertStatusTextWithIntl,
    convertStatusColor,
  };
};

export default useConsentRequestConverter;
