package de.fhg.iese.kickstarttrustee.storage.business.service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.consent.ConsentCreated;
import de.fhg.iese.kickstarttrustee.event.consent.ConsentRevoked;
import de.fhg.iese.kickstarttrustee.storage.business.model.Permission;
import de.fhg.iese.kickstarttrustee.storage.persistence.repository.PermissionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PermissionService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

	private static final String OWNER_ROLE = "OWNER";
	private static final String CONSUMER_ROLE = "CONSUMER";
	private static final String PROSUMER_ROLE = "PROSUMER";
	private static final String PROVIDER_ROLE = "PROVIDER";
	private static final String[] CONSUMPTION_ROLES = new String[] { CONSUMER_ROLE, PROSUMER_ROLE };
	private static final String[] PROVISION_ROLES = new String[] { PROSUMER_ROLE, PROVIDER_ROLE };
	private static final String DATA_CONSUMPTION = "DATA_CONSUMPTION";
	private static final String DATA_PROVISION = "DATA_PROVISION";

    private final IEventbus eventbus;
    private final DataStorageUserService userService;
    private final PermissionRepository repository;

    public PermissionService(IEventbus eventbus, DataStorageUserService userService, PermissionRepository repository) {
        this.eventbus = eventbus;
        this.userService = userService;
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(ConsentCreated.class, this::onConsentCreated);
        eventbus.registerConsumer(ConsentRevoked.class, this::onConsentRevoked);
    }

    private Mono<Permission> getOrCreatePermission(final String actorId, final String ownerId, final String dataItemType) {
        return repository.findFirstByActorIdAndOwnerIdAndDataItemType(actorId, ownerId, dataItemType)
                .map(Permission::new)
                .defaultIfEmpty(new Permission(actorId, ownerId, dataItemType, Set.of()));
    }

	/*
	 * This method checks whether the current user of the JWT is an owner. In case it
	 * is an owner it only returns true if the specified ownerId is the current userId.
	 * In case it is not an owner it checks whether the current user has at least one of
	 * the specified roles.
	 */
	public Mono<Boolean> isOwnerOrHasAnyRole(final String ownerId, final String[] roles) {
		final Mono<Boolean> hasOwnerRoleMono = userService.hasUserRole(OWNER_ROLE);
		return hasOwnerRoleMono.flatMap(hasOwnerRole -> {
			if (hasOwnerRole) {
				final Mono<String> userIdMono = userService.getCurrentUserId();
				return userIdMono.map(userId -> Objects.equals(userId, ownerId));
			}
			return userService.hasUserAnyRole(roles);
		});
	}

	private Mono<Boolean> hasPermission(final String[] roles, final String ownerId, final String dataItemType,
										final String operation) {
		final Mono<Boolean> hasRolePermissionMono = this.isOwnerOrHasAnyRole(ownerId, roles);
		return hasRolePermissionMono.flatMap(hasRolePermission -> {
			if (Boolean.FALSE.equals(hasRolePermission)) {
				return Mono.just(Boolean.FALSE);
			}
			return userService.getCurrentClientId()
					.flatMap(actorId -> getOrCreatePermission(actorId, ownerId, dataItemType))
					.map(permission -> permission.hasOperation(operation));
		});
	}

    public Mono<Boolean> hasConsumptionPermission(String ownerId, String dataItemType) {
		return hasPermission(CONSUMPTION_ROLES, ownerId, dataItemType, PermissionService.DATA_CONSUMPTION);
    }

    public Mono<Boolean> hasProvidingPermission(String ownerId, String dataItemType) {
		return hasPermission(PROVISION_ROLES, ownerId, dataItemType, PermissionService.DATA_PROVISION);
    }

    private void onConsentCreated(ConsentCreated consentCreated) {
		final String actorId = consentCreated.consentActorId();
		final String ownerId = consentCreated.ownerId();
        final Map<String, Set<String>> consentPermissions = consentCreated.consentPermissions();
        Flux.fromIterable(consentPermissions.entrySet())
                .flatMap(dataItemTypeOperation -> {
					final String dataItemType = dataItemTypeOperation.getKey();
					final Set<String> operations = dataItemTypeOperation.getValue();
					return getOrCreatePermission(actorId, ownerId, dataItemType).map(permission -> {
						permission.addOperations(operations);
						return permission.toEntity();
					});
				})
                .collectList()
                .flatMapMany(repository::saveAll)
                .doOnNext(p -> log.info("Permission added: id={} userId={}", p.id(), actorId))
                .subscribe();
    }

    private void onConsentRevoked(ConsentRevoked consentRevoked) {
		final String actorId = consentRevoked.consentActorId();
		final String ownerId = consentRevoked.ownerId();
		final Map<String, Set<String>> consentPermissions = consentRevoked.consentPermissions();
        Flux.fromIterable(consentPermissions.entrySet())
                .flatMap(dataItemTypeOperation -> {
					final String dataItemType = dataItemTypeOperation.getKey();
					final Set<String> operations = dataItemTypeOperation.getValue();
					return this.getOrCreatePermission(actorId, ownerId, dataItemType).map(permission -> {
						permission.removeOperations(operations);
						return permission.toEntity();
					});
				})
                .collectList()
                .flatMapMany(repository::saveAll)
                .doOnNext(p -> log.info("Permission removed: id={} userId={}", p.id(), actorId))
                .subscribe();
    }

	public Mono<Void> deletePermissionsByOwnerId(final String ownerId) {
		return repository.deleteByOwnerId(ownerId)
				.doOnSuccess(ignored -> log.info("Permissions deleted for owner:  ownerId={}", ownerId))
				.doOnError(error -> log.warn("Permissions deletion for owner failed: ownerId={} error={}", ownerId,
						error.getMessage()));
	}
}
