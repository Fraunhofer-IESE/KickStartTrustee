package de.fhg.iese.kickstarttrustee.catalog.api.dto;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItemType;

import javax.validation.constraints.NotNull;

public record DataItemTypeDTO(@NotNull String id, @NotNull String name) {
    public DataItemTypeDTO(IDataItemType dataItemType) {
        this(dataItemType.id(), dataItemType.name());
    }
}
