import { createTheme, ThemeProvider } from "@mui/material";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { deleteAccountAction } from "./components/account/DeleteAccount";
import Loader from "./components/loader/loader";
import { authCheck } from "./hooks/useAuthCheck";
import ErrorView from "./views/ErrorView";
import AuditErrorView from "./views/root/audit/AuditErrorView";
import AuditLogDetailView, {
  auditLogDetailLoader,
} from "./views/root/audit/AuditLogDetailView";
import AuditLogView, { auditLogLoader } from "./views/root/audit/AuditLogView";
import LoginView from "./views/root/auth/LoginView";
import LogoutView from "./views/root/auth/LogoutView";
import ConsentRequestDetailView, {
  consentRequestAcceptAction,
  consentRequestDetailLoader,
  consentRequestRejectAction,
} from "./views/root/consent-request/ConsentRequestDetailView";
import ConsentRequestErrorView from "./views/root/consent-request/ConsentRequestErrorView";
import ConsentRequestListView, {
  consentRequestListLoader,
} from "./views/root/consent-request/ConsentRequestListView";
import ConsentDetailView, {
  consentDetailLoader,
  consentRevokeAction,
} from "./views/root/consent/ConsentDetailView";
import ConsentErrorView from "./views/root/consent/ConsentErrorView";
import ConsentListView, {
  consentListLoader,
} from "./views/root/consent/ConsentListView";
import Home from "./views/root/home/Home";
import Profile from "./views/root/profile/Profile";
import RootView, { rootAction, rootLoader } from "./views/root/RootView";
import { useMemo } from "react";
import DiseaseWarningView, {
  diseaseWarningAction,
  diseaseWarningLoader,
} from "./views/root/service/DiseaseWarningView";
import { APP_BASE_PATH } from "./config/AppConfig";
import DataListView, { dataListLoader } from "./views/root/data/DataListView";
import DataErrorView from "./views/root/data/DataErrorView";
import DataDetailView, {
  dataDeleteAction,
  dataDetailLoader,
} from "./views/root/data/DataDetailView";

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
            action: rootAction,
            children: [
              {
                index: true,
//                loader: homeLoader,
                element: <Home />,
              },
              {
                path: "account/delete",
                action: deleteAccountAction,
              },
              {
                path: "audit",
                errorElement: <AuditErrorView />,
                children: [
                  {
                    index: true,
                    loader: auditLogLoader,
                    element: <AuditLogView />,
                  },
                  {
                    path: ":auditLogEntryId",
                    loader: auditLogDetailLoader,
                    element: <AuditLogDetailView />,
                  },
                ],
              },
              {
                path: "data",
                errorElement: <DataErrorView />,
                children: [
                  {
                    index: true,
                    loader: dataListLoader,
                    element: <DataListView />,
                  },
                  {
                    path: ":dataItemId",
                    loader: dataDetailLoader,
                    element: <DataDetailView />,
                  },
                  {
                    path: ":dataItemId/delete",
                    action: dataDeleteAction,
                  },
                ],
              },
              {
                path: "consent",
                errorElement: <ConsentErrorView />,
                children: [
                  {
                    index: true,
                    loader: consentListLoader,
                    element: <ConsentListView />,
                  },
                  {
                    path: ":consentId",
                    loader: consentDetailLoader,
                    element: <ConsentDetailView />,
                  },
                  {
                    path: ":consentId/revoke",
                    action: consentRevokeAction,
                  },
                ],
              },
              {
                path: "consent-request",
                errorElement: <ConsentRequestErrorView />,
                children: [
                  {
                    index: true,
                    loader: consentRequestListLoader,
                    element: <ConsentRequestListView />,
                  },
                  {
                    path: ":consentRequestId",
                    loader: consentRequestDetailLoader,
                    element: <ConsentRequestDetailView />,
                  },
                  {
                    path: ":consentRequestId/accept",
                    action: consentRequestAcceptAction,
                  },
                  {
                    path: ":consentRequestId/reject",
                    action: consentRequestRejectAction,
                  },
                ],
              },
              {
                path: "service",
                children: [
                  {
                    path: "disease-warning",
                    loader: diseaseWarningLoader,
                    action: diseaseWarningAction,
                    element: <DiseaseWarningView />,
                  },
                ],
              },
              {
                path: "profile",
                loader: authCheck,
                element: <Profile />,
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
