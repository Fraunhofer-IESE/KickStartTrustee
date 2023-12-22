package de.fhg.iese.kickstarttrustee.diseasewarning.business.service;

import de.fhg.iese.kickstarttrustee.common.business.service.IReactiveNotificationService;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.model.DiseaseWarning;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.model.DiseaseWarningSettings;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.dataitem.DataItemCreated;
import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;
import de.fhg.iese.kickstarttrustee.storage.business.service.ProviderDataStorageService;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DiseaseWarningService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(DiseaseWarningService.class);
    private static final String PROVIDER_ID = "disease-warning-service";
    // 10 Kilometer
    private static final int WARNING_DISTANCE = 10;
    private static final String DISEASE_REPORT_TYPE = "disease_report";
    private static final String DISEASE_WARNING_TYPE = "disease_warning";
    private static final String BOUNDARIES_KEY = "boundaries";

    private final IEventbus eventbus;
    private final DiseaseWarningDataService dataService;
    private final DiseaseWarningSettingsService settingsService;
    private final ProviderDataStorageService dataStorageService;
    private final IReactiveNotificationService notificationService;

    public DiseaseWarningService(IEventbus eventbus, DiseaseWarningDataService dataService, DiseaseWarningSettingsService settingsService, ProviderDataStorageService dataStorageService, IReactiveNotificationService notificationService) {
        this.eventbus = eventbus;
        this.dataService = dataService;
        this.settingsService = settingsService;
        this.dataStorageService = dataStorageService;
        this.notificationService = notificationService;
    }

    private boolean isDiseaseReportType(final DataItemCreated event) {
        return DISEASE_REPORT_TYPE.equals(event.dataItemType());
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(DataItemCreated.class, this::isDiseaseReportType, this::onDataItemCreated);
    }

    private Optional<SimpleFeature> getBoundaries(DataItem field) {
        final Map<String, Object> fieldData = field.getData();
        final Optional<String> optionalGeometry = dataService.getOptionJSONString(fieldData, BOUNDARIES_KEY);
        return optionalGeometry.flatMap(json -> {
            try (final GeoJSONReader reader = new GeoJSONReader(json)) {
                return Optional.of(reader.getFeature());
            } catch (IOException ex) {
                log.warn("Could not read GeoJSON", ex);
            }
            return Optional.empty();
        });
    }

    private boolean isInWarningDistance(DataItem field, Feature reportBoundaries) {
        final Optional<SimpleFeature> optionalBoundaries = getBoundaries(field);
        if (optionalBoundaries.isEmpty()) {
            log.warn("Field without boundaries found: id={}", field.getId());
            return false;
        }
        final SimpleFeature fieldFeature = optionalBoundaries.get();
        final BoundingBox fieldBoundingBox = fieldFeature.getBounds();
        final Point fieldBoundingBoxCenter = JTS.toGeometry(fieldBoundingBox).getCentroid();
        final BoundingBox reportBoundingBox = reportBoundaries.getBounds();
        final Point reportBoundingBoxCenter = JTS.toGeometry(reportBoundingBox).getCentroid();
        final GeodeticCalculator gc = new GeodeticCalculator();
        gc.setStartingGeographicPoint(fieldBoundingBoxCenter.getX(), fieldBoundingBoxCenter.getY());
        gc.setDestinationGeographicPoint(reportBoundingBoxCenter.getX(), reportBoundingBoxCenter.getY());
        final double distanceInMeters = gc.getOrthodromicDistance();
        final int distanceInKilometers = ((int) distanceInMeters) / 1000;
        return distanceInKilometers < WARNING_DISTANCE;
    }

    private Flux<DataItem> getFieldsInRange(final String ownerId, Mono<Feature> boundaries) {
        final Flux<DataItem> fields = dataService.getFieldDataForOwner(ownerId);
        return fields.zipWith(boundaries.repeat()).filter(tuple -> {
                    final DataItem field = tuple.getT1();
                    final Feature reportBoundaries = tuple.getT2();
                    return isInWarningDistance(field, reportBoundaries);
                }).map(Tuple2::getT1);
    }

    private Mono<List<String>> getFieldIdList(final Flux<DataItem> fields) {
        return fields.map(field -> {
            final Optional<String> optionalFieldId = dataService.getDataId(field);
            if (optionalFieldId.isEmpty()) {
                log.warn("Field without id in payload, ignoring dataItem: id={}", field.getId());
            }
            return optionalFieldId;
        }).filter(Optional::isPresent).map(Optional::get).collectList();
    }

    private Mono<DataItem> createDiseaseWarning(final String ownerId, final String disease, final String severity, List<String> endangeredFieldIds, final String date) {
        final DiseaseWarning diseaseWarning = new DiseaseWarning(disease, severity, endangeredFieldIds, date);
        return dataStorageService.storeDataItem(DISEASE_WARNING_TYPE, ownerId, PROVIDER_ID, diseaseWarning.asMap());
    }

    private Mono<Void> notifyFarmer(String ownerId, String disease) {
        final String title = "Disease Warning for " + disease;
        final String message = "Hello,\n"
                + "Some of your fields are in danger of " + disease + ". "
                + "Please check your disease warnings in our FMIS for details.";
        return notificationService.notifyOwner(ownerId, title, message);
    }

    private void warnOtherFarmers(final String reporterId, final String reportFieldId, final String disease, final String severity, final String date) {
        final Mono<DataItem> reportField = dataService.getFieldDataByOwnerIdAndFieldId(reporterId, reportFieldId);
        final Mono<Feature> boundaries = reportField.flatMap(f -> Mono.justOrEmpty(getBoundaries(f)));
        final Flux<DiseaseWarningSettings> diseaseWarningSettings = settingsService.getAllWithDiseaseWarningsEnabled();
        diseaseWarningSettings.flatMap(settings -> {
            final String ownerId = settings.getOwnerId();
            if (Objects.equals(reporterId, ownerId)) {
                return Mono.empty();
            }
            final Flux<DataItem> fieldsInRange = getFieldsInRange(ownerId, boundaries);
            final Mono<List<String>> endangeredFieldIds = getFieldIdList(fieldsInRange);
            final Mono<DataItem> diseaseWarning = endangeredFieldIds.flatMap(fieldIds -> {
                if (fieldIds.isEmpty()) {
                    log.info("No endangered fields for owner: ownerId={}", ownerId);
                    return Mono.empty();
                }
                return createDiseaseWarning(ownerId, disease, severity, fieldIds, date);
            });
            if (settings.isEmailNotificationEnabled()) {
                return diseaseWarning.flatMap(dataItem -> notifyFarmer(ownerId, disease));
            }
            return diseaseWarning.then();
        }).subscribe();
    }

    private void processDiseaseReportDataItem(final String id, final String ownerId, final Map<String, Object> data) {
        final boolean isConfidential = dataService.getOrDefaultBooleanValue(data, DiseaseWarningDataService.CONFIDENTIAL_KEY, Boolean.TRUE);
        if (isConfidential) {
            log.info("Disease report is confidential and therefore not considered: id={}", id);
            return;
        }
        final Optional<String> optionalFieldId = dataService.getOptionalStringValue(data, DiseaseWarningDataService.FIELD_ID);
        if (optionalFieldId.isEmpty()) {
            log.warn("Missing fieldId in disease report: id={}", id);
            return;
        }
        final Optional<String> optionalDisease = dataService.getOptionalStringValue(data, DiseaseWarningDataService.DISEASE_KEY);
        if (optionalDisease.isEmpty()) {
            log.warn("Missing disease in disease report: id={}", id);
            return;
        }
        final Optional<String> optionalSeverity = dataService.getOptionalStringValue(data, DiseaseWarningDataService.SEVERITY_KEY);
        if (optionalSeverity.isEmpty()) {
            log.warn("Missing severity in disease report: id={}", id);
            return;
        }
        final Optional<String> optionalDate = dataService.getOptionalStringValue(data, DiseaseWarningDataService.DATE_KEY);
        if (optionalDate.isEmpty()) {
            log.warn("Missing date in disease report: id={}", id);
            return;
        }
        final String fieldId = optionalFieldId.get();
        final String disease = optionalDisease.get();
        final String severity = optionalSeverity.get();
        final String date = optionalDate.get();
        warnOtherFarmers(ownerId, fieldId, disease, severity, date);
    }

    private void onDataItemCreated(DataItemCreated event) {
        final String id = event.id();
        final String ownerId = event.ownerId();
        final Mono<DiseaseWarningSettings> settingsMono = settingsService.getByOwnerId(ownerId);
        settingsMono.subscribe(settings -> {
            if (!settings.isDiseaseReportProcessingEnabled()) {
                log.info("Disease report processing disabled, ignoring disease report: reportId={}", id);
                return;
            }
            final Map<String, Object> data = event.data();
            processDiseaseReportDataItem(id, ownerId, data);
        }, error -> log.info("No disease warning settings for user, ignoring disease report: userId={} reportId={}", ownerId, id));
    }
}
