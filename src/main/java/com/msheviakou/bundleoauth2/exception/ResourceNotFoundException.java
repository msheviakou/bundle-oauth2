package com.msheviakou.bundleoauth2.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ResourceNotFoundException extends HttpException {

    public ResourceNotFoundException(String message) { super(message, NOT_FOUND); }

    public ResourceNotFoundException(String message, HttpStatus status) { super(message, status); }
}
