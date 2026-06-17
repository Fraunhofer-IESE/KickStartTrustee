//package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.factory;
///* Created by chwalek on 08.08.2025 */
//
//import examples.fakeDatabases.version_FakeCarusoExampleV2.FakeDatabase;
//import org.apache.jena.ontology.OntModel;
//import org.apache.jena.ontology.Ontology;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.ModelFactory;
//
//public class OntologyFactory_OLD {
//
//    private FakeDatabase database;
//
//    public OntologyFactory_OLD(FakeDatabase database){
//        this.database = database;
//    }
//
//
//    public static OntModel createOntModel(String URI){
//        OntModel model = ModelFactory.createOntologyModel();
//        model.createOntology(URI);
//        return model;
//    }
//
//
//    public static String getOntologyIRI(Model model){
//        OntModel ontModel = (OntModel) model;
//        Ontology o = ontModel.listOntologies().toList().get(0);
//        return o.getURI();
//    }
//
//
//}
