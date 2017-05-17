package rdf;

import org.apache.jena.query.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;

import java.io.IOException;

/**
 * Created by Peter & Natalia.
 */
public class Queries {

    private Dataset dataset;
    private final String prefixesDefault =
            "PREFIX rdf: <" + RDF.getURI() + "> " +
            "PREFIX foaf: <" + FOAF.getURI()+ "> ";
    private String nsExPersonsG, nsDelPersonsG, prefixProperties, prefixPersons;

    public Queries(Dataset dataset, String nsExPersonsG, String nsDelPersonsG, String nsProperties, String nsPersons) {
        this.dataset = dataset;
        this.nsExPersonsG = nsExPersonsG;
        this.nsDelPersonsG = nsDelPersonsG;
        prefixProperties = "PREFIX properties: <" + nsProperties + "> ";
        prefixPersons = "PREFIX person: <" + nsPersons + "> ";
    }

    private void updateDB (String query) {
        dataset.begin(ReadWrite.WRITE);
        try {
            UpdateAction.execute(UpdateFactory.create(query), dataset) ;
            dataset.commit();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            dataset.abort();
        } finally {
            dataset.end();
        }
    }

    protected void createPerson(String fullID) {
        updateDB(prefixesDefault + prefixPersons +
                "INSERT DATA { " +
                "person:" + fullID + " a foaf:Person. " +
                "person:" + fullID + " foaf:hasID \"" + fullID + "\". "+
                "}");
        LogHelper.logInfo("Created person resource with ID: " + fullID);
    }

    // SET PERSON DATA
    private void addProperty(String fullID, String nameSpace, String property, String value) {
        updateDB(prefixesDefault + prefixProperties + prefixPersons +
                "INSERT DATA { person:" + fullID + " " + nameSpace + ":" + property +" \"" + value + "\". }");
    }

    protected void setName(String fullID, String name) {
        addProperty(fullID, "foaf","name", name);
        LogHelper.logInfo("Set name property to " + name);
    }

    protected void setCompany(String fullID, String company) {
        addProperty(fullID, "foaf","worksFor", company);
        LogHelper.logInfo("Set company property to " + company);
    }

    protected void setGender(String fullID, String gender) {
        addProperty(fullID, "foaf","gender", gender);
        LogHelper.logInfo("Set gender property to " + gender);
    }

    protected void setBirthday(String fullID, String birthday) {
        addProperty(fullID, "foaf","birthday", birthday);
        LogHelper.logInfo("Set birthday property to " + birthday);
    }

    protected void setAddress(String fullID, String address) {
        addProperty(fullID, "foaf","hasAddress", address);
        LogHelper.logInfo("Set address property to " + address);
    }

    // CHANGE PERSON DATA
    private void changeProperty(String fullID, String nameSpace, String property, String value) {
        updateDB(prefixesDefault + prefixProperties +
                "DELETE { ?person " + nameSpace + ":" + property +" ?value. } " +
                "INSERT { ?person " + nameSpace + ":" + property + " \"" + value + "\". } " +
                "WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "?person " + nameSpace + ":" + property + " ?value. " +
                "}");
    }

    protected void changeName(String fullID, String name) {
        changeProperty(fullID, "foaf","name", name);
        LogHelper.logInfo("Changed name property to " + name);
        listPersonProperty(fullID, "foaf", "name");
    }

    protected void changeCompany(String fullID, String company) {
        changeProperty(fullID, "foaf","worksFor", company);
        LogHelper.logInfo("Changed company property to " + company);
        listPersonProperty(fullID, "foaf", "worksFor");
    }

    protected void changeGender(String fullID, String gender) {
        changeProperty(fullID, "foaf","gender", gender);
        LogHelper.logInfo("Changed gender property to " + gender);
        listPersonProperty(fullID, "foaf", "gender");
    }

    protected void changeBirthday(String fullID, String birthday) {
        changeProperty(fullID, "foaf","birthday", birthday);
        LogHelper.logInfo("Changed birthday property to " + birthday);
        listPersonProperty(fullID, "foaf", "birthday");
    }

    protected void changeAddress(String fullID, String address) {
        changeProperty(fullID, "foaf","hasAddress", address);
        LogHelper.logInfo("Changed address property to " + address);
        listPersonProperty(fullID, "foaf", "hasAddress");
    }

    protected void createNamedGraphs() {
        updateDB("CREATE GRAPH <" + nsExPersonsG + ">");
        //listGraph(nsExPersonsG);
        LogHelper.logInfo("Created graph <" + nsExPersonsG + ">");
        updateDB("CREATE GRAPH <" + nsDelPersonsG + ">");
        //listGraph(nsDelPersonsG);
        LogHelper.logInfo("Created graph <" + nsDelPersonsG + ">");
    }

