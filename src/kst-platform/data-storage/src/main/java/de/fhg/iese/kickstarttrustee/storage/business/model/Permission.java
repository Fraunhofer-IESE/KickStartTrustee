package de.fhg.iese.kickstarttrustee.storage.business.model;

import de.fhg.iese.kickstarttrustee.storage.persistence.entity.PermissionEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Permission {
    private final String id;
	private final String actorId;
    private final String ownerId;
    private final String dataItemType;
    private final Set<String> operations;

    public Permission(String actorId, String ownerId, String dataItemType, Set<String> operations) {
        this.id = null;
		this.actorId = Objects.requireNonNull(actorId);
		this.ownerId = Objects.requireNonNull(ownerId);
        this.dataItemType = Objects.requireNonNull(dataItemType);
        this.operations = new HashSet<>(Objects.requireNonNull(operations));
    }

    public Permission(PermissionEntity entity) {
        this.id = entity.id();
		this.actorId = entity.actorId();
		this.ownerId = entity.ownerId();
		this.dataItemType = entity.dataItemType();
        this.operations = new HashSet<>(entity.operations());
    }

    public String getId() {
        return id;
    }

	public String getActorId() {
		return actorId;
	}

    public String getOwnerId() {
        return ownerId;
    }

    public String getDataItemType() {
        return dataItemType;
    }

	public void addOperations(Collection<String> operations) {
		this.operations.addAll(operations);
	}

	public boolean hasOperation(String operation) {
		return this.operations.contains(operation);
	}

	public void removeOperations(Collection<String> operations) {
		this.operations.removeAll(operations);
	}

	public PermissionEntity toEntity() {
        return new PermissionEntity(id, actorId, ownerId, dataItemType, operations);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Permission that = (Permission) o;
		return Objects.equals(id, that.id) && Objects.equals(actorId, that.actorId)
				&& Objects.equals(ownerId, that.ownerId) && Objects.equals(dataItemType, that.dataItemType)
				&& Objects.equals(operations, that.operations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, actorId, ownerId, dataItemType, operations);
	}
}
