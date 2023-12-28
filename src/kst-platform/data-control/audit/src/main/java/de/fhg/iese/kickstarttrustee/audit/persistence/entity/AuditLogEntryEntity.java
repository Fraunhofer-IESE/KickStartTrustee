package de.fhg.iese.kickstarttrustee.audit.persistence.entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.TimeSeries;

@TimeSeries(collection="audit_log", timeField = "timestamp", metaField = "metaData")
public record AuditLogEntryEntity(String id, Instant timestamp, AuditLogEntryMetadata metaData, String entityId, String actorId, String operation, List<String> dataItems) {
}
