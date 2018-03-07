package com.example.videorentalstore.film;

import lombok.Getter;

@Getter
public class UpdateFilmCommand extends CreateFilmCommand {

    private Long id;

    public UpdateFilmCommand(Long id, String title, String type, int quantity) {
        super(title, type, quantity);
        this.id = id;
    }
}
