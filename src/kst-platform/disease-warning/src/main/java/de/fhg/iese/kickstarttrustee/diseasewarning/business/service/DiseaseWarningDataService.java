package de.fhg.iese.kickstarttrustee.diseasewarning.business.service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;
import de.fhg.iese.kickstarttrustee.storage.business.service.ConsumerDataStorageService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DiseaseWarningDataService {
    private static final String FIELD_DATA_TYPE = "field_data";
    private static final String ID = "id";
    static final String CONFIDENTIAL_KEY = "confidential";
    static final String DISEASE_KEY = "disease";
    static final String FIELD_ID = "fieldId";
    static final String SEVERITY_KEY = "severity";
    static final String DATE_KEY = "date";

    private final ConsumerDataStorageService dataStorageService;

    public DiseaseWarningDataService(ConsumerDataStorageService dataStorageService) {
        this.dataStorageService = dataStorageService;
    }

    Optional<String> getOptionJSONString(Map<String, Object> data,String key) {
        return Optional.ofNullable(data.get(key)).map(object -> {
            if (object instanceof Document document) {
                return document.toJson();
            }
            return object.toString();
        });
    }

    Optional<String> getOptionalStringValue(Map<String, Object> data, String key) {
        return Optional.ofNullable(data.get(key)).map(Object::toString);
    }

    Optional<Boolean> getOptionalBooleanValue(Map<String, Object> data, String key) {
        return getOptionalStringValue(data, key).map(Boolean::parseBoolean);
    }

    boolean getOrDefaultBooleanValue(Map<String, Object> data, String key, Boolean defaultValue) {
        return getOptionalBooleanValue(data, key).orElse(defaultValue);
    }

    Optional<String> getDataId(final DataItem dataItem) {
        final Map<String, Object> data = dataItem.getData();
        if (data == null) {
            return Optional.empty();
        }
        return getOptionalStringValue(data, ID);
    }

    public Flux<DataItem> getFieldDataForOwner(String ownerId) {
        return dataStorageService.getDataItemsByDataItemTypeAndOwnerId(FIELD_DATA_TYPE, ownerId);
    }

    public Mono<DataItem> getFieldDataByOwnerIdAndFieldId(String ownerId, String fieldId) {
        return getFieldDataForOwner(ownerId).filter(dataItem -> {
            final Optional<String> optionalId = getDataId(dataItem);
            return optionalId.map(id -> Objects.equals(id, fieldId)).orElse(Boolean.FALSE);
        }).next();
    }
}
