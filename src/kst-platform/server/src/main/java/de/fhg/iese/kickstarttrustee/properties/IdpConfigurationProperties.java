package de.fhg.iese.kickstarttrustee.properties;

public interface IdpConfigurationProperties {
    String adminUrl();
    IdpAdminCredentials adminCredentials();
    String issuerUri();
    String tokenUrl();
}
