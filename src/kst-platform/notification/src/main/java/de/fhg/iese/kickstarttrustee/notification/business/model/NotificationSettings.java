package de.fhg.iese.kickstarttrustee.notification.business.model;

import de.fhg.iese.kickstarttrustee.notification.persistence.entity.NotificationSettingsEntity;

import java.util.Objects;

public class NotificationSettings {
    private final String id;
    private final String userId;
    private String email;

    public NotificationSettings(String userId, String email) {
        this.id = null;
        this.userId = Objects.requireNonNull(userId);
        this.email = Objects.requireNonNull(email);
    }

    public NotificationSettings(NotificationSettingsEntity entity) {
        this.id = entity.id();
        this.userId = entity.userId();
        this.email = entity.email();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email);
    }

    public NotificationSettingsEntity toEntity() {
        return new NotificationSettingsEntity(id, userId, email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationSettings that = (NotificationSettings) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(email, that.email);
    }
}
