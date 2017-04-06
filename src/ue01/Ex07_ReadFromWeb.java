package ue01;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class Ex07_ReadFromWeb {
    public static void main (String[] args) {

        Model m = RDFDataMgr.loadModel("http://de.dbpedia.org/resource/Linz");
        RDFDataMgr.read(m, "http://de.dbpedia.org/resource/Salzburg");
        RDFDataMgr.write(System.out, m, Lang.TURTLE);
    }
}
