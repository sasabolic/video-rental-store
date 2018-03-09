package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(name = "days_rented")
    private int daysRented;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean active = true;

    public Rental(Film film, int daysRented) {
        this(film, daysRented, Instant.now());
    }

    public Rental(Film film, int daysRented, Instant startDate) {
        Objects.requireNonNull(film, "Rental's film cannot be null!");
        Objects.requireNonNull(startDate, "Rentals's start date cannot be null!");

        if (daysRented <= 0) {
            throw new IllegalArgumentException("Number of days rented cannot be negative!");
        }
        this.film = film;
        this.film.take();

        this.daysRented = daysRented;
        this.startDate = startDate;
        this.status = Status.PAYMENT_EXPECTED;
    }

    public Rental markActive() {
        if (this.status != Status.PAYMENT_EXPECTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark Rental as ACTIVE that is currently not PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.status = Status.ACTIVE;

        return this;
    }

    public Rental markExtraPaymentExpected() {
        if (this.status != Status.ACTIVE) {
            throw new IllegalStateException(
                    String.format("Cannot mark Rental as EXTRA_PAYMENT_EXPECTED that is currently not ACTIVE! Current status: %s.", this.status));
        }
        this.status = Status.EXTRA_PAYMENT_EXPECTED;

        return this;
    }

    public Rental markCompleted() {
        if (this.status != Status.EXTRA_PAYMENT_EXPECTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark Rental as COMPLETED that is currently not EXTRA_PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.status = Status.COMPLETED;
        this.active = false;
        this.endDate = Instant.now();
        this.film.returnBack();

        return this;
    }

    public BigDecimal calculatePrice() {
        if (this.endDate != null) {
            return BigDecimal.ZERO;
        }
        return this.film.calculatePrice(this.daysRented);
    }

    public BigDecimal calculateExtraCharges() {
        if (this.endDate == null) {
            return BigDecimal.ZERO;
        }
        return this.film.calculateExtraCharges(DAYS.between(this.startDate, this.endDate) - this.daysRented);
    }

    public int calculateBonusPoints() {
        return this.film.calculateBonusPoints(this.daysRented);
    }

    /**
     * Enumeration for all the statuses {@link Rental} can be in.
     */
    enum Status {
        PAYMENT_EXPECTED,

        ACTIVE,

        EXTRA_PAYMENT_EXPECTED,

        COMPLETED
    }
}
