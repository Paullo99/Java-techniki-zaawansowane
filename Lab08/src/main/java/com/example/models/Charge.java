package com.example.models;

import java.time.LocalDate;

public class Charge {
	
	private long chargeId;
	private LocalDate deadline;
	private float amount;
	private Installation installation;
	
	public Charge() {}

	public Charge(long chargeId, LocalDate deadline, float amount, Installation installation) {
		super();
		this.chargeId = chargeId;
		this.deadline = deadline;
		this.amount = amount;
		this.installation = installation;
	}

	public Charge(LocalDate deadline, float amount, Installation installation) {
		super();
		this.deadline = deadline;
		this.amount = amount;
		this.installation = installation;
	}

	public long getChargeId() {
		return chargeId;
	}

	public void setChargeId(long chargeId) {
		this.chargeId = chargeId;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Installation getInstallation() {
		return installation;
	}

	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	@Override
	public String toString() {
		return "Charge [chargeId=" + chargeId + ", deadline=" + deadline + ", amount=" + amount + ", installation="
				+ installation + "]";
	}
	
}
