package org.ahavah.portal.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LangSubsetValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LangSubset {
    String message() default "must be one of: JS, TS, PYTHON, PHP, DART";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
