package com.example.videorentalstore.customer.web;

import com.example.videorentalstore.customer.web.dto.WriteCustomerRequest;
import com.example.videorentalstore.film.web.dto.WriteFilmRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WriteCustomerRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        WriteCustomerRequest request = new WriteCustomerRequest("First", "Last");
        final Set<ConstraintViolation<WriteCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidFirstNameThenViolationForFirstName() {
        WriteCustomerRequest request = new WriteCustomerRequest("", "Last");
        final Set<ConstraintViolation<WriteCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("First name cannot be empty");
    }

    @Test
    public void whenRequestInvalidLastNameThenViolationForLastName() {
        WriteCustomerRequest request = new WriteCustomerRequest("First", "");
        final Set<ConstraintViolation<WriteCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Last name cannot be empty");
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        WriteCustomerRequest request = new WriteCustomerRequest("", "");
        final Set<ConstraintViolation<WriteCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
