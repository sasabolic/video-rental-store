package com.example.videorentalstore.core;

import com.example.videorentalstore.film.ReleaseType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Validates that the {@code java.lang.String} is a valid {@link ReleaseType} value.
 */
public class ValidReleaseTypeValidator implements ConstraintValidator<ValidReleaseType, String> {


    private ValidReleaseType constraintAnnotation;

    @Override
    public void initialize(ValidReleaseType constraintAnnotation) {
        // do nothing
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;

        for (ReleaseType enumConstant : ReleaseType.class.getEnumConstants()) {
            if (enumConstant.name().equals(value)) {
                isValid = true;
                break;
            }
        }

        if (!isValid && constraintAnnotation.message().isEmpty()) {
            final String validList = Arrays.stream(ReleaseType.class.getEnumConstants())
                    .map(e -> e.name()).collect(Collectors.joining(", "));

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("Invalid release type: '%s'. Allowed values are: '%s'", value, validList)).addConstraintViolation();
        }

        return isValid;
    }
}
