import "@fontsource/open-sans";
import React from "react";
import ReactDOM from "react-dom/client";
import { Experimental_CssVarsProvider as CssVarsProvider } from '@mui/material/styles';
import App from "./App";
import AuthContextProvider from "./components/context-providers/AuthContextProvider";
import Loader from "./components/loader/loader";
import "./index.css";
import KeycloakService, {
  KeycloakProviderInitConfig
} from "./services/KeycloakService";

const keycloakService = KeycloakService.getInstance();

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
root.render(
  <AuthContextProvider
    initOptions={KeycloakProviderInitConfig}
    keycloakService={keycloakService}
    LoadingComponent={<Loader />}
  >
    {/* <React.StrictMode> */}
      <CssVarsProvider>
        <App />
      </CssVarsProvider>
    {/* </React.StrictMode> */}
  </AuthContextProvider>
);
