package com.example.videorentalstore.film;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateFilmQuantityCommand {

    private Long id;
    private int quantity;
}
