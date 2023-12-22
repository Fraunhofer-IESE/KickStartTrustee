import { DataItemDTO } from "owner-api";
import React, { useCallback } from "react";

const useDataDownload = () => {
  const createDataBlob = useCallback((json?: object) => {
    return new Blob([JSON.stringify(json)], {
      type: "text/json",
    });
  }, []);

  const downloadData = useCallback(
    (
      downloadLinkRef: React.RefObject<HTMLAnchorElement>,
      dataItem?: DataItemDTO
    ) => {
      const { current: downloadLink } = downloadLinkRef;
      if (!downloadLink) {
        return;
      }
      const data = createDataBlob(dataItem?.data);
      const dataUrl = window.URL.createObjectURL(data);
      downloadLink.href = dataUrl;
      downloadLink.download = `${dataItem?.id}.json`
      downloadLink.click();
      window.URL.revokeObjectURL(dataUrl);
    },
    [createDataBlob]
  );

  return {
    createDataBlob,
    downloadData,
  };
};

export default useDataDownload;
