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

import type { LocalStoragePayload } from "../../../types/localStoragePayload";

type ModelingSessionState = {
  hasResolvedInitialLocalState: boolean;
  payload: LocalStoragePayload | null;
};

const modelingSessionState: ModelingSessionState = {
  hasResolvedInitialLocalState: false,
  payload: null,
};

export const getModelingFreeSessionState = (): ModelingSessionState =>
  modelingSessionState;

export const setModelingFreeSessionPayload = (
  payload: LocalStoragePayload | null,
): void => {
  modelingSessionState.payload = payload;
  modelingSessionState.hasResolvedInitialLocalState = true;
};

export const markModelingFreeSessionResolved = (): void => {
  modelingSessionState.hasResolvedInitialLocalState = true;
};

export const clearModelingFreeSessionState = (): void => {
  modelingSessionState.hasResolvedInitialLocalState = false;
  modelingSessionState.payload = null;
};
