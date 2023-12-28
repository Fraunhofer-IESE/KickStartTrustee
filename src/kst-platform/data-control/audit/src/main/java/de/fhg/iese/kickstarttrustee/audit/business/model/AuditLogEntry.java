package de.fhg.iese.kickstarttrustee.audit.business.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import de.fhg.iese.kickstarttrustee.audit.persistence.entity.AuditLogEntryEntity;
import de.fhg.iese.kickstarttrustee.audit.persistence.entity.AuditLogEntryMetadata;

public class AuditLogEntry {
    private final String id;
    private final Instant timestamp;
    private final String eventType;
    private final String ownerId;
    private final String entityId;
    private final String actorId;
    private final String operation;
    private final List<String> dataItemTypes;

    public AuditLogEntry(String eventType, String ownerId, String entityId, 
            String actorId, String operation, List<String> dataItemTypes) {
        this.id = null;
        this.timestamp = Instant.now();
        this.eventType = eventType;
        this.entityId = entityId;
        this.ownerId = ownerId;
        this.actorId = actorId;
        this.operation = operation;
        this.dataItemTypes = List.copyOf(dataItemTypes);
    }

    public AuditLogEntry(AuditLogEntryEntity entity) {
        this.id = entity.id();
        this.timestamp = entity.timestamp();
        this.eventType = entity.metaData().eventType();
        this.ownerId = entity.metaData().ownerId();
        this.entityId = entity.entityId();
        this.actorId = entity.actorId();
        this.operation = entity.operation();
        this.dataItemTypes = entity.dataItems();
    }

    public String getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getActorId() {
        return actorId;
    }

    public String getOperation() {
        return operation;
    }

    public List<String> getDataItemTypes() {
        return dataItemTypes;
    }

    public AuditLogEntryEntity toEntity() {
        final AuditLogEntryMetadata metadata = new AuditLogEntryMetadata(ownerId, eventType);
        return new AuditLogEntryEntity(id, timestamp, metadata, entityId, actorId, operation, dataItemTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, eventType, ownerId, entityId, actorId, operation, dataItemTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AuditLogEntry))
            return false;
        AuditLogEntry other = (AuditLogEntry) obj;
        return Objects.equals(id, other.id) && Objects.equals(timestamp, other.timestamp)
                && Objects.equals(eventType, other.eventType) && Objects.equals(ownerId, other.ownerId)
                && Objects.equals(entityId, other.entityId) && Objects.equals(actorId, other.actorId)
                && Objects.equals(operation, other.operation) && Objects.equals(dataItemTypes, other.dataItemTypes);
    }
}
