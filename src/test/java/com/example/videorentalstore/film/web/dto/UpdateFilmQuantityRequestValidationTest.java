package com.example.videorentalstore.film.web.dto;

import com.example.videorentalstore.film.web.dto.UpdateFilmQuantityRequest;
import com.example.videorentalstore.film.web.dto.WriteFilmRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateFilmQuantityRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        UpdateFilmQuantityRequest request = new UpdateFilmQuantityRequest(11);
        final Set<ConstraintViolation<UpdateFilmQuantityRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidThenViolationListNotEmpty() {
        UpdateFilmQuantityRequest request = new UpdateFilmQuantityRequest(null);
        final Set<ConstraintViolation<UpdateFilmQuantityRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Quantity cannot be null");
    }
}
