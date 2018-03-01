package com.example.videorentalstore.film;

public interface FilmService {

    Iterable<Film> findAll();

    Iterable<Film> findAllByName(String name);

    Film findById(Long id);

    Film save(CreateFilmCmd createFilmCmd);

    void update(Film film);

    void deleteById(Long id);

    void updateQuantity(UpdateFilmQuantityCmd updateFilmQuantityCmd);

}
