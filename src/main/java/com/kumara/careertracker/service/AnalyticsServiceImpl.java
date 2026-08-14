package com.kumara.careertracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kumara.careertracker.dto.ActivityPointDto;
import com.kumara.careertracker.dto.AnalyticsResponseDto;
import com.kumara.careertracker.dto.ApplicationStatusAnalyticsDto;
import com.kumara.careertracker.dto.CareerInsightDto;
import com.kumara.careertracker.dto.CareerJourneyDto;
import com.kumara.careertracker.entity.Application;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.enums.ApplicationStatus;
import com.kumara.careertracker.repository.ApplicationRepository;
import com.kumara.careertracker.repository.UserRepository;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

	private final ApplicationRepository applicationRepository;
	private final UserRepository userRepository;

	public AnalyticsServiceImpl(ApplicationRepository applicationRepository, UserRepository userRepository) {

		this.applicationRepository = applicationRepository;
		this.userRepository = userRepository;
	}

	@Override
	public AnalyticsResponseDto getAnalytics(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		List<Application> applications = applicationRepository.findByUser_Id(user.getId());

		Map<LocalDate, Long> activityMap = applications.stream()
				.collect(Collectors.groupingBy(Application::getAppliedDate, TreeMap::new, Collectors.counting()));

		List<ActivityPointDto> activityPoints = new ArrayList<>();

		for (Map.Entry<LocalDate, Long> entry : activityMap.entrySet()) {

			ActivityPointDto point = new ActivityPointDto();

			point.setDate(entry.getKey());

			point.setCount(entry.getValue());

			activityPoints.add(point);
		}

		CareerJourneyDto journey = new CareerJourneyDto();

		journey.setApplied(!applications.isEmpty());

		journey.setInterview(applications.stream().anyMatch(app -> app.getStatus() == ApplicationStatus.INTERVIEW));

		journey.setOffer(applications.stream().anyMatch(app -> app.getStatus() == ApplicationStatus.OFFER));

		ApplicationStatusAnalyticsDto statusAnalytics = new ApplicationStatusAnalyticsDto();

		statusAnalytics
				.setApplied(applications.stream().filter(app -> app.getStatus() == ApplicationStatus.APPLIED).count());

		statusAnalytics.setInterview(
				applications.stream().filter(app -> app.getStatus() == ApplicationStatus.INTERVIEW).count());

		statusAnalytics
				.setOffer(applications.stream().filter(app -> app.getStatus() == ApplicationStatus.OFFER).count());

		statusAnalytics.setRejected(
				applications.stream().filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count());

		CareerInsightDto insight = new CareerInsightDto();

		insight.setTitle("Career Pulse");

		insight.setMessage("You're building momentum. Keep applying consistently!");

		AnalyticsResponseDto dto = new AnalyticsResponseDto();

		dto.setCareerJourney(journey);

		dto.setCareerInsight(insight);

		dto.setApplicationStatus(statusAnalytics);

		dto.setApplicationActivity(activityPoints);

		return dto;
	}
}