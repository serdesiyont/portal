package org.ahavah.portal.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DivisionSubsetValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DivisionSubset {
    String message() default "must be one of: ALL, DJANGO, REACT, LARAVEL, DSA, BEGINNER, FLUTTER";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}