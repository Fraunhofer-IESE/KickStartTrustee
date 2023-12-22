package de.fhg.iese.kickstarttrustee.storage.persistence.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("data_storage_metadata")
@CompoundIndexes({
		@CompoundIndex(name = "dataItemType_ownerId", def = "{'dataItemType' : 1, 'ownerId': 1}")
})
public record MetaDataEntity(@Id String id, @Indexed String dataItemType, String ownerId, String providerId, Instant createdAt) {
}
