import { GridComparatorFn } from "@mui/x-data-grid";
import dayjs from "dayjs";

const DATE_FORMAT = "YYYY-MM-DD";

export const dateComparator: GridComparatorFn<string> = (v1?: string, v2?: string) => {
  if (!v1 || !v2) {
    return 0;
  }
  return dayjs(v1, DATE_FORMAT).diff(dayjs(v2, DATE_FORMAT));
}

const useDateComparator = () => {
    return dateComparator;
};

export default useDateComparator;
