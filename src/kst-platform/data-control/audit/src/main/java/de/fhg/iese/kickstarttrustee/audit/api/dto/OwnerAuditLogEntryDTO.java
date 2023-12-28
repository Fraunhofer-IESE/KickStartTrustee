package de.fhg.iese.kickstarttrustee.audit.api.dto;

import java.time.Instant;
import java.util.Collection;

import de.fhg.iese.kickstarttrustee.audit.business.model.AuditLogEntry;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record OwnerAuditLogEntryDTO(@NotNull String id, @NotNull Instant timestamp, @NotNull String eventType,
									@NotNull String ownerId, @NotNull String entityId,
									@NotNull String actorId, @NotNull String operation,
									@NotEmpty Collection<String> dataItemTypes) {

    public OwnerAuditLogEntryDTO(AuditLogEntry entry) {
        this(entry.getId(), entry.getTimestamp(), entry.getEventType(), entry.getOwnerId(), entry.getEntityId(),
                entry.getActorId(), entry.getOperation(), entry.getDataItemTypes());
    }
}
