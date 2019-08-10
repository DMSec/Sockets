package br.com.dmsec.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	
	public String name = "";
	//static ServerSocket variable
    private ServerSocket server;
    //socket server port on which it will listen
    private int port;
	
	public Server(String name, int port) {
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

	public static void main(String[] args) {
		
		Server runner1 = new Server("Thread-1", 6666);
		runner1.start();

		
		
	}

	
}
