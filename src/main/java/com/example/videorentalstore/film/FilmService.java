package com.example.videorentalstore.film;

import java.util.Optional;

public interface FilmService {

    Iterable<Film> findAll();

    Iterable<Film> findAllByName(String name);

    Optional<Film> findById(Long id);

    Film save(CreateFilmCommand createFilmCommand);

    Film update(UpdateFilmCommand updateFilmCommand);

    Film delete(Long id);

    Film updateQuantity(UpdateFilmQuantityCommand updateFilmQuantityCommand);

}
