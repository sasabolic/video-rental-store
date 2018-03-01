package com.example.videorentalstore.film;

import com.example.videorentalstore.pricing.ReleaseType;
import org.springframework.stereotype.Service;

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
    public Film findById(Long id) {
        return this.filmRepository.findOne(id);
    }

    @Override
    public Film save(CreateFilmCmd createFilmCmd) {
        final Film film = new Film(createFilmCmd.getName(), ReleaseType.valueOf(createFilmCmd.getType()), createFilmCmd.getQuantity());

        return this.filmRepository.save(film);
    }

    @Override
    public Film update(UpdateFilmCmd updateFilmCmd) {
        final Film film = this.filmRepository.findOne(updateFilmCmd.getId());

        film.process(updateFilmCmd);

        return this.filmRepository.save(film);
    }

    @Override
    public void delete(Long id) {
        final Film film = this.filmRepository.findOne(id);

        film.deactivate();

        this.filmRepository.save(film);
    }

    @Override
    public void updateQuantity(UpdateFilmQuantityCmd updateFilmQuantityCmd) {
        final Film film = this.filmRepository.findOne(updateFilmQuantityCmd.getId());

        film.increaseBy(updateFilmQuantityCmd.getQuantity());

        this.filmRepository.save(film);
    }
}
