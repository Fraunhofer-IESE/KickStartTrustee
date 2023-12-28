package de.fhg.iese.kickstarttrustee.consent.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.consent.api.dto.ConsumerConsentDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.ConsumerConsentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/consumer/consent")
@Tag(name = "consumer.consent")
public class ConsumerConsentController {
    private final ConsumerConsentService consentService;

    public ConsumerConsentController(ConsumerConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping
    public Flux<ConsumerConsentDTO> getAllConsents(@RequestParam Optional<ConsentStatus> status) {
        return consentService.getAllConsents(status).map(ConsumerConsentDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<ConsumerConsentDTO> getConsentById(@PathVariable String id) {
        return consentService.getConsentById(id).map(ConsumerConsentDTO::new);
    }
}
