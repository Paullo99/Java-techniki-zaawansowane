package com.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

	private int currentOffset;
	private long totalCount;
	
	public int getCurrentOffset() {
		return currentOffset;
	}
	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = currentOffset;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
