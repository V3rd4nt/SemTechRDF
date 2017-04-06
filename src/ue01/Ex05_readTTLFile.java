package ue01;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class Ex05_readTTLFile {

    public static void main (String[] args) {
        String path = Ex05_readTTLFile.class.getResource("example.ttl").getPath();

        Model m = RDFDataMgr.loadModel(path);

        RDFDataMgr.write(System.out, m, Lang.NTRIPLES);
}
}
