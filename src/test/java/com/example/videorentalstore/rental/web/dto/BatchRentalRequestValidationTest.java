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

public class BatchRentalRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        BatchRentalRequest request = new BatchRentalRequest(BatchRentalRequest.Action.PAY.name(), Arrays.asList(new ReturnBackRentalRequest(1L)));
        final Set<ConstraintViolation<BatchRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidActionThenViolationForAction() {
        BatchRentalRequest request = new BatchRentalRequest(null, Arrays.asList(new ReturnBackRentalRequest(1L)));
        final Set<ConstraintViolation<BatchRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Invalid action: 'null'. Allowed values are: 'PAY, RETURN, EXTRA_PAY'");
    }

    @Test
    public void whenRequestEmptyRentalsThenViolationListNotEmpty() {
        BatchRentalRequest request = new BatchRentalRequest(BatchRentalRequest.Action.PAY.name(), Collections.emptyList());
        final Set<ConstraintViolation<BatchRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of rental requests cannot be empty");
    }

    @Test
    public void whenRequestNullRentalsThenViolationForCreateRentalRequest() {
        BatchRentalRequest request = new BatchRentalRequest(BatchRentalRequest.Action.PAY.name(), null);
        final Set<ConstraintViolation<BatchRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        BatchRentalRequest request = new BatchRentalRequest(null, null);
        final Set<ConstraintViolation<BatchRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
