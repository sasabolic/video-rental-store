package com.example.videorentalstore.film;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

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

    public Film(String title, ReleaseType type, int quantity) {
        Objects.requireNonNull(title, "Film's title cannot be null!");
        Objects.requireNonNull(type, "Film's type cannot be null!");
        if (quantity < 0) {
            throw new IllegalArgumentException("Number of film copies cannot be negative!");
        }

        this.title = title;
        this.type = type;
        this.quantity = quantity;
    }

    public void update(String title, String type, int quantity) {
        this.title = title;
        this.type = ReleaseType.valueOf(type);
        this.quantity = quantity;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeQuantityBy(int quantity) {
        final int newQuantity = this.quantity + quantity;
        if (newQuantity < 0) {
            throw new IllegalStateException("Number of film copies cannot be negative!");
        }
        this.quantity = newQuantity;
    }

    public void take() {
        if (this.quantity == 0) {
            throw new IllegalStateException(String.format("There is not any available copy of the film '%s'", this.title));
        }
        this.quantity--;
    }

    public void returnBack() {
        this.quantity++;
    }

    public BigDecimal calculatePrice(long daysRented) {
        return type.calculatePrice(daysRented);
    }

    public BigDecimal calculateExtraCharges(long extraDays) {
        return type.calculatePrice(extraDays);
    }

    public int calculateBonusPoints(long daysRented) {
        return type.calculateBonusPoints(daysRented);
    }
}
