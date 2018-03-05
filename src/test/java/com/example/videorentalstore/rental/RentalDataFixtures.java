package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmDataFixtures;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class RentalDataFixtures {
    private static final Instant startDate = Instant.now().minus(3, ChronoUnit.DAYS);

    public static Rental rental() {
        return new Rental(1L, FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, startDate);
    }

    public static Rental rental(Film film) {
        return new Rental(1L, film, 1);
    }

    public static Rental rental(Film film, int daysRented) {
        return new Rental(1L, film, daysRented);
    }

    public static Rental rental(Film film, int daysRented, int startedDaysBeforeNow) {
        final Instant startDate = Instant.now().minus(startedDaysBeforeNow, ChronoUnit.DAYS);
        return new Rental(1L, film, daysRented, startDate);
    }

    public static List<Rental> rentals() {
        return Arrays.asList(
                new Rental(1L, FilmDataFixtures.newReleaseFilm("Matrix 11"), 1),
                new Rental(2L, FilmDataFixtures.regularReleaseFilm("Spider Man"), 5),
                new Rental(3L, FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2),
                new Rental(4L, FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7)
        );
    }

    public static List<Rental> rentals(int startedDaysBeforeNow) {
        final Instant startDate = Instant.now().minus(startedDaysBeforeNow, ChronoUnit.DAYS);
        return Arrays.asList(
                new Rental(1L, FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, startDate),
                new Rental(2L, FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, startDate),
                new Rental(3L, FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2, startDate),
                new Rental(4L, FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7, startDate)
        );
    }

    public static List<Rental> returnedRentals() {
        return Arrays.asList(
                new Rental(1L, FilmDataFixtures.newReleaseFilm("Matrix 11"), 1).markReturned(),
                new Rental(2L, FilmDataFixtures.regularReleaseFilm("Spider Man"), 5).markReturned(),
                new Rental(3L, FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2).markReturned(),
                new Rental(4L, FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7).markReturned()
        );
    }

    public static String idsJson() {
        return "[\"1\", \"2\", \"3\", \"4\"]";
    }

    public static String json() {
        return "[\n" +
                "  {\n" +
                "    \"film_id\":1,\"days_rented\":1\n" +
                "  },\n" +
                "  {\n" +
                "    \"film_id\":2,\"days_rented\":5\n" +
                "  },\n" +
                "  {\n" +
                "    \"film_id\":3,\"days_rented\":2\n" +
                "  },\n" +
                "  {\n" +
                "    \"film_id\":4,\"days_rented\":7\n" +
                "  }\n" +
                "]";

    }

}
