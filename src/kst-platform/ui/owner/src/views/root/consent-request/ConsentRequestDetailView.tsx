import { Button, Stack, Typography } from "@mui/material";
import {
  DataItemTypeDTO,
  OwnerConsentRequestDTO,
  OwnerConsentRequestDTOPurposeEnum,
  OwnerConsentRequestDTOStatusEnum,
  ResponseError,
  UpdateConsentRequestDTOStatusEnum,
} from "owner-api";
import { Fragment, useCallback, useState } from "react";
import { FormattedMessage } from "react-intl";
import {
  ActionFunction,
  LoaderFunction,
  SubmitOptions,
  useLoaderData,
  useSearchParams,
  useFetcher,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import Loader from "../../../components/loader/loader";
import useConsentRequestConverter from "../../../hooks/useConsentRequestConverter";
import useDateTimeFormatter from "../../../hooks/useDateTimeFormatter";
import useDataItemTypeFormatter from "../../../hooks/useDataItemTypeFormatter";
import DataField from "../../../components/data-field/DataField";
import CircleIcon from "@mui/icons-material/Circle";
import ConsentRequestAcceptConfirmationDialog from "./ConsentRequestAcceptConfirmationDialog";
import ConsentRequestRejectConfirmationDialog from "./ConsentRequestRejectConfirmationDialog";
import ConsentRequestAcceptedNotificationDialog from "./ConsentRequestAcceptedNotificationDialog";
import DetailsContainer from "../../../components/details/DetailsContainer";
import DetailsContentContainer from "../../../components/details/DetailsContentContainer";

const REDIRECT_URI = "redirect_uri";

const convertPurpose = (purpose: OwnerConsentRequestDTOPurposeEnum) => {
  switch (purpose) {
    case OwnerConsentRequestDTOPurposeEnum.Research:
      return "Research";
    case OwnerConsentRequestDTOPurposeEnum.Service:
      return "Service";
    case OwnerConsentRequestDTOPurposeEnum.Other:
      return "Other";
    default:
      break;
  }
  return "Unknown";
};

export type TConsentDetailData = {
  loaded: boolean;
  consentRequest?: OwnerConsentRequestDTO;
  dataItemTypes?: Array<DataItemTypeDTO>;
};

const handleResponseError = (id: string, e: ResponseError) => {
  const response = e.response;
  switch (response.status) {
    case 404:
      throw new Response(`Consent Request with id ${id} not found!`, {
        status: 404,
      });
    case 409:
      throw new Response(
        `Consent Request with id ${id} has been modified. Please check its current value and try again!`,
        { status: 409 }
      );
    default:
      break;
  }
  throw response;
};

export const consentRequestDetailLoader: LoaderFunction = async (
  args
): Promise<TConsentDetailData> => {
  const { params, request } = args;
  const { consentRequestId } = params;
  if (!consentRequestId) {
    // This is an internal bug, therefore don't handle that graceful because it should never happen
    throw new Error("No id given!");
  }
  try {
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const consentApi = await APIFactory.createConsentApi();
    const consentRequest = await consentApi.getOwnedConsentRequestById(
      consentRequestId,
      { signal: request.signal }
    );
    return { loaded: true, consentRequest, dataItemTypes };
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(consentRequestId, e);
    }
    return { loaded: true };
  }
};

export const consentRequestAcceptAction: ActionFunction = async (args) => {
  const { params, request } = args;
  const id = params.consentRequestId;
  if (!id) {
    // This is an internal bug, therefore don't handle that graceful because it should never happen
    throw new Error("No id given!");
  }
  try {
    const consentApi = await APIFactory.createConsentApi();
    await consentApi.updateOwnedConsentRequest(
      id,
      {
        id,
        status: UpdateConsentRequestDTOStatusEnum.Accepted,
      },
      { signal: request.signal }
    );
    const formData = await request.formData();
    const redirect_uri = formData.get(REDIRECT_URI);
    if (redirect_uri) {
      window.location.assign(redirect_uri.toString());
      return;
    }
    return null;
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(id, e);
    }
  }
};

export const consentRequestRejectAction: ActionFunction = async (args) => {
  const { params, request } = args;
  const id = params.consentRequestId;
  if (!id) {
    // This is an internal bug, therefore don't handle that graceful because it should never happen
    throw new Error("No id given!");
  }
  try {
    const consentApi = await APIFactory.createConsentApi();
    await consentApi.updateOwnedConsentRequest(
      id,
      {
        id,
        status: UpdateConsentRequestDTOStatusEnum.Rejected,
      },
      { signal: request.signal }
    );
    const formData = await request.formData();
    const redirect_uri = formData.get(REDIRECT_URI);
    if (redirect_uri) {
      window.location.assign(redirect_uri.toString());
      return;
    }
    return null;
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(id, e);
    }
  }
};

