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
    private String nsExPersonsG, nsDelPersonsG, nsProperties, prefixProperties;

    public Queries(Dataset dataset, String nsExPersonsG, String nsDelPersonsG, String nsProperties) {
        this.dataset = dataset;
        this.nsExPersonsG = nsExPersonsG;
        this.nsDelPersonsG = nsDelPersonsG;
        this.nsProperties = nsProperties;
        prefixProperties = "PREFIX properties: <" + nsProperties + "> ";
    }

    private void changeProperty(String fullID, String nameSpace, String property, String value) {
        updateDB(prefixesDefault + prefixProperties +
                "DELETE { ?person " + nameSpace + ":" + property +" ?value } " +
                "INSERT { ?person " + nameSpace + ":" + property + " \"" + value + "\" } " +
                "WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". " +
                "?person " + nameSpace + ":" + property + " ?value. }");
    }

    protected void changeName(String fullID, String name) {
        changeProperty(fullID, "foaf","name", name);
        LogHelper.logInfo("Changed name property to " + name);
        listPersons(fullID, "foaf", "name");
    }

    protected void changeCompany(String fullID, String company) {
        changeProperty(fullID, "foaf","worksFor", company);
        LogHelper.logInfo("Changed company property to " + company);
        listPersons(fullID, "foaf", "worksFor");
    }

    protected void changeGender(String fullID, String gender) {
        changeProperty(fullID, "foaf","gender", gender);
        LogHelper.logInfo("Changed gender property to " + gender);
        listPersons(fullID, "foaf", "gender");
    }

    protected void changeBirthday(String fullID, String birthday) {
        changeProperty(fullID, "foaf","birthday", birthday);
        LogHelper.logInfo("Changed birthday property to " + birthday);
        listPersons(fullID, "foaf", "birthday");
    }

    protected void changeAddress(String fullID, String address) {
        changeProperty(fullID, "foaf","hasAddress", address);
        LogHelper.logInfo("Changed address property to " + address);
        listPersons(fullID, "foaf", "hasAddress");
    }

    protected void createNamedGraphs() {
        updateDB("CREATE GRAPH <" + nsExPersonsG + ">");
        //listGraph(nsExPersonsG);
        LogHelper.logInfo("Created graph <" + nsExPersonsG + ">");
        updateDB("CREATE GRAPH <" + nsDelPersonsG + ">");
        //listGraph(nsDelPersonsG);
        LogHelper.logInfo("Created graph <" + nsDelPersonsG + ">");
    }

    protected void addFriend(String fullID, String fullID_friend) {
        updateDB(prefixesDefault + prefixProperties +
                "INSERT { GRAPH <" + nsExPersonsG + "> { " +
                ":" + fullID + " properties:hasFriend :" + fullID_friend + "} " +
                "WHERE { " +
                ":" + fullID + " a foaf:Person. " +
                ":" + fullID_friend + " a foaf:Person. " +
                " }");
     }

    protected void addNewPerson(String fullID) {
        updateDB(prefixesDefault +
                /*"INSERT { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?s ?p ?o }}" +
                "WHERE { " +
                "?s ?p ?o. " +
                "}"*/
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
                "?person foaf:hasAddress ?address. }");
    }

    protected void deleteAllPersons() {
        updateDB(prefixesDefault +
                //  "CLEAR GRAPH <" + nsDelPersonsG + ">"
                //  "CLEAR GRAPH <" + nsExPersonsG + ">"
                "DELETE {" +
                "GRAPH <" + nsExPersonsG + "> {?s ?p ?o}} " +
                "INSERT {" +
                "GRAPH <" + nsDelPersonsG + "> {?s ?p ?o}} " +
                "WHERE {" +
                "GRAPH <" + nsExPersonsG + "> {?s ?p ?o}} "
        );
    }

    protected void deletePerson(String fullID) throws IOException {
        updateDB(prefixesDefault +
                //  "CLEAR GRAPH <" + nsDelPersonsG + ">"
                //  "CLEAR GRAPH <" + nsExPersonsG + ">"
                "DELETE { " +
                "GRAPH <" + nsExPersonsG + "> {" +
                "?person a foaf:Person. " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "}}" +
                "INSERT { " +
                "GRAPH <" + nsDelPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "}}" +
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
                "}}"
        );
    }

    protected boolean personExists(String fullID) {
        return (output(prefixesDefault +
                "ASK { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". }", 2));
    }

    protected boolean personInGraph(String fullID) {
        return (output(prefixesDefault +
                "ASK { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\". }}", 2));
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

    protected void listAllPersons() {
        output(prefixesDefault +
                "SELECT * WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. }", 1);
    }

    protected void listPersons(String fullID, String nameSpace, String property) {
        output(prefixesDefault +
                "SELECT * WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:hasID \"" + fullID + "\" . " +
                "?person " + nameSpace + ":" + property + " ?" + property + ". }", 1);
    }

    protected void listGraph(String nsPersonG) {
        output(prefixesDefault +
                "SELECT * WHERE { " +
                "GRAPH <" + nsPersonG + "> { ?s ?p ?o }. }", 1);
    }

    protected void filter(int choice) throws IOException {
        switch (choice) {
            case 1:
                System.out.print(ModelCreator.genderText);
                char genderInput = ModelCreator.createChar();
                do {
                    if(genderInput == 'm') {
                        output(prefixesDefault +
                                "SELECT * WHERE { " +
                                "?person foaf:hasID ?fullID. " +
                                "?person foaf:name ?name. " +
                                "?person foaf:gender ?gender " +
                                "FILTER (?gender = 'male'). " +
                                "}", 1);;
                    } else if (genderInput == 'f') {
                        output(prefixesDefault +
                                "SELECT * WHERE { " +
                                "?person foaf:hasID ?fullID. " +
                                "?person foaf:name ?name. " +
                                "?person foaf:gender ?gender " +
                                "FILTER (?gender = 'female'). " +
                                "}", 1);;
                    } else
                        LogHelper.logError(ModelCreator.errorMsgGender);
                } while (genderInput != 'm' && genderInput != 'f');
                break;
            case 2:
                System.out.print(ModelCreator.addressText);
                String locationInput = ModelCreator.createString();
                output(prefixesDefault +
                        "SELECT * WHERE {" +
                        "?person foaf:hasID ?fullID. " +
                        "?person foaf:name ?name. " +
                        "?person foaf:hasAddress ?address. " +
                        "?person foaf:hasAddress \"" + locationInput + "\"" +
                        "}", 1);;

                        /*"SELECT * WHERE {" +
                        "?person foaf:name ?name. " +
                        "?person foaf:hasAddress ?address " +
                        "FILTER regex(str(?address), '" + locationInput + "', 'i'). " +
                        "}" */ //Select does not search the exactly string input
                break;
            default:
                LogHelper.logError("Value " + choice + " is not recognized as mode parameter");
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
                    default:
                        LogHelper.logError("Value " + mode + " is not recognized as mode parameter");
                }
            }
        } finally {
            dataset.end();
        }
        return answer;
    }
}
