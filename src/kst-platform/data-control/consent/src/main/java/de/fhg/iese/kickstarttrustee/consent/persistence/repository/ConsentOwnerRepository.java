package de.fhg.iese.kickstarttrustee.consent.persistence.repository;

import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentOwnerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ConsentOwnerRepository extends ReactiveCrudRepository<ConsentOwnerEntity, String> {
    Mono<Boolean> existsByUserId(String userId);
    Mono<Void> deleteByUserId(String userId);
}
