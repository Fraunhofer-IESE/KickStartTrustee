package de.fhg.iese.kickstarttrustee.audit.persistence.repository;

import java.time.Instant;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import de.fhg.iese.kickstarttrustee.audit.persistence.entity.AuditLogEntryEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuditLogEntryRepository extends ReactiveCrudRepository<AuditLogEntryEntity, String> {
    Flux<AuditLogEntryEntity> findByMetaData_OwnerIdAndTimestampBetween(String ownerId, Instant from, Instant to, Sort sort);
    Flux<AuditLogEntryEntity> findByMetaData_OwnerIdAndMetaData_EventTypeAndTimestampBetween(String ownerId, String eventType, Instant from, Instant to, Sort sort);

    Mono<Void> deleteByMetaData_OwnerId(String ownerId);
}
