package com.example.models;

public class Client {
	
	private long clientId;
	private String firstName;
	private String surname;
	
	public Client() {} 
	
	public Client(long id, String firstName, String surname) {
		super();
		this.clientId = id;
		this.firstName = firstName;
		this.surname = surname;
	}
	
	public Client(String firstName, String surname) {
		super();
		this.firstName = firstName;
		this.surname = surname;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long id) {
		this.clientId = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", firstName=" + firstName + ", surname=" + surname + "]";
	}

}
