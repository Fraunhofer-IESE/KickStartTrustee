package de.fhg.iese.kickstarttrustee.owner.persistence.repositories;

import de.fhg.iese.kickstarttrustee.owner.persistence.entity.OwnerProfileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface OwnerProfileRepository extends ReactiveCrudRepository<OwnerProfileEntity, String> {
    Mono<OwnerProfileEntity> findFirstByUserId(String userId);
}
