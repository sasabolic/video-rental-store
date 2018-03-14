package com.example.videorentalstore.film;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link FilmService} delegating persistence operations to {@link FilmRepository}.
 */
@Service
@Transactional
public class DefaultFilmService implements FilmService {

    private final FilmRepository filmRepository;

    public DefaultFilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public List<Film> findAll(String title) {
        if (title == null) {
            return this.filmRepository.findAll();
        }
        return this.filmRepository.findByTitle(title);
    }

    @Override
    public Film findById(Long id) {
        return this.filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", id)));
    }

    @Override
    public Film save(String title, String type, int quantity) {
        this.filmRepository.findByTitle(title).stream()
                .findAny().ifPresent(f -> {
                    throw new FilmUniqueViolationException(String.format("Film with title '%s' already exits", title));
                });

        final Film film = new Film(title, ReleaseType.valueOf(type), quantity);

        return this.filmRepository.save(film);
    }

    @Override
    public Film update(Long id, String title, String type, int quantity) {
        this.filmRepository.findByTitle(title).stream()
                .filter(f -> !f.getId().equals(id))
                .findAny().ifPresent(f -> {
                    throw new FilmUniqueViolationException(String.format("Film with title '%s' already exits", title));
                });

        final Film film = this.filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", id)));

        film.update(title, type, quantity);

        return this.filmRepository.save(film);
    }

    @Override
    public Film delete(Long id) {
        final Film film = this.filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", id)));

        film.deactivate();

        return this.filmRepository.save(film);
    }

    @Override
    public Film updateQuantity(Long id, int quantity) {
        final Film film = this.filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", id)));

        film.changeQuantityBy(quantity);

        return this.filmRepository.save(film);
    }
}
