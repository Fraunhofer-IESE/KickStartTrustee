package de.fhg.iese.kickstarttrustee.consent.business.model;

import java.util.Objects;

import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentOwnerEntity;

public class ConsentOwner {
    private final String id;
    private final String userId;

    public ConsentOwner(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public ConsentOwnerEntity toEntity() {
        return new ConsentOwnerEntity(id, userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ConsentOwner))
            return false;
        ConsentOwner other = (ConsentOwner) obj;
        return Objects.equals(id, other.id) && Objects.equals(userId, other.userId);
    }
}
