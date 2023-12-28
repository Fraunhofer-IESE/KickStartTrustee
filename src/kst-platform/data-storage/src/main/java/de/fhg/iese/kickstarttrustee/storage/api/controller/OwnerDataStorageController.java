package de.fhg.iese.kickstarttrustee.storage.api.controller;

import de.fhg.iese.kickstarttrustee.storage.business.service.OwnerDataStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import de.fhg.iese.kickstarttrustee.storage.api.dto.DataItemDTO;
import de.fhg.iese.kickstarttrustee.storage.api.dto.MetaDataDTO;
import de.fhg.iese.kickstarttrustee.storage.business.service.MetaDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/owner/data")
@Tag(name = "owner.data")
public class OwnerDataStorageController {
    private final OwnerDataStorageService ownerDataStorageService;
    private final MetaDataService metaDataService;

    public OwnerDataStorageController(OwnerDataStorageService ownerDataStorageService, MetaDataService metaDataService) {
        this.ownerDataStorageService = ownerDataStorageService;
        this.metaDataService = metaDataService;
    }

	@GetMapping
	public Flux<DataItemDTO> getDataItems(@RequestParam(required = false) Optional<String> dataItemType,
										  @RequestParam(required = false) Optional<String> providerId) {
		return ownerDataStorageService.getOwnDataItems(dataItemType, providerId).map(DataItemDTO::new);
	}

    @GetMapping("/{id}")
    public Mono<DataItemDTO> getDataItemById(@PathVariable String id) {
        return ownerDataStorageService.getOwnDataItemById(id).map(DataItemDTO::new);
    }

    @GetMapping("/{id}/meta")
    public Mono<MetaDataDTO> getMetaDataById(@PathVariable String id) {
        return metaDataService.getOwnMetaDataById(id).map(MetaDataDTO::new);
    }

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteDataItemById(@PathVariable String id) {
		return ownerDataStorageService.deleteOwnDataItemById(id);
	}
}
