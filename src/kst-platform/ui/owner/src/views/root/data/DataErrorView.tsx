import { useEffect } from "react";
import {
  isRouteErrorResponse,
  useNavigate,
  useRouteError,
} from "react-router-dom";
import Loader from "../../../components/loader/loader";

const DataErrorView = () => {
  const error = useRouteError();
  const navigate = useNavigate();
  useEffect(() => {
    if (isRouteErrorResponse(error) && error.status === 401) {
      navigate("/login");
    } else {
      navigate("/data", {
        replace: true,
        state: { error },
      });
    }
  }, [error, navigate]);

  return <Loader />;
};

export default DataErrorView;
