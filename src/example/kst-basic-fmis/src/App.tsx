import { createTheme, ThemeProvider } from "@mui/material";
import { useMemo } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Loader from "./components/loader/loader";
import ErrorView from "./views/ErrorView";
import LoginView from "./views/root/auth/LoginView";
import LogoutView from "./views/root/auth/LogoutView";
import Home, { homeLoader } from "./views/root/home/Home";
import RootView, { rootLoader } from "./views/root/RootView";
import Fields, { fieldsLoader } from "./views/root/fields/Fields";
import DiseaseWarningView, {
  diseaseWarningLoader,
} from "./views/root/disease-warning/DiseaseWarningView";
import DiseaseReportView, {
  diseaseReportLoader,
} from "./views/root/disease-report/DiseaseReportView";
import CreateDiseaseReportView, {
  createDiseaseReportAction,
} from "./views/root/disease-report/CreateDiseaseReportView";
import CreateFieldView, {
  createFieldAction,
} from "./views/root/fields/CreateFieldView";
import CreateFieldErrorView from "./views/root/fields/CreateFieldErrorView";
import ConsentView, {
  consentViewLoader,
} from "./views/root/consent/ConsentView";
import { authCheck } from "./hooks/useAuthCheck";

export const APP_BASE_PATH = "";

const theme = createTheme({
  typography: {
    fontFamily: ["Open Sans", "Arial", "sans-serif"].join(","),
  },
});

function App() {
  const router = useMemo(
    () =>
      createBrowserRouter(
        [
          {
            path: "/",
            element: <RootView />,
            errorElement: <ErrorView />,
            id: "root",
            loader: rootLoader,
            children: [
              {
                index: true,
                loader: homeLoader,
                element: <Home />,
              },
              {
                path: "consent",
                loader: consentViewLoader,
                element: <ConsentView />,
              },
              {
                path: "fields",
                loader: fieldsLoader,
                element: <Fields />,
              },
              {
                path: "fields/create",
                loader: authCheck,
                action: createFieldAction,
                element: <CreateFieldView />,
                errorElement: <CreateFieldErrorView />,
              },
              {
                path: "disease-report",
                loader: diseaseReportLoader,
                element: <DiseaseReportView />,
              },
              {
                path: "disease-report/create",
                loader: fieldsLoader,
                action: createDiseaseReportAction,
                element: <CreateDiseaseReportView />,
              },
              {
                path: "disease-warning",
                loader: diseaseWarningLoader,
                element: <DiseaseWarningView />,
              },
              {
                path: "login",
                element: <LoginView />,
              },
              {
                path: "logout",
                element: <LogoutView />,
              },
            ],
          },
        ],
        {
          basename: APP_BASE_PATH,
        }
      ),
    []
  );

  return (
    <ThemeProvider theme={theme}>
      <RouterProvider router={router} fallbackElement={<Loader />} />
    </ThemeProvider>
  );
}

export default App;
