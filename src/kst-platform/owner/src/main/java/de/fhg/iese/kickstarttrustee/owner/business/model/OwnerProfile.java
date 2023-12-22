package de.fhg.iese.kickstarttrustee.owner.business.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.fhg.iese.kickstarttrustee.owner.business.exception.NotSupportedLanguageException;
import de.fhg.iese.kickstarttrustee.owner.persistence.entity.OwnerProfileEntity;

public class OwnerProfile {
    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("en-US", "de-DE");

    private final String id;
    private final String userId;
    private final IdpProfile idpProfile;
    private String preferredLanguage;
    private final Instant createdAt;

    public OwnerProfile(String userId, IdpProfile idpProfile, String preferredLanguage) {
        this.id = null;
        this.userId = Objects.requireNonNull(userId);
        this.idpProfile = idpProfile;
        this.setPreferredLanguage(preferredLanguage);
        this.createdAt = Instant.now();
    }

    public OwnerProfile(OwnerProfileEntity entity, IdpProfile idpProfile) {
        this.id = entity.id();
        this.userId = entity.userId();
        this.idpProfile = idpProfile;
        this.preferredLanguage = entity.preferredLanguage();
        this.createdAt = entity.createdAt();
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public String getUserId() {
        return userId;
    }

    public IdpProfile getIdpProfile() {
        return idpProfile;
    }

    public Optional<String> getPreferredLanguage() {
        return Optional.ofNullable(preferredLanguage);
    }

    public void setPreferredLanguage(String preferredLanguage) {
        if (!SUPPORTED_LANGUAGES.contains(preferredLanguage)) {
            throw new NotSupportedLanguageException();
        }
        this.preferredLanguage = preferredLanguage;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public OwnerProfileEntity toEntity() {
        return new OwnerProfileEntity(id, userId, preferredLanguage, createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, idpProfile, preferredLanguage, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OwnerProfile))
            return false;
        OwnerProfile other = (OwnerProfile) obj;
        return Objects.equals(id, other.id) && Objects.equals(userId, other.userId)
                && Objects.equals(idpProfile, other.idpProfile)
                && Objects.equals(preferredLanguage, other.preferredLanguage)
                && Objects.equals(createdAt, other.createdAt);
    }
}
