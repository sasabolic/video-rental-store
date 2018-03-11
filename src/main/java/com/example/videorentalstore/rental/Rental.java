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
        this.status = Status.UP_FRONT_PAYMENT_EXPECTED;
    }

    public Rental markPaidUpFront() {
        if (this.status != Status.UP_FRONT_PAYMENT_EXPECTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as PAID_UP_FRONT that is currently not UP_FRONT_PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.status = Status.PAID_UP_FRONT;

        return this;
    }

    public Rental markInProcess() {
        if (this.status != Status.PAID_UP_FRONT) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as IN_PROCESS that is currently not PAID_UP_FRONT! Current status: %s.", this.status));
        }
        this.status = Status.IN_PROCESS;

        return this;
    }

    public void markReturned() {
        if (this.status != Status.IN_PROCESS) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as RETURNED that is currently not IN_PROCESS! Current status: %s.", this.status));
        }
        this.status = Status.RETURNED;
        this.returnDate = Instant.now();
        this.film.putBack();
    }

    public Rental markLatePaymentExpected() {
        if (this.status != Status.RETURNED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as LATE_PAYMENT_EXPECTED that is currently not RETURNED! Current status: %s.", this.status));
        }
        this.status = Status.LATE_PAYMENT_EXPECTED;

        return this;
    }

    public Rental markPayedLate() {
        if (this.status != Status.LATE_PAYMENT_EXPECTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as PAID_LATE that is currently not LATE_PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.status = Status.PAID_LATE;

        return this;
    }

    public Rental markCompleted() {
        if (this.status != Status.PAID_LATE) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental as COMPLETED that is currently not PAID_LATE! Current status: %s.", this.status));
        }
        this.status = Status.COMPLETED;
        this.active = false;

        return this;
    }

    public boolean isUpFrontPaymentExpected() {
        return this.status.equals(Status.UP_FRONT_PAYMENT_EXPECTED);
    }

    public boolean isPaidUpFront() {
        return this.status.equals(Status.PAID_UP_FRONT);
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

    public void deactivate() {
        if (!this.status.equals(Status.UP_FRONT_PAYMENT_EXPECTED)) {
            throw new IllegalStateException(String.format("Cannot deactivate rental that is currently not UP_FRONT_PAYMENT_EXPECTED! Current status: %s.", this.status));
        }
        this.active = false;
    }

    public BigDecimal calculatePrice() {
        return this.film.calculatePrice(this.daysRented);
    }

    public BigDecimal calculateExtraCharges() {
        if (this.returnDate == null) {
            throw new NullPointerException("Cannot calculate late charges if RETURN DATE is not set.");
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
        UP_FRONT_PAYMENT_EXPECTED,

        PAID_UP_FRONT,

        IN_PROCESS,

        RETURNED, // mozda i ne treba vec odma sledeci

        LATE_PAYMENT_EXPECTED,

        PAID_LATE, // mozda i ne treba vec odma sledeci

        COMPLETED
    }
}
