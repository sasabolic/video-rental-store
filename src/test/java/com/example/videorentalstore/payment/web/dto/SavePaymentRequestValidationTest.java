package com.example.videorentalstore.payment.web.dto;

import com.example.videorentalstore.invoice.Invoice;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SavePaymentRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRequestValidThenViolationListEmpty() {
        SavePaymentRequest request = new SavePaymentRequest("100", Invoice.Type.LATE_CHARGE.name());
        final Set<ConstraintViolation<SavePaymentRequest>> validate = validator.validate(request);

        assertThat(validate).isEmpty();
    }

    @Test
    public void whenRequestInvalidAmountThenViolationForAmount() {
        SavePaymentRequest request = new SavePaymentRequest(null, Invoice.Type.LATE_CHARGE.name());
        final Set<ConstraintViolation<SavePaymentRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Amount must not be empty");
    }

    @Test
    public void whenRequestInvalidTypeThenViolationForType() {
        SavePaymentRequest request = new SavePaymentRequest("100", "non-existing");
        final Set<ConstraintViolation<SavePaymentRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Invalid value: 'non-existing'. Allowed values are: 'UP_FRONT, LATE_CHARGE'");
    }

    @Test
    public void whenRequestNullTypeThenViolationForType() {
        SavePaymentRequest request = new SavePaymentRequest("100", null);
        final Set<ConstraintViolation<SavePaymentRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(1);
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsOnly("Type must not be null");
    }

    @Test
    public void whenRequestInvalidAllFieldsThenViolationListNotEmpty() {
        SavePaymentRequest request = new SavePaymentRequest(null, null);
        final Set<ConstraintViolation<SavePaymentRequest>> validate = validator.validate(request);

        assertThat(validate).isNotEmpty();
        assertThat(validate).hasSize(2);
    }
}
