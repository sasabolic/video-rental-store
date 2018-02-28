package com.example.videorentalstore.pricing;

import com.example.videorentalstore.inventory.NewReleaseFilm;
import com.example.videorentalstore.inventory.OldReleaseFilm;
import com.example.videorentalstore.inventory.RegularReleaseFilm;
import com.example.videorentalstore.rental.Rental;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class RentalDataFixtures {
    private static final Instant startDate = Instant.now().minus(3, ChronoUnit.DAYS);

    public static Rental rental() {
        return new Rental(new NewReleaseFilm("Matrix 11"), 1, startDate);
    }

    public static List<Rental> rentalList() {
        return Arrays.asList(
                new Rental(new NewReleaseFilm("Matrix 11"), 1, startDate),
                new Rental(new RegularReleaseFilm("Spider Man"), 5, startDate),
                new Rental(new RegularReleaseFilm("Spider Man 2"), 2, startDate),
                new Rental(new OldReleaseFilm("Out of Africa"), 7, startDate)
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
