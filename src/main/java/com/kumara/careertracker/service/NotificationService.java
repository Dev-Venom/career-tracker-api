package com.kumara.careertracker.service;

import java.util.List;

import com.kumara.careertracker.dto.NotificationResponseDto;
import com.kumara.careertracker.entity.User;

public interface NotificationService {

	List<NotificationResponseDto> getMyNotifications(String email);

	long getUnreadCount(String email);

	NotificationResponseDto markAsRead(Long id, String email);

	void markAllAsRead(String email);

	void createNotification(User user, String title, String message, String type);

}