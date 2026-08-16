package com.kumara.careertracker.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate interviewDate;

	private LocalTime interviewTime;

	private String interviewer;

	private String meetingLink;

	private String round;

	@Column(length = 1000)
	private String notes;

	@OneToOne
	@JoinColumn(name = "application_id", unique = true)
	private Application application;

	public Interview() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(LocalDate interviewDate) {
		this.interviewDate = interviewDate;
	}

	public LocalTime getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(LocalTime interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getInterviewer() {
		return interviewer;
	}

	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}

	public String getMeetingLink() {
		return meetingLink;
	}

	public void setMeetingLink(String meetingLink) {
		this.meetingLink = meetingLink;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}