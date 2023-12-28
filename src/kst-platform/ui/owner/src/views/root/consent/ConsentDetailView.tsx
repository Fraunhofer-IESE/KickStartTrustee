import { Button, Stack, Typography } from "@mui/material";
import {
  DataItemTypeDTO,
  OwnerConsentDTO,
  OwnerConsentDTOPurposeEnum,
  OwnerConsentDTOStatusEnum,
  ResponseError,
  UpdateConsentDTOStatusEnum,
} from "owner-api";
import { Fragment, useCallback, useState } from "react";
import { FormattedMessage } from "react-intl";
import {
  ActionFunction,
  LoaderFunction,
  SubmitOptions,
  useLoaderData,
  useNavigate,
  useSearchParams,
  useFetcher,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import Loader from "../../../components/loader/loader";
import useConsentConverter from "../../../hooks/useConsentConverter";
import useDateTimeFormatter from "../../../hooks/useDateTimeFormatter";
import useDataItemTypeFormatter from "../../../hooks/useDataItemTypeFormatter";
import DataField from "../../../components/data-field/DataField";
import CircleIcon from "@mui/icons-material/Circle";
import ConsentRevokeConfirmationDialog from "./ConsentRevokeConfirmationDialog";
import DetailsContainer from "../../../components/details/DetailsContainer";
import DetailsContentContainer from "../../../components/details/DetailsContentContainer";

const REDIRECT_URI = "redirect_uri";

const convertPurpose = (purpose: OwnerConsentDTOPurposeEnum) => {
  switch (purpose) {
    case OwnerConsentDTOPurposeEnum.Research:
      return "Research";
    case OwnerConsentDTOPurposeEnum.Service:
      return "Service";
    case OwnerConsentDTOPurposeEnum.Other:
      return "Other";
    default:
      break;
  }
  return "Unknown";
};

export type TConsentDetailData = {
  loaded: boolean;
  consent?: OwnerConsentDTO;
  dataItemTypes?: Array<DataItemTypeDTO>;
};

const handleResponseError = (id: string, e: ResponseError) => {
  const response = e.response;
  switch (response.status) {
    case 404:
      throw new Response(`Consent with id ${id} not found!`, {
        status: 404,
      });
    case 409:
      throw new Response(
        `Consent with id ${id} has been modified. Please check its current value and try again!`,
        { status: 409 }
      );
    default:
      break;
  }
  throw response;
};

export const consentDetailLoader: LoaderFunction = async (
  args
): Promise<TConsentDetailData> => {
  const { params, request } = args;
  const { consentId } = params;
  if (!consentId) {
    throw new Error("No id given!");
  }
  try {
    const catalogApi = await APIFactory.createCatalogApi();
    const dataItemTypes = await catalogApi.getDataItemTypes({
      signal: request.signal,
    });
    const consentApi = await APIFactory.createConsentApi();
    const consent = await consentApi.getOwnedConsentById(consentId, {
      signal: request.signal,
    });
    return { loaded: true, consent, dataItemTypes };
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(consentId, e);
    }
    return { loaded: true };
  }
};

