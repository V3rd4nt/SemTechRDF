package ue01;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class Ex06_readTRIGFile {

    public static void main (String[] args) {
        String path = Ex06_readTRIGFile.class.getResource("example.trig").getPath();

        Model m = RDFDataMgr.loadModel(path);

        RDFDataMgr.write(System.out, m, Lang.TRIG);

        System.out.println("-----------");

        RDFDataMgr.write(System.out, m, Lang.NQUADS);
    }
}
