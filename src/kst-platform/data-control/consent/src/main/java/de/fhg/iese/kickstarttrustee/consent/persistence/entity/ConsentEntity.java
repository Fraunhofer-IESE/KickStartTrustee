package de.fhg.iese.kickstarttrustee.consent.persistence.entity;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("consent")
public record ConsentEntity(@Id String id, String consentRequestId, ConsentUserEntity actor, @Indexed String ownerId,
							Map<String, Set<String>> consentPermissions, String dataUsageStatement, String purpose,
							Instant createdAt, @Version long version, String status) {
}
