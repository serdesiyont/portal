package org.ahavah.portal.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleSubsetValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleSubset {
    String message() default "must be one of: SOCIAL, ADMIN, MENTOR, STUDENT";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}