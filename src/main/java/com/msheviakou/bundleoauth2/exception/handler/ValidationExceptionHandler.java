package com.msheviakou.bundleoauth2.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.msheviakou.bundleoauth2.exception.model.ApiError;
import com.msheviakou.bundleoauth2.exception.model.ApiErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler implements GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getClass().getName() + ":" + exception.getMessage());

        List<String> details = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorDetails apiErrorDetails = ApiErrorDetails.apiErrorDetailsBuilder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.name())
                .message(exception.getMessage())
                .details(details)
                .build();

        return buildResponseEntity(apiErrorDetails);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity handleJsonMappingException(JsonMappingException exception) {
        log.error(exception.getClass().getName() + ":" + exception.getMessage());

        ApiError apiError = ApiError.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();

        return buildResponseEntity(apiError);
    }
}
