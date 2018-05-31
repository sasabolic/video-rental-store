package com.example.videorentalstore.film.web.dto.assembler;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.web.dto.FilmResponse;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link FilmResponseAssembler}.
 *
 * @author Sasa Bolic
 */
@Component
public class DefaultFilmResponseAssembler implements FilmResponseAssembler {

    @Override
    public FilmResponse of(Film entity) {
        return new FilmResponse(entity.getId(), entity.getTitle(), entity.getType().name(), entity.getQuantity());
    }
}
