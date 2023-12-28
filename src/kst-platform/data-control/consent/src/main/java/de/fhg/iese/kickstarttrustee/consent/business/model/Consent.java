package de.fhg.iese.kickstarttrustee.consent.business.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentNotActiveException;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentEntity;

public class Consent {
    private final String id;
    private final String consentRequestId;
    private final ConsentUser actor;
    private final String ownerId;
	private final ConsentPermissions consentPermissions;
	private final String dataUsageStatement;
    private final ConsentPurpose purpose;
    private final Instant createdAt;
    private final long version;
    private ConsentStatus status;

	public Consent(String consentRequestId, ConsentUser actor, String ownerId,
				   ConsentPermissions consentPermissions, String dataUsageStatement,
				   ConsentPurpose purpose) {
		this.id = null;
		this.consentRequestId = Objects.requireNonNull(consentRequestId);
		this.actor = Objects.requireNonNull(actor);
		this.ownerId = Objects.requireNonNull(ownerId);
		this.consentPermissions = Objects.requireNonNull(consentPermissions);
		this.dataUsageStatement = dataUsageStatement;
		this.purpose = purpose;
		this.createdAt = Instant.now();
		this.version = 0L;
		this.status = ConsentStatus.ACTIVE;
	}

	public Consent(ConsentRequest consentRequest) {
        this(consentRequest.getId(), consentRequest.getRequester(), consentRequest.getOwnerId(),
				consentRequest.getConsentPermissions(), consentRequest.getDataUsageStatement().orElse(null),
				consentRequest.getPurpose().orElse(null));
    }

    public Consent(ConsentEntity entity) {
        this.id = entity.id();
        this.consentRequestId = entity.consentRequestId();
        this.actor = new ConsentUser(entity.actor());
        this.ownerId = entity.ownerId();
		this.consentPermissions = new ConsentPermissions(entity.consentPermissions());
		this.dataUsageStatement = entity.dataUsageStatement();
        this.purpose = Optional.ofNullable(entity.purpose()).map(ConsentPurpose::valueOf).orElse(null);
        this.createdAt = entity.createdAt();
        this.version = entity.version();
        this.status = ConsentStatus.valueOf(entity.status());
    }

    public String getId() {
        return id;
    }

    public String getConsentRequestId() {
        return consentRequestId;
    }

    public ConsentUser getActor() {
        return actor;
    }

    public String getOwnerId() {
        return ownerId;
    }

	public ConsentPermissions getConsentPermissions() {
		return consentPermissions;
	}

	public Optional<String> getDataUsageStatement() {
        return Optional.ofNullable(dataUsageStatement);
    }

    public Optional<ConsentPurpose> getPurpose() {
        return Optional.ofNullable(purpose);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getVersion() {
        return version;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public void revoke() {
        if (this.status != ConsentStatus.ACTIVE) {
            throw new ConsentNotActiveException();
        }
        this.status = ConsentStatus.REVOKED;
    }

    public ConsentEntity toEntity() {
		final String purpose = Optional.ofNullable(this.purpose).map(ConsentPurpose::name).orElse(null);
		final Map<String, Set<String>> consentPermissions = this.consentPermissions.toSerializedMap();
        return new ConsentEntity(id, consentRequestId, actor.toEntity(), ownerId, consentPermissions,
				dataUsageStatement, purpose, createdAt, version, status.name());
    }

	@Override
	public int hashCode() {
		return Objects.hash(id, consentRequestId, actor, ownerId, consentPermissions, dataUsageStatement, purpose, createdAt, version, status);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Consent consent = (Consent) o;
		return Objects.equals(id, consent.id)
				&& version == consent.version
				&& Objects.equals(consentRequestId, consent.consentRequestId)
				&& Objects.equals(actor, consent.actor)
				&& Objects.equals(ownerId, consent.ownerId)
				&& Objects.equals(consentPermissions, consent.consentPermissions)
				&& Objects.equals(dataUsageStatement, consent.dataUsageStatement)
				&& purpose == consent.purpose
				&& Objects.equals(createdAt, consent.createdAt)
				&& status == consent.status;
	}
}
