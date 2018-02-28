package com.example.videorentalstore.rental;

import com.example.videorentalstore.inventory.Film;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Entity
public class Rental {

    public enum Status {
        RENTED,
        RETURNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long customerId;
    private final Film film;
    private final long daysRented;
    private final Instant startDate;
    private Instant endDate;
    private Status status;


    public Rental(Film film, long daysRented) {
        this(film, daysRented, Instant.now());
    }

    public Rental(Film film, long daysRented, Instant startDate) {
        this.film = film;
        this.daysRented = daysRented;
        this.startDate = startDate;
        this.status = Status.RENTED;
    }

    public void turnBack() {
        this.endDate = Instant.now();
        this.status = Status.RETURNED;
    }

    public BigDecimal calculatePrice() {
        return this.film.calculatePrice(this.daysRented);
    }

    public BigDecimal calculateExtraCharges() {
        return this.film.calculatePrice(DAYS.between(this.startDate, this.endDate) - this.daysRented);
    }

    public int calculateBonusPoints() {
        if (Status.RETURNED.equals(this.status)) {
            return this.film.calculateBonusPoints();
        }
        return 0;
    }

}
