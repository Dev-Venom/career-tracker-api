package com.kumara.careertracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kumara.careertracker.dto.InterviewRequestDto;
import com.kumara.careertracker.dto.InterviewResponseDto;
import com.kumara.careertracker.service.InterviewService;

@RestController
@RequestMapping("/interviews")
public class InterviewController {

	private final InterviewService interviewService;

	public InterviewController(InterviewService interviewService) {
		this.interviewService = interviewService;
	}

	@PostMapping
	public ResponseEntity<InterviewResponseDto> scheduleInterview(@RequestBody InterviewRequestDto request,
			Authentication authentication) {

		InterviewResponseDto interview = interviewService.scheduleInterview(request, authentication.getName());

		return new ResponseEntity<>(interview, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<InterviewResponseDto>> getMyInterviews(Authentication authentication) {

		return ResponseEntity.ok(interviewService.getMyInterviews(authentication.getName()));
	}

	@GetMapping("/upcoming")
	public ResponseEntity<List<InterviewResponseDto>> getUpcomingInterviews(Authentication authentication) {

		return ResponseEntity.ok(interviewService.getUpcomingInterviews(authentication.getName()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<InterviewResponseDto> getInterviewById(@PathVariable Long id, Authentication authentication) {

		System.out.println("Controller reached!");

		return ResponseEntity.ok(interviewService.getInterviewById(id, authentication.getName()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<InterviewResponseDto> updateInterview(@PathVariable Long id,
			@RequestBody InterviewRequestDto dto, Authentication authentication) {

		return ResponseEntity.ok(interviewService.updateInterview(id, dto, authentication.getName()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInterview(@PathVariable Long id, Authentication authentication) {

		interviewService.deleteInterview(id, authentication.getName());

		return ResponseEntity.noContent().build();
	}
}