package com.kumara.careertracker.dto;

public class LoginResponseDto {

	private String token;
	private String message;
	private UserResponseDto user;

	public LoginResponseDto(String token, String message, UserResponseDto user) {
		this.token = token;
		this.message = message;
		this.user = user;

	}

	public String getToken() {
		return token;
	}

	public String getMessage() {
		return message;
	}

	public UserResponseDto getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "LoginResponseDto{" + "token='" + token + '\'' + ", message='" + message + '\'' + ", user=" + user + '}';
	}
}