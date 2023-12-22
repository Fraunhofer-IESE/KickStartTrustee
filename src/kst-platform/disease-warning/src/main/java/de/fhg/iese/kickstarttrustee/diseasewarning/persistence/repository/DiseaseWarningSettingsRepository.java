package de.fhg.iese.kickstarttrustee.diseasewarning.persistence.repository;

import de.fhg.iese.kickstarttrustee.diseasewarning.persistence.entity.DiseaseWarningSettingsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DiseaseWarningSettingsRepository extends ReactiveCrudRepository<DiseaseWarningSettingsEntity, String> {
    Mono<Boolean> existsByOwnerId(String ownerId);
    Mono<DiseaseWarningSettingsEntity> findByOwnerId(String ownerId);
    Flux<DiseaseWarningSettingsEntity> findAllByIsDiseaseWarningCreationEnabled(boolean diseaseWarningCreationEnabled);
    Mono<Long> deleteByOwnerId(String ownerId);
}
