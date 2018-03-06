package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class UpdateFilmCommand {

    private Long id;
    private String title;
    private String type;
    private int quantity;

    public UpdateFilmCommand(Long id, String title, String type, int quantity) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.quantity = quantity;
    }
}