const ConsentRequestDetailView = () => {
  const [queryParams] = useSearchParams();
  const fetcher = useFetcher();
  const {
    consentRequest,
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TConsentDetailData;
  const { convertStatusText, convertStatusColor } =
    useConsentRequestConverter();
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);
  const dateTimeFormatter = useDateTimeFormatter();
  const [isAcceptConfirmationDialogOpen, setIsAcceptConfirmationDialogOpen] =
    useState<boolean>(false);
  const [isRejectConfirmationDialogOpen, setIsRejectConfirmationDialogOpen] =
    useState<boolean>(false);
  const [
    isAcceptedNotificationDialogOpen,
    setIsAcceptedNotificationDialogOpen,
  ] = useState<boolean>(false);

  const handleRejectClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      setIsRejectConfirmationDialogOpen(true);
    },
    []
  );

  const handleRejectConfirmationDialogClose = useCallback(() => {
    setIsRejectConfirmationDialogOpen(false);
  }, []);

  const handleRejectConfirmationDialogConfirmed = useCallback(() => {
    const id = consentRequest?.id;
    if (!id) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/consent-request/${id}/reject`,
    };
    const redirect_uri = queryParams.get(REDIRECT_URI);
    if (redirect_uri) {
      const formData = new FormData();
      formData.set(REDIRECT_URI, redirect_uri);
      fetcher.submit(formData, submitOptions);
    } else {
      fetcher.submit(null, submitOptions);
    }
    setIsRejectConfirmationDialogOpen(false);
  }, [consentRequest?.id, queryParams, fetcher]);

  const handleAcceptClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      setIsAcceptConfirmationDialogOpen(true);
    },
    []
  );

  const handleAcceptConfirmationDialogClose = useCallback(() => {
    setIsAcceptConfirmationDialogOpen(false);
  }, []);

  const handleAcceptConfirmationDialogConfirmed = useCallback(() => {
    const id = consentRequest?.id;
    if (!id) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/consent-request/${id}/accept`,
    };
    const redirect_uri = queryParams.get(REDIRECT_URI);
    if (redirect_uri) {
      const formData = new FormData();
      formData.set(REDIRECT_URI, redirect_uri);
      fetcher.submit(formData, submitOptions);
    } else {
      fetcher.submit(null, submitOptions);
    }
    setIsAcceptConfirmationDialogOpen(false);
    setIsAcceptedNotificationDialogOpen(true);
  }, [consentRequest?.id, queryParams, fetcher]);

  const handleAcceptedNotificationDialogClose = useCallback(() => {
    setIsAcceptedNotificationDialogOpen(false);
  }, []);

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="consent_request_details" />
      </PageHeader>
      <DetailsContainer>
        {loaded ? (
          <Fragment>
            <Stack spacing={2}>
              <Stack direction="row" spacing={0.5} alignItems="center">
                <Typography>
                  <FormattedMessage id="consent_status" />:
                </Typography>
                <CircleIcon
                  fontSize="small"
                  color={convertStatusColor(consentRequest?.status)}
                />
                <Typography>
                  {convertStatusText(consentRequest?.status)}
                </Typography>
              </Stack>
              <DetailsContentContainer variant="outlined">
                <Stack spacing={2}>
                  <DataField
                    name="consent-request-id"
                    label={<FormattedMessage id="consent_request_id" />}
                    value={consentRequest?.id}
                  />
                  <DataField
                    name="consent-request-created-at"
                    label={<FormattedMessage id="consent_request_created_at" />}
                    value={dateTimeFormatter(consentRequest?.createdAt)}
                  />
                </Stack>
              </DetailsContentContainer>
              <DetailsContentContainer variant="outlined">
                <Stack spacing={6}>
                  <Stack spacing={2}>
                    <DataField
                      name="consent-requester"
                      label={<FormattedMessage id="consent_requester" />}
                      value={consentRequest?.requester?.name}
                    />
                    <DataField
                      name="consent-request-consumed-data-items"
                      label={
                        <FormattedMessage id="consent_consumed_data_items" />
                      }
                      multiline={true}
                      value={dataItemTypeFormatter(
                        consentRequest?.consumedDataItemTypes
                      )}
                    />
                    <DataField
                      name="consent-request-provided-data-items"
                      label={
                        <FormattedMessage id="consent_provided_data_items" />
                      }
                      multiline={true}
                      value={dataItemTypeFormatter(
                        consentRequest?.providedDataItemTypes
                      )}
                    />
                    {consentRequest?.dataUsageStatement && (
                      <DataField
                        name="consent-request-data-usage-statement"
                        label={
                          <FormattedMessage id="consent_request_data_usage_statement" />
                        }
                        multiline={true}
                        value={consentRequest.dataUsageStatement}
                      />
                    )}
                    {consentRequest?.purpose && (
                      <DataField
                        name="consent-request-purpose"
                        label={
                          <FormattedMessage id="consent_request_purpose" />
                        }
                        value={convertPurpose(consentRequest.purpose)}
                      />
                    )}
                  </Stack>

                  {consentRequest?.status ===
                    OwnerConsentRequestDTOStatusEnum.Pending && (
                    <Stack
                      direction="row"
                      spacing={1}
                      justifyContent="flex-end"
                    >
                      <Button
                        variant="outlined"
                        color="success"
                        onClick={handleAcceptClicked}
                      >
                        <FormattedMessage id="consent_accept" />
                      </Button>
                      <Button
                        variant="outlined"
                        color="error"
                        onClick={handleRejectClicked}
                      >
                        <FormattedMessage id="consent_reject" />
                      </Button>
                    </Stack>
                  )}
                </Stack>
              </DetailsContentContainer>
            </Stack>
            <ConsentRequestAcceptConfirmationDialog
              open={isAcceptConfirmationDialogOpen}
              onClose={handleAcceptConfirmationDialogClose}
              onConfirmed={handleAcceptConfirmationDialogConfirmed}
            />
            <ConsentRequestRejectConfirmationDialog
              open={isRejectConfirmationDialogOpen}
              onClose={handleRejectConfirmationDialogClose}
              onConfirmed={handleRejectConfirmationDialogConfirmed}
            />
            <ConsentRequestAcceptedNotificationDialog
              open={isAcceptedNotificationDialogOpen}
              onClose={handleAcceptedNotificationDialogClose}
            />
          </Fragment>
        ) : (
          <Loader />
        )}
      </DetailsContainer>
    </Fragment>
  );
};

export default ConsentRequestDetailView;
