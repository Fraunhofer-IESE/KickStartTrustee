package de.fhg.iese.kickstarttrustee.properties;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItemType;
import de.fhg.iese.kickstarttrustee.common.properties.IDataItemProperties;

public class DataItemProperties implements IDataItemProperties {
    @NotEmpty 
    private List<DataItemTypeProperties> types;

    public DataItemProperties() {
        this.types = null;
    }

    @Override
    public List<? extends IDataItemType> getTypes() {
        return this.types;
    }

    public void setTypes(List<DataItemTypeProperties> types) {
        this.types = List.copyOf(types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(types);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataItemProperties other = (DataItemProperties) obj;
        return Objects.equals(types, other.types);
    }
}
