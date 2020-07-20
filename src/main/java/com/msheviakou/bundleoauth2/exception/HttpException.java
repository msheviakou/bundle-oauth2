package com.msheviakou.bundleoauth2.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public abstract class HttpException extends RuntimeException {
	
	private HttpStatus status = BAD_REQUEST;

	HttpException(String message) { super(message); }

	HttpException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getHttpStatus() {
		return this.status;
	}
	
	public String getMessage() { return super.getMessage(); }
}
