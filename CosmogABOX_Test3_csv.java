import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.atlas.csv.CSVParser;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLOutput;
import java.util.Iterator;


public class CosmogABOX_Test3_csv extends Object {


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
        OntProperty title = model.createOntProperty(NS + "title"); // for paper titles
        OntProperty venueTitle = model.createOntProperty(NS + "venueTitle"); // for venue titles
        OntProperty name = model.createOntProperty(NS + "name"); // for names of people
        OntProperty number = model.createOntProperty(NS + "number"); // journal numbers
        OntProperty volume = model.createOntProperty(NS + "volume"); // journal volumes
        OntProperty review = model.createOntProperty(NS + "review"); // review related to decision
        OntProperty location = model.createOntProperty(NS + "location"); // for conferences
        OntProperty keyword = model.createOntProperty(NS + "keyword"); // keyword for topic

/*

        // Write the TBOX model to a file
        FileOutputStream output = null;
        try {
            output = new FileOutputStream("cosmog_tbox_2.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(output);
*/
        // Create ABOX

        CSVParser reader = CSVParser.create("article_slice.csv");
        Iterator<List<String>> iterator = reader.iterator();
        List<String> columnNames = iterator.next();

        while (iterator.hasNext()) {
            List<String> line = iterator.next();

            Individual paper1 = paper.createIndividual(NS + line.get(columnNames.indexOf("article")));
            paper1.addProperty(title, line.get(columnNames.indexOf("title")));

            Individual journal1 = journal.createIndividual(NS + line.get(columnNames.indexOf("journalID")));
            journal1.addProperty(venueTitle, line.get(columnNames.indexOf("journal")));
            journal1.addProperty(number, line.get(columnNames.indexOf("number")));
            journal1.addProperty(volume, line.get(columnNames.indexOf("volume")));
            journal1.addProperty(publishesPaper, paper1);

            String[] topicString = line.get(columnNames.indexOf("topics")).split("|");
            List<String> topics = Arrays.asList(topicString);
            for (String top : topics) {
                Individual topic1 = topic.createIndividual(NS + "topic/" + top);
                journal1.addProperty(relatedTo, topic1);
                paper1.addProperty(discussesTopic, topic1);
                System.out.println(paper1);
            }
            String[] authors = line.get(columnNames.indexOf("author")).split("|");
            for (String auth : authors) {
                Individual author1 = author.createIndividual(NS + auth);
                author1.addProperty(writesPaper, paper1);
                System.out.println(author1);

            }



//            String[] reviewers = line.get(columnNames.indexOf("reviewed_by")).split("|");
//            for (int i = 0; i < reviewers.size(); i++) {
//            }
//            }
//            for (String rev : reviewers) {
//                Individual reviewer1 = reviewer.createIndividual(NS + rev);
//                reviewer1.addProperty(writesReview, paper1);
//            }


        }




        // create the individuals


/*
        // Write the ABOX model to a file
        output = null;
        try {
            output = new FileOutputStream("cosmog_abox_2.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(output);

*/



    }
}
