package com.kumara.careertracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kumara.careertracker.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUserEmailOrderByCreatedAtDesc(String email);

	long countByUserEmailAndIsReadFalse(String email);

	List<Notification> findByUserIdAndIsReadFalse(Long userId);

}