package com.example.videorentalstore.film;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        discriminatorType = DiscriminatorType.STRING,
        name = "type",
        columnDefinition = "VARCHAR(50)"
)
public abstract class Film {

    static final String NEW_RELEASE = "NEW_RELEASE";
    static final String REGULAR_RELEASE = "REGULAR_RELEASE";
    static final String OLD_RELEASE = "OLD_RELEASE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int quantity;

    public Film() {

    }

    public Film(String name) {
        this.name = name;
    }

    public Film(String name, int quantity) {
        this(name);
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

    public void take() {
        if (this.quantity == 0) {
            throw new RuntimeException("There is not any copy of the '" + this.name + "' film available");
        }
        this.quantity--;
    }

    public void returnBack() {
        this.quantity++;
    }

    public abstract BigDecimal calculatePrice(long daysRented);

    public abstract int calculateBonusPoints();

}
