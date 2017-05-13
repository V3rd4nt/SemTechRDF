package ue03;

import java.io.File;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;


public class C1_UpdateModel {
 
   public static void executeUpdate(Model m, File queryFile) {
	  UpdateRequest request = UpdateFactory.read(queryFile.getPath());
	  System.out.println(request);
	  UpdateAction.execute(request, m) ;
	  RDFDataMgr.write(System.out, m, Lang.TURTLE);
   }
 
   
   
	public static void main(String[] args) {
 
	  String modelUri = C1_UpdateModel.class.getResource("empty.ttl").getPath();
	  Model m = RDFDataMgr.loadModel(modelUri);

	  
      File queryDir = new File(C1_UpdateModel.class.getResource("updates").getPath());     
		
		File[]   files = queryDir.listFiles();
		
      for (int i = 0; i < files.length; i++) {
         File file = files[i];
	      System.out.println("\n\n------------------------------");
         System.out.println(" " + file.getName() + ":");
	      System.out.println("------------------------------");
	      executeUpdate(m,file);
	   }
	}
}
