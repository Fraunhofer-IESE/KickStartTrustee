package de.fhg.iese.kickstarttrustee.storage.business.model;

import java.time.Instant;
import java.util.Objects;

import de.fhg.iese.kickstarttrustee.storage.persistence.entity.MetaDataEntity;

public class MetaData {
    private final String id;
    private final String dataItemType;
    private final String ownerId;
    private final String providerId;
    private final Instant createdAt;

    public MetaData(String id, String dataItemType, String ownerId, String providerId) {
        this.id = id;
        this.dataItemType = Objects.requireNonNull(dataItemType);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.providerId = Objects.requireNonNull(providerId);
        this.createdAt = Instant.now();
    }

    public MetaData(MetaDataEntity entity) {
        this.id = entity.id();
        this.dataItemType = entity.dataItemType();
        this.ownerId = entity.ownerId();
        this.providerId = entity.providerId();
        this.createdAt = entity.createdAt();
    }

    public String getId() {
        return id;
    }

    public String getDataItemType() {
        return dataItemType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public MetaDataEntity toEntity() {
        return new MetaDataEntity(id, dataItemType, ownerId, providerId, createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataItemType, ownerId, providerId, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MetaData))
            return false;
        MetaData other = (MetaData) obj;
        return Objects.equals(id, other.id) && Objects.equals(dataItemType, other.dataItemType)
                && Objects.equals(ownerId, other.ownerId) && Objects.equals(providerId, other.providerId)
                && Objects.equals(createdAt, other.createdAt);
    }
}
