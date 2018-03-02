package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
import lombok.EqualsAndHashCode;
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
    @JoinColumn(name="film_id")
    private Film film;

    @Column(name = "days_rented")
    private long daysRented;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Rental(Film film, long daysRented) {
        this(film, daysRented, Instant.now());
    }

    public Rental(Film film, long daysRented, Instant startDate) {
        this.film = film;
        this.daysRented = daysRented;
        this.startDate = startDate;
        this.status = Status.RESERVED;
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

    public enum Status {
        RESERVED,

        RENTED,

        RETURNED
    }
}
