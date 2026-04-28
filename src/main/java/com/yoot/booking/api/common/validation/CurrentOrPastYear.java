package com.yoot.booking.api.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CurrentOrPastYearValidator.class)
public @interface CurrentOrPastYear {

    String message() default "birthYear must not be greater than the current year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
