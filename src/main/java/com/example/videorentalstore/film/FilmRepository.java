package com.example.videorentalstore.film;

import org.springframework.data.repository.CrudRepository;

public interface FilmRepository extends CrudRepository<Film, Long> {

    Iterable<Film> findByNameContainingIgnoreCase(String name);
}
