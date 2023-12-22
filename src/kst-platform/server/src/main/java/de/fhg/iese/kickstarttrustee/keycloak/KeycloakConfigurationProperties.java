package de.fhg.iese.kickstarttrustee.keycloak;

import de.fhg.iese.kickstarttrustee.properties.IdpAdminCredentials;
import de.fhg.iese.kickstarttrustee.properties.IdpConfigurationProperties;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class KeycloakConfigurationProperties implements IdpConfigurationProperties {
    @NotEmpty 
    private String url;
    @NotEmpty 
    private String realm;
    @Valid 
    private KeycloakAdminCredentials adminCredentials;

    public KeycloakConfigurationProperties(String url, String realm, KeycloakAdminCredentials adminCredentials) {
        this.url = url;
        this.realm = realm;
        this.adminCredentials = adminCredentials;
    }

    public KeycloakConfigurationProperties() {
        this(null, null, null);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setAdminCredentials(KeycloakAdminCredentials adminCredentials) {
        this.adminCredentials = adminCredentials;
    }

    public String adminUrl() {
        return this.url + "/admin/realms/" + this.realm;
    }

    public IdpAdminCredentials adminCredentials() {
        return adminCredentials;
    }

    public String issuerUri() {
        return this.url + "/realms/" + this.realm;
    }

    public String tokenUrl() {
        return this.issuerUri() + "/protocol/openid-connect/token";
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, realm, adminCredentials);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof KeycloakConfigurationProperties))
            return false;
        KeycloakConfigurationProperties other = (KeycloakConfigurationProperties) obj;
        return Objects.equals(url, other.url) && Objects.equals(realm, other.realm)
                && Objects.equals(adminCredentials, other.adminCredentials);
    }

    public record KeycloakAdminCredentials(@NotEmpty String clientId, @NotEmpty String clientSecret) implements IdpAdminCredentials {
    }
}
