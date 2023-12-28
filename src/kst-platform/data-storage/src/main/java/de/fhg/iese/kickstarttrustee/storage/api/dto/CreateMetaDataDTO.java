package de.fhg.iese.kickstarttrustee.storage.api.dto;

import javax.validation.constraints.NotEmpty;

public record CreateMetaDataDTO(@NotEmpty String dataItemType, @NotEmpty String ownerId) {
}
