package com.example;

public class Ticket {
	private int number;
	private Case c;
	
	public Ticket() {}

	public Ticket(Case c) {
		this.number = c.getCurrentNumber();
		this.c = c;
		c.increaseCurrentNumber();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Case getCase() {
		return c;
	}

	public void setCase(Case c) {
		this.c = c;
	}
	
}
