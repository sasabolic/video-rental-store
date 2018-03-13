package com.example.videorentalstore.payment.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SavePaymentRequest {

    @NotNull(message = "Invoice id cannot be null")
    private Long invoiceId;

    @JsonCreator
    public SavePaymentRequest(@JsonProperty("invoice_id") Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}
