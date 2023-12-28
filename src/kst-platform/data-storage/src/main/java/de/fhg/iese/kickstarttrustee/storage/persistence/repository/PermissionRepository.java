package de.fhg.iese.kickstarttrustee.storage.persistence.repository;

import de.fhg.iese.kickstarttrustee.storage.persistence.entity.PermissionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PermissionRepository extends ReactiveCrudRepository<PermissionEntity, String> {
	Mono<PermissionEntity> findFirstByActorIdAndOwnerIdAndDataItemType(String actorId, String ownerId, String dataItemType);

	Mono<Void> deleteByOwnerId(String ownerId);
}
