package com.example;

public interface QueueManagementMXBean {

	public void addCase(Case c);
	public void removeCase(Case c);
	public void editCase(Case c);
	//Method com.example.QueueManagementMXBean.getAllCases has parameter or return type that cannot be translated into an open type
	//Jesli by³ ArrayList
	public Case[] getAllCases();
}
