package com.example.videorentalstore.film.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.web.dto.FilmResponse;
import org.springframework.hateoas.Resources;

import java.util.Collection;

/**
 * Assembler interface for creating {@link FilmResponse} DTOs.
 */
public interface FilmResponseAssembler extends GenericResponseAssembler<Film, FilmResponse> {

    Resources<FilmResponse> of(final Collection<Film> entities, String title);
}
