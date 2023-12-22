import {
  Stack,
  Container,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
} from "@mui/material";
import {
  DataGrid,
  GridCallbackDetails,
  GridColDef,
  GridRowParams,
  GridRowsProp,
  MuiEvent,
  enUS,
  deDE,
  GridSortModel,
} from "@mui/x-data-grid";
import { useCallback, useMemo } from "react";
import { useNavigate, useSearchParams, useSubmit } from "react-router-dom";
import LOCALES from "../../i18n/locales";
import { useIntl } from "react-intl";

export type FilterValues = { [s: string]: string };

export type Filters = {
  name: string;
  label?: string;
  disableValueFormat?: boolean;
  values: FilterValues;
};

type FilterableListProps = {
  disableRowClick?: boolean;
  filters?: Array<Filters>;
  initialSorting?: GridSortModel;
  columns: Array<GridColDef>;
  rows: GridRowsProp;
};

const FilterableList = (props: FilterableListProps) => {
  const {
    disableRowClick = false,
    filters = [],
    initialSorting,
    columns,
    rows,
  } = props;
  const { locale } = useIntl();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const submit = useSubmit();

  const initialState = useMemo(() => {
    return initialSorting
      ? {
          sorting: {
            sortModel: initialSorting,
          },
        }
      : undefined;
  }, [initialSorting]);

  const localeText = useMemo(() => {
    switch (locale) {
      case LOCALES.GERMAN:
        return deDE.components.MuiDataGrid.defaultProps.localeText;
      default:
        return enUS.components.MuiDataGrid.defaultProps.localeText;
    }
  }, [locale]);

  const handleOnChange = useCallback(
    (event: SelectChangeEvent<string>) => {
      const formData = new FormData();
      const { name, value } = event.target;
      if (value) {
        formData.set(name, value);
      }
      filters.forEach((f) => {
        const filterName = f.name;
        if (filterName === name) {
          return;
        }
        const filterValue = searchParams.get(filterName);
        if (filterValue) {
          formData.set(filterName, filterValue);
        }
      });
      submit(formData);
    },
    [filters, searchParams, submit]
  );

  const handleOnRowClick = useCallback(
    (
      params: GridRowParams,
      event: MuiEvent<React.MouseEvent>,
      _details: GridCallbackDetails
    ) => {
      event.preventDefault();
      event.stopPropagation();
      const id = params.row.id;
      navigate(`${id}`);
    },
    [navigate]
  );

  return (
    <Stack spacing={1}>
      <Container disableGutters={true}>
        {filters.map((filter) => (
          <FormControl
            key={filter.name}
            variant="standard"
            sx={{ minWidth: "10rem", marginRight: "0.5rem" }}
          >
            <InputLabel id={`${filter.name}-filter-label`}>
              {filter.label}
            </InputLabel>
            <Select
              labelId={`${filter.name}-filter-label`}
              id={`${filter.name}-filter-label`}
              name={filter.name}
              label={filter.label}
              value={searchParams.get(filter.name) ?? ""}
              onChange={handleOnChange}
            >
              <MenuItem key="none" value="">
                <em>All</em>
              </MenuItem>
              {Object.entries(filter.values).map(([name, value]) => (
                <MenuItem key={value} value={value}>
                  {filter.disableValueFormat
                    ? name
                    : name.replace(/([A-Z])/g, " $1")}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        ))}
      </Container>
      <div style={{ height: "70vh" }}>
        <DataGrid
          columns={columns}
          rows={rows}
          disableColumnMenu={true}
          localeText={localeText}
          onRowClick={disableRowClick ? undefined : handleOnRowClick}
          initialState={initialState}
        />
      </div>
    </Stack>
  );
};

export default FilterableList;
