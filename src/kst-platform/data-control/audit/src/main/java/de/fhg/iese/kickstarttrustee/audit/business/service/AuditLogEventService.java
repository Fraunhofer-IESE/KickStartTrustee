package de.fhg.iese.kickstarttrustee.audit.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.audit.business.model.AuditLogEntry;
import de.fhg.iese.kickstarttrustee.audit.business.model.ConsentOperation;
import de.fhg.iese.kickstarttrustee.audit.business.model.DataOperation;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;
import de.fhg.iese.kickstarttrustee.event.consent.ConsentCreated;
import de.fhg.iese.kickstarttrustee.event.consent.ConsentRevoked;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemConsumed;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemCreated;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemDeleted;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuditLogEventService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(AuditLogEventService.class);

    private final IEventbus eventbus;
    private final AuditLogService auditLogService;

    public AuditLogEventService(IEventbus eventbus, AuditLogService auditLogService) {
        this.eventbus = eventbus;
        this.auditLogService = auditLogService;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(ConsentCreated.class, this::onConsentCreated);
        eventbus.registerConsumer(ConsentRevoked.class, this::onConsentRevoked);
        eventbus.registerConsumer(DataItemCreated.class, this::onDataItemCreated);
        eventbus.registerConsumer(DataItemConsumed.class, this::onDataItemConsumed);
        eventbus.registerConsumer(DataItemDeleted.class, this::onDataItemDeleted);
        eventbus.registerConsumer(OwnerAccountDeletedEvent.class, this::onOwnerAccountDeleted);
        log.info("Audit Log event listener setup completed");
    }

    private static String getEventType(Object event) {
        return event.getClass().getSimpleName();
    }

    public Flux<String> getAllEventTypes() {
        return Flux.just(ConsentCreated.class, ConsentRevoked.class, DataItemCreated.class, DataItemConsumed.class,
                DataItemDeleted.class).map(Class::getSimpleName);
    }

    private void onConsentCreated(ConsentCreated consentCreatedEvent) {
        Mono.just(consentCreatedEvent).map(event -> {
			final String eventType = AuditLogEventService.getEventType(event);
			final String operation = ConsentOperation.CREATE.name();
			final List<String> dataItemTypes = List.copyOf(event.consentPermissions().keySet());
			return new AuditLogEntry(eventType, event.ownerId(), event.id(), event.actorId(), operation, dataItemTypes);
		})
		.flatMap(auditLogService::addEntry).subscribe();
    }

    private void onConsentRevoked(ConsentRevoked consentRevokedEvent) {
        Mono.just(consentRevokedEvent).map(event -> {
            final String eventType = AuditLogEventService.getEventType(event);
            final String operation = ConsentOperation.REVOKE.name();
			final List<String> dataItemTypes = List.copyOf(event.consentPermissions().keySet());
            return new AuditLogEntry(eventType, event.ownerId(), event.id(), event.actorId(), operation, dataItemTypes);
        }).flatMap(auditLogService::addEntry).subscribe();
    }

    private void onDataItemCreated(DataItemCreated dataItemCreatedEvent) {
        Mono.just(dataItemCreatedEvent).map(event -> {
            final String eventType = AuditLogEventService.getEventType(event);
            final String operation = DataOperation.CREATE.name();
			final List<String> dataItemTypes = List.of(event.dataItemType());
            return new AuditLogEntry(eventType, event.ownerId(), event.id(), event.providerId(), operation, dataItemTypes);
        }).flatMap(auditLogService::addEntry).subscribe();
    }

    private void onDataItemConsumed(DataItemConsumed dataItemConsumedEvent) {
        Mono.just(dataItemConsumedEvent).map(event -> {
            final String eventType = AuditLogEventService.getEventType(event);
            final String operation = DataOperation.READ.name();
			final List<String> dataItemTypes = List.of(event.dataItemType());
            return new AuditLogEntry(eventType, event.ownerId(), event.id(), event.consumerId(), operation, dataItemTypes);
        }).flatMap(auditLogService::addEntry).subscribe();
    }

    private void onDataItemDeleted(DataItemDeleted dataItemDeletedEvent) {
        Mono.just(dataItemDeletedEvent).map(event -> {
            final String eventType = AuditLogEventService.getEventType(event);
            final String operation = DataOperation.DELETE.name();
			final List<String> dataItemTypes = List.of(event.dataItemType());
            return new AuditLogEntry(eventType, event.ownerId(), event.id(), event.actorId(), operation, dataItemTypes);
        }).flatMap(auditLogService::addEntry).subscribe();
    }

    private void onOwnerAccountDeleted(OwnerAccountDeletedEvent event) {
		final String accountId = event.accountId();
        auditLogService.deleteByOwnerId(accountId).subscribe();
    }
}
