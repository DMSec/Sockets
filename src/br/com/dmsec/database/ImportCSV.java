package br.com.dmsec.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.h2.tools.Csv;

import br.com.dmsec.model.Consumer;
import br.com.dmsec.thread.ClientThread;


public class ImportCSV {
	
	
    public static void main(String[] args) throws Exception {
    	
    	
    	
        ResultSet rs = new Csv().read("scripts/test.csv", null, null);
        ResultSetMetaData meta = rs.getMetaData();
        while (rs.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                System.out.println(
                    meta.getColumnLabel(i + 1) + ": " +
                    rs.getString(i + 1));
            }
            System.out.println();
        }
        rs.close();
        
        ImportCSV csv = new ImportCSV();
       // csv.insertWithStatement();
    }
    
    
    public String bulkLoad() {
    	//CREATE TABLE TEST AS SELECT * FROM CSVREAD('scripts/test.csv');
        //CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))
          //  AS SELECT * FROM CSVREAD('test.csv');
    	String bulkload = "CREATE TABLE TEST AS SELECT * FROM CSVREAD('scripts/test.csv')";
    	return bulkload;
    	
    }
    
    
    public HashMap<Integer, Consumer> getAllConsumers() {
    	HashMap<Integer, Consumer> consumers = new HashMap<Integer,Consumer>();
    	Connection connection = null;
    	
        try {
        	connection = H2Database.getDBConnection(true);
            Statement stmt = null;
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            
            //stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Asha')");

            ResultSet rs = stmt.executeQuery("select * from TEST");
            System.out.println("H2 Database selected through Statement");
            while (rs.next()) {
            	Consumer client = new Consumer();
            	client.setId(rs.getInt("id"));
            	client.setName(rs.getString("name"));
            	client.setAddress(rs.getString("address"));
            	client.setPort(rs.getInt("port"));
            	client.setNumRetries(rs.getInt("numRetries"));
            	client.setTimeToWaitMS(rs.getLong("timeToWaitMS"));
            	consumers.put(client.getId(), client);
                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
				connection.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
        }
    	
    	
    	return consumers;
    }
    
    
    
    
    public void insertWithStatement(boolean bulkload, boolean dbExist) throws SQLException {
        Connection connection = H2Database.getDBConnection(dbExist);
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            if(bulkload) {
            	stmt.execute(bulkLoad());
            }
            
            
            
            //stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Asha')");

            ResultSet rs = stmt.executeQuery("select * from TEST");
            System.out.println("H2 Database inserted through Statement");
            while (rs.next()) {
                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
    
}