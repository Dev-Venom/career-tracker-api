package com.kumara.careertracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kumara.careertracker.dto.ApplicationRequestDto;
import com.kumara.careertracker.dto.ApplicationResponseDto;
import com.kumara.careertracker.dto.DashboardResponseDto;
import com.kumara.careertracker.entity.Application;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.enums.ApplicationStatus;
import com.kumara.careertracker.exception.ResourceNotFoundException;
import com.kumara.careertracker.repository.ApplicationRepository;
import com.kumara.careertracker.repository.InterviewRepository;
import com.kumara.careertracker.repository.UserRepository;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	private final NotificationService notificationService;

	@Autowired
	private InterviewRepository interviewRepository;

	@Override
	public ApplicationResponseDto createApplication(ApplicationRequestDto dto, String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Application app = new Application();

		app.setCompanyName(dto.getCompanyName());
		app.setJobTitle(dto.getJobTitle());
		app.setLocation(dto.getLocation());
		app.setJobType(dto.getJobType());
		app.setStatus(dto.getStatus());
		app.setSalary(dto.getSalary());
		app.setAppliedDate(dto.getAppliedDate());
		app.setJobPlatform(dto.getJobPlatform());
		app.setJobUrl(dto.getJobUrl());
		app.setNotes(dto.getNotes());
		app.setUser(user);

		applicationRepository.save(app);

		notificationService.createNotification(user, "Application Added",
				"Your application for " + app.getCompanyName() + " has been added.", "APPLICATION");

		return mapToDto(app);
	}

	@Override
	public List<ApplicationResponseDto> getApplicationsByUser(Long userId) {

		List<Application> apps = applicationRepository.findByUser_Id(userId);

		return apps.stream().map(this::mapToDto).toList();
	}

	@Override
	public void deleteApplication(Long id, String email) {

		Application app = applicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found"));

		if (!app.getUser().getEmail().equals(email)) {

			throw new RuntimeException("You cannot delete this application");
		}

		interviewRepository.findByApplication(app).ifPresent(interviewRepository::delete);

		applicationRepository.delete(app);
	}

	@Override
	public ApplicationResponseDto getApplicationById(Long id) {

		Application app = applicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found with id : " + id));

		return mapToDto(app);

	}

	@Override
	public List<ApplicationResponseDto> getApplicationsByStatus(ApplicationStatus status) {

		List<Application> apps = applicationRepository.findByStatus(status);

		return apps.stream().map(this::mapToDto).toList();
	}

	@Override
	public DashboardResponseDto getDashboardStats(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<Application> applications = applicationRepository.findByUser_Id(user.getId());

		DashboardResponseDto dto = new DashboardResponseDto();

		dto.setTotalApplications(applications.size());

		dto.setApplied(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.APPLIED).count());

		dto.setInterview(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.INTERVIEW).count());

		dto.setRejected(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.REJECTED).count());

		dto.setOffer(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.OFFER).count());

		return dto;
	}

	@Override
	public List<ApplicationResponseDto> getMyApplications(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<Application> apps = applicationRepository.findByUser_Id(user.getId());

		return apps.stream().map(this::mapToDto).toList();
	}

	@Override
	public ApplicationResponseDto updateApplication(Long id, ApplicationRequestDto dto, String email) {

		Application app = applicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found"));

		if (!app.getUser().getEmail().equals(email)) {

			throw new RuntimeException("You cannot update this application");
		}

		ApplicationStatus oldStatus = app.getStatus();

		app.setCompanyName(dto.getCompanyName());
		app.setJobTitle(dto.getJobTitle());
		app.setLocation(dto.getLocation());
		app.setJobType(dto.getJobType());
		app.setStatus(dto.getStatus());
		app.setSalary(dto.getSalary());
		app.setAppliedDate(dto.getAppliedDate());
		app.setJobUrl(dto.getJobUrl());
		app.setNotes(dto.getNotes());
		app.setJobPlatform(dto.getJobPlatform());

		applicationRepository.save(app);

		if (oldStatus != dto.getStatus()) {

			String title = "Application Updated";
			String message = "";

			switch (dto.getStatus()) {

			case INTERVIEW:
				title = "Interview Stage";
				message = "Great news! Your application at " + app.getCompanyName()
						+ " has moved to the Interview stage.";
				break;

			case OFFER:
				title = "Offer Received";
				message = "Congratulations! You received an offer from " + app.getCompanyName() + ".";
				break;

			case REJECTED:
				title = "Application Rejected";
				message = "Your application at " + app.getCompanyName() + " has been marked as Rejected.";
				break;

			case ACCEPTED:
				title = "Application Accepted";
				message = "Fantastic! Your application at " + app.getCompanyName() + " has been accepted.";
				break;

			default:
				message = "Application status updated to " + dto.getStatus();
			}

			notificationService.createNotification(app.getUser(), title, message, "STATUS");
		}

		return mapToDto(app);
	}

	@Override
	public ApplicationResponseDto updateStatus(Long id, String status, String email) {

		Application app = applicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found"));

		if (!app.getUser().getEmail().equals(email)) {

			throw new RuntimeException("You cannot update this application");
		}

		app.setStatus(ApplicationStatus.valueOf(status));

		Application updated = applicationRepository.save(app);

		return mapToDto(updated);
	}

	private ApplicationResponseDto mapToDto(Application app) {

		ApplicationResponseDto dto = new ApplicationResponseDto();

		dto.setId(app.getId());
		dto.setCompanyName(app.getCompanyName());
		dto.setJobTitle(app.getJobTitle());
		dto.setLocation(app.getLocation());

		dto.setJobType(app.getJobType() != null ? app.getJobType().name() : null);

		dto.setStatus(app.getStatus() != null ? app.getStatus().name() : null);

		dto.setSalary(app.getSalary());
		dto.setAppliedDate(app.getAppliedDate());
		dto.setJobPlatform(app.getJobPlatform());
		dto.setJobUrl(app.getJobUrl());
		dto.setNotes(app.getNotes());

		return dto;

	}

	public ApplicationServiceImpl(ApplicationRepository applicationRepository, UserRepository userRepository,
			NotificationService notificationService) {

		this.applicationRepository = applicationRepository;
		this.userRepository = userRepository;
		this.notificationService = notificationService;
	}

}
