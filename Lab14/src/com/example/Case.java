package com.example;

import java.beans.ConstructorProperties;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeDataView;
import javax.management.openmbean.CompositeType;

public class Case implements CompositeDataView {

	private int id;
	private int currentNumber;
	private String name;
	private int priority;
	private char symbol;

	public Case() {
	}
	

	@ConstructorProperties({ "id", "name", "priority", "symbol", "currentNumber" })
	public Case(int id, String name, int priority, char symbol) {
		this.name = name;
		this.priority = priority;
		this.symbol = symbol;
		this.currentNumber = 0;
		this.id = id;
	}

	public static Case from(CompositeData cd) {
		return new Case((int) cd.get("id"), (String) cd.get("name"), (int) cd.get("priority"), (char) cd.get("symbol"));
	}

	@Override
	public CompositeData toCompositeData(CompositeType ct) {
		try {
			CompositeData cd = new CompositeDataSupport(ct, new String[] {"id" ,"name", "priority", "symbol", "currentNumber" },
					new Object[] {id, name, priority, symbol, currentNumber });
			assert ct.isValue(cd);
			return cd;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "[" + this.symbol + "]" + " " + this.name +  ", P:" + this.priority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCurrentNumber() {
		return currentNumber;
	}

	public void increaseCurrentNumber() {
		this.currentNumber++;
	}

}
