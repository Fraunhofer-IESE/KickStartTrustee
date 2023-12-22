package de.fhg.iese.kickstarttrustee.storage.business.service;

import de.fhg.iese.kickstarttrustee.common.business.exception.DataItemTypeNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.model.IDataItem;
import de.fhg.iese.kickstarttrustee.common.business.model.IMetadata;
import de.fhg.iese.kickstarttrustee.common.business.service.IReactiveDataItemValidator;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemCreated;
import de.fhg.iese.kickstarttrustee.storage.business.exception.DataItemInvalidException;
import de.fhg.iese.kickstarttrustee.storage.business.exception.DataItemNoPermissionException;
import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;
import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class ProviderDataStorageService {
    private static final Logger log = LoggerFactory.getLogger(ProviderDataStorageService.class);
    private static final String ACTION_CREATE = "create";

    private final IEventbus eventbus;
    private final List<IReactiveDataItemValidator> dataItemValidators;
    private final DataItemTypeService dataItemTypeService;
    private final DataStorageUserService dataStorageUserService;
	private final JsonSchemaValidator jsonSchemaValidator;
    private final MetaDataService metaDataService;
    private final PermissionService permissionService;
    private final StorageService storageService;

    public ProviderDataStorageService(IEventbus eventbus, List<IReactiveDataItemValidator> dataItemValidators,
                                      DataItemTypeService dataItemTypeService, DataStorageUserService dataStorageUserService,
                                      JsonSchemaValidator jsonSchemaValidator, MetaDataService metaDataService, 
									  PermissionService permissionService, StorageService storageService) {
        this.eventbus = eventbus;
        this.dataItemValidators = dataItemValidators;
        this.dataItemTypeService = dataItemTypeService;
        this.dataStorageUserService = dataStorageUserService;
		this.jsonSchemaValidator = jsonSchemaValidator;
        this.metaDataService = metaDataService;
        this.permissionService = permissionService;
        this.storageService = storageService;
    }

    private static Mono<Boolean> checkIf(Mono<Boolean> checkResult, Supplier<Throwable> throwableSupplier) {
        return checkResult.filter(b -> b).switchIfEmpty(Mono.error(throwableSupplier));
    }

    private static DataItemCreated createDataItemCreatedEvent(DataItem dataItem) {
        final String id = dataItem.getId();
        final MetaData meta = dataItem.getMetaData();
        final Map<String, Object> data = dataItem.getData();
        return new DataItemCreated(id, meta.getProviderId(), meta.getOwnerId(), meta.getDataItemType(), data);
    }

    private Mono<Boolean> isValidDataItem(IDataItem dataItem) {
        final String dataItemType = dataItem.metaData().dataItemType();
        final Flux<IReactiveDataItemValidator> validators = Flux.fromIterable(dataItemValidators);
        return validators.filter(v -> v.dataItemTypes().contains(dataItemType))
                .flatMap(v -> v.isValid(dataItem))
                .all(v -> v);
    }

    // only for service usage
    public Mono<DataItem> storeDataItem(String dataItemType, String ownerId, String providerId, Map<String, Object> data) {
        final String id = new ObjectId().toString();
        return storageService.storeData(id, data)
                .then(metaDataService.createMetaData(id, dataItemType, ownerId, providerId))
                .map(metaData -> new DataItem(id, metaData, data))
                .doOnNext(dataItem -> log.info("DataItem created: id={}, type={}", dataItem.getId(), dataItem.getMetaData().getDataItemType()))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(dataItem -> {
                    final DataItemCreated event = ProviderDataStorageService.createDataItemCreatedEvent(dataItem);
                    eventbus.publishEventAsync(event);
                });
    }

    public Mono<DataItem> provideDataItem(String dataItemType, String ownerId, Map<String, Object> data) {
        return checkIf(dataItemTypeService.existsDataItemType(dataItemType), () -> DataItemTypeNotFoundException.forDataItemType(dataItemType))
                .then(checkIf(permissionService.hasProvidingPermission(ownerId, dataItemType), () -> DataItemNoPermissionException.forAction(ACTION_CREATE)))
				.then(checkIf(jsonSchemaValidator.isValidDataItem(dataItemType, data), DataItemInvalidException::new))
                .then(Mono.fromSupplier(() -> new SimpleDataItem(dataItemType, ownerId, data)))
                .flatMap(dataItem -> checkIf(isValidDataItem(dataItem), DataItemInvalidException::new))
                .then(dataStorageUserService.getCurrentClientId())
                .flatMap(providerId -> storeDataItem(dataItemType, ownerId, providerId, data));
    }

    private record SimpleMetaData(String dataItemType, String ownerId) implements IMetadata {
    }

    private record SimpleDataItem(IMetadata metaData, Map<String, Object> data) implements IDataItem {
        public SimpleDataItem {
            data = Map.copyOf(data);
        }

        public SimpleDataItem(String dataItemType, String ownerId, Map<String, Object> data) {
            this(new SimpleMetaData(dataItemType, ownerId), data);
        }
    }
}
