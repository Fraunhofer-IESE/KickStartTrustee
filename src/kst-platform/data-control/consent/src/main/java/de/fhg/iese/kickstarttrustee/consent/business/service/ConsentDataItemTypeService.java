package de.fhg.iese.kickstarttrustee.consent.business.service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemTypeUpdated;
import reactor.core.publisher.Mono;

@Service
public class ConsentDataItemTypeService implements InitializingBean {
    private final IEventbus eventbus;
    private final Set<String> dataItemTypeIdCache;

    public ConsentDataItemTypeService(IEventbus eventbus) {
        this.eventbus = eventbus;
        this.dataItemTypeIdCache = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(DataItemTypeUpdated.class, this::onDataItemTypeUpdated);
    }

    public Mono<Boolean> existsAllDataItemTypes(Collection<String> dataItemTypeIds) {
        return Mono.just(dataItemTypeIdCache.containsAll(dataItemTypeIds));
    }

    private void onDataItemTypeUpdated(DataItemTypeUpdated event) {
        final String id = event.id();
        dataItemTypeIdCache.remove(id);
        dataItemTypeIdCache.add(id);
    }
}
