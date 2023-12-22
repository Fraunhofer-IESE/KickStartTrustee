import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
import { FC, useEffect } from "react";
import { useMap } from "react-leaflet";

type GeomanEnablerProps = {
  drawColor?: string;
};

export const GeomanEnabler: FC<GeomanEnablerProps> = (props) => {
  const { drawColor } = props;
  const map = useMap();
  useEffect(() => {
    map.pm.addControls({
      drawMarker: false,
      drawPolygon: true,
      editPolygon: true,
      drawPolyline: false,
      deleteLayer: true,
      position: "topleft",
      drawCircle: false,
      drawCircleMarker: false,
      drawRectangle: false,
      drawText: false,
    });

    map.pm.enableDraw("Polygon", {
      allowSelfIntersection: false,
      hintlineStyle: {
        color: drawColor,
        dashArray: [5, 5],
      },
      templineStyle: {
        color: drawColor,
      },
      pathOptions: {
        color: drawColor,
        fillColor: drawColor,
      },
    });

    map.pm.disableDraw("Polygon");
    return () => {
      map.pm.removeControls();
    };
  }, [map, drawColor]);
  return null;
};
