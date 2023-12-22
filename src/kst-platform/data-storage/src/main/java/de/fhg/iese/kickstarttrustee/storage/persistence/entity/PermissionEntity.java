package de.fhg.iese.kickstarttrustee.storage.persistence.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("data_storage_permission")
@CompoundIndexes({
		@CompoundIndex(name = "actorId_ownerId_dataItemType", def = "{'actorId': 1, 'ownerId': 1, 'dataItemType' : 1}")
})
public record PermissionEntity(@Id String id, String actorId, String ownerId, String dataItemType, Set<String> operations) {
    public PermissionEntity {
		operations = Set.copyOf(operations);
    }
}
