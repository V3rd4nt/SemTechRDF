package rdf;

import org.apache.jena.query.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;

/**
 * Created by Peter & Natalia.
 */
public class Queries {

    private Dataset dataset;
    private String prefixesDefault, prefixPersonProps, prefixPersons;

    public Queries(Dataset dataset, String nsPersonProps, String nsPersons) {
        this.dataset = dataset;
        this.prefixPersonProps = "PREFIX PersonProps: <" + nsPersonProps + ">\n";
        this.prefixPersons = "PREFIX Person: <" + nsPersons + ">\n";
        this.prefixesDefault =  "PREFIX rdf: <"+RDF.getURI()+">\n" + "PREFIX foaf: <"+FOAF.getURI()+">\n";
    }

    private void changeProperty(String name, String nameSpace, String property, String value) {
        String query = prefixesDefault;
        if (nameSpace.equals("PersonProps")) query += prefixPersonProps;
        query += "DELETE { ?person " + nameSpace + ":" + property +" ?o }\n" +
                "INSERT { ?person " + nameSpace + ":" + property + " \"" + value + "\" }\n" +
                "WHERE { ?person foaf:name \"" + name + "\" .\n" +
                "?person " + nameSpace + ":" + property + " ?o .\n}";
        //System.out.print(query);
        updateDB(query);
    }

    protected void changeCompany(String name, String company) {
        changeProperty(name, "PersonProps","worksFor", company);
        System.out.println("\tINFO:\tChanged company property of person " + name + " to " + company);
    }

    protected void changeGender(String name, String gender) {
        changeProperty(name, "foaf","gender", gender);
        System.out.println("\tINFO:\tChanged gender property of person " + name + " to " + gender);
    }

    protected void changeBirthday(String name, String birthday) {
        changeProperty(name, "foaf","birthday", birthday);
        System.out.println("\tINFO:\tChanged birthday property of person " + name + " to " + birthday);
    }

    protected void changeAddress(String name, String address) {
        changeProperty(name, "PersonProps","hasAddress", address);
        System.out.println("\tINFO:\tChanged address property of person " + name + " to " + address);
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
        String query = prefixesDefault + prefixPersonProps +
                "ASK { ?person foaf:name \"" + name + "\" . }";
        if(output(query, 2)) {
            System.out.print("\tERROR:\tThe person " + name + " already exists!\n");
            return true;
        } else return false;
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
        output(prefixesDefault + prefixPersonProps +
                        "SELECT * WHERE { " +
                        "?person foaf:name ?name. " +
                        "?person foaf:gender ?gender. " +
                        "?person foaf:birthday ?birthday. " +
                        "?person PersonProps:worksFor ?company. " +
                        "?person PersonProps:hasAddress ?address. }", 1);
    }

    protected void filter(int choice) {
        switch (choice) {
            case 1:
                //TODO: WRTIE QUERY TO CREATE FILTERED RESULT SETS FOR FOR GENDER
                output("", 1);
                break;
            case 2:
                //TODO: WRTIE QUERY TO CREATE FILTERED RESULT SETS FOR LOCATION
                output("", 1);
                break;
            default:
                System.err.print("value " + choice + " is not recognized as mode parameter");
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
                        System.err.print("value " + mode + " is not recognized as mode parameter");
                }
            }
        } finally {
            dataset.end();
        }
        return answer;
    }
}
