package com.example.videorentalstore.film.web.dto.assembler;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.web.FilmController;
import com.example.videorentalstore.film.web.dto.FilmResponse;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Implementation of {@link FilmResponseAssembler}.
 */
@Component
public class DefaultFilmResponseAssembler implements FilmResponseAssembler {

    @Override
    public FilmResponse of(Film entity) {
        return new FilmResponse(entity.getId(), entity.getTitle(), entity.getType().name(), entity.getQuantity());
    }

    @Override
    public Resources<FilmResponse> of(Collection<Film> entities, String title) {
        final Resources<FilmResponse> resources = new Resources<>(of(entities));

        resources.add(linkTo(methodOn(FilmController.class).getAll(title)).withSelfRel());

        return resources;
    }
}
