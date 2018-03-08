package com.example.videorentalstore.rental.web;

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

public class CreateRentalListRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        CreateRentalListRequest request = new CreateRentalListRequest(Arrays.asList(new CreateRentalRequest(1L, 10)));
        final Set<ConstraintViolation<CreateRentalListRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestNullThenViolationListNotEmpty() {
        CreateRentalListRequest request = new CreateRentalListRequest(null);
        final Set<ConstraintViolation<CreateRentalListRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of create rental requests cannot be null", "List of create rental requests cannot be empty");
    }

    @Test
    public void whenRequestEmptyThenViolationListNotEmpty() {
        CreateRentalListRequest request = new CreateRentalListRequest(Collections.emptyList());
        final Set<ConstraintViolation<CreateRentalListRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of create rental requests cannot be empty");
    }

    @Test
    public void whenRequestInvalidCreateRentalRequestThenViolationForCreateRentalRequest() {
        CreateRentalListRequest request = new CreateRentalListRequest(Arrays.asList(new CreateRentalRequest(null, 0)));
        final Set<ConstraintViolation<CreateRentalListRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
