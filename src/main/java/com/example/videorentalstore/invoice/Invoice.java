package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public class Invoice {

    private BigDecimal amount;
    private Customer customer;
    private Type type;

    public enum Type {

        UP_FRONT("up-front"),

        LATE_CHARGE("late-charge");

        private final String pathVariable;

        Type(String pathVariable) {
            this.pathVariable = pathVariable;
        }

        public static Type fromPathVariable(String value) {
            return Arrays.stream(values())
                    .filter(e -> e.pathVariable.equals(value))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Invoice type of for path variable '%s' does not exist.", value)));
        }

        public String pathVariable() {
            return this.pathVariable;
        }
    }
}
