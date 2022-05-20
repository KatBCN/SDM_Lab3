

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/** Test Model 1 - create a model and write it in XML form to standard out
 */
public class CosmogOntology extends Object {
    
    public static void main (String args[]) {

        // Create an empty RDFS ontology model using RDFS inference entailment
        OntModel model = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM_RDFS_INF );
        // Define the name space
        String NS = "http://www.cosmog.org/graphdb/#";
        // Specify prefix name
        model.setNsPrefix("cosmog", NS);

        // Create TBOX

        // Define Classes

        // Define roles of people
        OntClass author = model.createClass(NS + "author");
        OntClass reviewer = model.createClass(NS + "reviewer");
        OntClass chair = model.createClass(NS + "chair");
        OntClass editor = model.createClass(NS + "editor");
        OntClass director = model.createClass(NS + "director");
        OntClass person = model.createClass(NS + "person");
        // Add subclasses of person
        person.addSubClass(author);
        person.addSubClass(reviewer);
        person.addSubClass(director);
        // Add subclasses of director
        director.addSubClass(chair);
        director.addSubClass(editor);

        // Define types of papers
        OntClass demoArticle = model.createClass(NS+ "demo_article");
        OntClass shortArticle = model.createClass(NS+ "short_article");
        OntClass fullArticle = model.createClass(NS+ "full_article");
        OntClass article = model.createClass(NS+ "article");
        OntClass poster = model.createClass(NS+ "poster");
        OntClass paper = model.createClass(NS + "paper");
        // Add subclasses of paper
        paper.addSubClass(poster);
        paper.addSubClass(article);
        // Add subclasses of article
        article.addSubClass(demoArticle);
        article.addSubClass(shortArticle);
        article.addSubClass(fullArticle);

        // Define types of venues
        OntClass journal = model.createClass(NS + "journal");
        OntClass symposium = model.createClass(NS + "symposium");
        OntClass workshop = model.createClass(NS + "workshop");
        OntClass expertGroup = model.createClass(NS + "expert_group");
        OntClass conference = model.createClass(NS + "conference");
        OntClass venue = model.createClass(NS + "venue");
        // Add subclasses of venue
        venue.addSubClass(journal);
        venue.addSubClass(conference);
        // Add subclasses of conference
        conference.addSubClass(symposium);
        conference.addSubClass(workshop);
        conference.addSubClass(expertGroup);

        // Define other classes
        OntClass topic = model.createClass(NS + "topic");
        OntClass decision = model.createClass(NS + "decision");


        // Define Properties with domain and range

        OntProperty writesPaper = model.createOntProperty(NS + "writes_paper");
        writesPaper.addDomain(author);
        writesPaper.addRange(paper);

        OntProperty writesReview = model.createOntProperty(NS + "writes_review");
        writesReview.addDomain(reviewer);
        writesReview.addRange(decision);

        OntProperty assigns = model.createOntProperty(NS + "assigns");
        assigns.addDomain(director);
        assigns.addRange(reviewer);

        OntProperty discussesPaper = model.createOntProperty(NS + "discusses_paper");
        discussesPaper.addDomain(decision);
        discussesPaper.addRange(paper);

        OntProperty discussesTopic = model.createOntProperty(NS + "discusses_topic");
        discussesTopic.addDomain(paper);
        discussesTopic.addRange(topic);

        OntProperty edits = model.createOntProperty(NS + "edits");
        edits.addDomain(editor);
        edits.addRange(journal);

        OntProperty organizes = model.createOntProperty(NS + "organizes");
        organizes.addDomain(chair);
        organizes.addRange(conference);

        OntProperty publishesPoster = model.createOntProperty(NS + "publishes_poster");
        publishesPoster.addDomain(conference);
        publishesPoster.addRange(poster);

        OntProperty publishesPaper = model.createOntProperty(NS + "publishes_paper");
        publishesPaper.addDomain(venue);
        publishesPaper.addRange(article);

        OntProperty relatedTo = model.createOntProperty(NS + "related_to");
        relatedTo.addDomain(venue);
        relatedTo.addRange(topic);

        
        // Write the ontology model to a file
        FileOutputStream output = null;
        try {
            output = new FileOutputStream("cosmog_tbox.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(output);
    }
}
