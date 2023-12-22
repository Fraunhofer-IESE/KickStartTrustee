package de.fhg.iese.kickstarttrustee.catalog.business.exception;

public class JsonSchemaNotFoundException extends RuntimeException {
	public JsonSchemaNotFoundException(String msg) {
		super(msg);
	}

	public static JsonSchemaNotFoundException forDataItemType(String dataItemType) {
		return new JsonSchemaNotFoundException("JSON Schema for data item type " + dataItemType + " not found!");
	}
}
