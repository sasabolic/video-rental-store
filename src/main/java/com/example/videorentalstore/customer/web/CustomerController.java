package com.example.videorentalstore.customer.web;


import com.example.videorentalstore.customer.CreateCustomerCommand;
import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerService;
import com.example.videorentalstore.customer.UpdateCustomerCommand;
import com.example.videorentalstore.customer.web.dto.CustomerResponse;
import com.example.videorentalstore.customer.web.dto.WriteCustomerRequest;
import com.example.videorentalstore.customer.web.dto.assembler.CustomerResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerResponseAssembler customerResponseAssembler;

    public CustomerController(CustomerService customerService, CustomerResponseAssembler customerResponseAssembler) {
        this.customerService = customerService;
        this.customerResponseAssembler = customerResponseAssembler;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(@RequestParam(required = false) String name) {
        final List<Customer> result = this.customerService.findAll(name);

        return ResponseEntity.ok(this.customerResponseAssembler.of(result));
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerResponse> get(@PathVariable Long customerId) {
        final Customer result = this.customerService.findById(customerId);

        return ResponseEntity.ok(this.customerResponseAssembler.of(result));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody WriteCustomerRequest writeCustomerRequest) {
        final Customer result = this.customerService.save(new CreateCustomerCommand(writeCustomerRequest.getFirstName(), writeCustomerRequest.getLastName()));

        return ResponseEntity.ok(this.customerResponseAssembler.of(result));
    }

    @PutMapping(value = "/{customerId}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long customerId, @RequestBody WriteCustomerRequest writeCustomerRequest) {
        final Customer result = this.customerService.update(new UpdateCustomerCommand(customerId, writeCustomerRequest.getFirstName(), writeCustomerRequest.getLastName()));

        return ResponseEntity.ok(this.customerResponseAssembler.of(result));
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        this.customerService.delete(customerId);

        return ResponseEntity.noContent().build();
    }
}
