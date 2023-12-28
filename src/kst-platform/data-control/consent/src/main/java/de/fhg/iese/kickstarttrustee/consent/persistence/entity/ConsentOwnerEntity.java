package de.fhg.iese.kickstarttrustee.consent.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("consent_owner")
public record ConsentOwnerEntity(@Id String id, @Indexed String userId) {
}
