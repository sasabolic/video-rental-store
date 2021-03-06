package io.sixhours.videorentalstore.customer.web;


import io.sixhours.videorentalstore.customer.Customer;
import io.sixhours.videorentalstore.customer.CustomerService;
import io.sixhours.videorentalstore.customer.web.dto.CustomerResponse;
import io.sixhours.videorentalstore.customer.web.dto.SaveCustomerRequest;
import io.sixhours.videorentalstore.customer.web.dto.assembler.CustomerResponseAssembler;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * REST customer resources.
 *
 * @author Sasa Bolic
 */
@RestController
@RequestMapping("/customers")
@Api(tags = "customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerResponseAssembler customerResponseAssembler;

    public CustomerController(CustomerService customerService, CustomerResponseAssembler customerResponseAssembler) {
        this.customerService = customerService;
        this.customerResponseAssembler = customerResponseAssembler;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(@RequestParam(required = false) String name) {
        final List<Customer> customers = this.customerService.findAll(name);

        return ResponseEntity.ok(this.customerResponseAssembler.of(customers));
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerResponse> get(@PathVariable Long customerId) {
        final Customer customer = this.customerService.findById(customerId);

        return ResponseEntity.ok(this.customerResponseAssembler.of(customer));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid SaveCustomerRequest saveCustomerRequest) {
        final Customer customer = this.customerService.save(saveCustomerRequest.getFirstName(), saveCustomerRequest.getLastName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{customerId}")
                .buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{customerId}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long customerId, @RequestBody @Valid SaveCustomerRequest saveCustomerRequest) {
        final Customer result = this.customerService.update(customerId, saveCustomerRequest.getFirstName(), saveCustomerRequest.getLastName());

        return ResponseEntity.ok(this.customerResponseAssembler.of(result));
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        this.customerService.delete(customerId);

        return ResponseEntity.noContent().build();
    }
}
