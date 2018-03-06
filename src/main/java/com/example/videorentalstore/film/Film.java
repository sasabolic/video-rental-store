package com.example.videorentalstore.film;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ReleaseType type;

    private int quantity;

    private boolean active = true;

    public Film(String title, ReleaseType type) {
        this.title = title;
        this.type = type;
    }

    public Film(String title, ReleaseType type, int quantity) {
        this(title, type);
        this.quantity = quantity;
    }

    public void process(UpdateFilmCommand updateFilmCommand) {
        this.title = updateFilmCommand.getTitle();
        this.type = ReleaseType.valueOf(updateFilmCommand.getType());
        this.quantity = updateFilmCommand.getQuantity();
    }

    public void deactivate() {
        this.active = false;
    }

    public Film increaseBy(int quantity) {
        this.quantity = this.quantity + quantity;

        return this;
    }

    public Film take() {
        if (this.quantity == 0) {
            throw new RuntimeException("There is not any copy of the '" + this.title + "' film available");
        }
        this.quantity--;

        return this;
    }

    public Film returnBack() {
        this.quantity++;

        return this;
    }

    public BigDecimal calculatePrice(long daysRented) {
        return type.calculatePrice(daysRented);
    }

    public int calculateBonusPoints() {
        return type.calculateBonusPoints();
    }

}
