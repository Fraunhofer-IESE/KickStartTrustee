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

import JSZip from "jszip";

export const MODEL_EXPORT_FILE_NAMES = {
  formData: "formData.json",
  resultData: "resultData.json",
  executiveSummary: "executiveSummary.html",
  ttl: "model.ttl",
  validation: "validation.json",
  legacyFormData: "wizard.json",
  legacyResultData: "dtm.json",
} as const;

type CreateModelExportZipParams = {
  modelName?: string | null;
  formData?: unknown;
  resultData?: unknown;
  executiveSummary?: string;
  ttl?: string;
  validation?: unknown;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
  fileNamePrefix?: string;
};

const toSafeFileNamePart = (value: string | null | undefined): string => {
  const base = (value ?? "").trim().toLowerCase();
  if (!base) {
    return "modell";
  }

  return (
    base
      .replace(/\s+/g, "-")
      .replace(/[^a-z0-9\-_.]/g, "")
      .replace(/-+/g, "-")
      .replace(/^-+|-+$/g, "") || "modell"
  );
};

const getFileExtensionFromName = (fileName: string): string | null => {
  const normalized = fileName.trim();
  const lastDot = normalized.lastIndexOf(".");
  if (lastDot <= 0 || lastDot === normalized.length - 1) {
    return null;
  }
  return normalized.slice(lastDot + 1).toLowerCase();
};

const getFileNameFromPath = (fileName: string): string => {
  const normalized = fileName.replace(/\\/g, "/");
  const parts = normalized.split("/");
  return parts[parts.length - 1] || "avatar";
};

const extensionForAvatarMime = (mime: string): string => {
  if (mime.includes("png")) return "png";
  if (mime.includes("jpeg") || mime.includes("jpg")) return "jpg";
  if (mime.includes("webp")) return "webp";
  if (mime.includes("gif")) return "gif";
  if (mime.includes("svg")) return "svg";
  return "png";
};

export const downloadBlob = (blob: Blob, fileName: string) => {
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
};

export const createModelExportZip = async (
  params: CreateModelExportZipParams,
): Promise<{ blob: Blob; fileName: string } | null> => {
  const {
    modelName,
    formData,
    resultData,
    executiveSummary,
    ttl,
    validation,
    avatarDataUrl,
    avatarFileName,
    fileNamePrefix = "data-trustee-model",
  } = params;

  const zip = new JSZip();

  if (formData !== undefined && formData !== null) {
    zip.file(MODEL_EXPORT_FILE_NAMES.formData, JSON.stringify(formData, null, 2));
  }

  if (resultData !== undefined && resultData !== null) {
    zip.file(MODEL_EXPORT_FILE_NAMES.resultData, JSON.stringify(resultData, null, 2));
  }

  if (executiveSummary) {
    zip.file(MODEL_EXPORT_FILE_NAMES.executiveSummary, executiveSummary);
  }

  if (ttl) {
    zip.file(MODEL_EXPORT_FILE_NAMES.ttl, ttl);
  }

  if (validation !== undefined && validation !== null) {
    zip.file(MODEL_EXPORT_FILE_NAMES.validation, JSON.stringify(validation, null, 2));
  }

  if (avatarDataUrl) {
    try {
      const avatarBlob = await fetch(avatarDataUrl).then((response) => response.blob());
      const preferredName = avatarFileName ? getFileNameFromPath(avatarFileName) : "";
      const extFromName = preferredName ? getFileExtensionFromName(preferredName) : null;
      const ext = extFromName ?? extensionForAvatarMime(avatarBlob.type);
      const avatarExportName = preferredName || `avatar.${ext}`;
      zip.file(avatarExportName, avatarBlob);
    } catch (error) {
      console.error("Avatar konnte nicht in ZIP exportiert werden", error);
    }
  }

  if (Object.keys(zip.files).length === 0) {
    return null;
  }

  const ts = new Date().toISOString().slice(0, 19).replace(/[:T]/g, "-");
  const safeName = toSafeFileNamePart(modelName ?? null);
  const fileName = safeName
    ? `${fileNamePrefix}-${safeName}-${ts}.zip`
    : `${fileNamePrefix}-${ts}.zip`;

  const blob = await zip.generateAsync({ type: "blob" });
  return { blob, fileName };
};
