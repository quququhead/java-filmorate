package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastDateValidator implements ConstraintValidator<PastDate, LocalDate> {
    private LocalDate pastDate;

    @Override
    public void initialize(PastDate constraintAnnotation) {
        pastDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        return localDate != null && (localDate.isAfter(pastDate) || localDate.isEqual(pastDate));
    }
}
