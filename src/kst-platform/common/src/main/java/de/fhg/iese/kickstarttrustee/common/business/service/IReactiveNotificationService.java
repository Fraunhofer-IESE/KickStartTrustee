package de.fhg.iese.kickstarttrustee.common.business.service;

import reactor.core.publisher.Mono;

public interface IReactiveNotificationService {
    Mono<Void> notifyOwner(String ownerId, String title, String message);
}
