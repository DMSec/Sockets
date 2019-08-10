package br.com.dmsec.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Database {

	private static final String DB_DRIVER = "org.h2.Driver";
	//"jdbc:h2:~/test"
	//"jdbc:h2:/data/sample;IFEXISTS=TRUE";
	//"jdbc:h2:/media/douglasmsi/dados/Tools/test;IFEXISTS=TRUE"
	public static boolean IFEXISTS;
	
    private static final String DB_CONNECTION = "jdbc:h2:file:./db/test;IFEXISTS=";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";
	
    
	
	public static Connection getDBConnection(boolean dbExist) {
	        Connection dbConnection = null;
	        
	        try {
	            Class.forName(DB_DRIVER);
	        } catch (ClassNotFoundException e) {
	            System.out.println(e.getMessage());
	        }
	        try {
	            dbConnection = DriverManager.getConnection(DB_CONNECTION+dbExist, DB_USER, DB_PASSWORD);
	            return dbConnection;
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        return dbConnection;
	    }
	}

