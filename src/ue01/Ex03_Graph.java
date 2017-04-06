package ue01;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDFS;

public class Ex03_Graph {

    public static void main (String[] args) {

        String nsX = "http://www.example.com/x.rdf#";
        String nsBlack = "http://www.example.com/familyblack/";

        Model m = ModelFactory.createDefaultModel();
        m.setNsPrefix("black", nsBlack);
        m.setNsPrefix("x", nsX);
        m.setNsPrefix("rdfs", RDFS.getURI());
        m.setNsPrefix("foaf", FOAF.getURI());

        // Create Resources
        Resource jim = m.createResource(nsBlack + "jim");
        Resource john = m.createResource(nsBlack + "john");
        Resource something = m.createResource();

        // Create Properties
        Property hasSon = m.createProperty(nsX + "hasSon");
        Property age = m.createProperty(FOAF.getURI() + "age");
        Property likes = m.createProperty(nsX + "likes");

        // Add Statements / Triplets
        john.addProperty(hasSon, jim);
        john.addProperty(age, m.createTypedLiteral(23));
        john.addProperty(likes, something);

        m.add(something, RDFS.label, m.createLiteral("Soccer", "en"));
        m.add(something, RDFS.label, m.createLiteral("Fußball", "de"));

        m.add(jim, FOAF.givenname, "Jim");

        // Serialize in TURTLE

        // ALTERNATIV: m.write(System.out, "TURTLE");
        RDFDataMgr.write(System.out, m, Lang.TURTLE);

    }
}
