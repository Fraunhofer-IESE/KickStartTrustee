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

import { createBrowserRouter } from "react-router-dom";
import StartPage from "../components/startpage/StartPage";
import StartPageContent from "../components/startpage/StartPageContent";
import ModelingFree from "../components/modeling/free/ModelingFree";
import ModelingResultDetailPage from "../components/modeling/result/ModelingResultDetailPage";
import { routes } from "../config/routes";
import CompareOverview from "../components/compare/CompareOverview";
import Compare from "../components/compare/Compare";
import Layout from "../components/layout/Layout";
import Imprint from "../components/imprint/Imprint";
import PrivacyPolicy from "../components/privacypolicy/PrivacyPolicy";
import RouteErrorPage from "../components/shared/RouteErrorPage";

export const router = createBrowserRouter([
  {
    path: routes.startpage.path,
    element: <StartPage />,
    errorElement: <RouteErrorPage />,
    children: [
      {
        index: true,
        element: <StartPageContent />,
      },
      {
        path: routes.imprint.path.slice(1),
        element: <Imprint />,
      },
      {
        path: routes.privacypolicy.path.slice(1),
        element: <PrivacyPolicy />,
      },
    ],
  },
  {
    path: routes.dataTrustee.path,
    element: <Layout />,
    errorElement: <RouteErrorPage />,
    children: [
      {
        path: routes.dataTrusteeModeling.path,
        element: <ModelingFree />,
      },
      {
        path: routes.dataTrusteeModelingDetail.path,
        element: <ModelingResultDetailPage />,
      },
      {
        path: routes.dataTrusteeCompare.path,
        element: <CompareOverview />,
      },
      {
        path: routes.dataTrusteeCompareResult.path,
        element: <Compare />,
      },
    ],
  },
]);
