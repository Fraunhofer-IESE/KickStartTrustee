import { FormControl, Input, InputLabel } from '@mui/material';
import React, { FC } from 'react';

export type DataFieldProps = {
    name: string;
    label: React.ReactNode;
    multiline?: boolean;
    value: unknown;
};

const DataField: FC<DataFieldProps> = (props) => {
    const { name, label, multiline, value } = props;
    return (
        <FormControl variant="standard">
            <InputLabel shrink={true} htmlFor={name}>{label}</InputLabel>
            <Input id={name} multiline={multiline} value={value} disableUnderline={true} readOnly={true} />
        </FormControl>
    );
};

export default DataField;
