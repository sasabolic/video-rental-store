package com.example.videorentalstore.film.web.dto;

import com.example.videorentalstore.film.web.FilmController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Film response DTO.
 */
@Relation(collectionRelation = "films")
@Getter
public class FilmResponse extends ResourceSupport {

    private String title;

    private String type;

    private int quantity;

    public FilmResponse(Long id, String title, String type, int quantity) {
        this.title = title;
        this.type = type;
        this.quantity = quantity;

        add(linkTo(methodOn(FilmController.class).get(id)).withSelfRel());
    }
}
