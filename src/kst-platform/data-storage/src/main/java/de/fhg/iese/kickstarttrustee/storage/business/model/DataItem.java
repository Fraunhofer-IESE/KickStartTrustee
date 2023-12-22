package de.fhg.iese.kickstarttrustee.storage.business.model;

import java.util.Map;
import java.util.Objects;

public class DataItem {
    private final String id;
    private final MetaData metaData;
    private final Map<String, Object> data;

    public DataItem(String id, MetaData metaData, Map<String, Object> data) {
        this.id = id;
        this.metaData = Objects.requireNonNull(metaData);
        this.data = Objects.requireNonNull(data);
    }

    public String getId() {
        return id;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metaData, data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof DataItem))
            return false;
        DataItem other = (DataItem) obj;
        return Objects.equals(id, other.id) && Objects.equals(metaData, other.metaData)
                && Objects.equals(data, other.data);
    }
}
