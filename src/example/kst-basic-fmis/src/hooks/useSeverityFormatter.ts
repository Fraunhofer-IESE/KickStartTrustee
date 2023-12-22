import { useCallback } from "react";
import { IntlShape, useIntl } from "react-intl"
import DiseaseReportSeverity from "../model/DiseaseReportSeverity";

export const severityFormatter = (intl: IntlShape, severity?: DiseaseReportSeverity) => {
    const { formatMessage } = intl;
    switch(severity) {
        case DiseaseReportSeverity.Low:
            return formatMessage({ id: "disease_severity_low" });
        case DiseaseReportSeverity.Medium:
            return formatMessage({ id: "disease_severity_medium" });
        case DiseaseReportSeverity.High:
            return formatMessage({ id: "disease_severity_high" });    
    }
};

const useSeverityFormatter = () => {
    const intl = useIntl();

    const formatSeverity = useCallback((severity?: DiseaseReportSeverity) => {
        return severityFormatter(intl, severity);
    }, [intl]);

    return formatSeverity;
};

export default useSeverityFormatter;
