package com.kumara.careertracker.dto;

import java.util.List;

public class AnalyticsResponseDto {

	private CareerJourneyDto careerJourney;

	private CareerInsightDto careerInsight;

	private List<ActivityPointDto> applicationActivity;

	public List<ActivityPointDto> getApplicationActivity() {
		return applicationActivity;
	}

	public void setApplicationActivity(List<ActivityPointDto> applicationActivity) {
		this.applicationActivity = applicationActivity;
	}

	public CareerJourneyDto getCareerJourney() {
		return careerJourney;
	}

	public void setCareerJourney(CareerJourneyDto careerJourney) {
		this.careerJourney = careerJourney;
	}

	public CareerInsightDto getCareerInsight() {
		return careerInsight;
	}

	public void setCareerInsight(CareerInsightDto careerInsight) {
		this.careerInsight = careerInsight;
	}

}
