package com.example.videorentalstore.film;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ReleaseType type;

    private int quantity;

    private boolean active = true;

    public Film() {
    }

    public Film(String name, ReleaseType type) {
        this.name = name;
        this.type = type;
    }

    public Film(String name, ReleaseType type, int quantity) {
        this(name, type);
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReleaseType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void process(UpdateFilmCommand updateFilmCommand) {
        this.name = updateFilmCommand.getName();
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
            throw new RuntimeException("There is not any copy of the '" + this.name + "' film available");
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
