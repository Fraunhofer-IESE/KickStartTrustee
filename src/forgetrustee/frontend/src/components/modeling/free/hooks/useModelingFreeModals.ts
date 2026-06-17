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

import { useCallback, useState } from "react";

type UseModelingFreeModalsParams = {
  isSubmittingModel: boolean;
  modelSubmissionError: string | null;
  onStartModelingModalClosed?: () => void;
};

const useModelingFreeModals = ({
  isSubmittingModel,
  modelSubmissionError,
  onStartModelingModalClosed,
}: UseModelingFreeModalsParams) => {
  const [showResetModal, setShowResetModal] = useState(false);
  const [showStartModelingModal, setShowStartModelingModal] = useState(false);
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showAvatarUploadModal, setShowAvatarUploadModal] = useState(false);
  const [showHelp, setShowHelp] = useState(false);

  const openResetModal = useCallback(() => setShowResetModal(true), []);
  const closeResetModal = useCallback(() => setShowResetModal(false), []);

  const openStartModelingModal = useCallback(() => setShowStartModelingModal(true), []);
  const closeStartModelingModal = useCallback(() => {
    if (isSubmittingModel && !modelSubmissionError) {
      return;
    }

    setShowStartModelingModal(false);
    onStartModelingModalClosed?.();
  }, [isSubmittingModel, modelSubmissionError, onStartModelingModalClosed]);

  const forceCloseStartModelingModal = useCallback(() => {
    setShowStartModelingModal(false);
  }, []);

  const openUploadModal = useCallback(() => setShowUploadModal(true), []);
  const closeUploadModal = useCallback(() => setShowUploadModal(false), []);

  const openAvatarUploadModal = useCallback(() => setShowAvatarUploadModal(true), []);
  const closeAvatarUploadModal = useCallback(() => setShowAvatarUploadModal(false), []);

  const openHelpModal = useCallback(() => setShowHelp(true), []);
  const closeHelpModal = useCallback(() => setShowHelp(false), []);

  return {
    closeAvatarUploadModal,
    closeHelpModal,
    closeResetModal,
    closeStartModelingModal,
    closeUploadModal,
    forceCloseStartModelingModal,
    openAvatarUploadModal,
    openHelpModal,
    openResetModal,
    openStartModelingModal,
    openUploadModal,
    showAvatarUploadModal,
    showHelp,
    showResetModal,
    showStartModelingModal,
    showUploadModal,
  };
};

export default useModelingFreeModals;
