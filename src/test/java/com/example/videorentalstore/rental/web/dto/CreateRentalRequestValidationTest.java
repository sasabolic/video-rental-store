package com.example.videorentalstore.rental.web.dto;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateRentalRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        CreateRentalRequest request = new CreateRentalRequest(1L, 10);
        final Set<ConstraintViolation<CreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidFilmIdThenViolationForFilmId() {
        CreateRentalRequest request = new CreateRentalRequest(null, 10);
        final Set<ConstraintViolation<CreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Film id cannot be null");
    }

    @Test
    public void whenRequestNullDaysRentedThenViolationForDaysRented() {
        CreateRentalRequest request = new CreateRentalRequest(1L, null);
        final Set<ConstraintViolation<CreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Days rented cannot be null");
    }

    @Test
    public void whenRequestZeroDaysRentedThenViolationForDaysRented() {
        CreateRentalRequest request = new CreateRentalRequest(1L, 0);
        final Set<ConstraintViolation<CreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Days rented must have minimum value of: 1");
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        CreateRentalRequest request = new CreateRentalRequest(null, 0);
        final Set<ConstraintViolation<CreateRentalRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
