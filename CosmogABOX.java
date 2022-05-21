
import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.util.FileManager;
import java.io.*;


public class CosmogABOX extends Object {


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

        // Define attributes - properties which will be literals

        // DatatypeProperty is only supported with OWL models, not RDFS - the following lines throw exceptions
        // DatatypeProperty title = model.createDatatypeProperty(NS + "title");
        // DatatypeProperty name = model.createDatatypeProperty(NS + "name");
        // DatatypeProperty year = model.createDatatypeProperty(NS + "year");
        OntProperty title = model.createOntProperty(NS + "title");
        OntProperty name = model.createOntProperty(NS + "name");
        OntProperty year = model.createOntProperty(NS + "year");


        // Write the TBOX model to a file
        FileOutputStream output = null;
        try {
            output = new FileOutputStream("cosmog_tbox_2.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(output);

        // Create ABOX

        // create the individuals
        Individual topic1 = topic.createIndividual(NS + "databases");
        Individual topic2 = topic.createIndividual(NS + "bigdata");
        Individual journal1 = journal.createIndividual(NS + "DataIntell-v4-ed1");
        Individual editor1 = editor.createIndividual(NS + "Kat");
        Individual paper1 = paper.createIndividual(NS + "Integrating");
        Individual paper2 = paper.createIndividual(NS + "DatabasesPaper");
        Individual paper3 = paper.createIndividual(NS + "BigDataPaper");
        Individual paper4 = paper.createIndividual(NS + "DataBasesPaperPim");
        Individual author1 = author.createIndividual(NS + "01/4737");
        Individual author2 = author.createIndividual(NS + "PimAuthor");
        Individual conference1 = conference.createIndividual(NS + "IEEE-DB");
        Individual conference2 = conference.createIndividual(NS + "IEEE-BD");

        // add the properties
        journal1.addProperty(publishesPaper, paper1);
        journal1.addProperty(relatedTo, topic1);
        journal1.addProperty(year, model.createLiteral("2022"));
        editor1.addProperty(edits, journal1);
        editor1.addProperty(name, "Kat Weissman");
        author1.addProperty(writesPaper, paper1);
        author1.addProperty(writesPaper, paper2);
        author1.addProperty(writesPaper, paper3);
        author1.addProperty(name, model.createLiteral("Zhibin Chen"));
        author2.addProperty(writesPaper, paper4);
        author2.addProperty(name, model.createLiteral("Pim Schoolkate"));
        paper1.addProperty(discussesTopic, topic1);
        paper1.addProperty(title, model.createLiteral("Integrating Manifold Knowledge for Global Entity Linking with Heterogeneous Graphs."));
        conference1.addProperty(relatedTo, topic1);
        conference2.addProperty(relatedTo, topic2);
        conference1.addProperty(publishesPaper, paper2);
        conference1.addProperty(publishesPaper, paper4);
        conference2.addProperty(publishesPaper, paper3);
        paper2.addProperty(title, model.createLiteral("The Database paper Title"));
        paper3.addProperty(title, model.createLiteral("The Big Data paper Title"));
        paper4.addProperty(title, model.createLiteral("Pim's Databases Paper Title"));

        // Write the ABOX model to a file
        output = null;
        try {
            output = new FileOutputStream("cosmog_abox_1.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(output);





    }
}
