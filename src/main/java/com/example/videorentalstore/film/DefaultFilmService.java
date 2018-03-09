package com.example.videorentalstore.film;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link FilmService} delegating persistence operations to {@link FilmRepository}.
 */
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
        return this.filmRepository.findByTitle(title);
    }

    @Override
    public Film findById(Long id) {
        return this.filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", id)));
    }

    @Override
    public Film save(CreateFilmCommand createFilmCommand) {
        this.filmRepository.findByTitle(createFilmCommand.getTitle()).stream()
                .findAny().ifPresent(f -> {
                    throw new FilmUniqueViolationException(String.format("Film with title '%s' already exits", createFilmCommand.getTitle()));
                });

        final Film film = new Film(createFilmCommand.getTitle(), ReleaseType.valueOf(createFilmCommand.getType()), createFilmCommand.getQuantity());

        return this.filmRepository.save(film);
    }

    @Override
    public Film update(UpdateFilmCommand updateFilmCommand) {
        // TODO: 3/9/18 Extract filter().findAny().ifPresent() is reusable lambda. Sending only the predicate for filter action.
        this.filmRepository.findByTitle(updateFilmCommand.getTitle()).stream()
                .filter(f -> !f.getId().equals(updateFilmCommand.getId()))
                .findAny().ifPresent(f -> {
                    throw new FilmUniqueViolationException(String.format("Film with title '%s' already exits", updateFilmCommand.getTitle()));
                });

        final Film film = this.filmRepository.findById(updateFilmCommand.getId())
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", updateFilmCommand.getId())));

        film.update(updateFilmCommand.getTitle(), updateFilmCommand.getType(), updateFilmCommand.getQuantity());

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
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", updateFilmQuantityCommand.getId())));

        film.changeQuantityBy(updateFilmQuantityCommand.getQuantity());

        return this.filmRepository.save(film);
    }
}
