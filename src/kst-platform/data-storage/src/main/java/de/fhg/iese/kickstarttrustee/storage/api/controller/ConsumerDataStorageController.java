package de.fhg.iese.kickstarttrustee.storage.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.storage.api.dto.DataItemDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.MetaDataDTO;
import de.fhg.iese.kickstarttrustee.storage.business.service.ConsumerDataStorageService;
import de.fhg.iese.kickstarttrustee.storage.business.service.MetaDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/consumer/data")
@Tag(name = "consumer.data")
public class ConsumerDataStorageController {
    private final ConsumerDataStorageService consumerDataStorageService;
    private final MetaDataService metaDataService;

    public ConsumerDataStorageController(ConsumerDataStorageService consumerDataStorageService, MetaDataService metaDataService) {
        this.consumerDataStorageService = consumerDataStorageService;
        this.metaDataService = metaDataService;
    }

    @GetMapping("/{id}")
    public Mono<DataItemDTO> getDataItemById(@PathVariable String id) {
        return consumerDataStorageService.consumeDataItemById(id).map(DataItemDTO::new);
    }

    @GetMapping("/{id}/meta")
    public Mono<MetaDataDTO> getMetaDataById(@PathVariable String id) {
        return metaDataService.getMetaDataById(id).map(MetaDataDTO::new);
    }

    @GetMapping
    public Flux<DataItemDTO> getDataItems(@RequestParam String dataItemType, @RequestParam Optional<String> ownerId) {
		return ownerId.map(id -> consumerDataStorageService.consumeDataItemsByDataItemTypeAndOwnerId(dataItemType, id))
				.orElseGet(() -> consumerDataStorageService.consumeDataItemsByDataItemType(dataItemType))
				.map(DataItemDTO::new);
    }
}
