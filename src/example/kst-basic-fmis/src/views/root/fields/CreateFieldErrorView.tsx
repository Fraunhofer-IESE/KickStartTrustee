import { useMemo } from "react";
import {
  Navigate,
  isRouteErrorResponse,
  useRouteError,
} from "react-router-dom";

const CreateFieldErrorView = () => {
  const error = useRouteError();
  const state = useMemo(() => {
    return { error };
  }, [error]);

  if (isRouteErrorResponse(error) && error.status === 401) {
    return <Navigate to="/login" />;
  }

  return <Navigate to="/fields/create" replace={true} state={state} />;
};

export default CreateFieldErrorView;
