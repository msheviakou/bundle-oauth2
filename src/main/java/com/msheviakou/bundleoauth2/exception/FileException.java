package com.msheviakou.bundleoauth2.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class FileException extends HttpException {

    public FileException(String message) { super(message, INTERNAL_SERVER_ERROR); }
}
