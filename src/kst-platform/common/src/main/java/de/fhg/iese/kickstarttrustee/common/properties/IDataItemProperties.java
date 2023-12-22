package de.fhg.iese.kickstarttrustee.common.properties;

import java.util.List;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItemType;

public interface IDataItemProperties {
    List<? extends IDataItemType> getTypes();
}
