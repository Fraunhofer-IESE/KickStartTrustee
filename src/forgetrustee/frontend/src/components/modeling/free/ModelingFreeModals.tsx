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
import ModalAvatarUpload from "./modals/ModalAvatarUpload";
import ModalHelp from "./modals/ModalHelp";
import ModalImportPrompt from "./modals/ModalImportPrompt";
import ModalReset from "./modals/ModalReset";
import ModalStartModeling from "./modals/ModalStartModeling";
import ModalUpload from "./modals/ModalUpload";

type ModelingFreeModalsProps = {
  help: {
    open: boolean;
    onClose: () => void;
  };
  localImport: {
    open: boolean;
    onClose: () => void;
    onImport: () => void;
    payload: LocalStoragePayload | null;
  };
  reset: {
    open: boolean;
    onClose: () => void;
    onConfirm: () => void;
  };
  startModeling: {
    open: boolean;
    onClose: () => void;
    onConfirm: () => Promise<void>;
    isSubmitting: boolean;
    errorMessage: string | null;
  };
  upload: {
    open: boolean;
    onClose: () => void;
    onConfirm: (payload: LocalStoragePayload) => void;
  };
  avatarUpload: {
    open: boolean;
    avatarDataUrl: string | null;
    avatarFileName?: string | null;
    onClose: () => void;
    onConfirm: (payload: { file: File | null; dataUrl: string | null; cleared?: boolean }) => void;
  };
};

const ModelingFreeModals = ({
  help,
  localImport,
  reset,
  startModeling,
  upload,
  avatarUpload,
}: ModelingFreeModalsProps) => (
  <>
    <ModalHelp
      open={help.open}
      onClose={help.onClose}
    />
    <ModalImportPrompt
      open={localImport.open}
      onClose={localImport.onClose}
      onImport={localImport.onImport}
      payload={localImport.payload}
    />
    <ModalReset
      open={reset.open}
      onClose={reset.onClose}
      onConfirm={reset.onConfirm}
    />
    <ModalStartModeling
      open={startModeling.open}
      onClose={startModeling.onClose}
      onConfirm={startModeling.onConfirm}
      isSubmitting={startModeling.isSubmitting}
      errorMessage={startModeling.errorMessage}
    />
    <ModalUpload
      open={upload.open}
      onClose={upload.onClose}
      onConfirm={upload.onConfirm}
    />
    <ModalAvatarUpload
      open={avatarUpload.open}
      avatarDataUrl={avatarUpload.avatarDataUrl}
      avatarFileName={avatarUpload.avatarFileName}
      onClose={avatarUpload.onClose}
      onConfirm={avatarUpload.onConfirm}
    />
  </>
);

export default ModelingFreeModals;
