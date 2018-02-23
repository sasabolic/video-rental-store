package com.example.videorentalstore.billing;

import com.example.videorentalstore.inventory.Film;
import com.example.videorentalstore.rental.Rental;

import java.math.BigDecimal;
import java.util.List;

public class BillingService {

    public void calculatePrice(List<Rental> rentals) {

        rentals.forEach(r -> {
            final Film film = r.getFilm();
            final FilmType filmType = FilmType.now(film.getReleaseDate());

            final BigDecimal price = filmType.calculate(r.getDays());
            System.out.println(film.getName() + ": " + price);

        });

    }
}
