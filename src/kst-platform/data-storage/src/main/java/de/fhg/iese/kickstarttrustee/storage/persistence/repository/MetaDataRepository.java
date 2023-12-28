package de.fhg.iese.kickstarttrustee.storage.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.domain.Sort;
import de.fhg.iese.kickstarttrustee.storage.persistence.entity.MetaDataEntity;
import reactor.core.publisher.Flux;

public interface MetaDataRepository extends ReactiveCrudRepository<MetaDataEntity, String> {
	Flux<MetaDataEntity> findByDataItemType(String dataItemType);
	Flux<MetaDataEntity> findByDataItemTypeAndOwnerId(String dataItemType, String ownerId, Sort sort);
	Flux<MetaDataEntity> findByOwnerIdAndProviderId(String ownerId, String providerId, Sort sort);
	Flux<MetaDataEntity> findByDataItemTypeAndOwnerIdAndProviderId(String dataItemType, String ownerId, String providerId, Sort sort);
	Flux<MetaDataEntity> findByOwnerId(String ownerId, Sort sort);
	Flux<MetaDataEntity> deleteByOwnerId(String ownerId);
}
