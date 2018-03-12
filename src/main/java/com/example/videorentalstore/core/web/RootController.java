package com.example.videorentalstore.core.web;

import com.example.videorentalstore.customer.web.CustomerController;
import com.example.videorentalstore.film.web.FilmController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * REST root resource.
 */
@RestController
public class RootController {

    @GetMapping("/")
    ResponseEntity<ResourceSupport> root() {

        ResourceSupport resourceSupport = new ResourceSupport();

        resourceSupport.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        resourceSupport.add(linkTo(methodOn(CustomerController.class).getAll(null)).withRel("customers"));
        resourceSupport.add(linkTo(methodOn(FilmController.class).getAll(null)).withRel("films"));

        return ResponseEntity.ok(resourceSupport);
    }
}
