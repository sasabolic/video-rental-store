package io.sixhours.videorentalstore.rental;

import io.sixhours.videorentalstore.film.Film;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Rental entity.
 *
 * @author Sasa Bolic
 */
@Entity
@Getter
@NoArgsConstructor
@ToString
@Where(clause = "active = true")
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
    }

    public Rental markReturned() {
        this.active = false;
        this.endDate = Instant.now();
        this.film.putBack();

        return this;
    }

    public Money calculatePrice() {
        return this.film.calculatePrice(this.daysRented);
    }

    public Money calculateExtraCharges() {
        if (this.endDate == null) {
            throw new NullPointerException("Cannot create late charges if END DATE is not set.");
        }
        return this.film.calculateExtraCharges(DAYS.between(this.startDate, this.endDate) - this.daysRented);
    }

    public int calculateBonusPoints() {
        return this.film.calculateBonusPoints(this.daysRented);
    }
}
