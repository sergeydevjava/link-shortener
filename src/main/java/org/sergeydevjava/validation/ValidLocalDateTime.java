package org.sergeydevjava.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidLocalDateTimeValidator.class)
public @interface ValidLocalDateTime {

    String message() default "Дата не валидна";
    boolean shouldBeInFuture() default true;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
