import { DataItemDTO } from "kst-api";
import L from "leaflet";
import { FC, useMemo, useEffect } from "react";
import { useMap } from "react-leaflet";
import { Feature, FeatureCollection } from "geojson";

type MapCentererProps = {
  fieldDataItemsInFocus: Array<DataItemDTO>;
};

export const MapCenterer: FC<MapCentererProps> = (props) => {
  const { fieldDataItemsInFocus } = props;
  const map = useMap();

  const fieldDataItemsInFocusWithBoundaries = useMemo(
    () =>
      fieldDataItemsInFocus?.filter(
        (fieldDataItem) => fieldDataItem.data?.boundaries
      ),
    [fieldDataItemsInFocus]
  );

  const allFieldBoundariesFeatureCollection = useMemo(() => {
    const allFieldBoundaries = fieldDataItemsInFocusWithBoundaries.map(
      (fieldDataItem) => fieldDataItem.data?.boundaries as Feature
    );
    const allFieldBoundariesFeatureCollection: FeatureCollection = {
      type: "FeatureCollection",
      features: allFieldBoundaries,
    };
    return allFieldBoundariesFeatureCollection;
  }, [fieldDataItemsInFocusWithBoundaries]);

  useEffect(() => {
    if (allFieldBoundariesFeatureCollection.features.length > 0) {
      const geoJsonObj = L.geoJSON(allFieldBoundariesFeatureCollection);
      map.fitBounds(geoJsonObj.getBounds(), {
        maxZoom: map.getBoundsZoom(geoJsonObj.getBounds()),
      });
    }
  }, [map, allFieldBoundariesFeatureCollection]);
  return null;
};
