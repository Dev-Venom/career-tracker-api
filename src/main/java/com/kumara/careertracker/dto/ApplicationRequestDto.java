package com.kumara.careertracker.dto;

import java.time.LocalDate;

import com.kumara.careertracker.enums.ApplicationStatus;
import com.kumara.careertracker.enums.JobType;

import jakarta.validation.constraints.NotBlank;

public class ApplicationRequestDto {

	@NotBlank
	private String companyName;

	@NotBlank
	private String jobTitle;

	private String location;

	private JobType jobType;

	private ApplicationStatus status;

	private Double salary;

	private LocalDate appliedDate;

	private String jobUrl;

	private String notes;

	private String jobPlatform;

	public String getJobPlatform() {
		return jobPlatform;
	}

	public void setJobPlatform(String jobPlatform) {
		this.jobPlatform = jobPlatform;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public LocalDate getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}