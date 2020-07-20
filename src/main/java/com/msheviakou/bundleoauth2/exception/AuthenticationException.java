package com.msheviakou.bundleoauth2.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationException extends HttpException {

    public AuthenticationException(String message) { super(message, UNAUTHORIZED); }
}
