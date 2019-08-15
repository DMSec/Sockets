package br.com.dmsec.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.*;
import com.splunk.*;  

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;


public class Server2 extends Thread {

	
	final static String loggerName = "splunk.logger";
	
	public String name = "";
	//static ServerSocket variable
    private ServerSocket server;
    //socket server port on which it will listen
    private int port;
	
	public Server2(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
		
		
		
	}

	@Override
	public void run(){
		
		//create the socket server object
        try {
			server = new ServerSocket(this.port);
			while(true){
	            System.out.println(this.toString() + " Waiting for the client request");
	            //creating socket and waiting for client connection
	            Socket socket = server.accept();
	            //read from socket to ObjectInputStream object
	            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	            //convert ObjectInputStream object to String
	            String message = (String) ois.readObject();
	            System.out.println(this.toString()+ " Message Received: " + message);
	            //create ObjectOutputStream object
	            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	            //write object to Socket
	            oos.writeObject(this.toString()+" Hi Client "+message);
	            //close resources
	            ois.close();
	            oos.close();
	            socket.close();
	            //terminate the server if client sends exit request
	            if(message.equalsIgnoreCase("exit")) break;
	        }
	        System.out.println(this.toString()+ " Shutting down Socket server!!");
	        //close the ServerSocket object
	        server.close();
		} catch (IOException | ClassNotFoundException e1) {
			this.run();
		}
        //keep listens indefinitely until receives 'exit' call or program terminates
		
		// TODO Auto-generated method stub
//		for (int i = 0; i < 5; i++) {
//			System.out.println(this.toString() + " Hello: " + i);
//
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, IOException {
		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
		 // Create a map of arguments and add login parameters
        ServiceArgs loginArgs = new ServiceArgs();
        loginArgs.setHost("localhost");
        loginArgs.setPort(8089);
        loginArgs.setUsername("user");
        loginArgs.setPassword("pass");
        
        
        // Create a Service instance and log in with the argument map
        Service service = Service.connect(loginArgs);


        // A second way to create a new Service object and log in
        // Service service = new Service("localhost", 8089);
        // service.login("admin", "yourpassword");

        // A third way to create a new Service object and log in
        // Service service = new Service(loginArgs);
        // service.login();

        // Print installed apps to the console to verify login
        for (Application app : service.getApplications().values()) {
            System.out.println(app.getName());
        }
		
        // // Retrieve the index for the data
        Index myIndex = service.getIndexes().get("main");

        // Specify  values to apply to the event
        Args eventArgs = new Args();
        eventArgs.put("sourcetype", "access_combined.log");
        eventArgs.put("host", "local");

        // Submit an event over HTTP
        myIndex.submit(eventArgs, "This is my HTTP event");
		
		
		Server2 runner1 = new Server2("Thread-1", 6667);
		runner1.start();

		
	}

	
}
