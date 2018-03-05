package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.Rental;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "bonus_points")
    private long bonusPoints;

    @JsonIgnore
    private boolean active = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private List<Rental> rentals = new ArrayList<>();


    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void deactivate() {
        this.active = false;
    }

    public void addRental(Rental rental) {
        this.rentals.add(rental);
        if (rental.isNew()) {
            this.bonusPoints += rental.calculateBonusPoints();
        }
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
}
