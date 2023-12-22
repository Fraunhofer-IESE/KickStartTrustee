package de.fhg.iese.kickstarttrustee.diseasewarning.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.diseasewarning.api.dto.CreateDiseaseWarningSettingsDTO;
import de.fhg.iese.kickstarttrustee.diseasewarning.api.dto.DiseaseWarningSettingsDTO;
import de.fhg.iese.kickstarttrustee.diseasewarning.api.dto.UpdateDiseaseWarningSettingsDTO;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.service.DiseaseWarningSettingsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/service/disease-warning/settings")
@Tag(name = "service.disease-warning")
public class DiseaseWarningSettingsController {
    private final DiseaseWarningSettingsService diseaseWarningSettingsService;

    public DiseaseWarningSettingsController(DiseaseWarningSettingsService diseaseWarningSettingsService) {
        this.diseaseWarningSettingsService = diseaseWarningSettingsService;
    }

    @PostMapping
    public Mono<DiseaseWarningSettingsDTO> createOwnSettings(@RequestBody CreateDiseaseWarningSettingsDTO settings) {
        final boolean isDiseaseReportProcessingEnabled = settings.isDiseaseReportProcessingEnabled();
        final boolean isDiseaseWarningCreationEnabled = settings.isDiseaseWarningCreationEnabled();
        final boolean isEmailNotificationEnabled = settings.isEmailNotificationEnabled();
        return diseaseWarningSettingsService
                .create(isDiseaseReportProcessingEnabled, isDiseaseWarningCreationEnabled, isEmailNotificationEnabled)
                .map(DiseaseWarningSettingsDTO::new);
    }

    @GetMapping("/own")
    public Mono<DiseaseWarningSettingsDTO> getOwnSettings() {
        return diseaseWarningSettingsService.getOwnSettings().map(DiseaseWarningSettingsDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<DiseaseWarningSettingsDTO> getSettingsById(@PathVariable String id) {
        return diseaseWarningSettingsService.getById(id).map(DiseaseWarningSettingsDTO::new);
    }

    @PutMapping("/{id}")
    public Mono<DiseaseWarningSettingsDTO> updateSettings(@PathVariable String id,
            @RequestBody UpdateDiseaseWarningSettingsDTO settings) {
        final boolean isDiseaseReportProcessingEnabled = settings.isDiseaseReportProcessingEnabled();
        final boolean isDiseaseWarningCreationEnabled = settings.isDiseaseWarningCreationEnabled();
        final boolean isEmailNotificationEnabled = settings.isEmailNotificationEnabled();
        return diseaseWarningSettingsService
                .update(id, isDiseaseReportProcessingEnabled, isDiseaseWarningCreationEnabled, isEmailNotificationEnabled)
                .map(DiseaseWarningSettingsDTO::new);
    }
}
