import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.atlas.csv.CSVParser;
import org.apache.jena.rdf.model.Resource;

import javax.swing.text.html.InlineView;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLOutput;


public class CosmogABOX_resources {

    public static void main (String[] args) {

        // Create an empty RDFS ontology model using RDFS inference entailment
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
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
        OntClass demoArticle = model.createClass(NS + "demo_article");
        OntClass shortArticle = model.createClass(NS + "short_article");
        OntClass fullArticle = model.createClass(NS + "full_article");
        OntClass article = model.createClass(NS + "article");
        OntClass poster = model.createClass(NS + "poster");
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
        OntProperty conferenceFranchise = model.createOntProperty(NS + "conferenceFranchise");
        OntProperty verdict = model.createOntProperty(NS + "verdict");

//        //Write the TBOX model to a file
//        FileOutputStream output = null;
//        try {
//            output = new FileOutputStream("cosmog-TBOX.rdf");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        model.write(output);


        // CREATE THE ABOX

        CSVParser reader = CSVParser.create("article_slice.csv");
        Iterator<List<String>> iterator = reader.iterator();
        List<String> columnNames = iterator.next();

        while (iterator.hasNext()) {
            List<String> line = iterator.next();

            Resource paperResource = model.createResource(NS + "paper-" + line.get(columnNames.indexOf("article")));
            paperResource.addProperty(title, line.get(columnNames.indexOf("title")));

            Resource journalResource = model.createResource(NS + "journal-" + line.get(columnNames.indexOf("journalID")));
            journalResource.addProperty(venueTitle, line.get(columnNames.indexOf("journal")));
            journalResource.addProperty(number, line.get(columnNames.indexOf("number")));
            journalResource.addProperty(volume, line.get(columnNames.indexOf("volume")));

            journalResource.addProperty(publishesPaper, paperResource);

            Resource editorResource = model.createResource(NS + "editor-" + line.get(columnNames.indexOf("editor")));
            editorResource.addProperty(edits, journalResource);
            editorResource.addProperty(name, line.get(columnNames.indexOf("editor")));

            String[] topics = line.get(columnNames.indexOf("topics")).split("\\|");
            for (String top : topics) {
                Resource topicResource = model.createResource(NS + "topic-" + top);
                topicResource.addProperty(keyword, top);

                paperResource.addProperty(discussesTopic, topicResource);
                journalResource.addProperty(relatedTo, top);
            }

            String[] authors = line.get(columnNames.indexOf("author")).split("\\|");
            for (String auth : authors){
                Resource authorResource = model.createResource(NS + "author-" + auth);
                authorResource.addProperty(writesPaper, paperResource);
                authorResource.addProperty(name, auth);
            }

            List<String> reviewers = Arrays.asList(line.get(columnNames.indexOf("reviewed_by")).split("\\|"));
            List<String> reviews = Arrays.asList(line.get(columnNames.indexOf("reviews")).split("\\|"));
            List<String> decisions = Arrays.asList(line.get(columnNames.indexOf("decisions")).split("\\|"));
            List<String> decisionIDs = Arrays.asList(line.get(columnNames.indexOf("decisionID")).split("\\|"));

            for (int i = 0; i < reviewers.size(); i++) {
                Resource reviewerResource = model.createResource(NS + "reviewer-" + reviewers.get(i));
                Resource decisionResource = model.createResource(NS + "decision-" + decisionIDs.get(i));
                decisionResource.addProperty(review, reviews.get(i));
                decisionResource.addProperty(verdict, decisions.get(i));
                decisionResource.addProperty(discussesPaper, paperResource);
                reviewerResource.addProperty(writesReview, decisionResource);
                reviewerResource.addProperty(name, reviewers.get(i));
                editorResource.addProperty(assigns, reviewerResource);
            }
        }

        // ADD conferences to the ABOX

        CSVParser proceedingReader = CSVParser.create("proceeding_slice.csv");
        Iterator<List<String>> proceedingIterator = proceedingReader.iterator();
        List<String> proceedingColumnNames = proceedingIterator.next();

        while (proceedingIterator.hasNext()) {
            List<String> line = proceedingIterator.next();

            Resource paperResource = model.createResource(NS + "paper-" + line.get(proceedingColumnNames.indexOf("articleID")));
            paperResource.addProperty(title, line.get(proceedingColumnNames.indexOf("articleTitle")));

            Resource conferenceResource = model.createResource(NS + "conference-" + line.get(proceedingColumnNames.indexOf("conferenceID")));
            conferenceResource.addProperty(venueTitle, line.get(proceedingColumnNames.indexOf("conferenceTitle")));
            conferenceResource.addProperty(location, line.get(proceedingColumnNames.indexOf("location")));
            conferenceResource.addProperty(conferenceFranchise, line.get(proceedingColumnNames.indexOf("conference")));

            conferenceResource.addProperty(publishesPaper, paperResource);

            Resource chairResource = model.createResource(NS + "chair-" + line.get(proceedingColumnNames.indexOf("chair")));
            chairResource.addProperty(organizes, conferenceResource);
            chairResource.addProperty(name, line.get(proceedingColumnNames.indexOf("chair")));

            String[] topics = line.get(proceedingColumnNames.indexOf("topic")).split("\\|");
            for (String top : topics) {
                Resource topicResource = model.createResource(NS + "topic-" + top);
                topicResource.addProperty(keyword, top);

                paperResource.addProperty(discussesTopic, topicResource);
                conferenceResource.addProperty(relatedTo, top);
            }

            String[] authors = line.get(proceedingColumnNames.indexOf("author")).split("\\|");
            for (String auth : authors){
                Resource authorResource = model.createResource(NS + "author-" + auth);
                authorResource.addProperty(writesPaper, paperResource);
                authorResource.addProperty(name, auth);
            }

            List<String> reviewers = Arrays.asList(line.get(proceedingColumnNames.indexOf("reviewed_by")).split("\\|"));
            List<String> reviews = Arrays.asList(line.get(proceedingColumnNames.indexOf("reviews")).split("\\|"));
            List<String> decisions = Arrays.asList(line.get(proceedingColumnNames.indexOf("decisions")).split("\\|"));
            List<String> decisionIDs = Arrays.asList(line.get(proceedingColumnNames.indexOf("decisionID")).split("\\|"));
            for (int i = 0; i < reviewers.size(); i++) {
                Resource reviewerResource = model.createResource(NS + "reviewer-" + reviewers.get(i));
                Resource decisionResource = model.createResource(NS + "decision-" + decisionIDs.get(i));
                decisionResource.addProperty(verdict, decisions.get(i));
                decisionResource.addProperty(review, reviews.get(i));
                decisionResource.addProperty(discussesPaper, paperResource);
                reviewerResource.addProperty(writesReview, decisionResource);
                reviewerResource.addProperty(name, reviewers.get(i));
                chairResource.addProperty(assigns, reviewerResource);
            }
        }

        FileOutputStream output = null;
        try {
            output = new FileOutputStream("cosmog_abox.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(output);



        // BUILD THE LINKING FILE THAT LINKS THE ABOX WITH THE TBOX...

        // Create an empty RDFS ontology model using RDFS inference entailment
        OntModel linkingModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
        // Specify prefix name
        linkingModel.setNsPrefix("cosmog", NS);

        OntClass linkingDemoArticle = linkingModel.createClass(NS + "demo_article");
        OntClass linkingShortArticle = linkingModel.createClass(NS + "short_article");
        OntClass linkingFullArticle = linkingModel.createClass(NS + "full_article");
        OntClass linkingPoster = linkingModel.createClass(NS + "poster");

        OntClass linkingSymposium = linkingModel.createClass(NS + "symposium");
        OntClass linkingWorkshop = linkingModel.createClass(NS + "workshop");
        OntClass linkingExpertGroup = linkingModel.createClass(NS + "expert_group");
        OntClass linkingConference = linkingModel.createClass(NS + "conference");


        CSVParser linkingArticlesReader = CSVParser.create("article_slice.csv");
        Iterator<List<String>> linkingArticlesIterator = linkingArticlesReader.iterator();
        List<String> linkingArticlesColumnNames = linkingArticlesIterator.next();

        while (linkingArticlesIterator.hasNext()){
            List<String> line = linkingArticlesIterator.next();
            if (Objects.equals(line.get(linkingArticlesColumnNames.indexOf("paperType")), "demo_paper")) {
                Individual paperIndividual = linkingDemoArticle.createIndividual(NS + "paper-" + line.get(linkingArticlesColumnNames.indexOf("article")));
            } else if (Objects.equals(line.get(linkingArticlesColumnNames.indexOf("paperType")), "full_paper")) {
                Individual paperIndividual = linkingFullArticle.createIndividual(NS + "paper-" + line.get(linkingArticlesColumnNames.indexOf("article")));
            } else if (Objects.equals(line.get(linkingArticlesColumnNames.indexOf("paperType")), "short_paper")) {
                Individual paperIndividual = linkingShortArticle.createIndividual(NS + "paper-" + line.get(linkingArticlesColumnNames.indexOf("article")));
            }
        }

        CSVParser linkingProceedingReader = CSVParser.create("proceeding_slice.csv");
        Iterator<List<String>> linkingProceedingIterator = linkingProceedingReader.iterator();
        List<String> linkingProceedingColumnNames = linkingProceedingIterator.next();

        while (linkingArticlesIterator.hasNext()){
            List<String> line = linkingArticlesIterator.next();
            if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("articleType")), "demo_paper")) {
                Individual paperIndividual = linkingDemoArticle.createIndividual(NS + "paper-" + line.get(linkingProceedingColumnNames.indexOf("articleID")));
            } else if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("articleType")), "full_paper")) {
                Individual paperIndividual = linkingFullArticle.createIndividual(NS + "paper-" + line.get(linkingProceedingColumnNames.indexOf("articleID")));
            } else if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("articleType")), "short_paper")) {
                Individual paperIndividual = linkingShortArticle.createIndividual(NS + "paper-" + line.get(linkingProceedingColumnNames.indexOf("articleID")));
            } else if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("articleType")), "poster")) {
                Individual paperIndividual = linkingPoster.createIndividual(NS + "paper-" + line.get(linkingProceedingColumnNames.indexOf("articleID")));
            }

            if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("conferenceType")), "workshop")){
                Individual conferenceIndividual = linkingWorkshop.createIndividual(NS + "conference-" + line.get(linkingProceedingColumnNames.indexOf("conferenceID")));
            } else if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("conferenceType")), "symposium")){
                Individual conferenceIndividual = linkingSymposium.createIndividual(NS + "conference-" + line.get(linkingProceedingColumnNames.indexOf("conferenceID")));
            } else if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("conferenceType")), "expert_group")){
                Individual conferenceIndividual = linkingExpertGroup.createIndividual(NS + "conference-" + line.get(linkingProceedingColumnNames.indexOf("conferenceID")));
            } else if (Objects.equals(line.get(linkingProceedingColumnNames.indexOf("conferenceType")), "regular")){
                Individual conferenceIndividual = linkingConference.createIndividual(NS + "conference-" + line.get(linkingProceedingColumnNames.indexOf("conferenceID")));
            }
        }
        FileOutputStream linkingOutput = null;
        try {
            linkingOutput = new FileOutputStream("cosmog_TBOX_ABOX_link.rdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        linkingModel.write(linkingOutput);
    }
}