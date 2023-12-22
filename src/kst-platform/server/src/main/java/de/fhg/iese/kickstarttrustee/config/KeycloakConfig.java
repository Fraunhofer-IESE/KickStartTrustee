package de.fhg.iese.kickstarttrustee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;

import de.fhg.iese.kickstarttrustee.keycloak.KeycloakConfigurationProperties;
import de.fhg.iese.kickstarttrustee.keycloak.KeycloakGrantedAuthoritiesConverter;
import de.fhg.iese.kickstarttrustee.keycloak.KeycloakJwtAuthenticationConverter;
import de.fhg.iese.kickstarttrustee.properties.IdpConfigurationProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class KeycloakConfig {

	@Bean
	@Validated
	@ConfigurationProperties("keycloak")
	IdpConfigurationProperties idpConfigurationProperties() {
		return new KeycloakConfigurationProperties();
	}

    @Bean
	Converter<Jwt, Flux<GrantedAuthority>> keycloakGrantedAuthoritiesConverter() {
		return new KeycloakGrantedAuthoritiesConverter();
	}

	@Bean
	Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter(Converter<Jwt, Flux<GrantedAuthority>> converter) {
		return new KeycloakJwtAuthenticationConverter(converter);
	}
}
