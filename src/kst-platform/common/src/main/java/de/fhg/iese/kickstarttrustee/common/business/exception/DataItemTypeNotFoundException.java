package de.fhg.iese.kickstarttrustee.common.business.exception;

public class DataItemTypeNotFoundException extends RuntimeException {
    public DataItemTypeNotFoundException() {
        super("This data item type is not supported!");
    }

    public DataItemTypeNotFoundException(String msg) {
        super(msg);
    }

    public static DataItemTypeNotFoundException forDataItemType(String dataItemType) {
        return new DataItemTypeNotFoundException("Data item type " + dataItemType + " is not supported");
    }
}
