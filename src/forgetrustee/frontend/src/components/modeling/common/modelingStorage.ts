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

import type { DataTrusteeModelDraft } from "../../../types/dataTrusteeModelDraft";
import type { LocalStoragePayload } from "../../../types/localStoragePayload";

export const MODELING_FREE_FORM_KEY = "modelingFreeState.formValues";
export const MODELING_FREE_AVATAR_KEY = "modelingFreeState.avatar";
export const MODELING_FREE_RESULT_KEY = "modelingFreeState.result";
export const MODELING_FREE_META_KEY = "modelingFreeState.meta";

type ModelingFreeMeta = {
  createdAt?: string;
  savedAt?: string;
  hasPendingChanges?: boolean;
};

type ModelingFreeAvatar = {
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
};

const parseStorageItem = <T>(key: string): T | null => {
  const value = localStorage.getItem(key);
  if (!value) {
    return null;
  }

  try {
    return JSON.parse(value) as T;
  } catch (error) {
    console.error(`Konnte LocalStorage-Key '${key}' nicht laden`, error);
    return null;
  }
};

export const saveModelingFreePayload = (payload: LocalStoragePayload): void => {
  const existingMeta = parseStorageItem<ModelingFreeMeta>(
    MODELING_FREE_META_KEY,
  );
  const now = new Date().toISOString();
  const createdAt =
    existingMeta?.createdAt ?? payload.createdAt ?? payload.savedAt ?? now;

  const avatar: ModelingFreeAvatar = {
    avatarDataUrl: payload.avatarDataUrl ?? null,
    avatarFileName: payload.avatarFileName ?? null,
  };

  localStorage.setItem(
    MODELING_FREE_FORM_KEY,
    JSON.stringify(payload.formValues),
  );
  localStorage.setItem(MODELING_FREE_AVATAR_KEY, JSON.stringify(avatar));
  localStorage.setItem(
    MODELING_FREE_RESULT_KEY,
    JSON.stringify(payload.modelBuildResult ?? null),
  );
  localStorage.setItem(
    MODELING_FREE_META_KEY,
    JSON.stringify({
      createdAt,
      savedAt: now,
      hasPendingChanges: payload.hasPendingChanges ?? false,
    }),
  );
};

export const loadModelingFreePayload = (): LocalStoragePayload | null => {
  const formValues = parseStorageItem<DataTrusteeModelDraft>(
    MODELING_FREE_FORM_KEY,
  );
  const avatar = parseStorageItem<ModelingFreeAvatar>(MODELING_FREE_AVATAR_KEY);
  const modelBuildResult = parseStorageItem<
    LocalStoragePayload["modelBuildResult"]
  >(MODELING_FREE_RESULT_KEY);
  const meta = parseStorageItem<ModelingFreeMeta>(MODELING_FREE_META_KEY);

  const hasData =
    !!formValues ||
    !!avatar?.avatarDataUrl ||
    !!avatar?.avatarFileName ||
    !!modelBuildResult;

  if (!hasData) {
    return null;
  }

  return {
    formValues: formValues ?? ({} as DataTrusteeModelDraft),
    avatarDataUrl: avatar?.avatarDataUrl ?? null,
    avatarFileName: avatar?.avatarFileName ?? null,
    modelBuildResult: modelBuildResult ?? null,
    hasPendingChanges: meta?.hasPendingChanges ?? false,
    createdAt: meta?.createdAt,
    savedAt: meta?.savedAt,
  };
};

export const clearModelingFreePayload = (): void => {
  localStorage.removeItem(MODELING_FREE_FORM_KEY);
  localStorage.removeItem(MODELING_FREE_AVATAR_KEY);
  localStorage.removeItem(MODELING_FREE_RESULT_KEY);
  localStorage.removeItem(MODELING_FREE_META_KEY);
};
