package com.example.videorentalstore.film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f WHERE f.active = true")
    @Override
    List<Film> findAll();

    @Query("SELECT f FROM Film f WHERE f.id = ?1 AND f.active = true")
    @Override
    Optional<Film> findById(Long id);

    @Query("SELECT f FROM Film f WHERE UPPER(f.title) LIKE UPPER(concat('%', ?1,'%')) AND f.active = true")
    List<Film> findByTitle(String title);

    @Query("SELECT COUNT(f) FROM Film f WHERE f.active = true")
    @Override
    long count();
}
