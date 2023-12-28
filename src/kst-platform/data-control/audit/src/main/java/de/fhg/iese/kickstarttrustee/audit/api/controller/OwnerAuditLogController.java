package de.fhg.iese.kickstarttrustee.audit.api.controller;

import java.time.Instant;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.audit.api.dto.OwnerAuditLogEntryDTO;
import de.fhg.iese.kickstarttrustee.audit.business.service.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/owner/audit/log")
@Tag(name = "owner.audit")
public class OwnerAuditLogController {
    private final AuditLogService auditLogService;

    public OwnerAuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public Flux<OwnerAuditLogEntryDTO> getAuditLogEntries(@RequestParam Instant begin, @RequestParam Instant end,
            @RequestParam Optional<String> eventType) {
        return auditLogService.getMyEntries(begin, end, eventType).map(OwnerAuditLogEntryDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<OwnerAuditLogEntryDTO> getAuditLogEntryById(@PathVariable String id) {
        return auditLogService.getMyEntryById(id).map(OwnerAuditLogEntryDTO::new);
    }
}
