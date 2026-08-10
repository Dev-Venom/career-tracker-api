package com.kumara.careertracker.dto;

public class ApplicationStatusAnalyticsDto {

	private long applied;
	private long interview;
	private long offer;
	private long rejected;
	private ApplicationStatusAnalyticsDto applicationStatus;

	public ApplicationStatusAnalyticsDto getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatusAnalyticsDto applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public long getApplied() {
		return applied;
	}

	public void setApplied(long applied) {
		this.applied = applied;
	}

	public long getInterview() {
		return interview;
	}

	public void setInterview(long interview) {
		this.interview = interview;
	}

	public long getOffer() {
		return offer;
	}

	public void setOffer(long offer) {
		this.offer = offer;
	}

	public long getRejected() {
		return rejected;
	}

	public void setRejected(long rejected) {
		this.rejected = rejected;
	}
}