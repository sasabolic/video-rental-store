package com.example.videorentalstore.inventory;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Film(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract BigDecimal calculatePrice(long daysRented);

    public abstract int calculateBonusPoints();

}
