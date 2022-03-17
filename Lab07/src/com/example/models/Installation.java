package com.example.models;

public class Installation {
	
	private long installationId;
	private String routerNumber;
	private String city;
	private String address;
	private String postcode;
	private Client client;
	private PriceList priceList;
	
	public Installation() {}
	
	public Installation(long installationId, String routerNumber, String city, String address, String postcode,
			Client client, PriceList priceList) {
		super();
		this.installationId = installationId;
		this.routerNumber = routerNumber;
		this.city = city;
		this.address = address;
		this.postcode = postcode;
		this.client = client;
		this.priceList = priceList;
	}

	public Installation(String routerNumber, String city, String address, String postcode, Client client,
			PriceList priceList) {
		super();
		this.routerNumber = routerNumber;
		this.city = city;
		this.address = address;
		this.postcode = postcode;
		this.client = client;
		this.priceList = priceList;
	}

	public long getInstallationId() {
		return installationId;
	}

	public void setInstallationId(long installationId) {
		this.installationId = installationId;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	@Override
	public String toString() {
		return "Installation [installationId=" + installationId + ", routerNumber=" + routerNumber + ", city=" + city
				+ ", address=" + address + ", postcode=" + postcode + ", client=" + client.toString() 
				+ ", priceList=" + priceList.toString() + "]";
	}
	
	

}
