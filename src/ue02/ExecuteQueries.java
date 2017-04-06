package ue02;

import java.io.File;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;


public class ExecuteQueries {
 
   public static void executeQuery(Model m, File queryFile) {
      Query query = QueryFactory.read(queryFile.getPath());
      System.out.println(query);
      System.out.println("------------------------------");
      
      Op op = Algebra.compile(query);
      System.out.println(op.toString(query.getPrefixMapping()));
      System.out.println("------------------------------");
      
      QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
      try {
         if (query.isConstructType()) { 
            Model results = qexec.execConstruct() ;
            RDFDataMgr.write(System.out, results, Lang.TURTLE);
         } else if (query.isSelectType()) { 
            ResultSet results = qexec.execSelect() ;
            ResultSetFormatter.out(System.out, results, query) ;
         } else if (query.isAskType()) {
            System.out.println( (qexec.execAsk())? "-------\n| YES |\n-------" : "------\n| NO |\n------");
         } else if (query.isDescribeType()) {   
            Model results = qexec.execDescribe() ; 
            RDFDataMgr.write(System.out, results, Lang.TURTLE);
         }
      } catch (Exception e) {
    	  e.printStackTrace();
      } finally { qexec.close() ; }         
   }
 
   
   
	public static void main(String[] args) {
		
		String graphUri = ExecuteQueries.class.getResource("social_withblanks.ttl").getPath();
      File queryDir = new File(ExecuteQueries.class.getResource("query").getPath());     
		
		Model model = RDFDataMgr.loadModel(graphUri);
		
		File[]   files = queryDir.listFiles();

		
      for (int i = 0; i < files.length; i++) {
         File file = files[i];
	      System.out.println("\n\n------------------------------");
         System.out.println(" " + file.getName() + ":");
	      System.out.println("------------------------------");
	      executeQuery(model,file);
	   }
	}
}

