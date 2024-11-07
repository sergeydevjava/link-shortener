package org.sergeydevjava.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergeydevjava.dto.common.CommonResponse;
import org.sergeydevjava.dto.common.ValidationError;
import org.sergeydevjava.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class LinkShortenerException {

    private final String notFoundPage;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundExceptionException(NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_HTML)
                .body(notFoundPage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        List<ValidationError> validationError = bindingResult.getFieldErrors().stream()
                .map(fe -> ValidationError.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .build())
                .toList();

        log.warn("Ошибка валидации {}", validationError, e);

        return CommonResponse.builder()
                .errorMessage("Непредвиденное исключение: " + e.getMessage())
                .validationErrors(validationError)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        log.error("Непредвиденное исключение: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .errorMessage("Непредвиденное исключение: " + e.getMessage())
                .build();
    }
}
