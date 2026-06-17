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

import React, { useCallback, useMemo, useState } from "react";
import { PinnedTipsContext, type PinnedTip } from "./pinnedTipsContext";

// types and context are defined in pinnedTipsContext.ts

export const PinnedTipsProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [tips, setTips] = useState<PinnedTip[]>([]);

  const pinTip = useCallback((tip: PinnedTip) => {
    // Keep exactly one pinned tip. Pinning a new one replaces the previous entry.
    setTips([tip]);
  }, []);

  const unpinTip = useCallback((fieldName: string) => {
    setTips((prev) => prev.filter((t) => t.fieldName !== fieldName));
  }, []);

  const unpinAll = useCallback(() => {
    setTips([]);
  }, []);

  const isPinned = useCallback(
    (fieldName: string) => tips.some((t) => t.fieldName === fieldName),
    [tips],
  );

  const value = useMemo(
    () => ({ tips, pinTip, unpinTip, unpinAll, isPinned }),
    [tips, pinTip, unpinTip, unpinAll, isPinned],
  );

  return (
    <PinnedTipsContext.Provider value={value}>
      {children}
    </PinnedTipsContext.Provider>
  );
};
