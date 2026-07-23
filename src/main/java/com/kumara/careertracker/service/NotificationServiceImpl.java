package com.kumara.careertracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kumara.careertracker.dto.NotificationResponseDto;
import com.kumara.careertracker.entity.Notification;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.exception.ResourceNotFoundException;
import com.kumara.careertracker.repository.NotificationRepository;
import com.kumara.careertracker.repository.UserRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {

		this.notificationRepository = notificationRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<NotificationResponseDto> getMyNotifications(String email) {

		return notificationRepository.findByUserEmailOrderByCreatedAtDesc(email).stream().map(this::convertToDto)
				.toList();
	}

	@Override
	public long getUnreadCount(String email) {

		return notificationRepository.countByUserEmailAndIsReadFalse(email);
	}

	@Override
	public NotificationResponseDto markAsRead(Long id, String email) {

		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

		if (!notification.getUser().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized");
		}

		notification.setRead(true);

		notificationRepository.save(notification);

		return convertToDto(notification);
	}

	@Override
	public void markAllAsRead(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(user.getId());

		notifications.forEach(notification -> notification.setRead(true));

		notificationRepository.saveAll(notifications);
	}

	@Override
	public void createNotification(User user, String title, String message, String type) {

		Notification notification = new Notification();

		notification.setUser(user);
		notification.setTitle(title);
		notification.setMessage(message);
		notification.setType(type);
		notification.setRead(false);
		notification.setCreatedAt(LocalDateTime.now());

		notificationRepository.save(notification);
	}

	private NotificationResponseDto convertToDto(Notification notification) {

		NotificationResponseDto dto = new NotificationResponseDto();

		dto.setId(notification.getId());
		dto.setTitle(notification.getTitle());
		dto.setMessage(notification.getMessage());
		dto.setType(notification.getType());
		dto.setRead(notification.isRead());
		dto.setCreatedAt(notification.getCreatedAt());

		return dto;
	}
}