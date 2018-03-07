package com.example.videorentalstore.film;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        return this.filmRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Film findById(Long id) {
        return this.filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", id)));
    }

    @Override
    public Film save(CreateFilmCommand createFilmCommand) {
        final Film film = new Film(createFilmCommand.getTitle(), ReleaseType.valueOf(createFilmCommand.getType()), createFilmCommand.getQuantity());

        return this.filmRepository.save(film);
    }

    @Override
    public Film update(UpdateFilmCommand updateFilmCommand) {
        final Film film = this.filmRepository.findById(updateFilmCommand.getId())
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", updateFilmCommand.getId())));

        film.process(updateFilmCommand);

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
    public Film updateQuantity(UpdateFilmQuantityCommand updateFilmQuantityCommand) {
        final Film film = this.filmRepository.findById(updateFilmQuantityCommand.getId())
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", updateFilmQuantityCommand.getId())));;

        film.increaseBy(updateFilmQuantityCommand.getQuantity());

        return this.filmRepository.save(film);
    }
}
