package de.fhg.iese.kickstarttrustee.catalog.business.service;

import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.catalog.business.exception.JsonSchemaNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.exception.DataItemTypeNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.model.IDataItemType;
import de.fhg.iese.kickstarttrustee.common.properties.IDataItemProperties;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemTypeUpdated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataItemTypesService {
    private static final Logger log = LoggerFactory.getLogger(DataItemTypesService.class);

    private final IEventbus eventbus;
	private final ResourceLoader resourceLoader;
	private final List<? extends IDataItemType> supportedDataItemTypes;

	public DataItemTypesService(IEventbus eventbus, ResourceLoader resourceLoader, IDataItemProperties dataItemProperties) {
        this.eventbus = eventbus;
		this.resourceLoader = resourceLoader;
		this.supportedDataItemTypes = dataItemProperties.getTypes();
    }

	private static DataItemTypeUpdated createDataItemTypeUpdated(final IDataItemType dataItemType) {
		final String id = dataItemType.id();
		final String name = dataItemType.name();
		final String jsonSchemaLocation = dataItemType.jsonSchemaLocation();
		return new DataItemTypeUpdated(id, name, jsonSchemaLocation);
	}

    // This gets called after all beans have been initialized, so all eventbus
    // consumer are registered here already
    @EventListener
    public void afterContextRefreshed(ContextRefreshedEvent event) {
        supportedDataItemTypes.forEach(dataItemType -> {
			eventbus.publishEventAsync(DataItemTypesService.createDataItemTypeUpdated(dataItemType));
			log.info("Registered data item type: id={}, name={}", dataItemType.id(), dataItemType.name());
		});
    }

    public Flux<IDataItemType> getSupportedDataItemTypes() {
        return Flux.fromIterable(supportedDataItemTypes);
    }

	public Mono<Resource> getSchemaForDataItemType(final String id) {
		return getSupportedDataItemTypes()
				.filter(d -> Objects.equals(d.id(), id))
				.next()
				.switchIfEmpty(Mono.error(() -> DataItemTypeNotFoundException.forDataItemType(id)))
				.flatMap(dataItemType -> {
					final String jsonSchemaLocation = dataItemType.jsonSchemaLocation();
					if (jsonSchemaLocation == null) {
						return Mono.error(() -> JsonSchemaNotFoundException.forDataItemType(id));
					}
					return Mono.just(resourceLoader.getResource(jsonSchemaLocation));
				});
	}
}
