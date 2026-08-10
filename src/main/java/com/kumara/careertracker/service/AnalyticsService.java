package com.kumara.careertracker.service;

import com.kumara.careertracker.dto.AnalyticsResponseDto;

public interface AnalyticsService {

	AnalyticsResponseDto getAnalytics(String email);

}