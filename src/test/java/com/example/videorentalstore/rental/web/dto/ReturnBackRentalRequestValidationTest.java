package com.example.videorentalstore.rental.web.dto;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReturnBackRentalRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        ReturnBackRentalRequest request = new ReturnBackRentalRequest(1L);
        final Set<ConstraintViolation<ReturnBackRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidThenViolationListNotEmpty() {
        ReturnBackRentalRequest request = new ReturnBackRentalRequest(null);
        final Set<ConstraintViolation<ReturnBackRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
    }
}
