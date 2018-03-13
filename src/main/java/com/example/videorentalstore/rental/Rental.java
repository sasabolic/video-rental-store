package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;

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
    private Instant returnDate;

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
        this.status = Status.RESERVED;
    }

    public Rental markUpFrontPaymentExpected() {
        if (this.status != Status.RESERVED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as UP_FRONT_PAYMENT_EXPECTED that is currently not RESERVED! Current status: %s.", this.status));
        }
        this.status = Status.UP_FRONT_PAYMENT_EXPECTED;

        return this;
    }

    public Rental markInProcess() {
        if (this.status != Status.UP_FRONT_PAYMENT_EXPECTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as IN_PROCESS that is currently not UP_FRONT_PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.status = Status.IN_PROCESS;

        return this;
    }

    public Rental markReturned() {
        if (this.status != Status.IN_PROCESS) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as RETURNED that is currently not IN_PROCESS! Current status: %s.", this.status));
        }
        this.status = Status.RETURNED;
        this.returnDate = Instant.now();
        this.film.putBack();

        return this;
    }

    public Rental markLatePaymentExpected() {
        if (this.status != Status.RETURNED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as LATE_PAYMENT_EXPECTED that is currently not RETURNED! Current status: %s.", this.status));
        }
        this.status = Status.LATE_PAYMENT_EXPECTED;

        return this;
    }

    public Rental markCompleted() {
        if (this.status != Status.LATE_PAYMENT_EXPECTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as COMPLETED that is currently not LATE_PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.status = Status.COMPLETED;
        this.active = false;

        return this;
    }

    public boolean isReserved() {
        return this.status.equals(Status.RESERVED);
    }

    public boolean isUpFrontPaymentExpected() {
        return this.status.equals(Status.UP_FRONT_PAYMENT_EXPECTED);
    }

    public boolean isReturned() {
        return this.status.equals(Status.RETURNED);
    }

    public boolean isInProcess() {
        return this.status.equals(Status.IN_PROCESS);
    }

    public boolean isLatePaymentExpected() {
        return this.status.equals(Status.LATE_PAYMENT_EXPECTED);
    }

    public boolean isNotCompleted() {
        return !this.status.equals(Status.COMPLETED);
    }

    public boolean hasStatus(Status status) {
        return this.status.equals(status);
    }

    public Rental apply(Function<Rental, Rental> fun) {
        return fun.apply(this);
    }

    public Rental deactivate() {
        if (!this.status.equals(Status.RESERVED)) {
            throw new IllegalStateException(String.format("Cannot deactivate rental that is currently not RESERVED! Current status: %s.", this.status));
        }
        this.active = false;

        return this;
    }

    public BigDecimal calculatePrice() {
        return this.film.calculatePrice(this.daysRented);
    }

    public BigDecimal calculateExtraCharges() {
        if (this.returnDate == null) {
            throw new NullPointerException("Cannot create late charges if RETURN DATE is not set.");
        }
        return this.film.calculateExtraCharges(DAYS.between(this.startDate, this.returnDate) - this.daysRented);
    }

    public int calculateBonusPoints() {
        return this.film.calculateBonusPoints(this.daysRented);
    }

    /**
     * Enumeration for all the statuses {@link Rental} can be in.
     */
    public enum Status {
        RESERVED,

        UP_FRONT_PAYMENT_EXPECTED,

        IN_PROCESS,

        RETURNED,

        LATE_PAYMENT_EXPECTED,

        COMPLETED
    }
}
