package com.example.videorentalstore.inventory;

import org.springframework.data.repository.CrudRepository;

public interface FilmRepository extends CrudRepository<Film, Long> {

    Iterable<Film> findByNameContainingIgnoreCase(String name);
}
