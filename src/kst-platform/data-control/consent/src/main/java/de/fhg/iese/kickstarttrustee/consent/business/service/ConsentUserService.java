package de.fhg.iese.kickstarttrustee.consent.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;
import reactor.core.publisher.Mono;

@Service
public class ConsentUserService {
    private static final Logger log = LoggerFactory.getLogger(ConsentUserService.class);
    public static final String OWNER_ROLE = "OWNER";
    public static final String CONSUMER_ROLE = "CONSUMER";
    public static final String PROVIDER_ROLE = "PROVIDER";
	public static final String PROSUMER_ROLE = "PROSUMER";

    private static final String CLIENT_ID_CLAIM = "azp";
    private static final String CLIENT_NAME_CLAIM = "client_name";
    private static final String ROLES = "roles";
    private static final String CLAIM_REALM_ACCESS = "realm_access";

    private Mono<Jwt> getJwt() {
        return ReactiveSecurityContextHolder.getContext().flatMap(context -> {
            Authentication authentication = context.getAuthentication();
            if (authentication == null) {
                log.warn("No authentication information available!");
                return Mono.empty();
            }
            return Mono.just(authentication.getPrincipal());
        }).cast(Jwt.class);
    }

    public Mono<String> getCurrentUserId() {
        return getJwt().map(JwtClaimAccessor::getSubject);
    }

    public Mono<String> getCurrentClientId() {
        return getJwt().map(jwt -> jwt.getClaimAsString(CLIENT_ID_CLAIM));
    }

    public Mono<ConsentUser> getCurrentUserAsConsentRequester() {
        return getJwt().map(jwt -> {
            final String id = jwt.getClaimAsString(CLIENT_ID_CLAIM);
            final String name = jwt.getClaimAsString(CLIENT_NAME_CLAIM);
            return new ConsentUser(id, name);
        });
    }

    @SuppressWarnings("unchecked")
    public Mono<Boolean> hasUserRole(String role) {
        return getJwt().mapNotNull(jwt -> jwt.getClaimAsMap(CLAIM_REALM_ACCESS))
        .mapNotNull(realmAccess -> (List<String>) realmAccess.get(ROLES))
        .map(roles -> roles.contains(role))
        .defaultIfEmpty(Boolean.FALSE);
    }
}
