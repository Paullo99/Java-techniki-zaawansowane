package com.example.models;

import java.time.LocalDate;

public class Payment {
	
	private long paymentId;
	private LocalDate paymentDate;
	private float paymentAmount;
	private Installation installation;
	
	public Payment() {} 
	
	public Payment(long paymentId, LocalDate paymentDate, float paymentAmount, Installation installation) {
		super();
		this.paymentId = paymentId;
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.installation = installation;
	}

	public Payment(LocalDate paymentDate, float paymentAmount, Installation installation) {
		super();
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.installation = installation;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Installation getInstallation() {
		return installation;
	}

	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", paymentDate=" + paymentDate + ", paymentAmount=" + paymentAmount
				+ ", installation=" + installation + "]";
	}
	
}
