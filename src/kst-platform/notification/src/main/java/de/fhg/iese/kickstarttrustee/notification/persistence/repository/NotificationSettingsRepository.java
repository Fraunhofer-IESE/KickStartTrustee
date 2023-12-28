package de.fhg.iese.kickstarttrustee.notification.persistence.repository;

import de.fhg.iese.kickstarttrustee.notification.persistence.entity.NotificationSettingsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface NotificationSettingsRepository extends ReactiveCrudRepository<NotificationSettingsEntity, String> {
    Mono<NotificationSettingsEntity> findByUserId(String userId);
}
