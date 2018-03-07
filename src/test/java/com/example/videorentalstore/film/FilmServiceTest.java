package com.example.videorentalstore.film;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.example.videorentalstore.film.ReleaseType.NEW_RELEASE;
import static com.example.videorentalstore.film.ReleaseType.REGULAR_RELEASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class FilmServiceTest {

    private FilmService filmService;

    @Mock
    private static FilmRepository filmRepository;

    @Before
    public void setUp() {
        filmService = new DefaultFilmService(filmRepository);
    }

    @Test
    public void whenFindAllThenReturnListOfFilms() {
        doReturn(FilmDataFixtures.films()).when(filmRepository).findAll();

        final List<Film> result = filmService.findAll(null);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }

    @Test
    public void whenFindAllByTitleThenReturnListOfFilmsContainingTitle() {
        doReturn(FilmDataFixtures.filmsWithTitleSpiderMan()).when(filmRepository).findByTitleContainingIgnoreCase(isA(String.class));

        final List<Film> result = filmService.findAll("Spider Man");

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
    }

    @Test
    public void whenFindingByIdThenReturnOneResult() {
        doReturn(Optional.of(FilmDataFixtures.oldReleaseFilm())).when(filmRepository).findById(isA(Long.class));

        final Film result = filmService.findById(4L);

        assertThat(result).isNotNull();
    }

    @Test
    public void whenCreatingNewReleaseFilmThenReturnSavedFilm() {
        final String title = "Maze Runner: The Death Cure";
        final String type = "NEW_RELEASE";
        final int quantity = 10;

        final CreateFilmCommand createFilmCommand = new CreateFilmCommand(title, type, quantity);

        doReturn(FilmDataFixtures.newReleaseFilm(title, quantity)).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.save(createFilmCommand);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("title", title);
        assertThat(result).hasFieldOrPropertyWithValue("type", NEW_RELEASE);
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity);

    }

    @Test
    public void whenUpdatingFilmThenReturnSavedFilm() {
        final String title = "Maze Runner: The Death Cure";
        final String newTitle = "New Maze Runner: The Death Cure";
        final String newType = "REGULAR_RELEASE";
        final int quantity = 10;

        final UpdateFilmCommand updateFilmCommand = new UpdateFilmCommand(5L, newTitle, newType, quantity);

        doReturn(Optional.of(FilmDataFixtures.newReleaseFilm(title, quantity))).when(filmRepository).findById(isA(Long.class));
        doReturn(FilmDataFixtures.regularReleaseFilm(newTitle, quantity)).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.update(updateFilmCommand);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("title", newTitle);
        assertThat(result).hasFieldOrPropertyWithValue("type", REGULAR_RELEASE);
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity);
    }

    @Test
    public void whenDeleteThenDeactivateRecord() {
        final Film toBeReturned = FilmDataFixtures.oldReleaseFilm();
        doReturn(Optional.of(toBeReturned)).when(filmRepository).findById(isA(Long.class));
        doReturn(toBeReturned).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.delete(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("title", toBeReturned.getTitle());
        assertThat(result).hasFieldOrPropertyWithValue("type", toBeReturned.getType());
        assertThat(result).hasFieldOrPropertyWithValue("quantity", toBeReturned.getQuantity());
        assertThat(result).hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    public void whenUpdateQuantityThenIncreaseQuantity() {
        final int oldQuantity = 10;
        final Film toBeReturned = FilmDataFixtures.oldReleaseFilm("Out of Africa", oldQuantity);

        doReturn(Optional.of(toBeReturned)).when(filmRepository).findById(isA(Long.class));
        doReturn(toBeReturned).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.updateQuantity(new UpdateFilmQuantityCommand(1L, 23));

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("title", toBeReturned.getTitle());
        assertThat(result).hasFieldOrPropertyWithValue("type", toBeReturned.getType());
        assertThat(result).hasFieldOrPropertyWithValue("quantity", oldQuantity + 23);
    }
}
