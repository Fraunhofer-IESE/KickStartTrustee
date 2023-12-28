package de.fhg.iese.kickstarttrustee.owner.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.owner.business.service.OwnerAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/owner/account")
@Tag(name = "owner")
public class OwnerAccountController {
    private final OwnerAccountService ownerAccountService;

    public OwnerAccountController(OwnerAccountService ownerAccountService) {
        this.ownerAccountService = ownerAccountService;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String id) {
        return ownerAccountService.deleteAccountById(id);
    }
}
