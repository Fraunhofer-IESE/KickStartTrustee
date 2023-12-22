package de.fhg.iese.kickstarttrustee.storage.business.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaException;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import reactor.core.publisher.Mono;

@Service
public class JsonSchemaValidator {
	private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidator.class);

	private final DataItemTypeService dataItemTypeService;
	private final ResourceLoader resourceLoader;
	private final JsonSchemaFactory jsonSchemaFactory;
	private final ObjectMapper mapper;

	public JsonSchemaValidator(DataItemTypeService dataItemTypeService, ResourceLoader resourceLoader, Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
		this.dataItemTypeService = dataItemTypeService;
		this.resourceLoader = resourceLoader;
		this.jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
		this.mapper = jacksonObjectMapperBuilder.build();
	}

	private Mono<JsonSchema> getJsonSchema(final String jsonSchemaLocation) {
		return Mono.fromCallable(() -> {
			if (jsonSchemaLocation.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
				final Resource jsonSchemaResource = resourceLoader.getResource(jsonSchemaLocation);
				try (final InputStream schemaStream = jsonSchemaResource.getInputStream()) {
					return jsonSchemaFactory.getSchema(schemaStream);
				} catch (IOException e) {
					log.warn("Could not read json schema file", e);
					return null;
				}
			} else {
				try {
					return jsonSchemaFactory.getSchema(ResourceUtils.toURI(jsonSchemaLocation));
				} catch (JsonSchemaException e) {
					log.warn("Could not read json schema file", e);
					return null;
				}
			}
		});
	}

	private Mono<Boolean> validate(final JsonSchema jsonSchema, Map<String, Object> data) {
		return Mono.fromCallable(() -> {
			final JsonNode jsonNode = mapper.valueToTree(data);
			return jsonSchema.validate(jsonNode);
		}).doOnNext(validationResult -> {
			if (validationResult == null || validationResult.isEmpty()) {
				return;
			}
			final String message = validationResult.stream()
												.map(ValidationMessage::getMessage)
												.collect(Collectors.joining(", "));
			log.warn("Invalid dataItem provided: {}", message);
		}).map(Set::isEmpty);
	}

	public Mono<Boolean> isValidDataItem(final String dataItemType, final Map<String, Object> data) {
		return dataItemTypeService.getJsonSchemaLocation(dataItemType)
							.flatMap(this::getJsonSchema)
							.flatMap(jsonSchema -> this.validate(jsonSchema, data))
							.defaultIfEmpty(Boolean.TRUE);
	}
}
