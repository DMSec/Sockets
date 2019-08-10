package br.com.dmsec.model;

public class Consumer {
	
	private int Id;
	private String name;
	private String address;
	private int port;
	private int numRetries;
	private long timeToWaitMS;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getNumRetries() {
		return numRetries;
	}
	public void setNumRetries(int numRetries) {
		this.numRetries = numRetries;
	}
	public long getTimeToWaitMS() {
		return timeToWaitMS;
	}
	public void setTimeToWaitMS(long timeToWaitMS) {
		this.timeToWaitMS = timeToWaitMS;
	}
	
	

}
