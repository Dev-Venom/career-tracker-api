package com.kumara.careertracker.exception;

public class UnauthorizedResourceException extends RuntimeException {

	public UnauthorizedResourceException(String message) {
		super(message);
	}
}