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

type ColumnBackgroundSvgProps = {
  columnWidthsPercent: [number, number, number, number];
  width: number;
  height: number;
  gapPx?: number;
  borderRadiusPx?: number;
  borderColor?: string;
  fillColor?: string;
};

const normalizeWidths = (
  columnWidthsPercent: [number, number, number, number],
): [number, number, number, number] => {
  const clamped = columnWidthsPercent.map((value) =>
    Number.isFinite(value) ? Math.max(0, value) : 0,
  ) as [number, number, number, number];

  const total = clamped.reduce((sum, value) => sum + value, 0);
  if (total <= 0) {
    return [25, 25, 25, 25];
  }

  return clamped.map((value) => (value / total) * 100) as [
    number,
    number,
    number,
    number,
  ];
};

const ColumnBackgroundSvg = ({
  columnWidthsPercent,
  width,
  height,
  gapPx = 8,
  borderRadiusPx = 4,
  borderColor = "#95A3B8",
  fillColor = "rgba(255, 255, 255, 0.04)",
}: ColumnBackgroundSvgProps) => {
  const svgWidth = Math.max(1, width);
  const svgHeight = Math.max(1, height);
  const widths = normalizeWidths(columnWidthsPercent);
  const columns = widths.length;
  const totalGap = Math.max(0, columns - 1) * gapPx;
  const availableWidth = Math.max(0, svgWidth - totalGap);

  let currentX = 0;
  const rects = widths.map((widthPercent, index) => {
    const widthPx = (widthPercent / 100) * availableWidth;
    const rect = (
      <rect
        key={`column-${index}`}
        x={currentX}
        y={0}
        width={Math.max(0, widthPx - 1)}
        height={Math.max(0, svgHeight - 1)}
        rx={borderRadiusPx}
        ry={borderRadiusPx}
        fill={fillColor}
        stroke={borderColor}
        strokeWidth={1}
      />
    );

    currentX += widthPx + gapPx;
    return rect;
  });

  return (
    <svg
      width="100%"
      height="100%"
      viewBox={`0 0 ${svgWidth} ${height}`}
      preserveAspectRatio="none"
      aria-hidden="true"
      focusable="false"
    >
      {rects}
    </svg>
  );
};

export default ColumnBackgroundSvg;
