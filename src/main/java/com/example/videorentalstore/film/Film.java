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

    public void update(String title, String type, int quantity) {
        this.title = title;
        this.type = ReleaseType.valueOf(type);
        this.quantity = quantity;
    }

    public void deactivate() {
        this.active = false;
    }

    public void increaseBy(int quantity) {
        this.quantity = this.quantity + quantity;
    }

    public void take() {
        if (this.quantity == 0) {
            throw new RuntimeException("There is not any copy of the '" + this.title + "' film available");
        }
        this.quantity--;
    }

    public void returnBack() {
        this.quantity++;
    }

    public BigDecimal calculatePrice(long daysRented) {
        return type.calculatePrice(daysRented);
    }

    public int calculateBonusPoints() {
        return type.calculateBonusPoints();
    }
}
