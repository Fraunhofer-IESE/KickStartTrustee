package de.fhg.iese.kickstarttrustee.common.business.model;

import java.util.Map;

public interface IDataItem {
    IMetadata metaData();
    Map<String, Object> data();
}
