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

import axios, {
  AxiosError,
  AxiosHeaders,
  type AxiosRequestConfig,
  type RawAxiosHeaders,
} from "axios";
import config from "./config";

const normalizeMultipartPayload = (axiosConfig: AxiosRequestConfig): AxiosRequestConfig => {
  if (!(axiosConfig.data instanceof FormData)) {
    return axiosConfig;
  }

  const formData = axiosConfig.data;
  const requestPart = formData.get("request");

  // Ensure Spring can deserialize @RequestPart("request") into a DTO.
  if (typeof requestPart === "string") {
    formData.set("request", new Blob([requestPart], { type: "application/json" }));
  }

  const headers = AxiosHeaders.from((axiosConfig.headers ?? {}) as RawAxiosHeaders);
  headers.delete("Content-Type");

  return {
    ...axiosConfig,
    data: formData,
    headers,
  };
};

export const customInstance = <T>(axiosConfig: AxiosRequestConfig): Promise<T> => {
  const normalizedConfig = normalizeMultipartPayload(axiosConfig);
  const instance = axios.create({
    baseURL: config.restUrl,
    withCredentials: true,
  });

  return instance
    .request<T>({
      ...normalizedConfig,
      baseURL: config.restUrl, // TODO change this after switching to generated hooks
      // baseURL: "http://localhost:8080",
      withCredentials: true,
      withXSRFToken: true,
    })
    .then((res) => res.data);
};

export type ErrorType<Error> = AxiosError<Error>;

export type BodyType<BodyData> = BodyData;
