package de.fhg.iese.kickstarttrustee.keycloak;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
	private static final String USERNAME_CLAIM = "preferred_username";
	private final Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

	public KeycloakJwtAuthenticationConverter(Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
		Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
		this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
	}

	private Flux<GrantedAuthority> getJWTGrantedAuthorities(Jwt jwt) {
		return Optional.ofNullable(this.jwtGrantedAuthoritiesConverter.convert(jwt))
					   .orElse(Flux.empty());
	}

	private String extractUsername(Jwt jwt) {
		return jwt.hasClaim(USERNAME_CLAIM) ? jwt.getClaimAsString(USERNAME_CLAIM) : jwt.getSubject();
	}

	@Override
	public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
		return this.getJWTGrantedAuthorities(jwt)
				.collectList()
				.map((authorities) -> new JwtAuthenticationToken(jwt, authorities, extractUsername(jwt)));
	}
}
