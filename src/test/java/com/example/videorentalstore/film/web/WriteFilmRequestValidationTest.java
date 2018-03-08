package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.web.dto.WriteFilmRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WriteFilmRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        WriteFilmRequest request = new WriteFilmRequest("Title", "NEW_RELEASE", 11);
        final Set<ConstraintViolation<WriteFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidTypeThenViolationForType() {
        WriteFilmRequest request = new WriteFilmRequest("Title", "INVALID_TYPE", 11);
        final Set<ConstraintViolation<WriteFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Invalid release type: 'INVALID_TYPE'. Allowed values are: 'NEW_RELEASE, REGULAR_RELEASE, OLD_RELEASE'");
    }

    @Test
    public void whenRequestInvalidTitleThenViolationForTitle() {
        WriteFilmRequest request = new WriteFilmRequest(null, "NEW_RELEASE", 11);
        final Set<ConstraintViolation<WriteFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Title must not be empty");
    }

    @Test
    public void whenRequestInvalidQuantityThenViolationForQuantity() {
        WriteFilmRequest request = new WriteFilmRequest("Title", "NEW_RELEASE", null);

        final Set<ConstraintViolation<WriteFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Quantity cannot be null");
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        WriteFilmRequest request = new WriteFilmRequest("", "INVALID_TYPE", null);

        final Set<ConstraintViolation<WriteFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(3);
    }
}
