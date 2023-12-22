import {
  Button,
  Checkbox,
  FormControl,
  FormControlLabel,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { DataItemDTO, ResponseError } from "kst-api";
import { FC, Fragment } from "react";
import { FormattedMessage } from "react-intl";
import { ActionFunction, Form, redirect, useLoaderData } from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import Loader from "../../../components/loader/loader";
import useSeverityFormatter from "../../../hooks/useSeverityFormatter";
import DiseaseReportSeverity from "../../../model/DiseaseReportSeverity";
import KeycloakService from "../../../services/KeycloakService";
import { TFieldsData } from "../fields/Fields";
import dayjs from "dayjs";

export const createDiseaseReportAction: ActionFunction = async ({
  request,
}): Promise<Response> => {
  const keycloakService = KeycloakService.getInstance();
  const ownerId = keycloakService.getUserId();
  if (!ownerId) {
    throw new Error("Unknown owner!");
  }
  try {
    const formData = await request.formData();
    const confidential = Boolean(formData.get("confidential"));
    const formDate = formData.get("date")?.toString();
    const parsedDate = formDate ? dayjs(formDate, ['DD.MM.YYYY', "MM/DD/YYYY"], true) : dayjs();
    const date = parsedDate.format("YYYY-MM-DD");
    const prosumerDataApi = await APIFactory.createProsumerDataApi();
    await prosumerDataApi.createDataItem1(
      {
        metaData: {
          dataItemType: "disease_report",
          ownerId,
        },
        data: {
          disease: formData.get("disease") as object,
          severity: formData.get("severity") as object,
          date: date as unknown as object,
          fieldId: formData.get("fieldId") as object,
          confidential: confidential as unknown as object,
        },
      },
      { signal: request.signal }
    );
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    throw e;
  }
  return redirect("/disease-report");
};

const CreateDiseaseReportView: FC = () => {
  const { fieldDataItems, loaded } = useLoaderData() as TFieldsData;
  const severityFormatter = useSeverityFormatter();

  if (!loaded) {
    return <Loader />;
  }

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="disease_report_create" />
      </PageHeader>
      <Form method="post" action="/disease-report/create">
        <Stack spacing={2}>
          <TextField
            name="disease"
            label={<FormattedMessage id="disease" />}
            required={true}
            variant="standard"
          />
          <FormControl required={true} variant="standard">
            <InputLabel id="severity-select-label">
              <FormattedMessage id="disease_severity" />
            </InputLabel>
            <Select
              name="severity"
              labelId="severity-select-label"
              label={<FormattedMessage id="disease_severity" />}
              defaultValue=""
            >
              {Object.entries(DiseaseReportSeverity).map(([key, value]) => {
                return (
                  <MenuItem key={key} value={value}>
                    {severityFormatter(value)}
                  </MenuItem>
                );
              })}
            </Select>
          </FormControl>
          <FormControl required={true} variant="standard">
            <InputLabel id="field-select-label">
              <FormattedMessage id="disease_field" />
            </InputLabel>
            <Select
              name="fieldId"
              labelId="field-select-label"
              label={<FormattedMessage id="disease_field" />}
              defaultValue=""
            >
              {fieldDataItems.map((dataItem: DataItemDTO) => {
                const { data } = dataItem;
                if (!data) {
                  return null;
                }
                const id = `${data.id}`;
                const name = `${data.name}`;
                return (
                  <MenuItem key={id} value={id}>
                    {name}
                  </MenuItem>
                );
              })}
            </Select>
          </FormControl>
          <DatePicker
            label={<FormattedMessage id="disease_date" />}
            disableFuture={true}
            slotProps={{
              textField: {
                required: true,
                name: "date",
                variant: "standard",
              },
            }}
          />
          <FormControl variant="standard">
            <FormControlLabel
              control={<Checkbox name="confidential" />}
              label={<FormattedMessage id="disease_confidential" />}
            />
          </FormControl>
          <Button size="large" variant="contained" type="submit">
            <FormattedMessage id="disease_report_submit" />
          </Button>
        </Stack>
      </Form>
    </Fragment>
  );
};

export default CreateDiseaseReportView;
