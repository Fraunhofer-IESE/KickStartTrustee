package de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation;
/* Created by chwalek on 13.03.2026 */


import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ShaclReportDTO {

    private boolean conforms;
    private int resultsCount;
    private List<ShaclResultDTO> results = new ArrayList<>();
}
