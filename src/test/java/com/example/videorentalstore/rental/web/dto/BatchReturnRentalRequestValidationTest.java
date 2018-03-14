package com.example.videorentalstore.rental.web.dto;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchReturnRentalRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        BatchReturnRentalRequest request = new BatchReturnRentalRequest(Collections.singletonList(new ReturnRentalRequest(1L)));
        final Set<ConstraintViolation<BatchReturnRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestEmptyRentalsThenViolationListNotEmpty() {
        BatchReturnRentalRequest request = new BatchReturnRentalRequest(Collections.emptyList());
        final Set<ConstraintViolation<BatchReturnRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of return rental requests cannot be empty");
    }

    @Test
    public void whenRequestNullRentalsThenViolationForCreateRentalRequest() {
        BatchReturnRentalRequest request = new BatchReturnRentalRequest(null);
        final Set<ConstraintViolation<BatchReturnRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
    }
}
