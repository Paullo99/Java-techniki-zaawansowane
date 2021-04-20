package com.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteForThirdQuestion {

	private double data;

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}
}