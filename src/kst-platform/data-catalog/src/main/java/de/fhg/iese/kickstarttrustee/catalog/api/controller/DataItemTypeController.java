package de.fhg.iese.kickstarttrustee.catalog.api.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.catalog.api.dto.DataItemTypeDTO;
import de.fhg.iese.kickstarttrustee.catalog.business.service.DataItemTypesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/catalog/data-item/type")
@Tag(name = "catalog.dataitem")
public class DataItemTypeController {
	private final DataItemTypesService dataItemTypesService;

	public DataItemTypeController(DataItemTypesService dataItemTypesService) {
		this.dataItemTypesService = dataItemTypesService;
	}

	@GetMapping
	public ResponseEntity<Flux<DataItemTypeDTO>> getDataItemTypes() {
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(60L, TimeUnit.SECONDS))
				.body(dataItemTypesService.getSupportedDataItemTypes().map(DataItemTypeDTO::new));
	}

	@GetMapping("/{id}/schema")
	public Mono<Resource> getSchemaForDataItemType(@PathVariable String id) {
		return dataItemTypesService.getSchemaForDataItemType(id);
	}
}
