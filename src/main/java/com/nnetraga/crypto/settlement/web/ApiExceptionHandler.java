package com.nnetraga.crypto.settlement.web;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nnetraga.crypto.settlement.application.IdempotencyConflictException;
import com.nnetraga.crypto.settlement.application.IdempotencyKeyAlreadyExistsException;
import com.nnetraga.crypto.settlement.application.IdempotencyRequestInProgressException;
import com.nnetraga.crypto.settlement.web.dto.ApiError;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({
            IdempotencyConflictException.class,
            IdempotencyKeyAlreadyExistsException.class,
            IdempotencyRequestInProgressException.class,
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(RuntimeException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(IllegalArgumentException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ApiError(message);
    }
}
