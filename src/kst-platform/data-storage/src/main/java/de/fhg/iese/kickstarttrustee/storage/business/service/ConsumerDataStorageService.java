package de.fhg.iese.kickstarttrustee.storage.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemConsumed;
import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;
import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@Service
public class ConsumerDataStorageService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDataStorageService.class);

    private final IEventbus eventbus;
    private final MetaDataService metaDataService;
    private final DataItemsService dataItemsService;
    private final DataStorageUserService userService;

    public ConsumerDataStorageService(IEventbus eventbus, MetaDataService metaDataService,
									  DataItemsService dataItemsService, DataStorageUserService userService) {
        this.eventbus = eventbus;
        this.metaDataService = metaDataService;
        this.dataItemsService = dataItemsService;
        this.userService = userService;
    }

    private static DataItemConsumed createDataItemConsumedEvent(DataItem dataItem, String consumerId) {
        final MetaData meta = dataItem.getMetaData();
        return new DataItemConsumed(dataItem.getId(), consumerId, meta.getOwnerId(), meta.getDataItemType());
    }

    private Flux<DataItem> consumeDataItems(Mono<String> consumerIdMono, Flux<DataItem> dataItems) {
        return dataItems.doOnNext(d -> log.debug("DataItem consumed: id={}", d.getId()))
                .publishOn(Schedulers.boundedElastic())
                .zipWith(consumerIdMono.repeat())
                .doOnNext(tuple -> {
                    final DataItem dataItem = tuple.getT1();
                    final String consumerId = tuple.getT2();
                    final DataItemConsumed event = ConsumerDataStorageService.createDataItemConsumedEvent(dataItem,
                            consumerId);
                    eventbus.publishEventAsync(event);
                })
                .map(Tuple2::getT1);
    }

    private Flux<DataItem> consumeDataItems(final Flux<DataItem> dataItems) {
        return consumeDataItems(userService.getCurrentClientId(), dataItems);
    }

    public Flux<DataItem> consumeDataItemsByDataItemType(String dataItemType) {
        final Flux<MetaData> metaData = metaDataService.getMetaDataByDataItemType(dataItemType);
        final Flux<DataItem> dataItems = dataItemsService.getDataItemsForMetaData(metaData,
                metaDataService::hasConsumptionPermission);
        return this.consumeDataItems(dataItems);
    }

    public Flux<DataItem> consumeDataItemsByDataItemTypeAndOwnerId(String dataItemType, String ownerId) {
        final Flux<MetaData> metaData = metaDataService.getMetaDataByDataItemTypeAndOwnerId(dataItemType, ownerId);
        final Flux<DataItem> dataItems = dataItemsService.getDataItemsForMetaData(metaData,
                metaDataService::hasConsumptionPermission);
        return this.consumeDataItems(dataItems);
    }

    public Mono<DataItem> consumeDataItemById(String id) {
        final Mono<MetaData> metaData = metaDataService.getMetaDataById(id);
        final Mono<DataItem> dataItemMono = metaData.flatMap(dataItemsService::toDataItem);
        return dataItemMono.doOnNext(d -> log.debug("DataItem consumed: id={}", d.getId()))
                .publishOn(Schedulers.boundedElastic())
                .zipWith(userService.getCurrentClientId())
                .doOnNext(tuple -> {
                    final DataItem dataItem = tuple.getT1();
                    final String consumerId = tuple.getT2();
                    final DataItemConsumed event = ConsumerDataStorageService.createDataItemConsumedEvent(dataItem,
                            consumerId);
                    eventbus.publishEventAsync(event);
                })
                .map(Tuple2::getT1);
    }

    public Flux<DataItem> getDataItemsByDataItemTypeAndOwnerId(String dataItemType, String ownerId) {
        final Flux<MetaData> metaData = metaDataService.getMetaDataByDataItemTypeAndOwnerId(dataItemType, ownerId);
        return dataItemsService.toDataItems(metaData);
    }
}
