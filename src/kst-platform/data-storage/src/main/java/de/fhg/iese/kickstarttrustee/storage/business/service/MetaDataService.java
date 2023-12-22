package de.fhg.iese.kickstarttrustee.storage.business.service;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.common.business.exception.DataItemTypeNotFoundException;
import de.fhg.iese.kickstarttrustee.storage.business.exception.DataItemNotFoundException;
import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;
import de.fhg.iese.kickstarttrustee.storage.persistence.repository.MetaDataRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MetaDataService {
    private static final Logger log = LoggerFactory.getLogger(MetaDataService.class);
	private static final Sort DEFAULT_SORT = Sort.by(Direction.DESC, "createdAt");

	private final DataItemTypeService dataItemTypeService;
    private final DataStorageUserService userService;
    private final PermissionService permissionService;
    private final MetaDataRepository repository;

    public MetaDataService(DataItemTypeService dataItemTypeService, DataStorageUserService userService,
            PermissionService permissionService, MetaDataRepository repository) {
        this.dataItemTypeService = dataItemTypeService;
        this.userService = userService;
        this.permissionService = permissionService;
        this.repository = repository;
    }

    private Mono<Boolean> checkIfDataItemTypeExists(String dataItemType) {
        return dataItemTypeService.existsDataItemType(dataItemType)
                .filter(b -> b).switchIfEmpty(Mono.error(new DataItemTypeNotFoundException()));
    }

    private Publisher<Boolean> isOwner(final MetaData metaData) {
        return userService.getCurrentUserId().map(userId -> Objects.equals(metaData.getOwnerId(), userId));
    }

    Publisher<Boolean> hasConsumptionPermission(final MetaData metaData) {
        return permissionService.hasConsumptionPermission(metaData.getOwnerId(), metaData.getDataItemType());
    }

    Flux<MetaData> getMetaDataByDataItemType(String dataItemType) {
        return checkIfDataItemTypeExists(dataItemType)
                .thenMany(repository.findByDataItemType(dataItemType))
                .map(MetaData::new);
    }

	Flux<MetaData> getMetaDataByOwnerId(String ownerId) {
		return repository.findByOwnerId(ownerId, DEFAULT_SORT).map(MetaData::new);
	}

    Flux<MetaData> getMetaDataByDataItemTypeAndOwnerId(String dataItemType, String ownerId) {
        return checkIfDataItemTypeExists(dataItemType)
                .thenMany(repository.findByDataItemTypeAndOwnerId(dataItemType, ownerId, DEFAULT_SORT))
                .map(MetaData::new);
    }

	Flux<MetaData> getMetaDataByOwnerIdAndProviderId(String ownerId, String providerId) {
		return repository.findByOwnerIdAndProviderId(ownerId, providerId, DEFAULT_SORT).map(MetaData::new);
	}

	Flux<MetaData> getMetaDataByDataItemTypeAndOwnerIdAndProviderId(String dataItemType, String ownerId, String providerId) {
		return checkIfDataItemTypeExists(dataItemType)
				.thenMany(repository.findByDataItemTypeAndOwnerIdAndProviderId(dataItemType, ownerId, providerId, DEFAULT_SORT))
				.map(MetaData::new);
	}

	Flux<MetaData> getMetaDataForOwner(String ownerId, Optional<String> optionalDataItemType,
									   Optional<String> optionalProviderId) {
		final boolean hasDataItemType = optionalDataItemType.isPresent();
		final boolean hasProviderId = optionalProviderId.isPresent();
		if (hasDataItemType && hasProviderId) {
			final String dataItemType = optionalDataItemType.get();
			final String providerId = optionalProviderId.get();
			return getMetaDataByDataItemTypeAndOwnerIdAndProviderId(dataItemType, ownerId, providerId);
		}
		if (hasDataItemType) {
			final String dataItemType = optionalDataItemType.get();
			return getMetaDataByDataItemTypeAndOwnerId(dataItemType, ownerId);
		}
		if (hasProviderId) {
			final String providerId = optionalProviderId.get();
			return getMetaDataByOwnerIdAndProviderId(ownerId, providerId);
		}
		return getMetaDataByOwnerId(ownerId);
	}

    public Mono<MetaData> createMetaData(String id, String dataItemType, String ownerId, String providerId) {
        return checkIfDataItemTypeExists(dataItemType)
                .then(Mono.just(new MetaData(id, dataItemType, ownerId, providerId)))
                .map(MetaData::toEntity)
                .flatMap(repository::save)
                .doOnNext(m -> log.info("Stored metaData: id={}", m.id()))
                .map(MetaData::new);
    }

    private Mono<MetaData> getMetaDataById(String id,
            Function<? super MetaData, ? extends Publisher<Boolean>> asyncPredicate) {
        final Mono<MetaData> metaData = repository.findById(id).map(MetaData::new);
        return metaData.filterWhen(asyncPredicate)
                .switchIfEmpty(Mono.error(new DataItemNotFoundException()));
    }

    public Mono<MetaData> getMetaDataById(String id) {
        return getMetaDataById(id, this::hasConsumptionPermission);
    }

    public Mono<MetaData> getOwnMetaDataById(String id) {
        return getMetaDataById(id, this::isOwner);
    }

	public Mono<MetaData> deleteOwnMetaDataById(String id) {
		final Mono<MetaData> metaData = this.getOwnMetaDataById(id);
		return metaData.flatMap(m -> repository.deleteById(m.getId()).then(Mono.just(m)));
	}

    public Flux<MetaData> deleteMetaDataByOwnerId(String ownerId) {
        return repository.deleteByOwnerId(ownerId).map(MetaData::new);
    }
}
