package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.Rental;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "bonus_points")
    private Long bonusPoints;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<Rental> rentals = new ArrayList<>();

    public void addRental(Rental rental) {
        this.rentals.add(rental);
    }

    public BigDecimal calculate() {
        return this.rentals.stream()
                .filter(r -> Rental.Status.RENTED.equals(r.getStatus()))
                .map(r -> r.calculatePrice())
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
    }

    public BigDecimal calculateExtraCharges() {
        return this.rentals.stream()
                .filter(r -> Rental.Status.RETURNED.equals(r.getStatus()))
                .map(r -> r.calculateExtraCharges())
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
    }

    public int calculateBonusPoint() {
        return this.rentals.stream()
                .filter(r -> Rental.Status.RETURNED.equals(r.getStatus()))
                .map(r -> r.calculateBonusPoints())
                .reduce(0, (x, y) -> x + y);
    }
}
