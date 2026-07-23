package com.kumara.careertracker.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kumara.careertracker.config.JwtUtil;
import com.kumara.careertracker.dto.AuthResponse;
import com.kumara.careertracker.dto.RefreshRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JwtUtil jwtUtil = new JwtUtil();

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestParam String username) {

		User user = new User(username, "", new ArrayList<>());

		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);

		return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

		String username = jwtUtil.extractUsername(request.getRefreshToken());

		if (!jwtUtil.isTokenValid(request.getRefreshToken(), username)) {
			return ResponseEntity.status(401).body("Invalid refresh token");
		}

		String newAccessToken = jwtUtil.generateAccessToken(new User(username, "", new ArrayList<>()));

		return ResponseEntity.ok(new AuthResponse(newAccessToken, request.getRefreshToken()));
	}
}