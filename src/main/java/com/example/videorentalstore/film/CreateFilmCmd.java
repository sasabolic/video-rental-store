package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class CreateFilmCmd {

    private String name;
    private String type;
    private int quantity;

    public CreateFilmCmd(String name, String type, int quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }
}
