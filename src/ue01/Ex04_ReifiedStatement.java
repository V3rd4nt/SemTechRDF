package ue01;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;

public class Ex04_ReifiedStatement {

    public static void main(String[] args) {

        String nsDat = "http://www.example.com/data.rdf#";
        String nsVoc = "http://www.example.com/vocabulary.rdf#";

        Model m = ModelFactory.createDefaultModel();
        m.setNsPrefix("d", nsDat);
        m.setNsPrefix("rdf", RDF.getURI());
        m.setNsPrefix("v", nsVoc);
        m.setNsPrefix("foaf", FOAF.getURI());

        Resource susi = m.createResource(nsDat + "Susi");
        Resource john = m.createResource(nsDat + "John");
        Resource frank = m.createResource(nsDat + "Frank");

        Property thinks = m.createProperty(nsVoc + "thinks");
        Property denies = m.createProperty(nsVoc + "denies");

        ReifiedStatement johnKnowsSusi = m.createReifiedStatement(m.createStatement(john, FOAF.knows, susi));

        m.add(frank, thinks, johnKnowsSusi);
        m.add(john, denies, johnKnowsSusi);

        m.write(System.out, "TURTLE");
    }
}