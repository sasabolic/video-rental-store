package io.sixhours.videorentalstore.film.web.dto.assembler;

import io.sixhours.videorentalstore.core.GenericResponseAssembler;
import io.sixhours.videorentalstore.film.Film;
import io.sixhours.videorentalstore.film.web.dto.FilmResponse;

/**
 * Assembler interface for creating {@link FilmResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface FilmResponseAssembler extends GenericResponseAssembler<Film, FilmResponse> {
}
