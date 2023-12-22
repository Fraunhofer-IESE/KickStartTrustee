package de.fhg.iese.kickstarttrustee.audit.business.service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.audit.business.exception.AuditLogEntryNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.exception.InvalidTimeRangeException;
import de.fhg.iese.kickstarttrustee.audit.business.model.AuditLogEntry;
import de.fhg.iese.kickstarttrustee.audit.persistence.entity.AuditLogEntryEntity;
import de.fhg.iese.kickstarttrustee.audit.persistence.repository.AuditLogEntryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuditLogService {
    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);
    private static final Sort DEFAULT_SORT = Sort.by(Direction.DESC, "timestamp");

    private final AuditLogEntryRepository repository;

    public AuditLogService(AuditLogEntryRepository repository) {
        this.repository = repository;
    }

    private Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext().mapNotNull(context -> {
            final Authentication authentication = context.getAuthentication();
            return authentication.getPrincipal();
        }).cast(Jwt.class).map(JwtClaimAccessor::getSubject);
    }

    public Mono<AuditLogEntry> addEntry(AuditLogEntry entry) {
        return Mono.just(entry)
                .map(AuditLogEntry::toEntity)
                .flatMap(repository::save)
                .map(AuditLogEntry::new)
                .doOnNext(c -> log.info("Audit log entry added: id={}", c.getId()));
    }

    public Mono<Void> deleteByOwnerId(String ownerId) {
        return repository.deleteByMetaData_OwnerId(ownerId)
                .doOnSuccess(ignored -> log.info("Audit log entries deleted for owner:  ownerId={}", ownerId))
                .doOnError(error -> log.warn("Audit log entries deletion for owner failed: ownerId={} error={}", ownerId,
                        error.getMessage()));
    }

    public Flux<AuditLogEntry> getMyEntries(Instant begin, Instant end, Optional<String> optionalEventType) {
        if (begin.isAfter(end)) {
            return Flux.error(new InvalidTimeRangeException());
        }
        final Mono<String> currentUserId = this.getCurrentUserId();
        return currentUserId.flatMapMany(userId -> {
            if (optionalEventType.isEmpty()) {
                return repository.findByMetaData_OwnerIdAndTimestampBetween(userId, begin, end, DEFAULT_SORT);
            }
            final String eventType = optionalEventType.get();
            return repository.findByMetaData_OwnerIdAndMetaData_EventTypeAndTimestampBetween(userId, eventType, begin,
                    end, DEFAULT_SORT);
        }).map(AuditLogEntry::new);
    }

    private Mono<Boolean> isOwnEntry(AuditLogEntryEntity entity) {
        final Mono<String> currentUserId = this.getCurrentUserId();
        return currentUserId.map(ownerId -> Objects.equals(entity.metaData().ownerId(), ownerId));
    }

    public Mono<AuditLogEntry> getMyEntryById(String id) {
        return repository.findById(id)
                .filterWhen(this::isOwnEntry)
                .switchIfEmpty(Mono.error(new AuditLogEntryNotFoundException()))
                .map(AuditLogEntry::new);
    }
}
