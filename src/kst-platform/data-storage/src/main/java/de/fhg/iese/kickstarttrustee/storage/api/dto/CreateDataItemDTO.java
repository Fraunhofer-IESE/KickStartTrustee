package de.fhg.iese.kickstarttrustee.storage.api.dto;

import javax.validation.constraints.NotNull;
import java.util.Map;

public record CreateDataItemDTO(@NotNull CreateMetaDataDTO metaData, Map<String, Object> data) {
    public CreateDataItemDTO {
        data = Map.copyOf(data);
    }
}
