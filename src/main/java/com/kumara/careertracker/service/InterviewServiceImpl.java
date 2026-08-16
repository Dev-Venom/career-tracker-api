package com.kumara.careertracker.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kumara.careertracker.dto.InterviewRequestDto;
import com.kumara.careertracker.dto.InterviewResponseDto;
import com.kumara.careertracker.entity.Application;
import com.kumara.careertracker.entity.Interview;
import com.kumara.careertracker.exception.ResourceNotFoundException;
import com.kumara.careertracker.repository.ApplicationRepository;
import com.kumara.careertracker.repository.InterviewRepository;

@Service
public class InterviewServiceImpl implements InterviewService {

	private final InterviewRepository interviewRepository;
	private final ApplicationRepository applicationRepository;

	public InterviewServiceImpl(InterviewRepository interviewRepository, ApplicationRepository applicationRepository) {

		this.interviewRepository = interviewRepository;
		this.applicationRepository = applicationRepository;
	}

	@Override
	public InterviewResponseDto scheduleInterview(InterviewRequestDto request, String email) {

		Application application = getUserApplication(request.getApplicationId(), email);

		if (interviewRepository.findByApplication(application).isPresent()) {
			throw new RuntimeException("An interview already exists for this application");
		}

		Interview interview = new Interview();

		interview.setInterviewDate(request.getInterviewDate());
		interview.setInterviewTime(request.getInterviewTime());
		interview.setInterviewer(request.getInterviewer());
		interview.setMeetingLink(request.getMeetingLink());
		interview.setRound(request.getRound());
		interview.setNotes(request.getNotes());
		interview.setApplication(application);

		Interview savedInterview = interviewRepository.save(interview);

		return mapToDto(savedInterview);
	}

	@Override
	public List<InterviewResponseDto> getMyInterviews(String email) {

		return interviewRepository.findByApplication_User_EmailOrderByInterviewDateAscInterviewTimeAsc(email).stream()
				.map(this::mapToDto).toList();
	}

	@Override
	public List<InterviewResponseDto> getUpcomingInterviews(String email) {

		return interviewRepository.findByApplication_User_EmailOrderByInterviewDateAscInterviewTimeAsc(email).stream()
				.filter(interview -> interview.getInterviewDate() != null
						&& !interview.getInterviewDate().isBefore(LocalDate.now()))
				.map(this::mapToDto).toList();
	}

	@Override
	public InterviewResponseDto getInterviewById(Long id, String email) {

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

		verifyOwnership(interview, email);

		return mapToDto(interview);
	}

	@Override
	public InterviewResponseDto updateInterview(Long id, InterviewRequestDto request, String email) {

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

		verifyOwnership(interview, email);

		Application application = getUserApplication(request.getApplicationId(), email);

		interview.setInterviewDate(request.getInterviewDate());
		interview.setInterviewTime(request.getInterviewTime());
		interview.setInterviewer(request.getInterviewer());
		interview.setMeetingLink(request.getMeetingLink());
		interview.setRound(request.getRound());
		interview.setNotes(request.getNotes());
		interview.setApplication(application);

		Interview updatedInterview = interviewRepository.save(interview);

		return mapToDto(updatedInterview);
	}

	@Override
	public void deleteInterview(Long id, String email) {

		Interview interview = interviewRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

		verifyOwnership(interview, email);

		interviewRepository.delete(interview);
	}

	private Application getUserApplication(Long applicationId, String email) {

		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found"));

		if (!application.getUser().getEmail().equals(email)) {
			throw new RuntimeException("You cannot access this application");
		}

		return application;
	}

	private void verifyOwnership(Interview interview, String email) {

		if (interview.getApplication() == null || interview.getApplication().getUser() == null
				|| !interview.getApplication().getUser().getEmail().equals(email)) {

			throw new RuntimeException("You cannot access this interview");
		}
	}

	private InterviewResponseDto mapToDto(Interview interview) {

		InterviewResponseDto dto = new InterviewResponseDto();

		dto.setId(interview.getId());

		if (interview.getApplication() != null) {

			Application application = interview.getApplication();

			dto.setApplicationId(application.getId());
			dto.setCompanyName(application.getCompanyName());
			dto.setJobTitle(application.getJobTitle());
		}

		dto.setInterviewDate(interview.getInterviewDate());
		dto.setInterviewTime(interview.getInterviewTime());
		dto.setInterviewer(interview.getInterviewer());
		dto.setMeetingLink(interview.getMeetingLink());
		dto.setRound(interview.getRound());
		dto.setNotes(interview.getNotes());

		return dto;
	}
}