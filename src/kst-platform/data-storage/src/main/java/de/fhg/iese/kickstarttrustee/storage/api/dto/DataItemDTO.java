package de.fhg.iese.kickstarttrustee.storage.api.dto;

import java.util.Map;

import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record DataItemDTO(@NotEmpty String id, @NotNull MetaDataDTO metaData, Map<String, Object> data) {
    public DataItemDTO {
        data = Map.copyOf(data);
    }

    public DataItemDTO(DataItem dataItem) {
        this(dataItem.getId(), new MetaDataDTO(dataItem.getMetaData()), dataItem.getData());
    }
}
