package io.sixhours.videorentalstore.rental;

import io.sixhours.videorentalstore.film.Film;
import io.sixhours.videorentalstore.film.FilmDataFixtures;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class RentalDataFixtures {

    public static Rental rental() {
        return rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, 3);
    }

    public static Rental rental(Film film) {
        return rental(film, 1);
    }

    public static Rental rental(Film film, int daysRented) {
        return rental(film, daysRented, 0);
    }

    public static Rental rental(Film film, int daysRented, int startedDaysBeforeNow) {
        final Instant startDate = Instant.now().minus(startedDaysBeforeNow, ChronoUnit.DAYS);
        return new Rental(film, daysRented, startDate);
    }

    public static List<Rental> rentals() {
        return Arrays.asList(
                rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1),
                rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5),
                rental(FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2),
                rental(FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7)
        );
    }

    public static List<Rental> rentals(int startedDaysBeforeNow) {
        return Arrays.asList(
                rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, startedDaysBeforeNow),
                rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, startedDaysBeforeNow),
                rental(FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2, startedDaysBeforeNow),
                rental(FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7, startedDaysBeforeNow)
        );
    }

    public static List<Rental> returnedRentals() {
        return Arrays.asList(
                rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1).markReturned(),
                rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5).markReturned(),
                rental(FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2).markReturned(),
                rental(FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7).markReturned()
        );
    }

    public static String returnJson() {
        return "[\n" +
                "  {\"id\": 1},\n" +
                "  {\"id\": 2},\n" +
                "  {\"id\": 3},\n" +
                "  {\"id\": 4}\n" +
                "]";
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
