package de.fhg.iese.kickstarttrustee.owner.business.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdpProfile {
    private final String id;
    private final String email;
    private final String firstName;
    private final String lastName;

    public IdpProfile(String id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof IdpProfile))
            return false;
        IdpProfile other = (IdpProfile) obj;
        return Objects.equals(id, other.id) && Objects.equals(email, other.email)
                && Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
    }
}
