//package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation;
///* Created by chwalek on 07.08.2025 */
//
//import datamodel.ForgeClass;
//import datamodel.ForgeIndividual;
//import examples.fakeDatabases.version_FakeCarusoExampleV2.FakeDatabase;
//import apache_jena.JenaTranslater;
//import org.apache.jena.ontology.*;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.util.iterator.ExtendedIterator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//
//import static helper.StaticText.*;
//
//public class OntologyNavigation_ImplOLD implements OntologyNavigation {
//
//    private static final Logger LOG = LoggerFactory.getLogger(OntologyNavigation_ImplOLD.class);
//
//    private FakeDatabase database;
//
//    OntModel dt_ontology;
//    Map<String, Model> database_ontology;
//
//    public OntologyNavigation_ImplOLD(OntModel dt_ontology, Map<String, Model> database_ontology){
//        this.dt_ontology = dt_ontology;
//        this.database_ontology = database_ontology;
//    }
//
//    public OntologyNavigation_ImplOLD(FakeDatabase database){
//        this.database = database;
//    }
//
//    @Override
//    public List<OntClass> getAllOntologyClasses() {
//        return List.of();
//    }
//
//    public List<OntClass> getAllOntologyClasses(String[] extensionPrefix) {
//        // 2. Core-Vokabular, das wir überspringen möchten
//        final Set<String> CORE_CLASSES = Set.of(
//                RDF_PROPERTY,
//                RDF_LIST,
//                RDFS_CLASS,
//                RDFS_RESOURCE,
//                OWL_CLASS,
//                OWL_THING,
//                OWL_NOTHING,
//                OWL_ONTOLOGY,
//                OWL_OBJECT_PROPERTY,
//                OWL_DATATYPE_PROPERTY
//        );
//
//        // 4. Ergebnis-Liste vorbereiten
//        List<OntClass> userClasses = new ArrayList<>();
//
//        // 5. Über alle benannten Klassen iterieren (Iterator statt toList() spart Speicher)
//        ExtendedIterator<OntClass> iter = dt_ontology.listNamedClasses();
//        while (iter.hasNext()) {
//            OntClass cls = iter.next();
//
//            // a) Anonyme Klassen überspringen
//            if (cls.isAnon()) {
//                continue;
//            }
//
//            String uri = cls.getURI();
//
//            // b) Core-Klassen überspringen
//            if (CORE_CLASSES.contains(uri)) {
//                continue;
//            }
//
//            // c) Nur unser eigenes Vokabular (optional)
//            if (!uri.startsWith(KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE)) {
//                continue;
//            }
//
//            userClasses.add(cls);
//        }
//
//        if(extensionPrefix == null || extensionPrefix.length == 0){
//            return userClasses;
//        }
//
//        // 5. Andernfalls: filtere nur die Klassen, deren URI mit einem der
//        //    angegebenen Prefixe + "#" beginnt
//        List<String> prefixesWithHash = Arrays.stream(extensionPrefix)
//                .map(p -> p.endsWith("#") ? p : p + "#")
//                .toList();
//
//        List<OntClass> filteredClasses = new ArrayList<>();
//        for (OntClass cls : userClasses) {
//            String uri = cls.getURI();
//            LOG.debug("▶ Prüfe OntClass: {}", uri);
//
//            boolean keep = false;
//            for (String prefix : prefixesWithHash) {
//                LOG.debug("   - vergleiche mit Prefix „{}", prefix);
//                if (uri.contains(prefix)) {
//                    LOG.debug("     ✔ match: {}", prefix);
//                    keep = true;
//                    break;
//                } else {
//                    LOG.debug("     ✘ kein match");
//                }
//            }
//
//            if (keep) {
//                LOG.debug("   => behalte {}", uri);
//                filteredClasses.add(cls);
//            } else {
//                LOG.debug("   => verwerfe {}", uri);
//            }
//        }
//        LOG.debug("Ergebnis: {} Klassen behalten.", filteredClasses.size());
//
//        return filteredClasses;
//
//    }
//
//
//
//
//    public OntClass getOntologyClass(String uri) {
//        return dt_ontology.getOntClass(uri);
//    }
//
//
//    public List<Individual> getAllIndividuals(String ontologyUri) {
//        return List.of();
//    }
//
//
//
//    public List<Individual> getAllIndividuals(String ontologyUri, String... extensionPrefix) {
//        OntModel modelToLookIn = (OntModel) this.database_ontology.get(ontologyUri);
//
//        // 2. Core-Vokabular, das wir überspringen möchten
//        final Set<String> CORE_CLASSES = Set.of(
//                RDF_PROPERTY,
//                RDF_LIST,
//                RDFS_CLASS,
//                RDFS_RESOURCE,
//                OWL_CLASS,
//                OWL_THING,
//                OWL_NOTHING,
//                OWL_ONTOLOGY,
//                OWL_OBJECT_PROPERTY,
//                OWL_DATATYPE_PROPERTY
//        );
//
//        // 4. Ergebnis-Liste vorbereiten
//        List<Individual> userClasses = new ArrayList<>();
//
//        // 5. Über alle benannten Klassen iterieren (Iterator statt toList() spart Speicher)
//        ExtendedIterator<Individual> iter = modelToLookIn.listIndividuals();
//        while (iter.hasNext()) {
//            Individual individual = iter.next();
//
//            // a) Anonyme Klassen überspringen
//            if (individual.isAnon()) {
//                continue;
//            }
//
//            String uri = individual.getURI();
//
//            // b) Core-Klassen überspringen
//            if (CORE_CLASSES.contains(uri)) {
//                continue;
//            }
//
//            // c) Nur unser eigenes Vokabular (optional)
//            //if (!uri.startsWith(KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE)) {
//            //    continue;
//            //}
//
//            userClasses.add(individual);
//        }
//
//        if(extensionPrefix == null || extensionPrefix.length == 0){
//            return userClasses;
//        }
//
//        // 5. Andernfalls: filtere nur die Klassen, deren URI mit einem der
//        //    angegebenen Prefixe + "#" beginnt
//        List<String> prefixesWithHash = Arrays.stream(extensionPrefix)
//                .map(p -> p.endsWith("#") ? p : p + "#")
//                .toList();
//
//        List<Individual> filteredIndividuals = new ArrayList<>();
//        for (Individual cls : userClasses) {
//            String uri = cls.getURI();
//            LOG.debug("▶ Prüfe Individual: {}", uri);
//
//            boolean keep = false;
//            for (String prefix : prefixesWithHash) {
//                LOG.debug("   - vergleiche mit Prefix „{}", prefix);
//                if (uri.contains(prefix)) {
//                    LOG.debug("     ✔ match: {}", prefix);
//                    keep = true;
//                    break;
//                } else {
//                    LOG.debug("     ✘ kein match");
//                }
//            }
//
//            if (keep) {
//                LOG.debug("   => behalte {}", uri);
//                filteredIndividuals.add(cls);
//            } else {
//                LOG.debug("   => verwerfe {}", uri);
//            }
//        }
//        LOG.debug("Ergebnis: {} Individual behalten.", filteredIndividuals.size());
//
//        return filteredIndividuals;
//    }
//
//    @Override
//    public Individual getIndividual(String individualURI, String ontologyURI) {
//        OntModel modelToLookIn = (OntModel) this.database.getOntology_database().get(ontologyURI);
//        return modelToLookIn.getIndividual(individualURI);
//    }
//
//    @Override
//    public ObjectProperty getPropertyByURI(String propertyURI, String modelURI) {
//        return null;
//    }
//
//    @Override
//    public String getOntologyIRI(Model model) {
//        OntModel ontModel = (OntModel) model;
//        Ontology o = ontModel.listOntologies().toList().get(0);
//        return o.getURI();
//    }
//
//    @Override
//    public String getOntologyIRI(boolean fromBase, Model model) {
//        if(!fromBase){
//            return getOntologyIRI(model);
//        }
//
//        return getOntologyIRI(database.getOntology_base());
//    }
//
//    public ForgeClass translateToForgeClass(OntClass cls) {
//        return JenaTranslater.toForgeClass(cls, dt_ontology);
//    }
//
//    public ForgeIndividual translateToForgeIndividual(Individual individual) {
//        return JenaTranslater.toForgeIndividual(individual);
//    }
//}
