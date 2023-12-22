package de.fhg.iese.kickstarttrustee.diseasewarning.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.diseasewarning.business.exception.DiseaseWarningSettingsAlreadyExists;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.exception.DiseaseWarningSettingsNotFound;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.model.DiseaseWarningSettings;
import de.fhg.iese.kickstarttrustee.diseasewarning.persistence.repository.DiseaseWarningSettingsRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DiseaseWarningSettingsService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(DiseaseWarningSettingsService.class);

    private final IEventbus eventbus;
    private final DiseaseWarningUserService diseaseWarningUserService;
    private final DiseaseWarningSettingsRepository repository;

    public DiseaseWarningSettingsService(IEventbus eventbus, DiseaseWarningUserService diseaseWarningUserService,
            DiseaseWarningSettingsRepository repository) {
        this.eventbus = eventbus;
        this.diseaseWarningUserService = diseaseWarningUserService;
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(OwnerAccountDeletedEvent.class, e -> this.deleteSettingsByOwnerId(e.accountId()));
    }

    public Flux<DiseaseWarningSettings> getAllWithDiseaseWarningsEnabled() {
        return repository.findAllByIsDiseaseWarningCreationEnabled(true).map(DiseaseWarningSettings::new);
    }

    private Mono<Boolean> notExistsForOwnerId(String ownerId) {
        return repository.existsByOwnerId(ownerId).map(exists -> Boolean.TRUE.equals(exists) ? Boolean.FALSE : Boolean.TRUE);
    }

    public Mono<DiseaseWarningSettings> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(DiseaseWarningSettingsNotFound::new))
                .map(DiseaseWarningSettings::new);
    }

    public Mono<DiseaseWarningSettings> getByOwnerId(String ownerId) {
        return repository.findByOwnerId(ownerId)
                .switchIfEmpty(Mono.error(DiseaseWarningSettingsNotFound::new))
                .map(DiseaseWarningSettings::new);
    }

    public Mono<DiseaseWarningSettings> getOwnSettings() {
        return diseaseWarningUserService.getCurrentUserId().flatMap(this::getByOwnerId);
    }

    private Mono<DiseaseWarningSettings> save(Mono<DiseaseWarningSettings> settingsMono) {
        return settingsMono.map(DiseaseWarningSettings::toEntity)
                .flatMap(repository::save)
                .map(DiseaseWarningSettings::new)
                .doOnNext(settings -> log.info("Disease warning settings saved: id={}", settings.getId()));
    }

    public Mono<DiseaseWarningSettings> create(boolean isDiseaseReportProcessingEnabled,
            boolean isDiseaseWarningCreationEnabled, boolean isEmailNotificationEnabled) {
        final Mono<String> currentUserId = diseaseWarningUserService.getCurrentUserId();
        final Mono<DiseaseWarningSettings> settingsMono = currentUserId
                .filterWhen(this::notExistsForOwnerId)
                .switchIfEmpty(Mono.error(DiseaseWarningSettingsAlreadyExists::new))
                .map(ownerId -> new DiseaseWarningSettings(ownerId, isDiseaseReportProcessingEnabled,
                        isDiseaseWarningCreationEnabled, isEmailNotificationEnabled));
        return save(settingsMono);
    }

    public Mono<DiseaseWarningSettings> update(String id, boolean isDiseaseReportProcessingEnabled,
            boolean isDiseaseWarningCreationEnabled, boolean isEmailNotificationEnabled) {
        final Mono<DiseaseWarningSettings> settingsMono = getById(id).map(settings -> {
            settings.setDiseaseReportProcessingEnabled(isDiseaseReportProcessingEnabled);
            settings.setDiseaseWarningCreationEnabled(isDiseaseWarningCreationEnabled);
            settings.setEmailNotificationEnabled(isEmailNotificationEnabled);
            return settings;
        });
        return save(settingsMono);
    }

    private void deleteSettingsByOwnerId(String ownerId) {
        repository.deleteByOwnerId(ownerId)
                .doOnSuccess(ignored -> log.info("Disease Warnings Settings deleted for owner:  ownerId={}", ownerId))
                .doOnError(e -> log.warn("Disease Warnings Settings deletion for owner failed: ownerId={} error={}", ownerId,
                        e.getMessage()))
                .subscribe();
    }
}
