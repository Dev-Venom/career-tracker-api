package com.kumara.careertracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kumara.careertracker.entity.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

	List<Interview> findByInterviewDate(LocalDate interviewDate);

	List<Interview> findByInterviewDateGreaterThanEqual(LocalDate interviewDate);

	List<Interview> findByApplication_User_Id(Long userId);

	List<Interview> findByApplication_User_EmailOrderByInterviewDateAscInterviewTimeAsc(String email);
}