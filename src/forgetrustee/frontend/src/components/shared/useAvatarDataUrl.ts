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

import { useEffect, useState } from "react";
import { useGetDataTrusteeModelAvatar } from "../../api/generated/data-trustee-model-controller/data-trustee-model-controller";

const useAvatarDataUrl = (
  modelId?: string | null,
  fallbackDataUrl?: string | null,
): string | null => {
  const [avatarDataUrl, setAvatarDataUrl] = useState<string | null>(fallbackDataUrl ?? null);

  const { data: avatarBlob } = useGetDataTrusteeModelAvatar(modelId ?? "", {
    query: {
      enabled: Boolean(modelId) && !fallbackDataUrl,
    },
  });

  useEffect(() => {
    if (fallbackDataUrl) {
      setAvatarDataUrl(fallbackDataUrl);
      return;
    }

    if (!avatarBlob) {
      setAvatarDataUrl(null);
      return;
    }

    const objectUrl = URL.createObjectURL(avatarBlob);
    setAvatarDataUrl(objectUrl);

    return () => URL.revokeObjectURL(objectUrl);
  }, [avatarBlob, fallbackDataUrl]);

  return avatarDataUrl;
};

export default useAvatarDataUrl;
