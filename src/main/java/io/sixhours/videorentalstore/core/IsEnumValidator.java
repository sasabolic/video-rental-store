package io.sixhours.videorentalstore.core;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Validates that the {@code java.lang.String} is a valid {@link Enum} value.
 *
 * @author Sasa Bolic
 */
public class IsEnumValidator implements ConstraintValidator<IsEnum, String> {


    private IsEnum constraintAnnotation;

    @Override
    public void initialize(IsEnum constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isValid = false;

        final Enum<?>[] enumConstants = constraintAnnotation.enumClass().getEnumConstants();

        for (Enum<?> enumConstant : enumConstants) {
            if (enumConstant.name().equals(value)) {
                isValid = true;
                break;
            }
        }

        if (!isValid && constraintAnnotation.message().isEmpty()) {
            // add generic message
            final String validList = Arrays.stream(enumConstants)
                    .map(Enum::name).collect(Collectors.joining(", "));

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("Invalid value: '%s'. Allowed values are: '%s'", value, validList)).addConstraintViolation();
        }

        return isValid;
    }
}
