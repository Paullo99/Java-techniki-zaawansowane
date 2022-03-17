package com.example.dto;

public class InstallationDTO {
	
	private String routerNumber;
	private String city;
	private String address;
	private String postcode;
	private long clientId;
	private long priceListId;
	
	public InstallationDTO() {}

	public InstallationDTO( String routerNumber, String city, String address, String postcode,
			long clientId, long priceListId) {
		super();
		this.routerNumber = routerNumber;
		this.city = city;
		this.address = address;
		this.postcode = postcode;
		this.clientId = clientId;
		this.priceListId = priceListId;
	}

	public String getRouterNumber() {
		return routerNumber;
	}

	public void setRouterNumber(String routerNumber) {
		this.routerNumber = routerNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(long priceListId) {
		this.priceListId = priceListId;
	}
	
	
	
}
