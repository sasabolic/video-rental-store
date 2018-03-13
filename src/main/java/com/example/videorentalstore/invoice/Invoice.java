package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.rental.Rental;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invoice_rental",
            joinColumns = @JoinColumn(name = "invoice_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id", referencedColumnName = "id"))
    private Set<Rental> rentals = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private InvoiceType type;

    private boolean active = true;

    public Invoice(Customer customer, InvoiceType type) {
        Objects.requireNonNull(customer, "Invoice's customer cannot be null!");
        Objects.requireNonNull(type, "Invoice's type cannot be null!");

        this.type = type;
        this.rentals = customer.getRentals().stream()
                .filter(type.invoiceFilter())
                .map(type.invoceAction())
                .collect(Collectors.toSet());

        this.customer = customer;
        this.amount = type.calculateAmount(customer);
    }

    public void pay() {
        this.rentals.forEach(type.paymentAction());
    }

    public Invoice deactivate() {
        this.active = false;

        return this;
    }
}
