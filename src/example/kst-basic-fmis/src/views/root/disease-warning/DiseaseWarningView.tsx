import { GridColDef } from "@mui/x-data-grid";
import { DataItemDTO, ResponseError } from "kst-api";
import { FC, Fragment, useMemo } from "react";
import { FormattedMessage, IntlShape, useIntl } from "react-intl";
import { LoaderFunction, useLoaderData } from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import FilterableList from "../../../components/list/FilterableList";
import Loader from "../../../components/loader/loader";
import { dateFormatter } from "../../../hooks/useDateFormatter";
import { severityFormatter } from "../../../hooks/useSeverityFormatter";
import KeycloakService from "../../../services/KeycloakService";
import { dateComparator } from "../../../hooks/useDateComparator";

export type TDiseaseWarningData = {
  loaded: boolean;
  fieldDataItems: Array<DataItemDTO>;
  diseaseWarningItems: Array<DataItemDTO>;
};

export const diseaseWarningLoader: LoaderFunction = async ({
  request,
}): Promise<TDiseaseWarningData> => {
  const keycloakService = KeycloakService.getInstance();
  const ownerId = keycloakService.getUserId();
  try {
    const prosumerDataApi = await APIFactory.createProsumerDataApi();
    const [fieldDataItems, diseaseWarningItems] = await Promise.all([
      prosumerDataApi.getDataItems("field_data", ownerId, {
        signal: request.signal,
      }),
      prosumerDataApi.getDataItems("disease_warning", ownerId, {
        signal: request.signal,
      }),
    ]);

    return {
      loaded: true,
      fieldDataItems,
      diseaseWarningItems,
    };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true, fieldDataItems: [], diseaseWarningItems: [] };
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
    field: "endangeredFields",
    headerName: intl.formatMessage({ id: "disease_endangered_fields" }),
    flex: 1,
    minWidth: 300,
    valueGetter: ({ row }: { row: DataItemDTO }) => {
      const endangeredFields = (row.data?.endangeredFields ??
        []) as Array<string>;
      return endangeredFields
        .map((endangeredFieldFieldId) => {
          const matchingFieldDataItem = fieldDataItems.find((fieldDataItem) => {
            const fieldDataItemFieldId = fieldDataItem.data
              ?.id as unknown as string;
            return fieldDataItemFieldId === endangeredFieldFieldId;
          });
          return matchingFieldDataItem?.data?.name;
        })
        .filter((fieldName) => fieldName)
        .sort()
        .join(", ");
    },
  },
];

const DiseaseWarningView: FC = () => {
  const { fieldDataItems, diseaseWarningItems, loaded } =
    useLoaderData() as TDiseaseWarningData;
  const intl = useIntl();
  const columns = useMemo(
    () => createColumns(intl, fieldDataItems),
    [intl, fieldDataItems]
  );
  const filters = useMemo(() => [], []);

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="disease_warnings" />
      </PageHeader>
      {loaded ? (
        <FilterableList
          filters={filters}
          columns={columns}
          rows={diseaseWarningItems}
          initialSorting={[{ field: "date", sort: "desc" }]}
        />
      ) : (
        <Loader />
      )}
    </Fragment>
  );
};

export default DiseaseWarningView;
