package de.fhg.iese.kickstarttrustee.storage.api.controller;

import de.fhg.iese.kickstarttrustee.storage.api.dto.CreateDataItemDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.CreateMetaDataDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.DataItemDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.MetaDataDTO;
import de.fhg.iese.kickstarttrustee.storage.business.service.ConsumerDataStorageService;
import de.fhg.iese.kickstarttrustee.storage.business.service.MetaDataService;
import de.fhg.iese.kickstarttrustee.storage.business.service.ProviderDataStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/prosumer/data")
@Tag(name = "prosumer.data")
public class ProsumerDataStorageController {
	private final ConsumerDataStorageService consumerService;
	private final MetaDataService metaDataService;
	private final ProviderDataStorageService providerService;


	public ProsumerDataStorageController(ConsumerDataStorageService consumerService, MetaDataService metaDataService,
										 ProviderDataStorageService providerService) {
		this.consumerService = consumerService;
		this.metaDataService = metaDataService;
		this.providerService = providerService;
	}

	private Mono<DataItemDTO> provideDataItem(final CreateDataItemDTO dataItemDTO) {
		final CreateMetaDataDTO metaData = dataItemDTO.metaData();
		return providerService.provideDataItem(metaData.dataItemType(), metaData.ownerId(), dataItemDTO.data())
				.map(DataItemDTO::new);
	}

	@GetMapping("/{id}")
	public Mono<DataItemDTO> getDataItemById(@PathVariable String id) {
		return consumerService.consumeDataItemById(id).map(DataItemDTO::new);
	}

	@GetMapping("/{id}/meta")
	public Mono<MetaDataDTO> getMetaDataById(@PathVariable String id) {
		return metaDataService.getMetaDataById(id).map(MetaDataDTO::new);
	}

	@GetMapping
	public Flux<DataItemDTO> getDataItems(@RequestParam String dataItemType, @RequestParam Optional<String> ownerId) {
		return ownerId.map(id -> consumerService.consumeDataItemsByDataItemTypeAndOwnerId(dataItemType, id))
				.orElseGet(() -> consumerService.consumeDataItemsByDataItemType(dataItemType))
				.map(DataItemDTO::new);
	}

	@PostMapping
	public Mono<DataItemDTO> createDataItem(@RequestBody @Valid Mono<CreateDataItemDTO> dataItem) {
		return dataItem.flatMap(this::provideDataItem);
	}

	@PostMapping("/bulk")
	public Flux<DataItemDTO> createDataItems(@RequestBody @Valid Flux<CreateDataItemDTO> dataItems) {
		return dataItems.flatMap(this::provideDataItem);
	}
}
