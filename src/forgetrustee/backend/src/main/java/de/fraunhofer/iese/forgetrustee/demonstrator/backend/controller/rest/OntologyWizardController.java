
/*
 * Copyright (C) 2026 Fraunhofer IESE
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest;
/* Created by chwalek on 05.11.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.ApiErrorDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.RequestBodyAddRelationDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.RequestBody_AddCommentDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.RequestBody_CreateIndividualDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.TtlToModelRequest;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.response.DataTrusteeWizardBuildResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclReportDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.ConfigurationStrings;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper.DataTrusteeModelUiDocumentationMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper.OntologyDocumentationDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper.OntologyDocumentationLoader;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyWizardService;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.MetadataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/ontology/wizard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ontology Wizard Controller", description = "Provides endpoints for building and manipulating ontology models based on the Data Trustee Model. This includes creating new ontology instances, adding comments, and managing relationships between concepts.")
public class OntologyWizardController {

  private final OntologyWizardService ontologyWizardService;

  private final DataTrusteeWizardRequestMapper dataTrusteeWizardRequestMapper;

  private final DataTrusteeModelMapper dataTrusteeModelMapper;

  private final DataTrusteeModelResponseMapper dataTrusteeModelResponseMapper;

  private final MetadataService metadataService;

  @PostMapping(value = "/buildDataTrusteeModel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Build a Data Trustee ontology model")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Ontology model successfully built", content = @Content(schema = @Schema(implementation = DataTrusteeWizardBuildResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
  })
  @WithRateLimitProtection
  public ResponseEntity<DataTrusteeWizardBuildResponseDto> buildDataTrusteeModel(
      @RequestBody DataTrusteeWizardRequestDto wizardRequestDto) throws IOException {
    log
        .debug("Received request to build Data Trustee Model with name: {}",
            wizardRequestDto.getCore().getDataTrusteeName());
    final long start = System.currentTimeMillis();

    // convert from DTO to internal model and build ontology in the service
    log.debug("Mapping DataTrusteeWizardRequestDto to DataTrusteeModel");
    final DataTrusteeWizardModel wizardModel = this.dataTrusteeWizardRequestMapper
        .toModel(wizardRequestDto);

    log.debug("Building ontology model from DataTrusteeModel");
    final OntModel ontModel = this.ontologyWizardService.buildFromDataTrusteeModel(wizardModel);

    log.debug("Converting ontology model to TTL format");
    final String ttl = this.ontologyWizardService.toTTL(ontModel);

    log.debug("Validating ontology model with SHACL");
    final ShaclReportDTO validationReport = this.ontologyWizardService
        .validate(ontModel, wizardModel, "default");

    final DataTrusteeModelResponseDto dataTrusteeModelResponseDto = this.dataTrusteeModelResponseMapper
        .toDto(this.dataTrusteeModelMapper.toModel(wizardModel));

    final String executiveSummary = this.ontologyWizardService
        .generateExecutiveSummary(wizardModel);

    // Wenn der Service ohne Exception zurückkommt, ist core garantiert vorhanden
    final String ontologyURI = wizardModel.getCore().getOntologyURI();

    final long end = System.currentTimeMillis();
    final long timeNeeded = end - start;
    log.debug("Ontology model built and validated in {} ms", timeNeeded);

    log
        .debug("Building and returning result with ontology URI: {} and validation report",
            ontologyURI);
    OntologyDocumentationDto docs =
            OntologyDocumentationLoader.load(
                    Path.of("data/ontology-comments-labels.json")
            );

    final DataTrusteeWizardBuildResponseDto result = new DataTrusteeWizardBuildResponseDto();
    result.setRequest(wizardRequestDto);
    result.setDataTrusteeModel(dataTrusteeModelResponseDto);
    result.setTtl(ttl);
    result.setShaclReport(validationReport);
    result.setExecutiveSummary(executiveSummary);

    DataTrusteeModelUiDocumentationMapper.enrich(
            result.getDataTrusteeModel(),
            docs,
            "de"
    );

    result
        .getDataTrusteeModel()
        .setPresentation(this.metadataService.getResultPresentation("compare", "de"));

    return ResponseEntity.ok(result);
  }

  @PostMapping("/translateToDataTrusteeDataModel")
  public DataTrusteeWizardRequestDto fromTtl(@RequestBody TtlToModelRequest request) {
    return this.dataTrusteeWizardRequestMapper
        .toDto(
            this.ontologyWizardService.loadFromTtl(request.getTtlPath(), request.getOntologyURI()));
  }

  @PostMapping("/translateToExecutiveSummary")
  public String fromTtl(@RequestBody DataTrusteeWizardRequestDto modelDto) {
    return this.ontologyWizardService
        .generateExecutiveSummary(this.dataTrusteeWizardRequestMapper.toModel(modelDto));
  }

  //@PostMapping("/newOntologyInstance")
  /**
   * public ResponseEntity<String> createNewOntologyInstance(
   * 
   * @Parameter(description = "URI der neu zu erstellenden Ontologie", example =
   *                        "https://www.example.de/ontology#") @RequestParam String newOntologyURI)
   *                        {
   *                        if (newOntologyURI == null || newOntologyURI.isBlank()) {
   *                        return ResponseEntity.badRequest().build();
   *                        }
   *                        try {
   *                        final String createdUri = this.ontologyWizardService
   *                        .createNewInstanceOfOntology(newOntologyURI);
   *                        return ResponseEntity.created(URI.create(createdUri)).build();
   *                        } catch (final IllegalArgumentException e) {
   *                        return ResponseEntity.badRequest().body(e.getMessage());
   *                        } catch (final Exception e) {
   *                        return ResponseEntity.internalServerError().body("Internal error");
   *                        }
   *                        }
   **/

  //@PostMapping("/newDataTrustee")
  public ResponseEntity<String> createNewDataTrustee(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @Parameter(description = "Name der neu zu erstellenden Datentreuhand", example = "NewDataTrust") @RequestParam String dataTrusteeName) {
    final ForgeIndividual dataTrustee = this.ontologyWizardService
        .createDataTrustee(dataTrusteeName, ontologyURI, ConfigurationStrings.lang_setting);
    return ResponseEntity.accepted().body("DataTrustee created \n" + dataTrustee.toString());
  }

  //@PostMapping("/newDataTrusteeModel")
  public ResponseEntity<String> createNewDataTrusteeModel(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @Parameter(description = "Name des neu zu erstellenden Datentreuhandmodells", example = "NewDataTrustModel") @RequestParam String dataTrusteeModelName) {
    final ForgeIndividual dataTrusteeModel = this.ontologyWizardService
        .createDataTrusteeModel(dataTrusteeModelName, ontologyURI,
            ConfigurationStrings.lang_setting);
    return ResponseEntity.accepted().body("DataTrustee created \n" + dataTrusteeModel.toString());
  }

  //@PostMapping("/newActor")
  public ResponseEntity<String> createNewActor(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @Parameter(description = "Name des neu zu erstellenden Akteurs", example = "Fahrzeughalter") @RequestParam String actorName,

      @Parameter(description = "URI des Unterklasse des Akteurs", example = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#AffectedParty") @RequestParam String roleURI) {
    final ForgeIndividual actor = this.ontologyWizardService
        .createActorInOntology(actorName, ontologyURI, roleURI, ConfigurationStrings.lang_setting);
    return ResponseEntity.accepted().body("Actor created \n" + actor.toString());
  }

  //@PostMapping("/newData")
  public ResponseEntity<String> createNewData(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @Parameter(description = "Name der neu zu erstellenden Datenkategorie", example = "TelemetrieDaten") @RequestParam String dataName) {
    final ForgeIndividual data = this.ontologyWizardService
        .createDataInOntology(dataName, ontologyURI, ConfigurationStrings.lang_setting);
    return ResponseEntity.accepted().body("Data created \n" + data.toString());
  }

  //@PostMapping("/newIndividual")
  public ResponseEntity<String> createNewIndividual(
      @RequestBody RequestBody_CreateIndividualDTO requestBody) {
    final ForgeIndividual data = this.ontologyWizardService
        .createIndividual(requestBody.getIndividualName(), requestBody.getOntologyURI(),
            requestBody.getClassURI(), ConfigurationStrings.lang_setting);
    return ResponseEntity.accepted().body("Data created \n" + data.toString());
  }

  //@PostMapping("/addComment")
  public ResponseEntity<String> addComment(@RequestBody RequestBody_AddCommentDTO addCommentDTO) {
    final ForgeIndividual addedCommentIndividual = this.ontologyWizardService
        .addLiteral(addCommentDTO.getSubjectURI(), RDFS.comment.getURI(),
            addCommentDTO.getLiteral(), addCommentDTO.getLang(), addCommentDTO.getOntologyURI());
    return ResponseEntity.accepted().body(addedCommentIndividual.toString());
  }

  //@PatchMapping("/changeComment") //+delete TODO
  public ResponseEntity<String> changeComment(
      @RequestBody RequestBody_AddCommentDTO changeCommentDTO) {
    final ForgeIndividual addedCommentIndividual = this.ontologyWizardService
        .addLiteral(changeCommentDTO.getSubjectURI(), RDFS.comment.getURI(),
            changeCommentDTO.getLiteral(), changeCommentDTO.getLang(),
            changeCommentDTO.getOntologyURI());
    return ResponseEntity.accepted().body("Accepted");
  }

  //@PostMapping("/addSubtype")
  public ResponseEntity<String> addSubtype(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @Parameter(description = "URI des Subjekt", example = "https://www.example.de/ontology#Fahrzeughalter") @RequestParam String subjectURI,

      @Parameter(description = "URI Instanz-Ontologie", example = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner") @RequestParam String subtypeURI) {
    final ForgeIndividual addedNewSubtype = this.ontologyWizardService
        .addNewSubtype(ontologyURI, subjectURI, subtypeURI);
    return ResponseEntity.accepted().body(addedNewSubtype.toString());
  }

  //@PatchMapping("/removeSubtype") //+delete TODO
  public ResponseEntity<String> removeSubtype(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @Parameter(description = "URI des Subjekt", example = "https://www.example.de/ontology#Fahrzeughalter") @RequestParam String subjectURI,

      @Parameter(description = "URI Instanz-Ontologie", example = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner") @RequestParam String subtypeURI) {
    final ForgeIndividual removeSubtype = this.ontologyWizardService
        .removeNewSubtype(ontologyURI, subjectURI, subtypeURI);
    return ResponseEntity.accepted().body(removeSubtype.toString());
  }

  //For Frontend S-P-O
  //AffectedParty - hasAffiliation - Affiliation
  //AffectedParty - wirdVertretenVon - DataOwner
  //DataOwner - hasAffiliation - Affiliation
  // @PostMapping("/addRelation")
  public ResponseEntity<String> addRelation(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @RequestBody RequestBodyAddRelationDTO addRelationDTO) {
    this.ontologyWizardService
        .addProperty(addRelationDTO.getSubject_URI(), addRelationDTO.getProperty_URI(),
            addRelationDTO.getObject_URI(), ontologyURI,
            addRelationDTO.isRelationBetweenIndividuals());
    return ResponseEntity
        .accepted()
        .body("Relation "
            + addRelationDTO.getProperty_URI()
            + " between subject "
            + addRelationDTO.getSubject_URI()
            + " and object "
            + addRelationDTO.getObject_URI()
            + " created.");
  }

  //@DeleteMapping("/removeRelation")
  public ResponseEntity<String> removeRelation(@RequestParam String ontologyURI,
      @RequestBody RequestBodyAddRelationDTO addRelationDTO) {
    this.ontologyWizardService
        .removeProperty(addRelationDTO.getSubject_URI(), addRelationDTO.getProperty_URI(),
            addRelationDTO.getObject_URI(), ontologyURI, true);
    return ResponseEntity
        .accepted()
        .body("Relation "
            + addRelationDTO.getProperty_URI()
            + " between subject "
            + addRelationDTO.getSubject_URI()
            + " and object "
            + addRelationDTO.getObject_URI()
            + " deleted.");
  }

  //For Frontend S-P-O
  //AffectedParty - hasAffiliation - Affiliation
  //AffectedParty - wirdVertretenVon - DataOwner
  //DataOwner - hasAffiliation - Affiliation
  //@PostMapping("/addRelationToConcept")
  public ResponseEntity<String> addRelationToConcepz(
      @Parameter(description = "URI Instanz-Ontologie", example = "https://www.example.de/ontology#") @RequestParam String ontologyURI,

      @RequestBody RequestBodyAddRelationDTO addRelationDTO) {
    this.ontologyWizardService
        .addProperty(addRelationDTO.getSubject_URI(), addRelationDTO.getProperty_URI(),
            addRelationDTO.getObject_URI(), ontologyURI, false);
    return ResponseEntity
        .accepted()
        .body("Relation "
            + addRelationDTO.getProperty_URI()
            + " between subject "
            + addRelationDTO.getSubject_URI()
            + " and object "
            + addRelationDTO.getObject_URI()
            + " created.");
  }

  //@DeleteMapping("/removeRelationFromConcept")
  public ResponseEntity<String> removeRelationFromConcept(@RequestParam String ontologyURI,
      @RequestBody RequestBodyAddRelationDTO addRelationDTO) {
    this.ontologyWizardService
        .removeProperty(addRelationDTO.getSubject_URI(), addRelationDTO.getProperty_URI(),
            addRelationDTO.getObject_URI(), ontologyURI, false);
    return ResponseEntity
        .accepted()
        .body("Relation "
            + addRelationDTO.getProperty_URI()
            + " between subject "
            + addRelationDTO.getSubject_URI()
            + " and object "
            + addRelationDTO.getObject_URI()
            + " deleted.");
  }
}
