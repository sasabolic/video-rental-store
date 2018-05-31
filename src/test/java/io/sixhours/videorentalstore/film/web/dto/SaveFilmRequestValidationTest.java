package io.sixhours.videorentalstore.film.web.dto;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SaveFilmRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        SaveFilmRequest request = new SaveFilmRequest("Title", "NEW_RELEASE", 11);
        final Set<ConstraintViolation<SaveFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidTypeThenViolationForType() {
        SaveFilmRequest request = new SaveFilmRequest("Title", "INVALID_TYPE", 11);
        final Set<ConstraintViolation<SaveFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Invalid value: 'INVALID_TYPE'. Allowed values are: 'NEW_RELEASE, REGULAR_RELEASE, OLD_RELEASE'");
    }

    @Test
    public void whenRequestInvalidTitleThenViolationForTitle() {
        SaveFilmRequest request = new SaveFilmRequest(null, "NEW_RELEASE", 11);
        final Set<ConstraintViolation<SaveFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Title must not be empty");
    }

    @Test
    public void whenRequestInvalidQuantityThenViolationForQuantity() {
        SaveFilmRequest request = new SaveFilmRequest("Title", "NEW_RELEASE", null);

        final Set<ConstraintViolation<SaveFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Quantity cannot be null");
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        SaveFilmRequest request = new SaveFilmRequest("", "INVALID_TYPE", null);

        final Set<ConstraintViolation<SaveFilmRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(3);
    }
}
