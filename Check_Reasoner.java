import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDFS;

import java.io.FileOutputStream;

public class Check_Reasoner {
    public static void main(String[] args) throws Exception {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
        String TBox = "TBox.ttl";
        String ABox = "ABox_Ontology.ttl";
        try {
            String NS = "http://www.sdmlab.org/pub/#";
            model.setNsPrefix("pub", NS);

            /** * Creating the TBox */
            // model.createClass(NS + "Author");
            // model.createClass(NS + "Paper");
            // OntClass authorClass = model.getOntClass(NS + "Author");
            // Resource r = model.createResource(NS + "cls-author");
            OntClass authorClass = model.createClass(NS + "Author");
            OntClass paperClass = model.createClass(NS + "Paper");

            // you cannot use Object/Data Type Properties here because it´s OWL syntax
            Property submits = model.createOntProperty(NS + "submits");
            submits.addProperty(RDFS.domain, authorClass);
            submits.addProperty(RDFS.range, paperClass);

            //Store the ontology
            FileOutputStream output = new FileOutputStream(TBox);
            model.write(output, "Turtle");

            /** * Creating the ABox */

            // This only creates the resources ... you have to rely on the reasoner to connect them to the classes via the properties rdfs:domain or rdfs:range
            // Resource ataur = model.createOntResource(NS + "aut-Ataur");
            // Resource paper = model.createOntResource(NS + "paper-101");

            // This will create instances/individuals of these Classes by explicitly putting rdf:type
            Individual ataur = model.createIndividual(NS + "aut-Ataur", authorClass);
            Individual paper = model.createIndividual(NS + "paper-101", paperClass);

            // Property submits = model.getProperty(NS + "submits");
            model.add(ataur, submits, paper);


            //Store the ontology
            output = new FileOutputStream(ABox);
            model.write(output, "Turtle");
            // rdfs_model.write(output);

            // InfModel rdfs_model = ModelFactory.createRDFSModel(model);
            // RDFDataMgr.write(System.out, model, Lang.TURTLE);


        } catch (Exception ex) {
            System.out.println("I am sorry, something went wrong!! : " + ex);
            throw new Exception(ex.getMessage());
        }
    }
}
