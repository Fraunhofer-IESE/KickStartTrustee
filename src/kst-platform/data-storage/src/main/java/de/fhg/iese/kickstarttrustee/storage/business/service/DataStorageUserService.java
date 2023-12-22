package de.fhg.iese.kickstarttrustee.storage.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DataStorageUserService {
    private static final Logger log = LoggerFactory.getLogger(DataStorageUserService.class);
    private static final String CLIENT_ID_CLAIM = "azp";
    private static final String ROLES = "roles";
    private static final String CLAIM_REALM_ACCESS = "realm_access";

	private Mono<Jwt> getJwt() {
        return ReactiveSecurityContextHolder.getContext().flatMap(context -> {
            final Authentication authentication = context.getAuthentication();
            if (authentication == null) {
                log.warn("No authentication information available!");
                return Mono.empty();
            }
            return Mono.just(authentication.getPrincipal());
        }).cast(Jwt.class);
    }

    Mono<String> getCurrentClientId() {
        return getJwt().map(jwt -> jwt.getClaimAsString(CLIENT_ID_CLAIM));
    }

    Mono<String> getCurrentUserId() {
        return getJwt().map(JwtClaimAccessor::getSubject);
    }

	@SuppressWarnings("unchecked")
	private static List<String> getUserRoles(Jwt jwt) {
		return Optional.ofNullable(jwt.getClaimAsMap(CLAIM_REALM_ACCESS))
				.flatMap(realmAccess -> Optional.ofNullable((List<String>) realmAccess.get(ROLES)))
				.orElse(List.of());
	}

	public Mono<List<String>> getUserRoles() {
		return getJwt().map(DataStorageUserService::getUserRoles);
	}

	public Mono<Boolean> hasUserRole(final String role) {
		return getUserRoles()
				.flatMapMany(Flux::fromIterable)
				.any(userRole -> Objects.equals(userRole, role))
				.defaultIfEmpty(Boolean.FALSE);
	}

	public Mono<Boolean> hasUserAnyRole(final String... roles) {
		return getUserRoles()
				.map(userRoles -> Stream.of(roles).anyMatch(userRoles::contains))
				.defaultIfEmpty(Boolean.FALSE);
	}
}
