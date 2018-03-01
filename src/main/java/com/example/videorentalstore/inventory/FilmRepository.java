package com.example.videorentalstore.inventory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends CrudRepository<Film, Long> {

    Iterable<Film> findByNameContainingIgnoreCase(String name);
}
