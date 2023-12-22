import { FC, useCallback } from "react";
import {
  Feature,
  Polygon as GeojsonPolygon,
  MultiPolygon as GeojsonMultiPolygon,
} from "geojson";
import { useMap, useMapEvent } from "react-leaflet";
import { Layer, Map, Polygon as PolygonLayer } from "leaflet";
import L from "leaflet";

type FieldBoundariesExtractorProps = {
  onDrawnFieldBoundariesChange: (
    drawnFieldBoundaries: Feature<
      GeojsonPolygon | GeojsonMultiPolygon,
      any
    > | null
  ) => void;
};
export const FieldBoundariesExtractor: FC<FieldBoundariesExtractorProps> = (
  props
) => {
  const { onDrawnFieldBoundariesChange } = props;
  const map = useMap();
  const reportCurrentFieldBoundariesFromMap = useCallback(
    () => onDrawnFieldBoundariesChange(extractBoundariesFromMap(map)),
    [map, onDrawnFieldBoundariesChange]
  );

  useMapEvent("pm:create" as any, ({ layer }: { layer: Layer }) => {
    reportCurrentFieldBoundariesFromMap();
    registerEventListenersOnLayer(layer, reportCurrentFieldBoundariesFromMap);
  });
  return null;
};

const extractBoundariesFromMap = (
  map: Map
): Feature<GeojsonPolygon | GeojsonMultiPolygon, any> | null => {
  const polygonFeatures: Feature<GeojsonPolygon | GeojsonMultiPolygon, any>[] =
    [];
  const geomanDrawLayers = map.pm.getGeomanDrawLayers();
  geomanDrawLayers.forEach((layer) => {
    if (layer instanceof L.Polygon) {
      const polygonFeature = (layer as PolygonLayer).toGeoJSON();
      polygonFeatures.push(polygonFeature);
    }
  });
  if (polygonFeatures.length === 1) {
    return polygonFeatures[0];
  } else if (polygonFeatures.length > 1) {
    return combineToSingleMultiPolygonFeature(polygonFeatures);
  } else {
    return null;
  }
};

const combineToSingleMultiPolygonFeature = (
  polygonFeatures: Feature<GeojsonPolygon | GeojsonMultiPolygon, any>[]
) => {
  const multiPolygonFeature: Feature<GeojsonMultiPolygon, any> = {
    type: "Feature",
    properties: {},
    geometry: {
      type: "MultiPolygon",
      coordinates: [],
    },
  };

  polygonFeatures.forEach((polygonFeature) => {
    const featureGeometry = polygonFeature.geometry;
    if (featureGeometry.type === "Polygon") {
      multiPolygonFeature.geometry.coordinates.push(
        featureGeometry.coordinates
      );
    } else if (featureGeometry.type === "MultiPolygon") {
      const multiPolygonCoordinates = featureGeometry.coordinates;
      multiPolygonCoordinates.forEach((polygonCoordinates) => {
        multiPolygonFeature.geometry.coordinates.push(polygonCoordinates);
      });
    }
  });

  return multiPolygonFeature;
};

const registerEventListenersOnLayer = (layer: Layer, onUpdate: () => void) => {
  layer.on("pm:update", () => {
    onUpdate();
  });
  layer.on("pm:remove", () => {
    onUpdate();
  });
  layer.on("pm:cut", ({ layer }: { layer: Layer }) => {
    onUpdate();
    registerEventListenersOnLayer(layer, onUpdate);
  });
};
