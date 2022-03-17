package com.example.dto;

public class PaymentDTO {
	
	private String paymentDate;
	private float paymentAmount;
	private long installationId;
	
	public PaymentDTO() {} 
	
	public PaymentDTO(String paymentDate, float paymentAmount, long installationId) {
		super();
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.installationId = installationId;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public long getInstallationId() {
		return installationId;
	}

	public void setInstallation(long installationId) {
		this.installationId = installationId;
	}

	@Override
	public String toString() {
		return "Payment [paymentDate=" + paymentDate + ", paymentAmount=" + paymentAmount
				+ ", installationId=" + installationId + "]";
	}
	
}
