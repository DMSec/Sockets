package br.com.dmsec;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import br.com.dmsec.database.ImportCSV;
import br.com.dmsec.example.Server;
import br.com.dmsec.model.Consumer;
import br.com.dmsec.thread.ClientThread;


public class App {

	public static void main(String[] args) throws SQLException {
				
		HashMap<Integer, Consumer> consumers = null;
		//Criar DB senao existir
		String pathDB = "jdbc:h2:file:./db/test.mv.db";
		String path = pathDB.replace("jdbc:h2:file:", "");
        System.out.println(path);
        File dbFile = new File(path);
        if (!dbFile.exists()) {
            //Criar tabela de clients e popular
        	
        	ImportCSV csv = new ImportCSV();
        	csv.insertWithStatement(true, false);
        	consumers = csv.getAllConsumers();
        	        	
        }else {
        	System.out.println("DB exist");
        	//Criar tabela de clients e popular
        	
        	ImportCSV csv = new ImportCSV();
        	 consumers = csv.getAllConsumers();
        	
        }
            
        
        
        if(consumers == null || consumers.isEmpty() ) {
        	System.out.println("Base vazia");
        	System.exit(1);
        }else {
        	Server runner1 = new Server("Thread-1", 6666);
    		runner1.start();
    
    		Server runner2 = new Server("Thread-2", 6661);
    		runner2.start();
    		
    		Iterator<?> it = consumers.entrySet().iterator();
    	    while (it.hasNext()) {
    	        @SuppressWarnings("rawtypes")
				Map.Entry pair = (Map.Entry)it.next();
    	        Consumer consumidor = (Consumer) pair.getValue();
    	        ClientThread client = new ClientThread(consumidor);
    	            	      
    	        client.start();
    	        	
    	        
    	        
    	        //it.remove(); // avoids a ConcurrentModificationException
    	        
    	    }

        }
           
		
	}

}