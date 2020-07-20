package com.msheviakou.bundleoauth2.exception.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Getter
public class ApiErrorDetails extends ApiError {

    private final List<String> details;

    @Builder(builderMethodName = "apiErrorDetailsBuilder")
    public ApiErrorDetails(Integer status, String error, String message, LocalDateTime timestamp, List<String> details) {
        super(status, error, message, now());
        this.details = details;
    }
}
