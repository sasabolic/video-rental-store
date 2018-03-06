package com.example.videorentalstore.film;

import java.util.List;

public interface FilmService {

    List<Film> findAll();

    List<Film> findAllByTitle(String title);

    Film findById(Long id);

    Film save(CreateFilmCommand createFilmCommand);

    Film update(UpdateFilmCommand updateFilmCommand);

    Film delete(Long id);

    Film updateQuantity(UpdateFilmQuantityCommand updateFilmQuantityCommand);

}
