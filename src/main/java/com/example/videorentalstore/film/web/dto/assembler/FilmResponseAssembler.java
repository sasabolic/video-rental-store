package com.example.videorentalstore.film.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.web.dto.FilmResponse;

/**
 * Assembler interface for creating {@link FilmResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface FilmResponseAssembler extends GenericResponseAssembler<Film, FilmResponse> {
}
