package de.fhg.iese.kickstarttrustee.consent.business.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsentPermissions extends HashMap<String, Set<ConsentOperationType>> {

	public ConsentPermissions(Set<String> dataItemTypes, ConsentOperationType operationType) {
		super(dataItemTypes.size());
		for (final String dataItemType: dataItemTypes) {
			this.put(dataItemType, Set.of(operationType));
		}
	}

	public ConsentPermissions(Map<String, Set<String>> serializedMap) {
		super(serializedMap.size());
		serializedMap.forEach((dataItemType, serializedOperations) -> {
			final Set<ConsentOperationType> operations = deserializeOperations(serializedOperations);
			this.put(dataItemType, operations);
		});
	}

	public ConsentPermissions() {
		super();
	}

	private static Set<ConsentOperationType> deserializeOperations(Set<String> operations) {
		return operations.stream().map(ConsentOperationType::valueOf).collect(Collectors.toSet());
	}

	private static Set<String> serializeOperations(Set<ConsentOperationType> operations) {
		return operations.stream().map(ConsentOperationType::name).collect(Collectors.toSet());
	}

	public Map<String, Set<String>> toSerializedMap() {
		final Map<String, Set<String>> result = new HashMap<>(this.size());
		this.forEach((dataItemType, operations) -> {
			final Set<String> serializeOperations = serializeOperations(operations);
			result.put(dataItemType, serializeOperations);
		});
		return result;
	}

	public void addOperationType(String dataItemType, ConsentOperationType operationType) {
		final Set<ConsentOperationType> operationTypes = new HashSet<>(this.getOrDefault(dataItemType, Set.of()));
		operationTypes.add(operationType);
		this.put(dataItemType, operationTypes);
	}

	public Set<String> getDataItemTypesByOperationType(final ConsentOperationType operationType) {
		final Set<String> dataItemTypes = new HashSet<>();
		this.forEach((dataItemType, operations) -> {
			if (operations.contains(operationType)) {
				dataItemTypes.add(dataItemType);
			}
		});
		return dataItemTypes;
	}

	public Set<String> getConsumedDataItemTypes() {
		return getDataItemTypesByOperationType(ConsentOperationType.DATA_CONSUMPTION);
	}

	public Set<String> getProvidedDataItemTypes() {
		return getDataItemTypesByOperationType(ConsentOperationType.DATA_PROVISION);
	}

	public Set<String> getDataItemTypes() {
		return this.keySet();
	}
}
