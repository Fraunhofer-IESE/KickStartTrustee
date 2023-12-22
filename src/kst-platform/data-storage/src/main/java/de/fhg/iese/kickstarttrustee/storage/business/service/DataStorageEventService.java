package de.fhg.iese.kickstarttrustee.storage.business.service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;
import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class DataStorageEventService implements InitializingBean {
    private final IEventbus eventbus;
    private final MetaDataService metaDataService;
	private final PermissionService permissionService;
    private final StorageService storageService;

    public DataStorageEventService(IEventbus eventbus, MetaDataService metaDataService, PermissionService permissionService, StorageService storageService) {
        this.eventbus = eventbus;
        this.metaDataService = metaDataService;
		this.permissionService = permissionService;
        this.storageService = storageService;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(OwnerAccountDeletedEvent.class, this::onOwnerAccountDeleted);
    }

    private void onOwnerAccountDeleted(OwnerAccountDeletedEvent event) {
        final String ownerId = event.accountId();
		permissionService.deletePermissionsByOwnerId(ownerId)
				.thenMany(metaDataService.deleteMetaDataByOwnerId(ownerId))
                .map(MetaData::getId)
                .flatMap(storageService::deleteDataById)
                .subscribe();
    }
}
