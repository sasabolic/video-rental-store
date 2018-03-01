package com.example.videorentalstore.film;

public interface FilmService {

    Iterable<Film> findAll();

    Iterable<Film> findAllByName(String name);

    Film findById(Long id);

    Film save(CreateFilmCmd createFilmCmd);

    Film update(UpdateFilmCmd updateFilmCmd);

    void delete(Long id);

    void updateQuantity(UpdateFilmQuantityCmd updateFilmQuantityCmd);

}
