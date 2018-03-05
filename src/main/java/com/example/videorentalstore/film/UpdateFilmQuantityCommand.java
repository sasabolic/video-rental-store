package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class UpdateFilmQuantityCommand {

    private Long id;

    private int quantity;

    public UpdateFilmQuantityCommand(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
