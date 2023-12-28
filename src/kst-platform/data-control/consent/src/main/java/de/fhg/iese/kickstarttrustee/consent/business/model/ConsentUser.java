package de.fhg.iese.kickstarttrustee.consent.business.model;

import java.util.Objects;

import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentUserEntity;

public class ConsentUser {
    private final String id;
    private final String name;

    public ConsentUser(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }

    public ConsentUser(ConsentUserEntity entity) {
        this(entity.id(), entity.name());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ConsentUserEntity toEntity() {
        return new ConsentUserEntity(id, name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ConsentUser))
            return false;
        ConsentUser other = (ConsentUser) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }
}
