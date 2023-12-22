package de.fhg.iese.kickstarttrustee.properties;

import javax.validation.constraints.NotBlank;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItemType;

public record DataItemTypeProperties(@NotBlank String id, @NotBlank String name, String schema) implements IDataItemType {

    @Override
    public String jsonSchemaLocation() {
        return this.schema;
    }
}
