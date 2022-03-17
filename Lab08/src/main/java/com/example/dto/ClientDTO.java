package com.example.dto;

public class ClientDTO {
	private String firstName;
	private String surname;
	
	public ClientDTO() {} 

	
	public ClientDTO(String firstName, String surname) {
		super();
		this.firstName = firstName;
		this.surname = surname;
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
		return "Client [firstName=" + firstName + ", surname=" + surname + "]";
	}

}
