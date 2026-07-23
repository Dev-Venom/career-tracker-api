package com.kumara.careertracker.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kumara.careertracker.dto.InterviewRequestDto;
import com.kumara.careertracker.dto.InterviewResponseDto;
import com.kumara.careertracker.entity.Application;
import com.kumara.careertracker.entity.Interview;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.repository.ApplicationRepository;
import com.kumara.careertracker.repository.InterviewRepository;
import com.kumara.careertracker.repository.UserRepository;

@Service
public class InterviewServiceImpl implements InterviewService {

	private final InterviewRepository interviewRepository;
	private final ApplicationRepository applicationRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	public InterviewServiceImpl(InterviewRepository interviewRepository, ApplicationRepository applicationRepository,
			UserRepository userRepository, NotificationService notificationService) {

		this.interviewRepository = interviewRepository;
		this.applicationRepository = applicationRepository;
		this.userRepository = userRepository;
		this.notificationService = notificationService;
	}

	@Override
	public InterviewResponseDto scheduleInterview(InterviewRequestDto request) {

		User currentUser = getCurrentUser();

		Application application = applicationRepository.findById(request.getApplicationId())
				.orElseThrow(() -> new RuntimeException("Application not found"));

		if (!application.getUser().getId().equals(currentUser.getId())) {
			throw new RuntimeException("Unauthorized");
		}

		Interview interview = new Interview();

		interview.setApplication(application);

		interview.setInterviewDate(request.getInterviewDate());

		interview.setInterviewTime(request.getInterviewTime());

		interview.setInterviewer(request.getInterviewer());

		interview.setMeetingLink(request.getMeetingLink());

		interview.setRound(request.getRound());

		interview.setNotes(request.getNotes());

		Interview savedInterview = interviewRepository.save(interview);

		notificationService.createNotification(currentUser, "Interview Scheduled",
				"Interview with " + application.getCompanyName() + " scheduled on " + interview.getInterviewDate(),
				"INTERVIEW");

		return mapToDto(savedInterview);
	}

	@Override
	public List<InterviewResponseDto> getMyInterviews() {

		User currentUser = getCurrentUser();

		List<Interview> interviews = interviewRepository
				.findByApplication_User_EmailOrderByInterviewDateAscInterviewTimeAsc(currentUser.getEmail());

		return interviews.stream().map(this::mapToDto).toList();
	}

	@Override
	public List<InterviewResponseDto> getUpcomingInterviews() {

		User currentUser = getCurrentUser();

		List<Interview> interviews = interviewRepository
				.findByApplication_User_EmailOrderByInterviewDateAscInterviewTimeAsc(currentUser.getEmail());

		return interviews.stream().filter(interview -> !interview.getInterviewDate().isBefore(LocalDate.now()))
				.sorted((i1, i2) -> i1.getInterviewDate().compareTo(i2.getInterviewDate())).map(this::mapToDto)
				.toList();
	}

	@Override
	public InterviewResponseDto updateInterview(Long id, InterviewRequestDto request) {

		User currentUser = getCurrentUser();

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Interview not found"));

		if (!interview.getApplication().getUser().getId().equals(currentUser.getId())) {

			throw new RuntimeException("Unauthorized");
		}

		interview.setInterviewDate(request.getInterviewDate());

		interview.setInterviewTime(request.getInterviewTime());

		interview.setInterviewer(request.getInterviewer());

		interview.setMeetingLink(request.getMeetingLink());

		interview.setRound(request.getRound());

		interview.setNotes(request.getNotes());

		Interview updated = interviewRepository.save(interview);

		return mapToDto(updated);
	}

	@Override
	public void deleteInterview(Long id) {

		User currentUser = getCurrentUser();

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Interview not found"));

		if (!interview.getApplication().getUser().getId().equals(currentUser.getId())) {

			throw new RuntimeException("Unauthorized");
		}

		interviewRepository.delete(interview);
	}

	@Override
	public List<InterviewResponseDto> getUpcomingInterviews(String email) {

		return interviewRepository.findByApplication_User_EmailOrderByInterviewDateAscInterviewTimeAsc(email).stream()
				.filter(interview -> !interview.getInterviewDate().isBefore(LocalDate.now())).map(this::mapToDto)
				.toList();
	}

	@Override
	public InterviewResponseDto getInterviewById(Long id, String email) {

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Interview not found"));

		if (!interview.getApplication().getUser().getEmail().equals(email)) {

			throw new RuntimeException("Unauthorized");
		}

		return mapToDto(interview);
	}

	@Override
	public InterviewResponseDto updateInterview(Long id, InterviewRequestDto dto, String email) {

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Interview not found"));

		if (!interview.getApplication().getUser().getEmail().equals(email)) {

			throw new RuntimeException("Unauthorized");
		}

		interview.setInterviewDate(dto.getInterviewDate());
		interview.setInterviewTime(dto.getInterviewTime());
		interview.setInterviewer(dto.getInterviewer());
		interview.setRound(dto.getRound());
		interview.setMeetingLink(dto.getMeetingLink());
		interview.setNotes(dto.getNotes());

		interviewRepository.save(interview);

		return mapToDto(interview);
	}

	@Override
	public void deleteInterview(Long id, String email) {

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Interview not found"));

		if (!interview.getApplication().getUser().getEmail().equals(email)) {

			throw new RuntimeException("Unauthorized");
		}

		interviewRepository.delete(interview);
	}

	private User getCurrentUser() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	private InterviewResponseDto mapToDto(Interview interview) {

		InterviewResponseDto dto = new InterviewResponseDto();

		dto.setId(interview.getId());
		dto.setApplicationId(interview.getApplication().getId());
		dto.setCompanyName(interview.getApplication().getCompanyName());
		dto.setJobTitle(interview.getApplication().getJobTitle());
		dto.setInterviewDate(interview.getInterviewDate());
		dto.setInterviewTime(interview.getInterviewTime());
		dto.setInterviewer(interview.getInterviewer());
		dto.setMeetingLink(interview.getMeetingLink());
		dto.setRound(interview.getRound());
		dto.setNotes(interview.getNotes());

		return dto;
	}

}