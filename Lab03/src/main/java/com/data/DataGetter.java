package com.data;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.app.Lab03Application;

public class DataGetter {

	private static HttpHeaders headers;

	private static void createHeaders() {

		headers = new HttpHeaders();
		//key
		headers.add("x-rapidapi-key", "1234xyz");
		headers.add("x-rapidapi-host", "wft-geo-db.p.rapidapi.com");
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	}

	public static long getDataForFirstQuestion(int index) {

		String[] cityCodes = { "Q270", "Q31487", "Q580", "Q268", "Q1792", "Q1799", "Q588" };

		String url = "https://wft-geo-db.p.rapidapi.com/v1/geo/cities/";
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append(cityCodes[index]);

		createHeaders();

		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<QuoteForFirstQuestion> response = Lab03Application.getRestTemplate().exchange(sb.toString(),
				HttpMethod.GET, request, QuoteForFirstQuestion.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("Request Successful.");
			System.out.println(response.getBody());
		} else {
			System.out.println("Request Failed");
			System.out.println(response.getStatusCode());
		}

		QuoteForFirstQuestion quote = response.getBody();

		return quote.getData().getPopulation() / 1000;
	}

	public static long getDataForSecondQuestion(int index, String population) {

		String[] regionCodes = { "MZ", "MA", "WP", "PM", "DS", "SL" };

		String url = "https://wft-geo-db.p.rapidapi.com/v1/geo/countries/PL/regions/";
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append(regionCodes[index]);
		sb.append("/cities?minPopulation=");
		sb.append(population);

		createHeaders();

		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<QuoteForSecondQuestion> response = Lab03Application.getRestTemplate().exchange(sb.toString(),
				HttpMethod.GET, request, QuoteForSecondQuestion.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("Request Successful.");
			System.out.println(response.getBody());
		} else {
			System.out.println("Request Failed");
			System.out.println(response.getStatusCode());
		}

		QuoteForSecondQuestion quote = response.getBody();

		return quote.getMetadata().getTotalCount();
	}

	public static long getDataForThirdQuestion(int index) {

		String[] countryCodes = { "Q64", "Q84", "Q2807", "Q1085", "Q585", "Q90", "Q220", "Q1741", "Q1435" };

		String url = "https://wft-geo-db.p.rapidapi.com/v1/geo/cities/Q270/distance?fromCityId=";
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append(countryCodes[index]);
		sb.append("&distanceUnit=KM");

		createHeaders();

		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<QuoteForThirdQuestion> response = Lab03Application.getRestTemplate().exchange(sb.toString(),
				HttpMethod.GET, request, QuoteForThirdQuestion.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("Request Successful.");
			System.out.println(response.getBody());
		} else {
			System.out.println("Request Failed");
			System.out.println(response.getStatusCode());
		}

		QuoteForThirdQuestion quote = response.getBody();

		System.out.println((long) quote.getData());

		return (long) quote.getData();
	}
}
