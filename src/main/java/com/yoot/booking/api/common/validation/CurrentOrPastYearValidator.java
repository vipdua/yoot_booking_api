package com.yoot.booking.api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Passes if the value is {@code null} (let {@code @NotNull} handle that) or if the value does not
 * exceed the current year.
 */
public class CurrentOrPastYearValidator implements ConstraintValidator<CurrentOrPastYear, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null is handled by @NotNull
        }
        return value <= LocalDate.now().getYear();
    }
}
