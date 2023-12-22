package de.fhg.iese.kickstarttrustee.consent.persistence.repository;

import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConsentRepository extends ReactiveCrudRepository<ConsentEntity, String> {
    Mono<Boolean> existsByActor_IdAndOwnerIdAndStatus(String actorId, String ownerId, String status);

    Mono<ConsentEntity> findFirstByIdAndOwnerId(String id, String ownerId);

    Flux<ConsentEntity> findAllByOwnerId(String ownerId, Sort sort);
    Flux<ConsentEntity> findAllByOwnerIdAndStatus(String ownerId, String status, Sort sort);

    Mono<ConsentEntity> findFirstByIdAndActor_Id(String id, String actorId);

    Flux<ConsentEntity> findAllByActor_Id(String actorId, Sort sort);
    Flux<ConsentEntity> findAllByActor_IdAndStatus(String actorId, String status, Sort sort);

    Mono<Void> deleteByOwnerId(String ownerId);
}
