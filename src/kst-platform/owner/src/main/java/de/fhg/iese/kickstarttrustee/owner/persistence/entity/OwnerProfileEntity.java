package de.fhg.iese.kickstarttrustee.owner.persistence.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("owner_profile")
public record OwnerProfileEntity(@Id String id, @Indexed(unique = true) String userId, String preferredLanguage, Instant createdAt) {
}
