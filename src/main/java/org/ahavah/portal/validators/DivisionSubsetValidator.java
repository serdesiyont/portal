package org.ahavah.portal.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class DivisionSubsetValidator implements ConstraintValidator<DivisionSubset, String> {
    private static final Set<String> ALLOWED = Set.of(
        "ALL", "DJANGO", "REACT", "LARAVEL", "DSA", "BEGINNER", "FLUTTER"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && ALLOWED.contains(value.toUpperCase());
    }
}
