package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class UpdateFilmQuantityCmd {

    private Long id;

    private int quantity;

    public UpdateFilmQuantityCmd(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
