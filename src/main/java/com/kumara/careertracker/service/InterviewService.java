package com.kumara.careertracker.service;

import java.util.List;

import com.kumara.careertracker.dto.InterviewRequestDto;
import com.kumara.careertracker.dto.InterviewResponseDto;

public interface InterviewService {

	InterviewResponseDto scheduleInterview(InterviewRequestDto request, String email);

	List<InterviewResponseDto> getMyInterviews(String email);

	List<InterviewResponseDto> getUpcomingInterviews(String email);

	InterviewResponseDto getInterviewById(Long id, String email);

	InterviewResponseDto updateInterview(Long id, InterviewRequestDto dto, String email);

	void deleteInterview(Long id, String email);
}