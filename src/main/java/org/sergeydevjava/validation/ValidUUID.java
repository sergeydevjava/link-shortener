package org.sergeydevjava.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUUIDValidator.class)
public @interface ValidUUID {

    String message() default "Некоректный uuid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
