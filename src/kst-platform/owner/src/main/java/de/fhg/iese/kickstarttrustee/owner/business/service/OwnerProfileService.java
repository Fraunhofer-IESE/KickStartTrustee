package de.fhg.iese.kickstarttrustee.owner.business.service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.profile.OwnerProfileCreated;
import de.fhg.iese.kickstarttrustee.event.profile.OwnerProfileUpdated;
import de.fhg.iese.kickstarttrustee.owner.business.exception.OwnerProfileAlreadyExistsException;
import de.fhg.iese.kickstarttrustee.owner.business.exception.OwnerProfileNotFoundException;
import de.fhg.iese.kickstarttrustee.owner.business.model.OwnerProfile;
import de.fhg.iese.kickstarttrustee.owner.persistence.entity.OwnerProfileEntity;
import de.fhg.iese.kickstarttrustee.owner.persistence.repositories.OwnerProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class OwnerProfileService {
    private static final Logger log = LoggerFactory.getLogger(OwnerProfileService.class);

    private final IEventbus eventbus;
    private final OwnerIdpService ownerIdpService;
    private final OwnerProfileRepository repository;

    public OwnerProfileService(IEventbus eventbus, OwnerIdpService ownerIdpService, OwnerProfileRepository repository) {
        this.eventbus = eventbus;
        this.ownerIdpService = ownerIdpService;
        this.repository = repository;
    }

    private Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext().mapNotNull(context -> {
            Authentication authentication = context.getAuthentication();
            return authentication.getPrincipal();
        }).cast(Jwt.class).map(JwtClaimAccessor::getSubject);
    }

    public Mono<OwnerProfile> getOwnProfile() {
        final Mono<String> userId = this.getCurrentUserId();
        final Mono<OwnerProfileEntity> entity = userId.flatMap(repository::findFirstByUserId);
        return entity.switchIfEmpty(Mono.error(OwnerProfileNotFoundException::new))
                .zipWith(ownerIdpService.getOwnIdpProfile())
                .map(t -> new OwnerProfile(t.getT1(), t.getT2()));
    }

    public Mono<OwnerProfile> getProfileById(String id) {
        final Mono<String> userId = this.getCurrentUserId();
        final Mono<OwnerProfileEntity> entity = repository.findById(id);
        return entity.filterWhen(e -> userId.map(uId -> e.userId().equals(uId)))
                .switchIfEmpty(Mono.error(OwnerProfileNotFoundException::new))
                .zipWith(ownerIdpService.getOwnIdpProfile())
                .map(t -> new OwnerProfile(t.getT1(), t.getT2()));
    }

    public Mono<OwnerProfile> createOwnProfile(String preferredLanguage) {
        final Mono<String> userId = this.getCurrentUserId();
        return userId.flatMap(uId -> {
                    final OwnerProfile ownerProfile = new OwnerProfile(uId, null, preferredLanguage);
                    return repository.save(ownerProfile.toEntity());
                })
                .onErrorMap(DuplicateKeyException.class, e -> new OwnerProfileAlreadyExistsException())
                .zipWith(ownerIdpService.getOwnIdpProfile())
                .map(t -> new OwnerProfile(t.getT1(), t.getT2()))
                .doOnNext(p -> log.info("User Profile created: id={}", p.getId().orElseThrow()))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(p -> {
                    final String id = p.getId().orElseThrow();
                    final String email = p.getIdpProfile().getEmail();
                    eventbus.publishEventAsync(new OwnerProfileCreated(id, p.getUserId(), email, preferredLanguage));
                });
    }

    public Mono<OwnerProfile> updateOwnProfile(String id, String preferredLanguage) {
        final Mono<OwnerProfile> ownProfile = this.getProfileById(id);
        return ownProfile.flatMap(profile -> {
                    profile.setPreferredLanguage(preferredLanguage);
                    return repository.save(profile.toEntity());
                })
                .zipWith(ownerIdpService.getOwnIdpProfile())
                .map(t -> new OwnerProfile(t.getT1(), t.getT2()))
                .doOnNext(p -> log.info("User Profile updated: id={}", p.getId().orElseThrow()))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(p -> {
                    final String userId = p.getUserId();
                    final String email = p.getIdpProfile().getEmail();
                    eventbus.publishEventAsync(new OwnerProfileUpdated(id, userId, email, preferredLanguage));
                });
    }

    public Mono<Void> deleteOwnProfile(String id) {
        final Mono<String> userId = this.getCurrentUserId();
        final Mono<OwnerProfileEntity> entity = repository.findById(id);
        return entity.filterWhen(e -> userId.map(uId -> e.userId().equals(uId)))
                .flatMap(e -> repository.deleteById(id))
                .doOnSuccess(ignored -> log.info("User Profile deleted: id={}", id));
    }
}
