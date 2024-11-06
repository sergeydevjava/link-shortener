package org.sergeydevjava.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ValidLocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private boolean shouldBeInFuture;
    private String customMessage;

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
        this.shouldBeInFuture = constraintAnnotation.shouldBeInFuture();
        this.customMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return !shouldBeInFuture;
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
            if (shouldBeInFuture) {
                if (!dateTime.isAfter(LocalDateTime.now())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(customMessage).addConstraintViolation();
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
