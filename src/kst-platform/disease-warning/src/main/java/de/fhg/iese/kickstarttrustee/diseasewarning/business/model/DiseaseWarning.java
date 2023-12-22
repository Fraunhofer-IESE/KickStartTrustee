package de.fhg.iese.kickstarttrustee.diseasewarning.business.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DiseaseWarning {
    private final String disease;
    private final String severity;
    private final List<String> endangeredFields;
    private final String date;

    public DiseaseWarning(String disease, String severity, List<String> endangeredFields, String date) {
        this.disease = disease;
        this.severity = severity;
        this.endangeredFields = endangeredFields;
        this.date = date;
    }

    public String getDisease() {
        return disease;
    }

    public String getSeverity() {
        return severity;
    }

    public List<String> getEndangeredFields() {
        return endangeredFields;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Object> asMap() {
        return Map.of("disease", disease,
                "severity", severity,
                "endangeredFields", endangeredFields,
                "date", date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disease, severity, endangeredFields, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof DiseaseWarning))
            return false;
        DiseaseWarning other = (DiseaseWarning) obj;
        return Objects.equals(disease, other.disease) && Objects.equals(severity, other.severity)
                && Objects.equals(endangeredFields, other.endangeredFields) && Objects.equals(date, other.date);
    }
}
