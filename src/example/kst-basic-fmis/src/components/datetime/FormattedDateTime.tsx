import { FC, Fragment } from "react";
import useDateTimeFormatter from "../../hooks/useDateTimeFormatter";

export type FormattedDateTimeProps = {
    value: string | Date;
};

const FormattedDateTime: FC<FormattedDateTimeProps> = (props) => {
    const { value } = props;
    const dateTimeFormatter = useDateTimeFormatter();

    return (
        <Fragment>
            {dateTimeFormatter(value)}
        </Fragment>
    );
};

export default FormattedDateTime;
