package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.common.business.exception.DataItemTypeNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.exception.DataOwnerNotFoundException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.ActiveConsentExistsException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentRequestModifiedException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentRequestNoPermission;
import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentRequestNotFoundException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.PendingConsentRequestExistsException;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOperationType;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPermissions;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class ConsentRequestService {
    private static final Logger log = LoggerFactory.getLogger(ConsentRequestService.class);
    private static final Sort DEFAULT_SORT = Sort.by(Direction.DESC, "createdAt");

    private final ConsentDataItemTypeService dataItemTypeService;
    private final ConsentOwnerService consentOwnerService;
    private final ConsentService consentService;
    private final ConsentUserService consentUserService;
    private final ConsentRequestRepository consentRequestRepository;

    public ConsentRequestService(ConsentDataItemTypeService dataItemTypeService,
            ConsentOwnerService consentOwnerService, ConsentService consentService,
            ConsentUserService consentUserService, ConsentRequestRepository consentRequestRepository) {
        this.dataItemTypeService = dataItemTypeService;
        this.consentOwnerService = consentOwnerService;
        this.consentService = consentService;
        this.consentUserService = consentUserService;
        this.consentRequestRepository = consentRequestRepository;
    }

	private Mono<Boolean> checkIf(Mono<Boolean> conditionMono, Supplier<Throwable> errorSupplier) {
		return conditionMono.filter(b -> b).switchIfEmpty(Mono.error(errorSupplier));
	}

    private Mono<Boolean> isOwnerUser(ConsentRequest consentRequest) {
        final Mono<String> userIdMono = consentUserService.getCurrentUserId();
        return userIdMono.map(userId -> Objects.equals(userId, consentRequest.getOwnerId()));
    }

    private Flux<ConsentRequest> getConsentRequestsByStatus(Optional<ConsentRequestStatus> optionalStatus) {
        final Mono<String> currentRequesterId = consentUserService.getCurrentClientId();
		return currentRequesterId.flatMapMany(requesterId -> {
			if (optionalStatus.isEmpty()) {
				return consentRequestRepository.findAllByRequester_Id(requesterId, DEFAULT_SORT);
			}
			final String status = optionalStatus.get().name();
			return consentRequestRepository.findAllByRequester_IdAndStatus(requesterId, status, DEFAULT_SORT);
		}).map(ConsentRequest::new);
    }

    Flux<ConsentRequest> getAllConsentRequestsForRole(String role, Optional<ConsentRequestStatus> status) {
        Objects.requireNonNull(role, "Role must not be null!");

        final Mono<Boolean> hasUserRoleMono = consentUserService.hasUserRole(role);
        return hasUserRoleMono.flatMapMany(hasUserRole -> {
            if (hasUserRole) {
                return getConsentRequestsByStatus(status);
            }
            final Mono<Boolean> hasOwnerRole = consentUserService.hasUserRole(ConsentUserService.OWNER_ROLE);
            return hasOwnerRole.flatMapMany(isOwner -> {
                if (!isOwner) {
                    return Flux.empty();
                }
                return getConsentRequestsByStatus(status).filterWhen(this::isOwnerUser);
            });
        });
    }

    private Mono<ConsentRequest> getConsentRequestById(final String id) {
        final Mono<String> currentRequesterId = consentUserService.getCurrentClientId();
        return currentRequesterId.flatMap(requesterId -> consentRequestRepository.findFirstByIdAndRequester_Id(id, requesterId))
				.switchIfEmpty(Mono.error(ConsentRequestNotFoundException::new))
                .map(ConsentRequest::new);
    }

    Mono<ConsentRequest> getConsentRequestForRoleById(String role, String id) {
        Objects.requireNonNull(role, "Role must not be null!");
        Objects.requireNonNull(id, "Id must not be null!");

        final Mono<Boolean> hasUserRoleMono = consentUserService.hasUserRole(role);
        return hasUserRoleMono.flatMap(hasUserRole -> {
            if (hasUserRole) {
                return getConsentRequestById(id);
            }
            final Mono<Boolean> hasOwnerRole = consentUserService.hasUserRole(ConsentUserService.OWNER_ROLE);
            return hasOwnerRole.flatMap(isOwner -> {
                if (!isOwner) {
                    return Mono.error(ConsentRequestNotFoundException::new);
                }
                return getConsentRequestById(id).filterWhen(this::isOwnerUser)
						.switchIfEmpty(Mono.error(ConsentRequestNotFoundException::new));
            });
        });
    }

	public Mono<Boolean> noPendingConsentRequestExists(ConsentUser requester, String ownerId) {
		final String requesterId = requester.getId();
		final String status = ConsentRequestStatus.PENDING.name();
		return consentRequestRepository
				.existsByRequester_IdAndOwnerIdAndStatus(requesterId, ownerId, status)
				.map(b -> Boolean.TRUE.equals(b) ? Boolean.FALSE : Boolean.TRUE);
	}

	private Mono<ConsentRequest> requestConsent(String ownerId, ConsentPermissions consentPermissions, String dataUsageStatement, ConsentPurpose purpose) {
        return checkIf(dataItemTypeService.existsAllDataItemTypes(consentPermissions.getDataItemTypes()), DataItemTypeNotFoundException::new)
                .then(checkIf(consentOwnerService.existsByUserId(ownerId), DataOwnerNotFoundException::new))
                .then(consentUserService.getCurrentUserAsConsentRequester())
				.filterWhen(requester -> noPendingConsentRequestExists(requester, ownerId))
				.switchIfEmpty(Mono.error(PendingConsentRequestExistsException::new))
                .filterWhen(requester -> consentService.noActiveConsentExists(requester, ownerId))
                .switchIfEmpty(Mono.error(ActiveConsentExistsException::new))
                .map(requester -> new ConsentRequest(requester, ownerId, consentPermissions, dataUsageStatement, purpose))
                .map(ConsentRequest::toEntity)
                .flatMap(consentRequestRepository::save)
                .map(ConsentRequest::new)
                .doOnNext(consentRequest -> log.info("Consent request created: id={}", consentRequest.getId()));
    }

	Mono<ConsentRequest> requestConsentForRole(String role, String ownerId, ConsentPermissions consentPermissions, String dataUsageStatement, ConsentPurpose purpose) {
		final Mono<Boolean> hasUserRoleMono = consentUserService.hasUserRole(role);
		return hasUserRoleMono.flatMap(hasUserRole -> {
			if (hasUserRole) {
				return requestConsent(ownerId, consentPermissions, dataUsageStatement, purpose);
			}
			final Mono<Boolean> hasOwnerRole = consentUserService.hasUserRole(ConsentUserService.OWNER_ROLE);
			return hasOwnerRole.flatMap(isOwner -> {
				if (!isOwner) {
					return Mono.error(ConsentRequestNoPermission::new);
				}
				final Mono<String> currentUserId = consentUserService.getCurrentUserId();
				return currentUserId.flatMap(userId -> {
					if (!Objects.equals(userId, ownerId)) {
						return Mono.error(ConsentRequestNoPermission::new);
					}
					return requestConsent(ownerId, consentPermissions, dataUsageStatement, purpose);
				});
			});
		});
	}

    Mono<ConsentRequest> requestConsentForRole(String role, String ownerId, Set<String> dataItemTypes, ConsentOperationType operationType, String dataUsageStatement, ConsentPurpose purpose) {
		final ConsentPermissions consentPermissions = new ConsentPermissions(dataItemTypes, operationType);
		return requestConsentForRole(role, ownerId, consentPermissions, dataUsageStatement, purpose);
    }

	Mono<Void> retractConsentRequest(Mono<ConsentRequest> consentRequest) {
        return consentRequest.flatMap(c -> {
					c.retract();
					return consentRequestRepository.save(c.toEntity());
				})
                .onErrorMap(OptimisticLockingFailureException.class, ignored -> new ConsentRequestModifiedException())
                .doOnNext(c -> log.info("Consent request retracted: id={}", c.id()))
                .then();
    }
}
