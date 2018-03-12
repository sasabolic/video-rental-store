package com.example.videorentalstore.payment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PaymentDataFixtures {

    public static Receipt receipt() {
        return receipt(BigDecimal.TEN);
    }

    public static Receipt receipt(BigDecimal amount) {
        return new Receipt(amount);
    }

    public static List<Receipt> receipts() {
        return Arrays.asList(
                receipt(BigDecimal.valueOf(250)),
                receipt(BigDecimal.valueOf(110)),
                receipt(BigDecimal.valueOf(30)),
                receipt(BigDecimal.valueOf(90))
        );
    }

    public static String json() {
        return "{\n" +
                "  \"amount\": \"250.00\",\n" +
                "  \"type\": \"UP_FRONT\"\n" +
                "}";
    }
}
