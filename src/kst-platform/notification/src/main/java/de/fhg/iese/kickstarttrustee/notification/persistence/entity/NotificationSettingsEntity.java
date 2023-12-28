package de.fhg.iese.kickstarttrustee.notification.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notification_settings")
public record NotificationSettingsEntity(@Id String id, @Indexed String userId, String email) {
}
