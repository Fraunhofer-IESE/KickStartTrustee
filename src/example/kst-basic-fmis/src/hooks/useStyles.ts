import { CSSProperties, useMemo } from "react";

export type TStyles = { [s: string]: CSSProperties };
export type TStyleCreator = () => TStyles;

const useStyles = (styleCreator: TStyleCreator): Readonly<TStyles> => {
    return useMemo(() => styleCreator(), [styleCreator]);
};

export default useStyles;
