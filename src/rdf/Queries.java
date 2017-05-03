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
        System.out.println("INFO:\tChanged company property of person " + name + " to " + company);
    }

    protected void changeGender(String name, String gender) {
        changeProperty(name, "foaf","gender", gender);
        System.out.println("INFO:\tChanged gender property of person " + name + " to " + gender);
    }

    protected void changeBirthday(String name, String birthday) {
        changeProperty(name, "foaf","birthday", birthday);
        System.out.println("INFO:\tChanged birthday property of person " + name + " to " + birthday);
    }

    protected void changeAddress(String name, String address) {
        changeProperty(name, "PersonProps","hasAddress", address);
        System.out.println("INFO:\tChanged address property of person " + name + " to " + address);
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
        displayModel();
    }

    protected void displayModel() {
        dataset.begin(ReadWrite.READ);
        try {
            Query query = QueryFactory.create(
                    prefixesDefault + prefixPersonProps +
                            "SELECT * WHERE { " +
                            "?person foaf:name ?name. " +
                            "?person foaf:gender ?gender. " +
                            "?person foaf:birthday ?birthday. " +
                            "?person PersonProps:worksFor ?company. " +
                            "?person PersonProps:hasAddress ?address. }");
            try (QueryExecution qEx = QueryExecutionFactory.create(query ,dataset) ) {
                ResultSet res = qEx.execSelect();
                ResultSetFormatter.out(System.out, res, query);
            }
        } finally { dataset.end(); }
    }
}
