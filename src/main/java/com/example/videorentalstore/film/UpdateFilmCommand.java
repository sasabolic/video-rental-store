package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class UpdateFilmCommand {

    private Long id;
    private String name;
    private String type;
    private int quantity;

    public UpdateFilmCommand(Long id, String name, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }
}
