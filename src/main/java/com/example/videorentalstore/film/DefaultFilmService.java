package com.example.videorentalstore.film;

import com.example.videorentalstore.pricing.ReleaseType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultFilmService implements FilmService {

    private final FilmRepository filmRepository;

    public DefaultFilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Iterable<Film> findAll() {
        return this.filmRepository.findAll();
    }

    @Override
    public Iterable<Film> findAllByName(String name) {
        return this.filmRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return this.filmRepository.findById(id);
    }

    @Override
    public Film save(CreateFilmCmd createFilmCmd) {
        final Film film = new Film(createFilmCmd.getName(), ReleaseType.valueOf(createFilmCmd.getType()), createFilmCmd.getQuantity());

        return this.filmRepository.save(film);
    }

    @Override
    public Film update(UpdateFilmCmd updateFilmCmd) {
        final Film film = this.filmRepository.findById(updateFilmCmd.getId())
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", updateFilmCmd.getId())));

        film.process(updateFilmCmd);

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
    public Film updateQuantity(UpdateFilmQuantityCmd updateFilmQuantityCmd) {
        final Film film = this.filmRepository.findById(updateFilmQuantityCmd.getId())
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", updateFilmQuantityCmd.getId())));;

        film.increaseBy(updateFilmQuantityCmd.getQuantity());

        return this.filmRepository.save(film);
    }
}
