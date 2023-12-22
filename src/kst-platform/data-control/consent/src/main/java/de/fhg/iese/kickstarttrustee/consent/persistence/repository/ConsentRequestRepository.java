package de.fhg.iese.kickstarttrustee.consent.persistence.repository;

import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentRequestEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConsentRequestRepository extends ReactiveCrudRepository<ConsentRequestEntity, String> {
	Mono<Boolean> existsByRequester_IdAndOwnerIdAndStatus(String requesterId, String ownerId, String status);

	Flux<ConsentRequestEntity> findAllByRequester_Id(String requesterId, Sort sort);

	Flux<ConsentRequestEntity> findAllByRequester_IdAndStatus(String requesterId, String status, Sort sort);

	Flux<ConsentRequestEntity> findAllByOwnerId(String ownerId, Sort sort);

	Mono<ConsentRequestEntity> findFirstByIdAndRequester_Id(String id, String requesterId);

	Mono<Void> deleteByOwnerId(String ownerId);
}
