package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.Rental;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private boolean active = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private List<Rental> rentals = new ArrayList<>();

    public Customer(String firstName, String lastName) {
        Objects.requireNonNull(firstName, "Customer's first name cannot be null!");
        Objects.requireNonNull(lastName, "Customer's last name cannot be null!");

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void update(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void deactivate() {
        this.rentals.stream().filter(Rental::isActive).findAny().ifPresent(r -> {
            throw new IllegalStateException(String.format("Customer with id '%d' cannot be deactivated while containing rentals in status ACTIVE.", this.id));
        });
        this.active = false;
    }

    public void addRental(Rental rental) {
        this.rentals.add(rental);
        if (rental.isActive()) {
            this.bonusPoints += rental.calculateBonusPoints();
        }
    }

    public BigDecimal calculatePrice() {
        return this.rentals.stream()
                .map(Rental::calculatePrice)
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
    }

    public BigDecimal calculateExtraCharges() {
        return this.rentals.stream()
                .map(Rental::calculateExtraCharges)
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
    }
}
