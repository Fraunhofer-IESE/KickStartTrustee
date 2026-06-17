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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper;
/* Created by chwalek on 02.07.2025 */

import lombok.experimental.UtilityClass;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

@UtilityClass
public class StaticText {

  public static final String REGEX_FILE_NAME = "[\\\\/:*?\"<>|]";

  public static final String KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE = "https://forgetrustee.kickstarttrustee.de/ontology/";

  public static final String FORGETRUSTEE_AFFECTED_PARTY ="https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#AffectedParty";
  public static final String FORGETRUSTEE_DATA_OWNER ="https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner";
  public static final String FORGETRUSTEE_DATA_PROVIDER ="https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataProvider";
  public static final String FORGETRUSTEE_DATA_CONSUMER ="https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataConsumer";

  public static final String FORGETRUSTEE_SOURCE_SYSTEM = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#SourceSystem";
  public static final String FORGETRUSTEE_TARGET_SYSTEM = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#TargetSystem";

  public static final String FORGETRUSTEE_PREPARATION_TECHNIQUE = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#PreparationTechnique";
  public static final String FORGETRUSTEE_ANALYSIS_TECHNIQUE = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#AnalysisTechnique";

  @Deprecated
  public static final String FORGETRUSTEE_SECURITY_TECHNIQUE = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#SecurityTechnique";

  public static final String FORGETRUSTEE_SECURITY_RECEPTION_TECHNOQUE = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#SecurityReceptionTechnique" ;


  public static final String PROP_PROCESSING_BASIS_TITLE = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#processingBasisTitle";
  public static final String PROP_PROCESSING_BASIS_DESCRIPTION = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#processingBasisDescription";

  public static final String FORGETRUSTEE_PROP_SECURED_BY = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#securedByTechnique";

  public static final String FAKE_CARUSO_IRI = "http://example.org/fake-caruso-ontology/";

  public static final String SKOS_CONCEPT_URI = "http://www.w3.org/2004/02/skos/core#Concept";
  public static final String SELECTION_STATUS_URI = "https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#SelectionStatus";
  public static final String OWL_CLASS_URI = "http://www.w3.org/2002/07/owl#Class";

  //===== Example files

  public static final String inputCore = "dtm-core.ttl";

  public static final String inputBusiness = "dtm-business-extension.ttl";

  public static final String inputConsent = "dtm-consent-extension.ttl";

  public static final String inputSecurity = "dtm-security-extension.ttl";

  public static final String inputData = "dtm-data-extension.ttl";

  public static final String inputMisc = "dtm-misc-extension.ttl";

  public static final String pathToCoreShape = "dtm-shapes.ttl";

  public static final String prefixCore = "dtm-core";

  public static final String prefixBusiness = "dtm-business";

  public static final String prefixConsent = "dtm-consent";

  public static final String prefixSecurity = "dtm-sec";

  public static final String prefixData = "dtm-data";

  public static final String prefixMisc = "dtm-misc";

  //===== Wizard Prefix Helper

  public static final String PREFIX_RECEPTION = "Reception";
  public static final String PREFIX_PREPARATION = "Preparation";
  public static final String PREFIX_FORWARDING = "Forwarding";
  public static final String PREFIX_STORAGE = "Storage";
  public static final String PREFIX_ANALYSIS = "Analysis";

  public static final String PREFIX_CUSTOM = "Custom";
  public static final String PREFIX_SECURITY = "Security";

  public static final String PREFIX_SOURCE_SYSTEM = "SourceSystem";
  public static final String PREFIX_TARGET_SYSTEM = "TargetSystem";

  //=====

  public static final String TURTLE = "TURTLE";

  public static final String RDFS_LABEL = "rdfs:label";

  public static final String RDF_PROPERTY = RDF.getURI() + "Property";

  public static final String RDF_LIST = RDF.getURI() + "List";

  public static final String RDFS_CLASS = RDFS.getURI() + "Class";

  public static final String RDFS_RESOURCE = RDFS.getURI() + "Resource";

  public static final String OWL_CLASS = OWL.getURI() + "Class";

  public static final String OWL_THING = OWL.getURI() + "Thing";

  public static final String OWL_NOTHING = OWL.getURI() + "Nothing";

  public static final String OWL_ONTOLOGY = OWL.getURI() + "Ontology";

  public static final String OWL_OBJECT_PROPERTY = OWL.getURI() + "ObjectProperty";

  public static final String OWL_DATATYPE_PROPERTY = OWL.getURI() + "DatatypeProperty";

  public static final String PREDICATE_TOP_OBJECT_PROPERTY = "http://www.w3.org/2002/07/owl#topObjectProperty";

  public static final String PREDICATE_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";


}
