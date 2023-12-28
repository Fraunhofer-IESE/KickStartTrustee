package de.fhg.iese.kickstarttrustee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

import de.fhg.iese.kickstarttrustee.properties.IdpAdminCredentials;
import de.fhg.iese.kickstarttrustee.properties.IdpConfigurationProperties;

@Configuration
public class IdpClientConfig {
    private static final String CLIENT_REGISTRATION_ID = "keycloak-admin";

    private final IdpConfigurationProperties idpConfigurationProperties;

    public IdpClientConfig(IdpConfigurationProperties idpConfigurationProperties) {
        this.idpConfigurationProperties = idpConfigurationProperties;
    }

    @Bean
    ReactiveClientRegistrationRepository clientRegistrationRepository() {
        IdpAdminCredentials adminCredentials = this.idpConfigurationProperties.adminCredentials();
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId(CLIENT_REGISTRATION_ID)
                .tokenUri(idpConfigurationProperties.tokenUrl())
                .clientId(adminCredentials.clientId())
                .clientSecret(adminCredentials.clientSecret())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }

    @Bean
    ReactiveOAuth2AuthorizedClientService authorizedClientService(
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService);
        authorizedClientManager
                .setAuthorizedClientProvider(new ClientCredentialsReactiveOAuth2AuthorizedClientProvider());
        return authorizedClientManager;
    }

    @Bean
    WebClient idpClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2FilterFunction = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);
        oauth2FilterFunction.setDefaultClientRegistrationId(CLIENT_REGISTRATION_ID);

        return WebClient.builder().baseUrl(idpConfigurationProperties.adminUrl()).filter(oauth2FilterFunction).build();
    }
}
