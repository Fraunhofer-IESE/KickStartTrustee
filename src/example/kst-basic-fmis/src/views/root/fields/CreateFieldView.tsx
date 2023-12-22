import { Box, Button, Grid, Stack, TextField } from "@mui/material";
import { ResponseError } from "kst-api";
import { FC, Fragment, useCallback, useEffect, useMemo, useState } from "react";
import { FormattedMessage, useIntl } from "react-intl";
import {
  ActionFunction,
  Form,
  isRouteErrorResponse,
  redirect,
  useLocation,
  useNavigate,
} from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import KeycloakService from "../../../services/KeycloakService";
import { v4 as uuidv4 } from "uuid";
import {
  Feature,
  Polygon as GeojsonPolygon,
  MultiPolygon as GeojsonMultiPolygon,
} from "geojson";
import ErrorDialog from "../../../components/dialogs/ErrorDialog";
import BasicFmisMapComponent from "../../../components/map/BasicFmisMapComponent";

const handleResponseError = (e: ResponseError) => {
  const response = e.response;
  switch (response.status) {
    case 400:
      throw new Response("invalid_field_data_error_message", { status: 400 });

    default:
      break;
  }
  throw response;
};

export const createFieldAction: ActionFunction = async ({
  request,
}): Promise<Response> => {
  const keycloakService = KeycloakService.getInstance();
  const ownerId = keycloakService.getUserId();
  if (!ownerId) {
    throw new Error("Unknown owner!");
  }
  try {
    const formData = await request.formData();
    const fieldData = {
      id: formData.get("id") as string,
      name: formData.get("name") as string,
      boundaries: JSON.parse(formData.get("boundaries") as string),
    };
    const prosumerDataApi = await APIFactory.createProsumerDataApi();

    await prosumerDataApi.createDataItem1(
      {
        metaData: {
          dataItemType: "field_data",
          ownerId,
        },
        data: fieldData as unknown as { [key: string]: object },
      },
      { signal: request.signal }
    );
  } catch (e) {
    if (e instanceof ResponseError) {
      handleResponseError(e);
    }
    throw e;
  }
  return redirect("/fields");
};

const CreateFieldView: FC = () => {
  const intl = useIntl();
  const location = useLocation();
  const navigate = useNavigate();

  const [boundariesStr, setBoundariesStr] = useState("");
  const updateBoundariesStr = useCallback(
    (
      drawnFieldBoundaries: Feature<
        GeojsonPolygon | GeojsonMultiPolygon,
        any
      > | null
    ) => {
      setBoundariesStr(
        drawnFieldBoundaries !== null
          ? JSON.stringify(drawnFieldBoundaries)
          : ""
      );
    },
    []
  );
  const suggestedFieldId = useMemo(() => {
    return uuidv4();
  }, []);

  const error = useMemo(() => location.state?.error, [location.state]);
  const [isErrorDialogOpen, setIsErrorDialogOpen] = useState<boolean>(
    () => error !== undefined && error !== null
  );

  const handleClose = useCallback(() => {
    const { pathname, state } = location;
    const newState = { ...state };
    delete newState.error;
    navigate(pathname, { replace: true, state: newState });
  }, [location, navigate]);

  useEffect(() => {
    setIsErrorDialogOpen(error !== undefined && error !== null);
  }, [error]);

  const localizedErrorMessages: Record<string, string> = useMemo(() => {
    return {
      invalid_field_data_error_message: intl.formatMessage({
        id: "invalid_field_data_error_message",
      }),
    };
  }, [intl]);

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="field_create" />
      </PageHeader>
      <Form method="post" action="/fields/create">
        <Grid container spacing={2}>
          <Grid item xs={5}>
            <Stack spacing={2}>
              <TextField
                name="id"
                label={<FormattedMessage id="field_id" />}
                required={true}
                variant="standard"
                defaultValue={suggestedFieldId}
              />
              <TextField
                name="name"
                label={<FormattedMessage id="field_name" />}
                required={true}
                variant="standard"
              />
              <TextField
                name="boundaries"
                label={<FormattedMessage id="field_boundaries" />}
                required={true}
                placeholder={intl.formatMessage({
                  id: "field_boundaries_via_map",
                })}
                variant="standard"
                value={boundariesStr}
              />
              <Box>
                <Button size="large" variant="contained" type="submit">
                  <FormattedMessage id="field_create" />
                </Button>
              </Box>
            </Stack>
          </Grid>
          <Grid item xs={7}>
            <Box mt={1}>
              <BasicFmisMapComponent
                enableDraw={true}
                enableGeocoder={true}
                onDrawnFieldBoundariesChange={updateBoundariesStr}
              />
            </Box>
          </Grid>
        </Grid>
      </Form>
      <ErrorDialog
        open={isErrorDialogOpen}
        onClose={handleClose}
        message={
          isRouteErrorResponse(error) ? (
            `${localizedErrorMessages[error.data] ?? error.data}`
          ) : (
            <FormattedMessage id="error_unknown" />
          )
        }
      />
    </Fragment>
  );
};

export default CreateFieldView;
