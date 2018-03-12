package com.example.videorentalstore.payment.web.dto;

import com.example.videorentalstore.core.IsEnum;
import com.example.videorentalstore.invoice.Invoice;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SavePaymentRequest {

    @NotNull(message = "Amount must not be empty")
    private String amount;

    @NotNull(message = "Type must not be null")
    @IsEnum(enumClass = Invoice.Type.class)
    private String type;

    @JsonCreator
    public SavePaymentRequest(@JsonProperty("amount") String amount,
                           @JsonProperty("type") String type) {
        this.amount = amount;
        this.type = type;
    }
}
