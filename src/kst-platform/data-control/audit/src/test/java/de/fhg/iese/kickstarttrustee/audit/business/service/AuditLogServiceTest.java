package de.fhg.iese.kickstarttrustee.audit.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.fhg.iese.kickstarttrustee.audit.business.exception.AuditLogEntryNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.exception.InvalidTimeRangeException;
import de.fhg.iese.kickstarttrustee.audit.business.model.AuditLogEntry;
import de.fhg.iese.kickstarttrustee.audit.persistence.repository.AuditLogEntryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {
    @Mock
    private AuditLogEntryRepository repositoryMock;

    @Test
    void test_createEntry() {
        lenient().when(repositoryMock.save(any())).thenAnswer(a -> Mono.just(a.getArgument(0)));

        final String ownerId = "ff6cb840-8626-4cd8-9eb5-ae8c7b825e46";
        final String entityId = "95ef1bd8-9664-4c6a-a319-bc59d92f2b1e";
        final String userId = "testprovider";
        final AuditLogEntry entry = new AuditLogEntry("ConsentCreated", ownerId, entityId, userId, "CREATE", List.of("location"));

        final AuditLogService auditLogService = new AuditLogService(repositoryMock);
        final Mono<AuditLogEntry> result = auditLogService.addEntry(entry);

        StepVerifier.create(result)
            .consumeNextWith(auditLogEntry -> {
                assertEquals("ConsentCreated", auditLogEntry.getEventType());
                assertNotNull(auditLogEntry.getTimestamp());
                assertEquals(ownerId, auditLogEntry.getOwnerId());
                assertEquals(entityId, auditLogEntry.getEntityId());
                assertEquals(userId, auditLogEntry.getActorId());
                assertEquals("CREATE", auditLogEntry.getOperation());
                assertIterableEquals(List.of("location"), auditLogEntry.getDataItemTypes());
            })
            .verifyComplete();
    }

    @Test
    void test_getMyEntries_invalidTimeRange() {
        final Instant begin = Instant.now();
        final Instant end = begin.minus(1L, ChronoUnit.DAYS);

        final AuditLogService auditLogService = new AuditLogService(repositoryMock);
        final Flux<AuditLogEntry> result = auditLogService.getMyEntries(begin, end, Optional.empty());

        StepVerifier.create(result)
				.verifyError(InvalidTimeRangeException.class);
    }

    @Test
    void test_getMyEntries_empty() {
        lenient().when(repositoryMock.findByMetaData_OwnerIdAndTimestampBetween(anyString(), any(), any(), any()))
                .thenReturn(Flux.empty());

        final Instant begin = Instant.now();
        final Instant end = begin.plus(1L, ChronoUnit.DAYS);

        final AuditLogService auditLogService = new AuditLogService(repositoryMock);
        final Flux<AuditLogEntry> result = auditLogService.getMyEntries(begin, end, Optional.empty());

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void test_getMyEntryById_empty() {
        lenient().when(repositoryMock.findById(anyString()))
                .thenReturn(Mono.empty());

        final AuditLogService auditLogService = new AuditLogService(repositoryMock);
        final Mono<AuditLogEntry> result = auditLogService.getMyEntryById("testid");

        StepVerifier.create(result)
                .verifyError(AuditLogEntryNotFoundException.class);
    }
}
