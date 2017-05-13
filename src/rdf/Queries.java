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
    private String nsExPersonsG, nsDelPersonsG, nsProperties;

    public Queries(Dataset dataset, String nsExPersonsG, String nsDelPersonsG, String nsProperties) {
        this.dataset = dataset;
        this.nsExPersonsG = nsExPersonsG;
        this.nsDelPersonsG = nsDelPersonsG;
        this.nsProperties = nsProperties;
    }

    private void changeProperty(String name, String nameSpace, String property, String value) {
        updateDB(prefixesDefault +
                "DELETE { ?person " + nameSpace + ":" + property +" ?value } " +
                "INSERT { ?person " + nameSpace + ":" + property + " \"" + value + "\" } " +
                "WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:name \"" + name + "\". " +
                "?person " + nameSpace + ":" + property + " ?value. }");
    }

    protected void changeName(String name, String newName) {
        changeProperty(name, "foaf","name", newName);
        LogHelper.logInfo("Changed name property to " + newName);
        listPersons(newName, "foaf", "name");
    }

    protected void changeCompany(String name, String company) {
        changeProperty(name, "foaf","worksFor", company);
        LogHelper.logInfo("Changed company property to " + company);
        listPersons(name, "foaf", "worksFor");
    }

    protected void changeGender(String name, String gender) {
        changeProperty(name, "foaf","gender", gender);
        LogHelper.logInfo("Changed gender property to " + gender);
        listPersons(name, "foaf", "gender");
    }

    protected void changeBirthday(String name, String birthday) {
        changeProperty(name, "foaf","birthday", birthday);
        LogHelper.logInfo("Changed birthday property to " + birthday);
        listPersons(name, "foaf", "birthday");
    }

    protected void changeAddress(String name, String address) {
        changeProperty(name, "foaf","hasAddress", address);
        LogHelper.logInfo("Changed address property to " + address);
        listPersons(name, "foaf", "hasAddress");
    }

    protected void createNamedGraphs() {
        updateDB("CREATE GRAPH <" + nsExPersonsG + ">");
        //listGraph(nsExPersonsG);
        LogHelper.logInfo("Created graph <" + nsExPersonsG + ">");
        updateDB("CREATE GRAPH <" + nsDelPersonsG + ">");
        //listGraph(nsDelPersonsG);
        LogHelper.logInfo("Created graph <" + nsDelPersonsG + ">");
    }

    protected void addNewPerson(String name) {
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
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name \"" + name + "\". " +
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

    protected void deletePerson(String name) throws IOException {
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
                "?person foaf:hasID ?fullID. " +
                "?person foaf:name \"" + name + "\". " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. " +
                "}}"
        );
    }

    protected boolean personExists(String name) {
        return (output(prefixesDefault +
                "ASK { " +
                "?person a foaf:Person. " +
                "?person foaf:name \"" + name + "\". }", 2));
    }

    protected boolean personInGraph(String name) {
        return (output(prefixesDefault +
                "ASK { " +
                "GRAPH <" + nsExPersonsG + "> { " +
                "?person a foaf:Person. " +
                "?person foaf:name \"" + name + "\". }}", 2));
    }

    private void updateDB (String query) {
        dataset.begin(ReadWrite.WRITE);
        try {
            UpdateRequest request = UpdateFactory.create(query);
            UpdateAction.execute(request, dataset) ;
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

    protected void listPersons(String name, String nameSpace, String property) {
        output(prefixesDefault +
                "SELECT * WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:name \"" + name + "\" . " +
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
