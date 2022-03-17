package com.example.dto;

public class PriceListDTO {
	
	private String serviceType;
	private float price;
	
	public PriceListDTO() {} 
	
	public PriceListDTO(String serviceType, float price) {
		super();
		this.serviceType = serviceType;
		this.price = price;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "PriceList [serviceType=" + serviceType + ", price=" + price + "]";
	}
	
}
