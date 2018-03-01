package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class UpdateFilmCmd {

    private Long id;
    private String name;
    private String type;
    private int quantity;

    public UpdateFilmCmd(Long id, String name, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }
}
