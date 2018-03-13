package com.example.videorentalstore.invoice.web.dto;

import com.example.videorentalstore.core.IsEnum;
import com.example.videorentalstore.invoice.InvoiceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SaveInvoiceRequest {

    @NotNull(message = "Type must not be null")
    @IsEnum(enumClass = InvoiceType.class)
    private String type;

    @JsonCreator
    public SaveInvoiceRequest(@JsonProperty("type") String type) {
        this.type = type;
    }
}
