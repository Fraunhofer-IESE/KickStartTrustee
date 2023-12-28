package de.fhg.iese.kickstarttrustee.notification.business.service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.profile.OwnerProfileCreated;
import de.fhg.iese.kickstarttrustee.event.profile.OwnerProfileUpdated;
import de.fhg.iese.kickstarttrustee.notification.business.exception.NotificationSettingsNotFoundException;
import de.fhg.iese.kickstarttrustee.notification.business.model.NotificationSettings;
import de.fhg.iese.kickstarttrustee.notification.persistence.entity.NotificationSettingsEntity;
import de.fhg.iese.kickstarttrustee.notification.persistence.repository.NotificationSettingsRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationSettingsService implements InitializingBean {
    private final IEventbus eventbus;
    private final NotificationSettingsRepository repository;

    public NotificationSettingsService(IEventbus eventbus, NotificationSettingsRepository repository) {
        this.eventbus = eventbus;
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(OwnerProfileCreated.class, this::onOwnerProfileCreated);
        eventbus.registerConsumer(OwnerProfileUpdated.class, this::onOwnerProfileUpdated);
    }

    Mono<String> getOwnerMail(String ownerId) {
        return repository.findByUserId(ownerId).switchIfEmpty(Mono.error(NotificationSettingsNotFoundException::new))
                .map(NotificationSettingsEntity::email);
    }

    private void onOwnerProfileCreated(OwnerProfileCreated event) {
        Mono.just(new NotificationSettings(event.userId(), event.email()))
                .map(NotificationSettings::toEntity)
                .flatMap(repository::save)
                .subscribe();
    }

    private void onOwnerProfileUpdated(OwnerProfileUpdated event) {
        repository.findByUserId(event.userId())
                .map(entity -> {
                    final NotificationSettings settings = new NotificationSettings(entity);
                    settings.setEmail(event.email());
                    return settings;
                })
                .defaultIfEmpty(new NotificationSettings(event.userId(), event.email()))
                .map(NotificationSettings::toEntity)
                .flatMap(repository::save)
                .subscribe();
    }
}
