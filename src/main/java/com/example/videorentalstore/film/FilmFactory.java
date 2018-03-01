package com.example.videorentalstore.film;

import static com.example.videorentalstore.film.Film.NEW_RELEASE;
import static com.example.videorentalstore.film.Film.OLD_RELEASE;
import static com.example.videorentalstore.film.Film.REGULAR_RELEASE;

public class FilmFactory {

    static Film create(CreateFilmCmd createFilmCmd) {
        switch (createFilmCmd.getType()) {
            case NEW_RELEASE:
                return new NewReleaseFilm(createFilmCmd.getName(), createFilmCmd.getQuantity());
            case REGULAR_RELEASE:
                return new NewReleaseFilm(createFilmCmd.getName(), createFilmCmd.getQuantity());
            case OLD_RELEASE:
                return new NewReleaseFilm(createFilmCmd.getName(), createFilmCmd.getQuantity());
            default:
                throw new RuntimeException("Film with type '" + createFilmCmd.getType() + "' is not supported");
        }
    }

}
