import { GridColDef } from "@mui/x-data-grid";
import { Fragment, useCallback, useEffect, useMemo, useState } from "react";
import { FormattedMessage, IntlShape, useIntl } from "react-intl";
import { Link, LoaderFunction, useLoaderData } from "react-router-dom";
import APIFactory from "../../../api/APIFactory";
import PageHeader from "../../../components/header/PageHeader";
import FilterableList from "../../../components/list/FilterableList";
import Loader from "../../../components/loader/loader";
import KeycloakService from "../../../services/KeycloakService";
import { DataItemDTO, ResponseError } from "kst-api";
import { Box, Button, Grid, Stack, Typography } from "@mui/material";

import BasicFmisMapComponent from "../../../components/map/BasicFmisMapComponent";
import { LayerGroup, Popup, Tooltip, GeoJSON } from "react-leaflet";
import { GeoJsonObject } from "geojson";

export type TFieldsData = {
  loaded: boolean;
  fieldDataItems: Array<DataItemDTO>;
};

export const fieldsLoader: LoaderFunction = async ({
  request,
}): Promise<TFieldsData> => {
  const keycloakService = KeycloakService.getInstance();
  const ownerId = keycloakService.getUserId();
  try {
    const prosumerDataApi = await APIFactory.createProsumerDataApi();
    const fieldDataItems = await prosumerDataApi.getDataItems(
      "field_data",
      ownerId,
      { signal: request.signal }
    );
    return {
      loaded: true,
      fieldDataItems,
    };
  } catch (e) {
    if (e instanceof ResponseError) {
      throw e.response;
    }
    return { loaded: true, fieldDataItems: [] };
  }
};

const createColumns = (intl: IntlShape): GridColDef[] => [
  {
    field: "name",
    headerName: intl.formatMessage({ id: "field_name" }),
    type: "string",
    minWidth: 200,
    flex: 1,
    valueGetter: ({ row }) => row.data.name,
  },
];

const Fields = () => {
  const { fieldDataItems, loaded } = useLoaderData() as TFieldsData;
  const intl = useIntl();
  const columns = useMemo(() => createColumns(intl), [intl]);
  const filters = useMemo(() => [], []);

  const fieldDataItemsWithBoundaries = useMemo(
    () =>
      fieldDataItems?.filter((fieldDataItem) => fieldDataItem.data?.boundaries),
    [fieldDataItems]
  );

  const [fieldDataItemsInFocus, setFieldDataItemsInFocus] = useState<
    Array<DataItemDTO>
  >(fieldDataItemsWithBoundaries);

  useEffect(() => {
    setFieldDataItemsInFocus(fieldDataItemsWithBoundaries);
  }, [fieldDataItemsWithBoundaries]);

  const handleOnRowClick = useCallback(
    (row: DataItemDTO) => {
      setFieldDataItemsInFocus([
        ...fieldDataItemsWithBoundaries.filter(
          (fieldDataItem) => fieldDataItem.id === row.id
        ),
      ]);
    },
    [fieldDataItemsWithBoundaries]
  );

  const COLORS = [
    "#22bf73",
    "#e0ca3c",
    "#F78E69",
    "#88CCF1",
    "#875C74",
    "#C86FC9",
  ];

  return (
    <Fragment>
      <PageHeader>
        <FormattedMessage id="fields" />
      </PageHeader>
      <Box display="flex" justifyContent="flex-end">
        <Link to="/fields/create">
          <Button variant="contained">
            <FormattedMessage id="field_create" />
          </Button>
        </Link>
      </Box>
      {loaded ? (
        <Grid container spacing={2}>
          <Grid item xs={5}>
            <FilterableList
              filters={filters}
              columns={columns}
              rows={fieldDataItems}
              onRowClick={handleOnRowClick}
              initialSorting={[{ field: "name", sort: "asc" }]}
            />
          </Grid>
          <Grid item xs={7}>
            <Box mt={1}>
              <BasicFmisMapComponent
                fieldDataItemsInFocus={fieldDataItemsInFocus}
              >
                {fieldDataItemsWithBoundaries && (
                  <LayerGroup>
                    {fieldDataItemsWithBoundaries.map(
                      (fieldDataItem, index) => (
                        <GeoJSON
                          key={fieldDataItem.id}
                          data={fieldDataItem.data?.boundaries as GeoJsonObject}
                          pathOptions={{
                            color: COLORS[index % COLORS.length],
                          }}
                        >
                          <Tooltip>
                            <Stack spacing={1}>
                              <Typography>
                                {`${intl.formatMessage({
                                  id: "field_name",
                                })}: ${fieldDataItem.data?.name}`}
                              </Typography>
                              <Typography>
                                {`${intl.formatMessage({ id: "field_id" })}: ${
                                  fieldDataItem.data?.id
                                }`}
                              </Typography>
                              <Typography>{`${intl.formatMessage({
                                id: "data_item_id",
                              })}: ${fieldDataItem.id}`}</Typography>
                            </Stack>
                          </Tooltip>
                          <Popup>
                            <Stack spacing={1}>
                              <Typography>
                                {`${intl.formatMessage({
                                  id: "field_name",
                                })}: ${fieldDataItem.data?.name}`}
                              </Typography>
                              <Typography>
                                {`${intl.formatMessage({ id: "field_id" })}: ${
                                  fieldDataItem.data?.id
                                }`}
                              </Typography>
                              <Typography>{`${intl.formatMessage({
                                id: "data_item_id",
                              })}: ${fieldDataItem.id}`}</Typography>
                            </Stack>
                          </Popup>
                        </GeoJSON>
                      )
                    )}
                  </LayerGroup>
                )}
              </BasicFmisMapComponent>
            </Box>
          </Grid>
        </Grid>
      ) : (
        <Loader />
      )}
    </Fragment>
  );
};

export default Fields;
