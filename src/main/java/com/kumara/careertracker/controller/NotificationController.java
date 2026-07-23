package com.kumara.careertracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kumara.careertracker.dto.NotificationResponseDto;
import com.kumara.careertracker.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping
	public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(Authentication authentication) {

		return ResponseEntity.ok(notificationService.getMyNotifications(authentication.getName()));
	}

	@GetMapping("/unread-count")
	public ResponseEntity<Long> getUnreadCount(Authentication authentication) {

		return ResponseEntity.ok(notificationService.getUnreadCount(authentication.getName()));
	}

	@PutMapping("/{id}/read")
	public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long id, Authentication authentication) {

		return ResponseEntity.ok(notificationService.markAsRead(id, authentication.getName()));
	}

	@PutMapping("/read-all")
	public ResponseEntity<Void> markAllAsRead(Authentication authentication) {

		notificationService.markAllAsRead(authentication.getName());

		return ResponseEntity.ok().build();
	}

}