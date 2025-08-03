package org.ahavah.portal.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class RoleSubsetValidator implements ConstraintValidator<RoleSubset, String> {
    private static final Set<String> ALLOWED = Set.of(
            "SOCIAL", "ADMIN", "MENTOR", "STUDENT"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && ALLOWED.contains(value.toUpperCase());
    }
}