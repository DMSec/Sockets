package br.com.dmsec.thread;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import br.com.dmsec.database.ImportCSV;
import br.com.dmsec.exceptions.RetryOnException;
import br.com.dmsec.model.Consumer;


public class ClientThread extends Thread {

	public String name = "";
	public int port;
	public RetryOnException retryHandler;
	private Consumer consumidor;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
		
	}
	
	public ClientThread(Consumer consumidor) {
		this.consumidor = consumidor;
		this.name = consumidor.getName();
		this.port = consumidor.getPort();
		System.out.println(toString()+ " created: "+ Calendar.getInstance().getTime());
	}

	@Override
	public void run() {
		
		//try {
			//get the localhost IP address, if server is running on some other IP, you need to use that
		// For handling retries
        
	        InetAddress host;
	        
	        RetryOnException retryHandler = new RetryOnException(this.consumidor.getNumRetries(), this.consumidor.getTimeToWaitMS(), this.consumidor.getName());
	        
	        while(true) {
	        	try {
					
					host = InetAddress.getLocalHost();
					Socket socket = null;
			        ObjectOutputStream oos = null;
			        ObjectInputStream ois = null;
			        for(int i=0; i<5;i++){
			            //establish socket connection to server
			            socket = new Socket(host.getHostName(), this.port);
			            //write to socket using ObjectOutputStream
			            oos = new ObjectOutputStream(socket.getOutputStream());
			            System.out.println(toString()+ " Sending request to Socket Server");
			            if(i==4000)oos.writeObject("exit");
			            else oos.writeObject(""+i);
			            //read the server response message
			            ois = new ObjectInputStream(socket.getInputStream());
			            String message = (String) ois.readObject();
			            System.out.println(toString()+ " Message: " + message);
			            //close resources
			            ois.close();
			            oos.close();
			            Thread.sleep(4000);
			        }
				} catch (Exception ex)
	            {
	                try {
						retryHandler.exceptionOccurred();
						continue;
					} catch (Exception e) {
						ClientThread.currentThread().interrupt();
						System.out.println(toString()+ " interrupted");
						return;
					}
	                
	            }
	        
	        	
	        }
	        
			
	         
			
		//}
		
	}
	
	public static void main(String[] args) throws SQLException  {
//		
//		ClientThread runner1 = new ClientThread("Client-1", 6666);
//		
//		runner1.start();
//		

		
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
