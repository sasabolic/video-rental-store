package com.example.videorentalstore.rental;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RentalException extends RuntimeException {

    private List<Exception> exceptions = new ArrayList<>();

    public RentalException(String message, List<Exception> exceptions) {
        super(message);
        this.exceptions.addAll(exceptions);
    }

    public boolean isEmpty() {
        return this.exceptions.isEmpty();
    }
}
