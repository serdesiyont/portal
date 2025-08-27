package org.ahavah.portal.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class LangSubsetValidator implements ConstraintValidator<LangSubset, String> {

    private static final Set<String> ALLOWED = Set.of(
            "JAVASCRIPT", "TYPESCRIPT", "PYTHON", "PHP", "DART"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && ALLOWED.contains(value.toUpperCase());
    }
}
