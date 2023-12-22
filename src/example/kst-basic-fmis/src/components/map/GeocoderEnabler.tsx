import { FC, useEffect, useMemo } from "react";
import { useMap } from "react-leaflet";
import { Geocoder } from "leaflet-control-geocoder";
import "leaflet-control-geocoder/dist/Control.Geocoder.css";

export const GeocoderEnabler: FC = () => {
  const map = useMap();

  const geocoder = useMemo(() => {
    return new Geocoder({
      position: "topleft",
    });
  }, []);

  useEffect(() => {
    map.addControl(geocoder);
    return () => {
      map.removeControl(geocoder);
    };
  }, [map, geocoder]);
  return null;
};
