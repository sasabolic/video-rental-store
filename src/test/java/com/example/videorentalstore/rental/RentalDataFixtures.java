package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.FilmDataFixtures;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class RentalDataFixtures {
    private static final Instant startDate = Instant.now().minus(3, ChronoUnit.DAYS);

    public static Rental rental() {
        return new Rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, startDate);
    }

    public static List<Rental> rentalList() {
        return Arrays.asList(
                new Rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, startDate),
                new Rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, startDate),
                new Rental(FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2, startDate),
                new Rental(FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7, startDate)
        );
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
