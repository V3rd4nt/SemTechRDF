package rdf;

import org.apache.jena.query.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Peter & Natalia.
 */
public class Queries {

    private Dataset dataset;
    private final String prefixesDefault =
            "PREFIX rdf: <"+RDF.getURI()+"> " +
            "PREFIX foaf: <"+FOAF.getURI()+"> ";

    public Queries(Dataset dataset) {
        this.dataset = dataset;
    }

    private void changeProperty(String name, String nameSpace, String property, String value) {
        String query = prefixesDefault +
                "DELETE { ?person " + nameSpace + ":" + property +" ?o } " +
                "INSERT { ?person " + nameSpace + ":" + property + " \"" + value + "\" } " +
                "WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:name \"" + name + "\" . " +
                "?person " + nameSpace + ":" + property + " ?o . }";
        //System.out.print(query);
        updateDB(query);
    }

    protected void changeName(String name, String newName) {
        changeProperty(name, "foaf","name", newName);
        LogHelper.logInfo("Changed name property to " + newName);
    }

    protected void changeCompany(String name, String company) {
        changeProperty(name, "foaf","worksFor", company);
        LogHelper.logInfo("Changed company property to " + company);
    }

    protected void changeGender(String name, String gender) {
        changeProperty(name, "foaf","gender", gender);
        LogHelper.logInfo("Changed gender property to " + gender);
    }

    protected void changeBirthday(String name, String birthday) {
        changeProperty(name, "foaf","birthday", birthday);
        LogHelper.logInfo("Changed birthday property to " + birthday);
    }

    protected void changeAddress(String name, String address) {
        changeProperty(name, "foaf","hasAddress", address);
        LogHelper.logInfo("Changed address property to " + address);
    }

    protected void createNamedGraphs() {
        //TODO: WRITE QUERIES FOR CREATING NAMED GRAPHS FOR EXISTING AND DELETED PERSONS
    }

    protected void addPerson(String name) {
        //TODO: WRITE QUERY FOR ADDING A PERSON TO THE NAMED GRAPH FPR EXISITING PERSONS
    }

    protected void deletePerson(String name) {
        //TODO: WRITE QUERY FOR DELETING A PERSON FROM THE EXISITNG PERSONS GRAPH AND ADDING
        //TODO: IT TO THE DELETED PERSONS GRAPH
    }

    protected void deleteAllPersons() {
        //TODO: WRITE QUERY TO DELETE ALL EXISTING PERSONS FROM THE DATABASE
    }

    protected boolean personExists(String name) {
        String query = prefixesDefault +
                "ASK { " +
                "?person a foaf:Person. " +
                "?person foaf:name \"" + name + "\" . }";
        return (output(query, 2));
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
        listAllPersons();
    }

    protected void listAllPersons() {
        output(prefixesDefault +
                "SELECT * WHERE { " +
                "?person a foaf:Person. " +
                "?person foaf:name ?name. " +
                "?person foaf:gender ?gender. " +
                "?person foaf:birthday ?birthday. " +
                "?person foaf:worksFor ?company. " +
                "?person foaf:hasAddress ?address. }", 1);
    }

    protected void filter(int choice) throws IOException {
        switch (choice) {
            case 1:
                char genderInput = createChar();
                do {
                    if(genderInput == 'm') {
                        output(prefixesDefault +
                                "SELECT * WHERE { " +
                                "?person foaf:name ?name. " +
                                "?person foaf:gender ?g " +
                                "FILTER (?g = 'male'). " +
                                "}", 1);;
                    } else if (genderInput == 'f') {
                        output(prefixesDefault +
                                "SELECT * WHERE { " +
                                "?person foaf:name ?name. " +
                                "?person foaf:gender ?g " +
                                "FILTER (?g = 'female'). " +
                                "}", 1);;
                    } else
                        LogHelper.logError("Sorry, that's not valid.\n" +
                                "A gender must be (m)ale or (f)emale. Please try again: ");
                } while (genderInput != 'm' && genderInput != 'f');
                break;
            case 2:
                //TODO: WRTIE QUERY TO CREATE FILTERED RESULT SETS FOR LOCATION
                String locationInput = createString();
                output(prefixesDefault +
                        "SELECT * WHERE { " +
                        "?person foaf:name ?name. " +
                        "?person foaf:hasAddress ?a " +
                        "FILTER (?a = '" + locationInput + "'). " +
                        "}", 1);;
                break;
            default:
                LogHelper.logError("Value " + choice + " is not recognized as mode parameter");
        }
    }

    protected static char createChar() throws IOException {
        System.out.print("Enter a gender - (m)ale or (f)emale to filter: \n\t");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line.length() == 0) return '\u0000';
        else return line.charAt(0);
    }

    protected static String createString() throws IOException {
        System.out.print("Enter a location to filter: \n\t");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        return line;
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
