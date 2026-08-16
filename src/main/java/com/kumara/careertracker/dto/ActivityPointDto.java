package com.kumara.careertracker.dto;

import java.time.LocalDate;

public class ActivityPointDto {

	private LocalDate date;

	private long count;

	public ActivityPointDto() {
	}

	public ActivityPointDto(LocalDate date, long count) {
		this.date = date;
		this.count = count;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}