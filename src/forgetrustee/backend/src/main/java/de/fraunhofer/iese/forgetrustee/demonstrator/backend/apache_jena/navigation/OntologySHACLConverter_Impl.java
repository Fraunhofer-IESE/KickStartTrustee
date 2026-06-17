
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation;
/* Created by chwalek on 12.08.2025 */

//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.OntModelFactory;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.ontapi.model.OntModel;
//import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.shacl.Shapes;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;

@Service
public class OntologySHACLConverter_Impl {

  /**
   * Diese Methode konvertiert ein OntModel in einen String im angegebenen Format.
   * 
   * @param  model  Das zu konvertierende Model.
   * @param  format Das Format, in dem das Model ausgegeben werden soll (z.B. "RDF/XML", "TURTLE").
   * @return        Der RDF-Inhalt des Modells als String.
   */
  public static String retrieveStringFromOntModel(Model model, String format) {
    // Erstelle einen StringWriter, um den RDF-Inhalt zu speichern
    StringWriter writer = new StringWriter();

    // Schreibe das Model in den StringWriter im angegebenen Format
    model.write(writer, format);

    // Gib den Inhalt des StringWriters als String zurück
    return writer.toString();
  }

  public static OntModel retrieveOntModelFromText(String fileContent, String format) {

    // OWL + Rules inference (ähnlich zu OWL_MEM_MICRO_RULE_INF)
    OntModel model = OntModelFactory.createModel(OntSpecification.OWL2_DL_MEM);

    // format z.B. "TURTLE", "RDFXML", "JSONLD"
    Lang lang = RDFLanguages.nameToLang(format);
    if (lang == null) {
      throw new IllegalArgumentException("Unbekanntes RDF Format: " + format);
    }

    try (StringReader reader = new StringReader(fileContent)) {
      RDFDataMgr.read(model, reader, null, lang);
    }
    //RDFDataMgr.read(model, new StringReader(fileContent).toString(), lang);

    return model;
  }

  public static OntModel retrieveOntModelFromText(String fileContent, String format,
      String namespace) {
    // Ersatz für OWL_MEM_MICRO_RULE_INF -> Rules Inference
    OntModel model = OntModelFactory.createModel(OntSpecification.OWL2_FULL_MEM_RULES_INF);

    // Prefix setzen
    model.setNsPrefix("", namespace);

    // Format ermitteln (TTL, RDFXML, JSONLD, ...)
    Lang lang = RDFLanguages.nameToLang(format);
    if (lang == null) {
      throw new IllegalArgumentException("Unbekanntes RDF Format: " + format);
    }

    // RDF aus String laden
    RDFDataMgr.read(model, new StringReader(fileContent).toString(), lang);

    return model;
  }

  public static Shapes retrieveShapesFromSHACLText(String fileContent, String format) {
    // Erstelle ein Modell, das die SHACL-Daten hält
    Model model = ModelFactory.createDefaultModel();

    // Lese den RDF-Inhalt (SHACL-Text) in das Modell ein
    model.read(new java.io.StringReader(fileContent), null, format);

    // Verwende Jena SHACL, um die Shapes aus dem Modell zu extrahieren
    Shapes shapes = Shapes.parse(model);

    return shapes;
  }

  /**
   * Diese Methode konvertiert ein Shapes-Objekt in einen String im angegebenen Format.
   * 
   * @param  shapes Das Shapes-Objekt, das serialisiert werden soll.
   * @param  format Das Format, in dem die Shapes ausgegeben werden sollen (z.B. "TURTLE",
   *                  "RDF/XML").
   * @return        Der RDF-Inhalt des Shapes-Objekts als String.
   */
  public static String retrieveStringFromShape(Shapes shapes, String format) {
    // Erstelle ein temporäres Model
    Model model = ModelFactory.createDefaultModel();

    // Serialisiere die Shapes in das Model
    // Jena SHACL bietet nicht direkt eine Methode, um Shapes als Model zu extrahieren, aber wir können
    // die Shapes über eine SHACL-Validierung oder eine andere Methode serialisieren.
    // Hier verwenden wir `shapes.getGraph()` und fügen es zu einem Model hinzu.
    model.add((Model) shapes.getGraph());

    // Erstelle einen StringWriter, um den RDF-Inhalt zu speichern
    StringWriter writer = new StringWriter();

    // Schreibe das Modell in den StringWriter im angegebenen Format
    model.write(writer, format);

    // Gib den Inhalt des StringWriters als String zurück
    return writer.toString();
  }
}
