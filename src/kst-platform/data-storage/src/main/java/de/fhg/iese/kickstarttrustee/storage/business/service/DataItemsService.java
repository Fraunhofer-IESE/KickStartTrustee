package de.fhg.iese.kickstarttrustee.storage.business.service;

import de.fhg.iese.kickstarttrustee.storage.business.model.DataItem;
import de.fhg.iese.kickstarttrustee.storage.business.model.MetaData;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Service
public class DataItemsService {
	private final StorageService storageService;

	public DataItemsService(StorageService storageService) {
		this.storageService = storageService;
	}

	public Mono<DataItem> toDataItem(MetaData metaData) {
		final String id = metaData.getId();
		final Mono<Map<String, Object>> data = storageService.getData(id);
		return data.map(d -> new DataItem(id, metaData, d));
	}

	public Flux<DataItem> toDataItems(Flux<MetaData> metaData) {
		return metaData.flatMap(this::toDataItem);
	}

	public Flux<DataItem> getDataItemsForMetaData(Flux<MetaData> metaData,
												   Function<? super MetaData, ? extends Publisher<Boolean>> filter) {
		return metaData.filterWhen(filter).flatMap(this::toDataItem);
	}

}
