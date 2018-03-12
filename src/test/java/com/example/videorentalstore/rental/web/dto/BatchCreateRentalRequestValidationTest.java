package com.example.videorentalstore.rental.web.dto;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchCreateRentalRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        BatchCreateRentalRequest request = new BatchCreateRentalRequest(Arrays.asList(new CreateRentalRequest(1L, 10)));
        final Set<ConstraintViolation<BatchCreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestNullThenViolationListNotEmpty() {
        BatchCreateRentalRequest request = new BatchCreateRentalRequest(null);
        final Set<ConstraintViolation<BatchCreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of pay rental requests cannot be null", "List of pay rental requests cannot be empty");
    }

    @Test
    public void whenRequestEmptyThenViolationListNotEmpty() {
        BatchCreateRentalRequest request = new BatchCreateRentalRequest(Collections.emptyList());
        final Set<ConstraintViolation<BatchCreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of pay rental requests cannot be empty");
    }

    @Test
    public void whenRequestInvalidCreateRentalRequestThenViolationForCreateRentalRequest() {
        BatchCreateRentalRequest request = new BatchCreateRentalRequest(Arrays.asList(new CreateRentalRequest(null, 0)));
        final Set<ConstraintViolation<BatchCreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
