package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = PastDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PastDate {
    String value();

    String message() default "Release date must be equal to or after {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
