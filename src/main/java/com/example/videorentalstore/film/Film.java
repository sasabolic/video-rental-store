package com.example.videorentalstore.film;

import com.example.videorentalstore.pricing.ReleasePolicyFactory;
import com.example.videorentalstore.pricing.ReleaseType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void process(UpdateFilmCmd updateFilmCmd) {
        this.name = updateFilmCmd.getName();
        this.type = ReleaseType.valueOf(updateFilmCmd.getType());
        this.quantity = updateFilmCmd.getQuantity();
    }

    public void deactivate() {
        this.active = false;
    }

    public void increaseBy(int quantity) {
        this.quantity = this.quantity + quantity;
    }

    public void take() {
        if (this.quantity == 0) {
            throw new RuntimeException("There is not any copy of the '" + this.name + "' film available");
        }
        this.quantity--;
    }

    public void returnBack() {
        this.quantity++;
    }

    public BigDecimal calculatePrice(long daysRented) {
//        return ReleasePolicy.of(type).calculatePrice(daysRented);
        return ReleasePolicyFactory.of(type).calculatePrice(daysRented);
    }

    public int calculateBonusPoints() {
        return ReleasePolicyFactory.of(type).calculateBonusPoints();
    }

}
