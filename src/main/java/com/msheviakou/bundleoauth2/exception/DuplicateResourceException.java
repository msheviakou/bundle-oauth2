package com.msheviakou.bundleoauth2.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicateResourceException extends HttpException {

    public DuplicateResourceException(String message) { super(message, CONFLICT); }
}
