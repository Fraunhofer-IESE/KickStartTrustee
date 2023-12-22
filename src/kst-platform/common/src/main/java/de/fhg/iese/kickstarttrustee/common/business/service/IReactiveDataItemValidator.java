package de.fhg.iese.kickstarttrustee.common.business.service;

import java.util.List;

import de.fhg.iese.kickstarttrustee.common.business.model.IDataItem;

import reactor.core.publisher.Mono;

public interface IReactiveDataItemValidator {
    List<String> dataItemTypes();
    Mono<Boolean> isValid(IDataItem dataItem);
    String name();
}
