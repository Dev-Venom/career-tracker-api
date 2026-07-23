package com.kumara.careertracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kumara.careertracker.entity.Application;
import com.kumara.careertracker.enums.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	List<Application> findByUser_Id(Long userId);

	List<Application> findByStatus(ApplicationStatus status);

}