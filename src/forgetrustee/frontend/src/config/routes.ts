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

import type { RouteConfig } from "../types/routeconfig";

export const routes: Record<string, RouteConfig> = {
  startpage: {
    name: "Startseite",
    path: "/",
  },
  dataTrustee: {
    name: "DataTrustee",
    path: "/dt",
  },
  dataTrusteeOverview: {
    name: "Übersicht",
    path: "/dt/overview",
  },
  dataTrusteeModeling: {
    name: "Modellierung",
    path: "/dt/modeling",
  },
  dataTrusteeModelingFree: {
    name: "Modellierung",
    path: "/dt/modeling",
  },
  dataTrusteeModelingDetail: {
    name: "Detailansicht",
    path: "/dt/modeling/detail/:modelId?",
  },
  dataTrusteeCompare: {
    name: "Vergleich Übersicht",
    path: "/dt/compare",
  },
  dataTrusteeCompareResult: {
    name: "Vergleich",
    path: "/dt/compare/result",
  },
  inform: {
    name: "Informieren",
    path: "https://fraunhofer-iese.github.io/KickStartTrustee/",
  },
  imprint: {
    name: "Impressum",
    path: "/imprint",
  },
  privacypolicy: {
    name: "Datenschutzerklärung",
    path: "/privacypolicy",
  },
};
