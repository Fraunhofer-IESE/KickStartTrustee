package de.fhg.iese.kickstarttrustee.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Kickstart Trustee",
		description = "This is the API of the Kickstart Trustee backend", version = "v1"),
		security = {@SecurityRequirement(name = "security_auth")})
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
		flows = @OAuthFlows(
				authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
						tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", scopes = {
						@OAuthScope(name = "consumer.consent:manage"),
						@OAuthScope(name = "consumer.data:read"),
						@OAuthScope(name = "owner.audit:manage"),
						@OAuthScope(name = "owner.consent:manage"),
						@OAuthScope(name = "owner.data:manage"),
						@OAuthScope(name = "owner.profile:manage"),
						@OAuthScope(name = "prosumer.consent:manage"),
						@OAuthScope(name = "prosumer.data:manage"),
						@OAuthScope(name = "provider.consent:manage"),
						@OAuthScope(name = "provider.data:write")
				}),
				clientCredentials = @OAuthFlow(tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", scopes = {
						@OAuthScope(name = "consumer.consent:manage"),
						@OAuthScope(name = "consumer.data:read"),
						@OAuthScope(name = "owner.audit:manage"),
						@OAuthScope(name = "owner.consent:manage"),
						@OAuthScope(name = "owner.data:manage"),
						@OAuthScope(name = "owner.profile:manage"),
						@OAuthScope(name = "prosumer.consent:manage"),
						@OAuthScope(name = "prosumer.data:manage"),
						@OAuthScope(name = "provider.consent:manage"),
						@OAuthScope(name = "provider.data:write")
				})
		))
@Configuration
public class OpenAPIConfig {
}
