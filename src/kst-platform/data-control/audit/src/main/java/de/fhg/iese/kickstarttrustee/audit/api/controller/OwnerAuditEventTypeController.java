package de.fhg.iese.kickstarttrustee.audit.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.audit.business.service.AuditLogEventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/owner/audit/event-type")
@Tag(name = "owner.audit")
public class OwnerAuditEventTypeController {
    private final AuditLogEventService auditLogEventService;

    public OwnerAuditEventTypeController(AuditLogEventService auditLogEventService) {
        this.auditLogEventService = auditLogEventService;
    }

    @GetMapping
    public Mono<List<String>> getEventTypes() {
        return auditLogEventService.getAllEventTypes().collectList();
    }
}
