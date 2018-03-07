package com.example.videorentalstore.film;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateFilmCommand {

    private String title;
    private String type;
    private int quantity;
}
