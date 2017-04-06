package ue01;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;

public class Ex02_HelloWorld {
    public static void main (String[] args) {

        // create an empty Model
        Model m = ModelFactory.createDefaultModel();

        // create the resource
        Resource example = m.createResource("http://example.org/helloworld");

        // add the property
        example.addProperty(RDFS.label, "Hello World!");

        m.write(System.out, "TURTLE");
    }
}
