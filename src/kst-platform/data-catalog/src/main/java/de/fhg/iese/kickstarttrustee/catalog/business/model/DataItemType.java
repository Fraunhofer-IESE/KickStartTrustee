package de.fhg.iese.kickstarttrustee.catalog.business.model;

import java.util.Objects;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItemType;

public class DataItemType implements IDataItemType {
    private final String id;
    private final String name;
	private final String jsonSchemaLocation;

    public DataItemType(String id, String name, String jsonSchemaLocation) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
		this.jsonSchemaLocation = jsonSchemaLocation;
	}

	public DataItemType(String id, String name) {
		this(id, name, null);
	}

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

	public String jsonSchemaLocation() {
		return jsonSchemaLocation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, jsonSchemaLocation);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DataItemType that = (DataItemType) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name)
				&& Objects.equals(jsonSchemaLocation, that.jsonSchemaLocation);
	}
}
