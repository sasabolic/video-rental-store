package com.example.videorentalstore.film;

import java.util.List;

/**
 * Interface for actions on {@code Film}.
 *
 * @author Sasa Bolic
 */
public interface FilmService {

    /**
     * Returns list of {@code Film} for given {@code title}.
     *
     * @param title the title
     * @return the list of all {@code Film} if {@code title} is {@code null} or list of {@code Film}         who's title field is containing String in given {@code title} value.
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
     * Creates new {@code Film} based on given values.
     *
     * @param title    the title
     * @param type     the type
     * @param quantity the quantity
     * @return the film
     */
    Film save(String title, String type, int quantity);

    /**
     * Updates {@code Film} with given values.
     *
     * @param id       the id
     * @param title    the title
     * @param type     the type
     * @param quantity the quantity
     * @return the film
     */
    Film update(Long id, String title, String type, int quantity);

    /**
     * Deactivates {@code Film} with given {@code id}.
     *
     * @param id the id
     * @return the film
     */
    Film delete(Long id);

    /**
     * Updates {@code Film} quantity by increasing/decreasing it by the value {@code quantity}.
     *
     * @param id       the id
     * @param quantity the quantity
     * @return the film
     */
    Film updateQuantity(Long id, int quantity);
}
