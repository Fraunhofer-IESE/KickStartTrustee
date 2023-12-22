import { LayersControl, MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import {
  Feature,
  Polygon as GeojsonPolygon,
  MultiPolygon as GeojsonMultiPolygon,
} from "geojson";

import iconMarker from "leaflet/dist/images/marker-icon.png";
import iconRetina from "leaflet/dist/images/marker-icon-2x.png";
import iconShadow from "leaflet/dist/images/marker-shadow.png";
import L, { LatLngExpression } from "leaflet";
import { FC, PropsWithChildren } from "react";
import { FieldBoundariesExtractor } from "./FieldBoundariesExtractor";
import { GeocoderEnabler } from "./GeocoderEnabler";
import { GeomanEnabler } from "./GeomanEnabler";
import { DataItemDTO } from "kst-api";
import { MapCenterer } from "./MapCenterer";
L.Icon.Default.mergeOptions({
  iconRetinaUrl: iconRetina,
  iconUrl: iconMarker,
  shadowUrl: iconShadow,
});

type BasicFmisMapComponentProps = {
  enableDraw?: boolean;
  drawColor?: string;
  onDrawnFieldBoundariesChange?: (
    drawnFieldBoundaries: Feature<
      GeojsonPolygon | GeojsonMultiPolygon,
      any
    > | null
  ) => void;
  enableGeocoder?: boolean;
  center?: LatLngExpression;
  zoom?: number;
  fieldDataItemsInFocus?: Array<DataItemDTO>;
};

const BasicFmisMapComponent: FC<
  PropsWithChildren<BasicFmisMapComponentProps>
> = (props) => {
  const {
    children,
    enableDraw = false,
    drawColor = "#22bf73",
    onDrawnFieldBoundariesChange,
    enableGeocoder = false,
    center = [51.1638175, 10.4478313],
    zoom = 6,
    fieldDataItemsInFocus,
  } = props;

  return (
    <MapContainer
      maxBounds={[
        [-90, -180],
        [90, 180],
      ]}
      center={center}
      zoom={zoom}
      minZoom={2}
      scrollWheelZoom={false}
    >
      <LayersControl position="topright">
        <LayersControl.BaseLayer checked name="Karte">
          <TileLayer
            url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
        </LayersControl.BaseLayer>
        <LayersControl.BaseLayer name="Satellit">
          <TileLayer
            url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
            attribution="Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community"
            maxZoom={30}
            maxNativeZoom={20}
          />
        </LayersControl.BaseLayer>
      </LayersControl>
      {children}
      {fieldDataItemsInFocus && (
        <MapCenterer fieldDataItemsInFocus={fieldDataItemsInFocus} />
      )}
      {enableGeocoder && <GeocoderEnabler />}
      {enableDraw && <GeomanEnabler drawColor={drawColor} />}
      {enableDraw && onDrawnFieldBoundariesChange && (
        <FieldBoundariesExtractor
          onDrawnFieldBoundariesChange={onDrawnFieldBoundariesChange}
        />
      )}
    </MapContainer>
  );
};

export default BasicFmisMapComponent;
