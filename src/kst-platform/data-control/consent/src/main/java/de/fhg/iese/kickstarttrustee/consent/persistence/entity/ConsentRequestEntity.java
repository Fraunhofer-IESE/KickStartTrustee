package de.fhg.iese.kickstarttrustee.consent.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Document("consent_request")
public record ConsentRequestEntity(@Id String id, ConsentUserEntity requester, @Indexed String ownerId,
								   Map<String, Set<String>> consentPermissions, String dataUsageStatement,
								   String purpose, Instant createdAt, @Version long version, String status) {
    public String requesterId() {
        return requester().id();
    }
}
