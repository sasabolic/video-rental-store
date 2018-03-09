package com.example.videorentalstore.core;

import com.example.videorentalstore.film.ReleaseType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Validates that the {@code java.lang.String} is a valid {@link ReleaseType} value.
 */
public class ValidActionValidator implements ConstraintValidator<ValidAction, String> {


    private ValidAction constraintAnnotation;

    @Override
    public void initialize(ValidAction constraintAnnotation) {
        // do nothing
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;

        final Enum<?>[] enumConstants = constraintAnnotation.enumClass().getEnumConstants();

        for (Enum<?> enumConstant : enumConstants) {
            if (enumConstant.name().equals(value)) {
                isValid = true;
                break;
            }
        }

        if (!isValid && constraintAnnotation.message().isEmpty()) {
            final String validList = Arrays.stream(enumConstants)
                    .map(e -> e.name()).collect(Collectors.joining(", "));

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("Invalid action: '%s'. Allowed values are: '%s'", value, validList)).addConstraintViolation();
        }

        return isValid;
    }
}
