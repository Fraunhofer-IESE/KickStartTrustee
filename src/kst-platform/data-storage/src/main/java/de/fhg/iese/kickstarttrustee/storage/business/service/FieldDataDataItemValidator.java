package de.fhg.iese.kickstarttrustee.storage.business.service;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItem;
import de.fhg.iese.kickstarttrustee.common.business.service.IReactiveDataItemValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class FieldDataDataItemValidator implements IReactiveDataItemValidator {
	private static final Logger log = LoggerFactory.getLogger(FieldDataDataItemValidator.class);

	private static final String FIELD_DATA_TYPE = "field_data";
	private static final List<String> DATA_ITEM_TYPES = List.of(FIELD_DATA_TYPE);
	private static final String ID_KEY = "id";

	private final ConsumerDataStorageService dataStorageService;

	public FieldDataDataItemValidator(final ConsumerDataStorageService dataStorageService) {
		this.dataStorageService = dataStorageService;
	}

	private static Optional<String> getOptionalStringValue(final Map<String, Object> data, final String key) {
		return Optional.ofNullable(data.get(key)).map(Object::toString);
	}

	private static boolean isBlank(Optional<String> value) {
		return value.map(StringUtils::isBlank).orElse(Boolean.TRUE);
	}

	@Override
	public List<String> dataItemTypes() {
		return FieldDataDataItemValidator.DATA_ITEM_TYPES;
	}

	@Override
	public Mono<Boolean> isValid(final IDataItem dataItem) {
		final Map<String, Object> data = dataItem.data();
		if (data == null) {
			log.warn("No data in dataItem");
			return Mono.just(Boolean.FALSE);
		}

		final Optional<String> fieldIdOptional = getOptionalStringValue(data, ID_KEY);
		if (isBlank(fieldIdOptional)) {
			log.warn("Invalid or missing id attribute in dataItem");
			return Mono.just(Boolean.FALSE);
		}

		// check that field id is unique in the scope of the owner
		final String fieldId = fieldIdOptional.orElseThrow();
		final String ownerId = dataItem.metaData().ownerId();
		return dataStorageService.getDataItemsByDataItemTypeAndOwnerId(FIELD_DATA_TYPE, ownerId)
				.all(fieldDataItem -> {
					final Optional<String> idOptional = Optional.ofNullable(fieldDataItem.getData())
							.flatMap(fieldDataItemData -> getOptionalStringValue(fieldDataItemData, ID_KEY));
					return idOptional.map(id -> !Objects.equals(id, fieldId)).orElse(Boolean.TRUE);
				});
	}

	@Override
	public String name() {
		return this.getClass().getSimpleName();
	}
}
