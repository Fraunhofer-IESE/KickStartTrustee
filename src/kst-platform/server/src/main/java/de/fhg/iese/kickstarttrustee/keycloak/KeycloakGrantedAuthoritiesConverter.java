package de.fhg.iese.kickstarttrustee.keycloak;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import reactor.core.publisher.Flux;

public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {
    private static final String ROLES = "roles";
	private static final String CLAIM_REALM_ACCESS = "realm_access";

    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthoritiesConverter;

    public KeycloakGrantedAuthoritiesConverter() {
        this.defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    @SuppressWarnings("unchecked")
	private Collection<String> getRealmRoles(Jwt jwt) {
		return Optional.ofNullable(jwt.getClaimAsMap(CLAIM_REALM_ACCESS))
					   .map(realmAccess -> (List<String>) realmAccess.get(ROLES))
					   .orElse(Collections.emptyList());
	}

    private Collection<GrantedAuthority> getDefaultGrantedAuthorities(Jwt jwt) {
		return Optional.ofNullable(defaultAuthoritiesConverter.convert(jwt))
					   .orElse(Collections.emptySet());
	}

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        final Stream<GrantedAuthority> realmAuthorities = this.getRealmRoles(jwt).stream().map(SimpleGrantedAuthority::new);
        final Stream<GrantedAuthority> defaultAuthorities = this.getDefaultGrantedAuthorities(jwt).stream();
        final Stream<GrantedAuthority> allAuthorities = Stream.concat(realmAuthorities, defaultAuthorities);
        return Flux.fromStream(allAuthorities);
    }
}
