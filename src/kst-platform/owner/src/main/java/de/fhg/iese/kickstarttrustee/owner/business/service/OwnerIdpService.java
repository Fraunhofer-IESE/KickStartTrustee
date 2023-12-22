package de.fhg.iese.kickstarttrustee.owner.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.fhg.iese.kickstarttrustee.owner.business.model.IdpProfile;
import reactor.core.publisher.Mono;

@Service
public class OwnerIdpService {
    private static final Logger log = LoggerFactory.getLogger(OwnerIdpService.class);

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_GIVEN_NAME = "given_name";
    private static final String CLAIM_FAMILY_NAME = "family_name";

    private final WebClient idpClient;

    public OwnerIdpService(WebClient idClient) {
        this.idpClient = idClient;
    }

	public Mono<IdpProfile> getOwnIdpProfile() {
		return ReactiveSecurityContextHolder.getContext().flatMap(context -> {
			final Authentication authentication = context.getAuthentication();
			if (authentication == null) {
				log.warn("No authentication information available!");
				return Mono.empty();
			}
			return Mono.just(authentication.getPrincipal());
		}).cast(Jwt.class).map(jwt -> {
			final String userId = jwt.getSubject();
			final String email = jwt.getClaimAsString(CLAIM_EMAIL);
			final String firstname = jwt.getClaimAsString(CLAIM_GIVEN_NAME);
			final String lastname = jwt.getClaimAsString(CLAIM_FAMILY_NAME);
			return new IdpProfile(userId, email, firstname, lastname);
		});
	}

	public Mono<IdpProfile> getIdpProfileByUserId(String id) {
		return idpClient.get()
				.uri("/users/{id}", id)
				.exchangeToMono(response -> {
					if (response.statusCode().isError()) {
						return response.createException()
								.doOnNext(e -> log.warn("Could not get idp user: userId={}, error={}", id,
										e.getMessage()))
								.flatMap(Mono::error);
					}
					return response.bodyToMono(IdpProfile.class);
				});
	}

	public Mono<Void> deleteUserById(String id) {
		return idpClient.delete()
				.uri("/users/{id}", id)
				.exchangeToMono(response -> {
					if (response.statusCode().isError()) {
						return response.createException()
								.doOnNext(e -> log.warn("Could not delete idp user: userId={}, error={}", id,
										e.getMessage()))
								.flatMap(Mono::error);
					}
					return response.releaseBody();
				});
	}
}
