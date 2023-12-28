package de.fhg.iese.kickstarttrustee.owner.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.owner.api.dto.CreateOwnerProfileDTO;
import de.fhg.iese.kickstarttrustee.owner.api.dto.OwnerProfileDTO;
import de.fhg.iese.kickstarttrustee.owner.api.dto.UpdateOwnerProfileDTO;
import de.fhg.iese.kickstarttrustee.owner.business.service.OwnerProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/owner/profile")
@Tag(name = "owner")
public class OwnerProfileController {
    private final OwnerProfileService ownerProfileService;

    public OwnerProfileController(OwnerProfileService ownerProfileService) {
        this.ownerProfileService = ownerProfileService;
    }

    @GetMapping("/me")
    public Mono<OwnerProfileDTO> getMyProfile() {
        return ownerProfileService.getOwnProfile().map(OwnerProfileDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<OwnerProfileDTO> getProfileById(@PathVariable String id) {
        return ownerProfileService.getProfileById(id).map(OwnerProfileDTO::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OwnerProfileDTO> createMyProfile(@RequestBody @Valid CreateOwnerProfileDTO createOwnerProfileDTO) {
        return ownerProfileService.createOwnProfile(createOwnerProfileDTO.preferredLanguage())
                .map(OwnerProfileDTO::new);
    }

    @PutMapping("/{id}")
    public Mono<OwnerProfileDTO> updateMyProfile(@PathVariable String id,
            @RequestBody @Valid UpdateOwnerProfileDTO updateOwnerProfileDTO) {
        return ownerProfileService.updateOwnProfile(id, updateOwnerProfileDTO.preferredLanguage())
                .map(OwnerProfileDTO::new);
    }
}
