package de.fhg.iese.kickstarttrustee.storage.business.model;

import java.util.Objects;

public class DataItemType {
    private final String id;

    public DataItemType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof DataItemType))
            return false;
        DataItemType other = (DataItemType) obj;
        return Objects.equals(id, other.id);
    }
}
