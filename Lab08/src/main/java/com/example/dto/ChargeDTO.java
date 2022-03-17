package com.example.dto;

public class ChargeDTO {
	
	private String deadline;
	private float amount;
	private long installationId;
	
	public ChargeDTO() { }
	
	public ChargeDTO(String deadline, float amount, long installationId) {
		super();
		this.deadline = deadline;
		this.amount = amount;
		this.installationId = installationId;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public long getInstallationId() {
		return installationId;
	}

	public void setInstallationId(long installationId) {
		this.installationId = installationId;
	}
}
