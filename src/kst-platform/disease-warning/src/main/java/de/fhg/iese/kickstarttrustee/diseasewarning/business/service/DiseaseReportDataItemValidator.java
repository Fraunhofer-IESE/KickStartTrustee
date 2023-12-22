package de.fhg.iese.kickstarttrustee.diseasewarning.business.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.fhg.iese.kickstarttrustee.common.business.service.IReactiveDataItemValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItem;
import reactor.core.publisher.Mono;

@Service
public class DiseaseReportDataItemValidator implements IReactiveDataItemValidator {
    private static final Logger log = LoggerFactory.getLogger(DiseaseReportDataItemValidator.class);
    private static final List<String> DATA_ITEM_TYPES = List.of("disease_report");
    private final DiseaseWarningDataService dataService;

    public DiseaseReportDataItemValidator(DiseaseWarningDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public List<String> dataItemTypes() {
        return DiseaseReportDataItemValidator.DATA_ITEM_TYPES;
    }

    @Override
    public Mono<Boolean> isValid(IDataItem dataItem) {
        final Map<String, Object> data = dataItem.data();
        if (data == null) {
            log.warn("No data in dataItem");
            return Mono.just(Boolean.FALSE);
        }
        final Optional<String> optionalFieldId = dataService.getOptionalStringValue(data, DiseaseWarningDataService.FIELD_ID);
        if (optionalFieldId.isEmpty()) {
            log.warn("Missing fieldId attribute in dataItem");
            return Mono.just(Boolean.FALSE);
        }
        final String fieldId = optionalFieldId.get();
        final String ownerId = dataItem.metaData().ownerId();
        return dataService.getFieldDataForOwner(ownerId).any(fieldDataItem -> {
            final Optional<String> optionalId = dataService.getDataId(fieldDataItem);
            return optionalId.map(id -> Objects.equals(id, fieldId)).orElse(Boolean.FALSE);
        });
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }
}
