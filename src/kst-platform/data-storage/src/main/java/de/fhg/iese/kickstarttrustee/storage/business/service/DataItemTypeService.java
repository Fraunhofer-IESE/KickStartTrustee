package de.fhg.iese.kickstarttrustee.storage.business.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemTypeUpdated;
import reactor.core.publisher.Mono;

@Service
public class DataItemTypeService implements InitializingBean {
    private final IEventbus eventbus;
	private final Set<String> dataItemTypeIdCache;
    private final Map<String, String> schemaLocationCache;

    public DataItemTypeService(IEventbus eventbus) {
        this.eventbus = eventbus;
		this.dataItemTypeIdCache = ConcurrentHashMap.newKeySet();
        this.schemaLocationCache = new ConcurrentHashMap<>();
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(DataItemTypeUpdated.class, this::onDataItemTypeUpdated);
    }

    public Mono<Boolean> existsDataItemType(final String id) {
        return Mono.fromSupplier(() -> dataItemTypeIdCache.contains(id));
    }

	public Mono<String> getJsonSchemaLocation(final String id) {
		return  Mono.fromSupplier(() -> schemaLocationCache.get(id));
	}

    private void onDataItemTypeUpdated(DataItemTypeUpdated event) {
        final String id = event.id();
		final String jsonSchemaLocation = event.jsonSchemaLocation();
		if (jsonSchemaLocation != null) {
        	schemaLocationCache.put(id, jsonSchemaLocation);
		}
		dataItemTypeIdCache.add(id);
    }
}
