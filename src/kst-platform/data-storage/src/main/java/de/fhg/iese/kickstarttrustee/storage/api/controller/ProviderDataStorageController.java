package de.fhg.iese.kickstarttrustee.storage.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.storage.api.dto.CreateDataItemDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.CreateMetaDataDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.DataItemDTO;
import de.fhg.iese.kickstarttrustee.storage.business.service.ProviderDataStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/provider/data")
@Tag(name = "provider.data")
public class ProviderDataStorageController {
    private final ProviderDataStorageService dataItemService;

    public ProviderDataStorageController(ProviderDataStorageService dataItemService) {
        this.dataItemService = dataItemService;
    }

	private Mono<DataItemDTO> provideDataItem(final CreateDataItemDTO dataItemDTO) {
		final CreateMetaDataDTO metaData = dataItemDTO.metaData();
		return dataItemService.provideDataItem(metaData.dataItemType(), metaData.ownerId(), dataItemDTO.data())
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
