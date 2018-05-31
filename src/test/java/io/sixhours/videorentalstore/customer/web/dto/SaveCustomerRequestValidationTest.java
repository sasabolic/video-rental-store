package io.sixhours.videorentalstore.customer.web.dto;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SaveCustomerRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        SaveCustomerRequest request = new SaveCustomerRequest("First", "Last");
        final Set<ConstraintViolation<SaveCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidFirstNameThenViolationForFirstName() {
        SaveCustomerRequest request = new SaveCustomerRequest("", "Last");
        final Set<ConstraintViolation<SaveCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("First name cannot be empty");
    }

    @Test
    public void whenRequestInvalidLastNameThenViolationForLastName() {
        SaveCustomerRequest request = new SaveCustomerRequest("First", "");
        final Set<ConstraintViolation<SaveCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Last name cannot be empty");
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        SaveCustomerRequest request = new SaveCustomerRequest("", "");
        final Set<ConstraintViolation<SaveCustomerRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
