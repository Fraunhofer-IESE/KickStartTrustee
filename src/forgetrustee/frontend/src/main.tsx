/*
 Copyright 2026 Fraunhofer IESE

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.tsx";

import "@fontsource-variable/roboto";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import axios, { type AxiosRequestConfig } from "axios";
import Cookies from "js-cookie";
import config from "./config/config.ts";

function onCUDCall(config: AxiosRequestConfig): boolean {
  return (
    config.method === "post" ||
    config.method === "put" ||
    config.method === "patch" ||
    config.method === "delete"
  );
}

// configure axios
axios.defaults.baseURL = config.restUrl;
axios.defaults.withCredentials = true;
axios.defaults.xsrfCookieName = "XSRF-TOKEN";
axios.defaults.xsrfHeaderName = "X-XSRF-TOKEN";

axios.interceptors.request.use(
  async (config) => {
    if (config.url?.endsWith("/csrf")) {
      return config;
    }
    // call csrf dummy endpoint to get cookie if there is none
    const xsrfCookieName = config.xsrfCookieName;
    if (!xsrfCookieName) {
      return config;
    }
    const xsrfCookieValue = Cookies.get(xsrfCookieName);
    if (!xsrfCookieValue) {
      try {
        await axios.post("/csrf");
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      } catch (_err) {
        //ignore any error
      }
    }
    return config;
  },
  null,
  { runWhen: onCUDCall },
);

const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </StrictMode>,
);
