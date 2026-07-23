package com.kumara.careertracker.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kumara.careertracker.dto.ApplicationRequestDto;
import com.kumara.careertracker.dto.ApplicationResponseDto;
import com.kumara.careertracker.dto.DashboardResponseDto;
import com.kumara.careertracker.dto.StatusUpdateDto;
import com.kumara.careertracker.entity.Application;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.enums.ApplicationStatus;
import com.kumara.careertracker.repository.ApplicationRepository;
import com.kumara.careertracker.service.ApplicationService;
import com.kumara.careertracker.service.UserService;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private UserService userService;

	private final ApplicationRepository applicationRepository;

	public ApplicationController(ApplicationRepository applicationRepository) {

		this.applicationRepository = applicationRepository;

	}

	@PostMapping
	public ApplicationResponseDto createApplication(@RequestBody ApplicationRequestDto dto,
			Authentication authentication) {

		String email = authentication.getName();

		return applicationService.createApplication(dto, email);
	}

	@GetMapping("/my")
	public List<ApplicationResponseDto> getMyApplications() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return applicationService.getMyApplications(email);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApplicationResponseDto> getApplicationById(@PathVariable Long id) {

		return ResponseEntity.ok(applicationService.getApplicationById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApplicationResponseDto> updateApplication(@PathVariable Long id,
			@RequestBody ApplicationRequestDto dto) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return ResponseEntity.ok(applicationService.updateApplication(id, dto, email));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteApplication(@PathVariable Long id) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		applicationService.deleteApplication(id, email);

		return ResponseEntity.ok("Application deleted successfully");
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<ApplicationResponseDto>> getApplicationsByStatus(
			@PathVariable ApplicationStatus status) {

		return ResponseEntity.ok(applicationService.getApplicationsByStatus(status));
	}

	@GetMapping("/dashboard")
	public ResponseEntity<DashboardResponseDto> getDashboardStats() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return ResponseEntity.ok(applicationService.getDashboardStats(email));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<ApplicationResponseDto> updateStatus(@PathVariable Long id,
			@RequestBody StatusUpdateDto dto) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return ResponseEntity.ok(applicationService.updateStatus(id, dto.getStatus(), email));
	}

	@GetMapping("/whoami")
	public List<ApplicationResponseDto> getMyApplications(Authentication authentication) {

		String email = authentication.getName();

		System.out.println("EMAIL FROM JWT = " + email);

		User user = userService.findByEmail(email);

		System.out.println("USER FOUND ID = " + user.getId());

		List<ApplicationResponseDto> result = applicationService.getApplicationsByUser(user.getId());

		System.out.println("APPLICATION COUNT = " + result.size());

		return result;

	}

	@GetMapping("/stats/monthly")
	public Map<String, Integer> getMonthlyApplications() {

		List<Application> apps = applicationRepository.findAll();

		Map<String, Integer> result = new HashMap<>();

		for (Application app : apps) {

			if (app.getStatus() != null) {

				String month = app.setAppliedDate(LocalDate.now());

				result.put(month, result.getOrDefault(month, 0) + 1);

			}

		}

		return result;
	}

	@GetMapping("/stats/status")
	public Map<String, Integer> getStatusStats() {

		List<Application> apps = applicationRepository.findAll();

		Map<String, Integer> result = new HashMap<>();

		for (Application app : apps) {

			if (app.getStatus() != null) {

				String status = app.getStatus().name();

				result.put(status, result.getOrDefault(status, 0) + 1);

			}

		}

		return result;
	}

	@GetMapping("/stats/company")
	public Map<String, Integer> getCompanyStats() {

		List<Application> apps = applicationRepository.findAll();

		Map<String, Integer> result = new HashMap<>();

		for (Application app : apps) {

			if (app.getCompanyName() != null) {

				result.put(app.getCompanyName(), result.getOrDefault(app.getCompanyName(), 0) + 1);

			}

		}

		return result;
	}

}