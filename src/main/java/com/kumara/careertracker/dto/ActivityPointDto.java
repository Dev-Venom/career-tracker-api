package com.kumara.careertracker.dto;

public class ActivityPointDto {

	private String label;

	private int value;

	public ActivityPointDto() {
	}

	public ActivityPointDto(String label, int value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}