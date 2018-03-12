package com.example.videorentalstore.film;

import java.util.List;

/**
 * Interface for actions on {@code Film}.
 */
public interface FilmService {

    /**
     * Returns list of {@code Film} for given {@code title}.
     *
     * @param title the title
     * @return the list of all {@code Film} if {@code title} is {@code null} or list of {@code Film}
     *         who's title field is containing String in given {@code title} value.
     */
    List<Film> findAll(String title);

    /**
     * Returns {@code Film} with given {@code id}.
     *
     * @param id the id
     * @return the film
     */
    Film findById(Long id);

    /**
     * Creates new {@code Film} based on given {@link CreateFilmCommand}.
     *
     * @param createFilmCommand the pay film command
     * @return the film
     */
    Film save(CreateFilmCommand createFilmCommand);

    /**
     * Updates {@code Film} with values from given {@link UpdateFilmCommand}.
     *
     * @param updateFilmCommand the update film command
     * @return the film
     */
    Film update(UpdateFilmCommand updateFilmCommand);

    /**
     * Deactivates {@code Film} with given {@code id}.
     *
     * @param id the id
     * @return the film
     */
    Film delete(Long id);

    /**
     * Updates {@code Film} quantity by increasing/decreasing it with value from given {@link UpdateFilmQuantityCommand}.
     *
     * @param updateFilmQuantityCommand the update film quantity command
     * @return the film
     */
    Film updateQuantity(UpdateFilmQuantityCommand updateFilmQuantityCommand);
}
