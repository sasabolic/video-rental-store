package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

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

    public Rental(Film film, int daysRented) {
        this(film, daysRented, Instant.now());
    }

    public Rental(Film film, int daysRented, Instant startDate) {
        this.film = film;
        this.film.take();

        this.daysRented = daysRented;
        this.startDate = startDate;
        this.status = Status.RENTED;
    }

    public Rental(long id, Film film, int daysRented, Instant startDate) {
        this(film, daysRented, startDate);
        this.id = id;
    }

    public Rental(long id, Film film, int daysRented) {
        this(film, daysRented);
        this.id = id;
    }

    public boolean isNew() {
        return this.status == Status.RENTED;
    }

    public Rental markReturned() {
        if (this.status != Status.RENTED) {
            throw new IllegalStateException(
                    String.format("Cannot mark rental with id '%d' as RETURNED that is currently not RENTED! Current status: %s.", this.id, this.status));
        }

        this.endDate = Instant.now();
        this.film.returnBack();
        this.status = Status.RETURNED;

        return this;
    }

    public BigDecimal calculatePrice() {
        return this.film.calculatePrice(this.daysRented);
    }

    public BigDecimal calculateExtraCharges() {
        return this.film.calculatePrice(DAYS.between(this.startDate, this.endDate) - this.daysRented);
    }

    public int calculateBonusPoints() {
        return this.film.calculateBonusPoints();
    }

    public enum Status {
        RENTED,

        RETURNED
    }
}
