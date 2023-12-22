import { Box, Button } from "@mui/material";
import { GridColDef } from "@mui/x-data-grid";
import { DataItemDTO, ResponseError } from "kst-api";
import { FC, Fragment, useMemo } from "react";
import { FormattedMessage, IntlShape, useIntl } from "react-intl";
import { Link, LoaderFunction, useLoaderData } from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import FilterableList from "../../../components/list/FilterableList";
import Loader from "../../../components/loader/loader";
import { dateComparator } from "../../../hooks/useDateComparator";
import { dateFormatter } from "../../../hooks/useDateFormatter";
import { severityFormatter } from "../../../hooks/useSeverityFormatter";
import KeycloakService from "../../../services/KeycloakService";

export type TDiseaseReportData = {
  loaded: boolean;
  fieldDataItems: Array<DataItemDTO>;
  diseaseReportItems: Array<DataItemDTO>;
};

export const diseaseReportLoader: LoaderFunction = async ({
  request,
}): Promise<TDiseaseReportData> => {
  const keycloakService = KeycloakService.getInstance();
  const ownerId = keycloakService.getUserId();
  try {
    const prosumerDataApi = await APIFactory.createProsumerDataApi();
    const [fieldDataItems, diseaseReportItems] = await Promise.all([
      prosumerDataApi.getDataItems("field_data", ownerId, {
        signal: request.signal,
      }),
      prosumerDataApi.getDataItems("disease_report", ownerId, {
        signal: request.signal,
      }),
    ]);

    return {
      loaded: true,
      fieldDataItems,
      diseaseReportItems,
    };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true, fieldDataItems: [], diseaseReportItems: [] };
  }
};

const createColumns = (
  intl: IntlShape,
  fieldDataItems: Array<DataItemDTO>
): GridColDef[] => [
  {
    field: "date",
    headerName: intl.formatMessage({ id: "date" }),
    valueGetter: ({ row }: { row: DataItemDTO }) => row.data?.date,
    valueFormatter: ({ value }) => dateFormatter(intl, value),
    sortComparator: dateComparator,
  },
  {
    field: "disease",
    headerName: intl.formatMessage({ id: "disease" }),
    width: 300,
    valueGetter: ({ row }: { row: DataItemDTO }) => row.data?.disease,
  },
  {
    field: "severity",
    headerName: intl.formatMessage({ id: "disease_severity" }),
    width: 150,
    valueFormatter: ({ value }) => severityFormatter(intl, value),
    valueGetter: ({ row }: { row: DataItemDTO }) => row.data?.severity,
  },
  {
    field: "field",
    headerName: intl.formatMessage({ id: "disease_field" }),
    width: 300,
    valueGetter: ({ row }: { row: DataItemDTO }) =>
      fieldDataItems.find((item) => item.data?.id === row.data?.fieldId)?.data
        ?.name,
  },
  {
    field: "confidential",
    headerName: intl.formatMessage({ id: "disease_confidential" }),
    width: 200,
    type: "boolean",
    valueGetter: ({ row }: { row: DataItemDTO }) => row.data?.confidential,
  },
];

const DiseaseReportView: FC = () => {
  const { fieldDataItems, diseaseReportItems, loaded } =
    useLoaderData() as TDiseaseReportData;
  const intl = useIntl();
  const columns = useMemo(
    () => createColumns(intl, fieldDataItems),
    [intl, fieldDataItems]
  );
  const filters = useMemo(() => [], []);

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="disease_reports" />
      </PageHeader>
      <Box display="flex" justifyContent="flex-end">
        <Link to="/disease-report/create">
          <Button variant="contained">
            <FormattedMessage id="disease_report_create" />
          </Button>
        </Link>
      </Box>
      {loaded ? (
        <FilterableList
          filters={filters}
          columns={columns}
          rows={diseaseReportItems}
          initialSorting={[{ field: "date", sort: "desc" }]}
        />
      ) : (
        <Loader />
      )}
    </Fragment>
  );
};

export default DiseaseReportView;
