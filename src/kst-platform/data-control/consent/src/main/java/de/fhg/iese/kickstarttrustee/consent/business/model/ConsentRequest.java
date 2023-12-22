package de.fhg.iese.kickstarttrustee.consent.business.model;

import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentRequestAlreadyProcessedException;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentRequestEntity;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ConsentRequest {
    private final String id;
    private final ConsentUser requester;
    private final String ownerId;
	private final ConsentPermissions consentPermissions;
    private final String dataUsageStatement;
    private final ConsentPurpose purpose;
    private final Instant createdAt;
    private final long version;
    private ConsentRequestStatus status;

    public ConsentRequest(ConsentUser requester, String ownerId, ConsentPermissions consentPermissions,
						  String dataUsageStatement, ConsentPurpose purpose) {
        this.id = null;
        this.requester = Objects.requireNonNull(requester);
        this.ownerId = Objects.requireNonNull(ownerId);
		this.consentPermissions = Objects.requireNonNull(consentPermissions);
        this.dataUsageStatement = dataUsageStatement;
        this.purpose = purpose;
        this.createdAt = Instant.now();
        this.version = 0L;
        this.status = ConsentRequestStatus.PENDING;
    }

    public ConsentRequest(final ConsentRequestEntity entity) {
        this.id = entity.id();
        this.requester = new ConsentUser(entity.requester());
        this.ownerId = entity.ownerId();
		this.consentPermissions = new ConsentPermissions(entity.consentPermissions());
        this.dataUsageStatement = entity.dataUsageStatement();
        this.purpose = entity.purpose() != null ? ConsentPurpose.valueOf(entity.purpose()) : null;
        this.createdAt = entity.createdAt();
        this.version = entity.version();
        this.status = ConsentRequestStatus.valueOf(entity.status());
    }

    public String getId() {
        return id;
    }

    public ConsentUser getRequester() {
        return requester;
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

    public ConsentRequestStatus getStatus() {
        return status;
    }

    private void checkPendingStatus() {
        if (status != ConsentRequestStatus.PENDING) {
            throw new ConsentRequestAlreadyProcessedException();
        }
    }

    public void accept() {
        this.checkPendingStatus();
        this.status = ConsentRequestStatus.ACCEPTED;
    }

    public void reject() {
        this.checkPendingStatus();
        this.status = ConsentRequestStatus.REJECTED;
    }

    public void retract() {
        this.checkPendingStatus();
        this.status = ConsentRequestStatus.RETRACTED;
    }

    public ConsentRequestEntity toEntity() {
        final String purpose = this.purpose != null ? this.purpose.name() : null;
		final Map<String, Set<String>> consentPermissions = this.consentPermissions.toSerializedMap();
        return new ConsentRequestEntity(id, requester.toEntity(), ownerId, consentPermissions, dataUsageStatement,
				purpose, createdAt, version, status.name());
    }

	@Override
	public int hashCode() {
		return Objects.hash(id, requester, ownerId, consentPermissions, dataUsageStatement, purpose, createdAt, version, status);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ConsentRequest that = (ConsentRequest) o;
		return version == that.version && Objects.equals(id, that.id) && Objects.equals(requester, that.requester)
				&& Objects.equals(ownerId, that.ownerId) && Objects.equals(consentPermissions, that.consentPermissions)
				&& Objects.equals(dataUsageStatement, that.dataUsageStatement) && purpose == that.purpose
				&& Objects.equals(createdAt, that.createdAt) && status == that.status;
	}
}