    protected void addNewPersonG(String fullID) {
        updateDB(prefixesDefault +
                "INSERT { GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "}}" +
                "WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "}");
    }

    protected void deleteAllPersonsG() {
        updateDB(prefixesDefault +
                "DELETE { " +
                "GRAPH <" + nsExPersonsG + "> {?s ?p ?o.}} " +
                "INSERT { " +
                "GRAPH <" + nsDelPersonsG + "> {?s ?p ?o.}} " +
                "WHERE { " +
                "GRAPH <" + nsExPersonsG + "> {?s ?p ?o.}} "
        );
    }

    protected void deletePersonG(String fullID) throws IOException {
        updateDB(prefixesDefault + prefixProperties +
                "DELETE { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "?person properties:hasFriend ?friend. " +
                "}} " +
                "INSERT { " +
                "GRAPH <" + nsDelPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "?person properties:hasFriend ?friend. " +
                "}} " +
                "WHERE { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "?person properties:hasFriend ?friend. " +
                "}}"
        );
    }

    private void addRelation(String fullID, String nameSpace, String property, String fullID_) {
        // add relation to default graph
        updateDB(prefixesDefault + prefixProperties + prefixPersons +
                "INSERT DATA { " +
                "person:" + fullID + " " + nameSpace + ":" + property + " person:" + fullID_ + ". " +
                "}");
        // add relation to existing persons graph
        updateDB(prefixesDefault + prefixProperties + prefixPersons +
                "INSERT DATA { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "person:" + fullID + " " + nameSpace + ":" + property + " person:" + fullID_ + ". }" +
                "}");
    }

    protected void addFriend(String fullID, String fullID_) {
        addRelation(fullID, "properties", "hasFriend", fullID_);
        LogHelper.logInfo("Added friend property");
    }

    protected void deleteRelation(String fullID, String nameSpace, String property, String fullID_) {
        // delete relation from default graph
        updateDB(prefixesDefault + prefixProperties + prefixPersons +
                "DELETE { " +
                "?person " + nameSpace + ":" + property + " person:" + fullID_ + ". " +
                "} " +
                "WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "}");
        // delete relation from existing graph
        updateDB(prefixesDefault + prefixProperties + prefixPersons +
                "DELETE { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person " + nameSpace + ":" + property + " person:" + fullID_ + ". " +
                "}} " +
                "WHERE { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "}}");
    }

    protected void deleteFriend(String fullID, String fullID_) {
        deleteRelation(fullID, "properties", "hasFriend", fullID_);
        LogHelper.logInfo("Deleted friend property");
    }

    protected boolean personExists(String fullID) {
        return (output(prefixesDefault +
                "ASK { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". }", 2));
    }

    protected boolean personExistsG(String fullID) {
        return (output(prefixesDefault +
                "ASK { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". }}", 2));
    }

    protected void listPersonProperty(String fullID, String nameSpace, String property) {
        output(prefixesDefault + prefixProperties +
                "SELECT * WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "?person " + nameSpace + ":" + property + " ?" + property + ". }", 1);

    }

    protected void listGraph(String nsPersonG) {
        output(prefixesDefault +
                "SELECT * WHERE { " +
                "GRAPH <" + nsPersonG + "> { ?s ?p ?o. }}", 1);
    }

    protected void filter(int mode) throws IOException {
        switch (mode) {
            case 1:
                System.out.print(ModelCreator.genderText);
                char genderInput = ModelCreator.createChar();
                do {
                    if(genderInput == 'm') {
                        // filter for all males
                        output(prefixesDefault +
                                "SELECT * WHERE { " +
                                "?person foaf:hasID ?fullID. " +
                                "?person foaf:name ?name. " +
                                "?person foaf:gender ?gender. " +
                                "FILTER (?gender = 'male'). " +
                                "}", 1);;
                    } else if (genderInput == 'f') {
                        // filter for all females
                        output(prefixesDefault +
                                "SELECT * WHERE { " +
                                "?person foaf:hasID ?fullID. " +
                                "?person foaf:name ?name. " +
                                "?person foaf:gender ?gender. " +
                                "FILTER (?gender = 'female'). " +
                                "}", 1);;
                    } else
                        LogHelper.logError(ModelCreator.errorMsgGender);
                } while (genderInput != 'm' && genderInput != 'f');
                break;
            case 2:
                System.out.print(ModelCreator.addressText);
                String locationInput = ModelCreator.createString();
                // filter for string in address property
                output(prefixesDefault +
                        "SELECT * WHERE {" +
                        "?person foaf:hasID ?id. " +
                        "?person foaf:name ?name. " +
                        "?person foaf:hasAddress ?address. " +
                        "FILTER regex(str(?address), \"" + locationInput + "\", 'i'). " +
                        "}", 1);;
                break;
            default: displayChoiceError(mode);
        }
    }

    protected boolean output(String query, int mode) {
        boolean answer = false;
        dataset.begin(ReadWrite.READ);
        try {
            Query Q = QueryFactory.create(query);
            try (QueryExecution qEx = QueryExecutionFactory.create(Q, dataset)) {
                switch (mode) {
                    case 1:
                        // Write full information about all persons to console
                        ResultSet res = qEx.execSelect();
                        ResultSetFormatter.out(System.out, res, Q);
                        break;
                    case 2:
                        // Determine if a particular person already exists
                        answer = qEx.execAsk();
                        break;
                    default: displayChoiceError(mode);
                }
            }
        } finally {
            dataset.end();
        }
        return answer;
    }

    public static void displayChoiceError(int choice) {
        LogHelper.logError("Value " + choice + " is not recognized as mode parameter");
    }
}
