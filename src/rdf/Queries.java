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
    private String prefixesDefault, prefixPersonProps;

    public Queries(Dataset dataset, String nsPersonProps) {
        this.dataset = dataset;
        this.prefixPersonProps = "PREFIX PersonProps: <" + nsPersonProps + ">\n";
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

    protected void deletePerson(String name) {
        //TODO: WRITE DELETE QUERY
    }

    protected void deleteAllPersons() {
        //TODO: WRITE DELETE QUERY
    }

    protected boolean personExists(String name) {
        //TODO: WRITE ASK QUERY
        System.out.print("\tERROR:\tThe person " + name + " does not exist!");
        return false;
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
                        "?person PersonProps:hasAddress ?address. }");
    }

    protected void filter(char choice) {
        //TODO: WRTIE FILTER QUERY FOR GENDER AND LOCATION
    }

    protected void output(String query) {
        dataset.begin(ReadWrite.READ);
        try {
            Query Q = QueryFactory.create(query);
            try (QueryExecution qEx = QueryExecutionFactory.create(Q ,dataset) ) {
                ResultSet res = qEx.execSelect();
                ResultSetFormatter.out(System.out, res, Q);
            }
        } finally { dataset.end(); }
    }
}