export const consentRevokeAction: ActionFunction = async (args) => {
  const { params, request } = args;
  const id = params.consentId;
  if (!id) {
    throw new Error("No id given!");
  }
  try {
    const consentApi = await APIFactory.createConsentApi();
    await consentApi.updateOwnedConsent(
      id,
      {
        id,
        status: UpdateConsentDTOStatusEnum.Revoked,
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

const ConsentDetailView = () => {
  const [queryParams] = useSearchParams();
  const fetcher = useFetcher();
  const {
    consent,
    dataItemTypes = [],
    loaded,
  } = useLoaderData() as TConsentDetailData;
  const navigate = useNavigate();
  const { convertStatusText, convertStatusColor } = useConsentConverter();
  const dataItemTypeFormatter = useDataItemTypeFormatter(dataItemTypes);
  const dateTimeFormatter = useDateTimeFormatter();
  const [isRevokeConfirmationDialogOpen, setIsRevokeConfirmationDialogOpen] =
    useState<boolean>(false);

  const handleRevokeClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      setIsRevokeConfirmationDialogOpen(true);
    },
    []
  );

  const handleRevokeConfirmationDialogClose = useCallback(() => {
    setIsRevokeConfirmationDialogOpen(false);
  }, []);

  const handleRevokeConfirmationDialogConfirmed = useCallback(() => {
    const id = consent?.id;
    if (!id) {
      return;
    }
    const submitOptions: SubmitOptions = {
      method: "post",
      action: `/consent/${id}/revoke`,
    };
    const redirect_uri = queryParams.get(REDIRECT_URI);
    if (redirect_uri) {
      const formData = new FormData();
      formData.set(REDIRECT_URI, redirect_uri);
      fetcher.submit(formData, submitOptions);
    } else {
      fetcher.submit(null, submitOptions);
    }
    setIsRevokeConfirmationDialogOpen(false);
  }, [consent?.id, queryParams, fetcher]);

  const handleViewDetailsButtonClicked = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();
      const id = consent?.consentRequestId;
      if (!id) {
        return;
      }
      navigate(`/consent-request/${id}`);
    },
    [navigate, consent?.consentRequestId]
  );

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="consent_details" />
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
                  color={convertStatusColor(consent?.status)}
                />
                <Typography>{convertStatusText(consent?.status)}</Typography>
              </Stack>
              <DetailsContentContainer variant="outlined">
                <Stack spacing={2}>
                  <DataField
                    name="consent-id"
                    label={<FormattedMessage id="consent_id" />}
                    value={consent?.id}
                  />
                  <DataField
                    name="consent-created-at"
                    label={<FormattedMessage id="consent_created_at" />}
                    value={dateTimeFormatter(consent?.createdAt)}
                  />
                </Stack>
              </DetailsContentContainer>
              <DetailsContentContainer>
                <Stack spacing={2}>
                  <DataField
                    name="consent-request-id"
                    label={<FormattedMessage id="consent_request_id" />}
                    value={consent?.consentRequestId}
                  />
                  <Stack direction="row" spacing={1} justifyContent="flex-end">
                    <Button
                      variant="contained"
                      onClick={handleViewDetailsButtonClicked}
                    >
                      <FormattedMessage id="view_details" />
                    </Button>
                  </Stack>
                </Stack>
              </DetailsContentContainer>
              <DetailsContentContainer>
                <Stack spacing={6}>
                  <Stack spacing={2}>
                    <DataField
                      name="consent-consumer"
                      label={<FormattedMessage id="consent_consumer" />}
                      value={consent?.actor?.name}
                    />
                    <DataField
                      name="consent-consumed-data-items"
                      label={
                        <FormattedMessage id="consent_consumed_data_items" />
                      }
                      multiline={true}
                      value={dataItemTypeFormatter(
                        consent?.consumedDataItemTypes
                      )}
                    />
                    <DataField
                      name="consent-provided-data-items"
                      label={
                        <FormattedMessage id="consent_provided_data_items" />
                      }
                      multiline={true}
                      value={dataItemTypeFormatter(
                        consent?.providedDataItemTypes
                      )}
                    />
                    {consent?.dataUsageStatement && (
                      <DataField
                        name="consent-data-usage-statement"
                        label={
                          <FormattedMessage id="consent_data_usage_statement" />
                        }
                        multiline={true}
                        value={consent.dataUsageStatement}
                      />
                    )}
                    {consent?.purpose && (
                      <DataField
                        name="consent-purpose"
                        label={<FormattedMessage id="consent_purpose" />}
                        value={convertPurpose(consent.purpose)}
                      />
                    )}
                  </Stack>
                  {consent?.status === OwnerConsentDTOStatusEnum.Active && (
                    <Stack
                      direction="row"
                      spacing={1}
                      justifyContent="flex-end"
                    >
                      <Button
                        variant="outlined"
                        color="error"
                        onClick={handleRevokeClicked}
                      >
                        <FormattedMessage id="consent_revoke" />
                      </Button>
                    </Stack>
                  )}
                </Stack>
              </DetailsContentContainer>
            </Stack>
            <ConsentRevokeConfirmationDialog
              open={isRevokeConfirmationDialogOpen}
              onClose={handleRevokeConfirmationDialogClose}
              onConfirmed={handleRevokeConfirmationDialogConfirmed}
            />
          </Fragment>
        ) : (
          <Loader />
        )}
      </DetailsContainer>
    </Fragment>
  );
};

export default ConsentDetailView;
