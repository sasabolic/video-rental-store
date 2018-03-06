package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class CreateFilmCommand {

    private String name;
    private String type;
    private int quantity;

    public CreateFilmCommand(String name, String type, int quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

}
