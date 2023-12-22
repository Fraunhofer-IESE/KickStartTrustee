package de.fhg.iese.kickstarttrustee.storage.business.service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemDeleted;
import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;
import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
public class OwnerDataStorageService {
	private static final Logger log = LoggerFactory.getLogger(OwnerDataStorageService.class);

	private final IEventbus eventbus;
	private final DataItemsService dataItemsService;
	private final DataStorageUserService userService;
	private final MetaDataService metaDataService;
	private final StorageService storageService;

	public OwnerDataStorageService(IEventbus eventbus, DataItemsService dataItemsService, 
								DataStorageUserService userService, MetaDataService metaDataService, 
								StorageService storageService) {
		this.eventbus = eventbus;
		this.dataItemsService = dataItemsService;
		this.userService = userService;
		this.metaDataService = metaDataService;
		this.storageService = storageService;
	}

	public Flux<DataItem> getDataItemsForOwner(String ownerId, Optional<String> dataItemType, Optional<String> providerId) {
		final Flux<MetaData> metaData = metaDataService.getMetaDataForOwner(ownerId, dataItemType, providerId);
		return dataItemsService.toDataItems(metaData);
	}

	public Mono<DataItem> getOwnDataItemById(String id) {
		final Mono<MetaData> metaData = metaDataService.getOwnMetaDataById(id);
		return metaData.flatMap(dataItemsService::toDataItem);
	}

	public Mono<Void> deleteOwnDataItemById(String id) {
		return metaDataService.deleteOwnMetaDataById(id)
				.flatMap(metaData -> storageService.deleteDataById(id).then(Mono.just(metaData)))
				.doOnNext(metaData -> log.info("DataItem deleted: id={}", metaData.getId()))
				.zipWith(userService.getCurrentClientId())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(tuple -> {
					final MetaData metaData = tuple.getT1();
					final String actorId = tuple.getT2();
					final String ownerId = metaData.getOwnerId();
					final String dataItemType = metaData.getDataItemType();
                    eventbus.publishEventAsync(new DataItemDeleted(id, ownerId, actorId, dataItemType));
                })
				.then();
	}

	public Flux<DataItem> getOwnDataItems(Optional<String> dataItemType, Optional<String> providerId) {
		final Mono<String> currentUserId = userService.getCurrentUserId();
		return currentUserId.flatMapMany((ownerId) -> getDataItemsForOwner(ownerId, dataItemType, providerId));
	}
}
