package com.example.videorentalstore.rental.web.dto;

import com.example.videorentalstore.rental.web.dto.ReturnBackRentalRequest;
import com.example.videorentalstore.rental.web.dto.ReturnBackRentalRequestList;
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

public class ReturnBackRentalRequestListValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        ReturnBackRentalRequestList request = new ReturnBackRentalRequestList(Arrays.asList(new ReturnBackRentalRequest(1L)));
        final Set<ConstraintViolation<ReturnBackRentalRequestList>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestNullThenViolationListNotEmpty() {
        ReturnBackRentalRequestList request = new ReturnBackRentalRequestList(null);
        final Set<ConstraintViolation<ReturnBackRentalRequestList>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of return rental requests cannot be null", "List of return rental requests cannot be empty");
    }

    @Test
    public void whenRequestEmptyThenViolationListNotEmpty() {
        ReturnBackRentalRequestList request = new ReturnBackRentalRequestList(Collections.emptyList());
        final Set<ConstraintViolation<ReturnBackRentalRequestList>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("List of return rental requests cannot be empty");
    }
    @Test
    public void whenRequestInvalidCreateRentalRequestThenViolationForCreateRentalRequest() {
        ReturnBackRentalRequestList request = new ReturnBackRentalRequestList(Arrays.asList(new ReturnBackRentalRequest(null)));
        final Set<ConstraintViolation<ReturnBackRentalRequestList>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
    }
}
