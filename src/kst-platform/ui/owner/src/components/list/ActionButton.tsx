import { Button, ButtonProps } from "@mui/material";
import React, { useCallback } from "react";

export type ActionButtonProps<V> = Omit<ButtonProps, "onClick"> & { onClick: (entry: V) => void, data: V | undefined };

const ActionButton = <V,>(props: ActionButtonProps<V>) => {
    const { children, onClick, data, ...otherProps } = props;

    const handleClick = useCallback((event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        event.stopPropagation();
        if (data) {
            onClick(data);
        }
    }, [onClick, data]);

    return (
        <Button {...otherProps} onClick={handleClick}>
            {children}
        </Button>
    );
};

export default ActionButton;
