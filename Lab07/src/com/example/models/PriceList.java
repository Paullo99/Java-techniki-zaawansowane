package com.example.models;

public class PriceList {
	
	private long priceListId;
	private String serviceType;
	private float price;
	
	public PriceList() {} 
	
	public PriceList(String serviceType, float price) {
		super();
		this.serviceType = serviceType;
		this.price = price;
	}
	
	public PriceList(long priceListId, String serviceType, float price) {
		super();
		this.priceListId = priceListId;
		this.serviceType = serviceType;
		this.price = price;
	}

	public long getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(long priceListId) {
		this.priceListId = priceListId;
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
		return "PriceList [priceListId=" + priceListId + ", serviceType=" + serviceType + ", price=" + price + "]";
	}
	
}
