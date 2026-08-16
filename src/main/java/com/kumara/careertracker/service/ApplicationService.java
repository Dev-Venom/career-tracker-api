package com.kumara.careertracker.service;

import java.util.List;

import com.kumara.careertracker.dto.ApplicationRequestDto;
import com.kumara.careertracker.dto.ApplicationResponseDto;
import com.kumara.careertracker.dto.DashboardResponseDto;
import com.kumara.careertracker.enums.ApplicationStatus;

public interface ApplicationService {

	ApplicationResponseDto createApplication(ApplicationRequestDto dto, String email);

	ApplicationResponseDto updateApplication(Long id, ApplicationRequestDto dto, String email);

	void deleteApplication(Long id, String email);

	List<ApplicationResponseDto> getApplicationsByUser(Long userId);

	List<ApplicationResponseDto> getApplicationsByStatus(ApplicationStatus status);

	ApplicationResponseDto getApplicationById(Long id, String email);

	DashboardResponseDto getDashboardStats(String email);

	List<ApplicationResponseDto> getMyApplications(String email);

	ApplicationResponseDto updateStatus(Long id, String status, String email);

}