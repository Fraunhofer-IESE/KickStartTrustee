package de.fhg.iese.kickstarttrustee.config;

import java.util.List;

import org.springframework.context.annotation.Bean;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import de.fhg.iese.kickstarttrustee.properties.IdpConfigurationProperties;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private final static String SCOPE_PREFIX = "SCOPE_";
    private final String issuerUri;

    public SecurityConfig(IdpConfigurationProperties idpConfigurationProperties) {
        this.issuerUri = idpConfigurationProperties.issuerUri();
    }

    private static String scopeAuthority(String scope) {
        return SCOPE_PREFIX + scope;
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
            Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter) {
        http.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/consumer/consent", "/consumer/consent/**", "/consumer/consent-request", "/consumer/consent-request/**")
							.hasAuthority(scopeAuthority("consumer.consent:manage"))
						.pathMatchers("/consumer/data", "/consumer/data/**")
							.hasAuthority(scopeAuthority("consumer.data:read"))
						.pathMatchers("/owner/audit/**")
							.hasAuthority(scopeAuthority("owner.audit:manage"))
						.pathMatchers("/owner/consent", "/owner/consent/**", "/owner/consent-request", "/owner/consent-request/**")
							.hasAuthority(scopeAuthority("owner.consent:manage"))
						.pathMatchers("/owner/data", "/owner/data/**")
							.hasAuthority(scopeAuthority("owner.data:manage"))
						.pathMatchers("/owner/account")
							.hasAuthority(scopeAuthority("owner.profile:manage"))
						.pathMatchers("/owner/profile", "/owner/profile/**")
							.hasAuthority(scopeAuthority("owner.profile:manage"))
						.pathMatchers("/prosumer/consent", "/prosumer/consent/**", "/prosumer/consent-request", "/prosumer/consent-request/**")
							.hasAuthority(scopeAuthority("prosumer.consent:manage"))
						.pathMatchers("/prosumer/data", "/prosumer/data/**")
							.hasAuthority(scopeAuthority("prosumer.data:manage"))
						.pathMatchers("/provider/consent", "/provider/consent/**", "/provider/consent-request", "/provider/consent-request/**")
							.hasAuthority(scopeAuthority("provider.consent:manage"))
						.pathMatchers("/provider/data", "/provider/data/**")
							.hasAuthority(scopeAuthority("provider.data:write"))
						.pathMatchers("/actuator/**").hasIpAddress("127.0.0.1")
						.pathMatchers("/swagger-ui.html").permitAll()
						.pathMatchers("/v3/api-docs").permitAll()
						.pathMatchers("/v3/api-docs/**").permitAll()
						.pathMatchers("/webjars/**").permitAll()
						.anyExchange().authenticated())
						.oauth2ResourceServer(oauth2 -> oauth2
								.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
						.cors(withDefaults())
						.oauth2Client(withDefaults());
        return http.build();
    }
}
