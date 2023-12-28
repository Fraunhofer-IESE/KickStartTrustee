package de.fhg.iese.kickstarttrustee.event.dataitem;

import java.util.Map;

public record DataItemCreated(String id, String providerId, String ownerId, String dataItemType, Map<String, Object> data) {
    public DataItemCreated {
        data = Map.copyOf(data);
    }
}
