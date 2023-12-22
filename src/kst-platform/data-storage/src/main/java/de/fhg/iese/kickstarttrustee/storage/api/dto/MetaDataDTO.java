package de.fhg.iese.kickstarttrustee.storage.api.dto;

import java.time.Instant;

import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record MetaDataDTO(@NotEmpty String dataItemType, @NotEmpty String ownerId, String providerId, @NotNull Instant createdAt) {
    public MetaDataDTO(MetaData metaData) {
        this(metaData.getDataItemType(), metaData.getOwnerId(), metaData.getProviderId(), metaData.getCreatedAt());
    }
}
