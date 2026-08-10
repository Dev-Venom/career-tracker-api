package com.kumara.careertracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kumara.careertracker.dto.AnalyticsResponseDto;
import com.kumara.careertracker.service.AnalyticsService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

	private final AnalyticsService analyticsService;

	public AnalyticsController(AnalyticsService analyticsService) {

		this.analyticsService = analyticsService;

	}

	@GetMapping
	public ResponseEntity<AnalyticsResponseDto> getAnalytics(Authentication authentication) {

		return ResponseEntity.ok(analyticsService.getAnalytics(authentication.getName()));

	}

}
