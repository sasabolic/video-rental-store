package com.example.videorentalstore.film;

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
        final Film film = FilmFactory.create(createFilmCmd);

        return this.filmRepository.save(film);
    }

    @Override
    public void update(UpdateFilmCmd updateFilmCmd) {
        final Film film = this.filmRepository.findOne(updateFilmCmd.getId());

        film.process(updateFilmCmd);

        this.filmRepository.save(film);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void updateQuantity(UpdateFilmQuantityCmd updateFilmQuantityCmd) {

    }
}
