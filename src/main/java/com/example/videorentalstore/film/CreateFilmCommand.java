package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class CreateFilmCommand {

    private String title;
    private String type;
    private int quantity;

    public CreateFilmCommand(String title, String type, int quantity) {
        this.title = title;
        this.type = type;
        this.quantity = quantity;
    }
}
