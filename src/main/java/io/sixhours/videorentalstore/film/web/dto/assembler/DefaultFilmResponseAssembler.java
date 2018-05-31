package io.sixhours.videorentalstore.film.web.dto.assembler;

import io.sixhours.videorentalstore.film.Film;
import io.sixhours.videorentalstore.film.web.dto.FilmResponse;
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
