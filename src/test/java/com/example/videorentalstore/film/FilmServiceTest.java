package com.example.videorentalstore.film;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(SpringRunner.class)
public class FilmServiceTest {

    private FilmService filmService;

    @Mock
    private static FilmRepository filmRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        filmService = new DefaultFilmService(filmRepository);
    }

    @Test
    public void whenFindingAllThenReturnListOfFilms() {
        doReturn(FilmDataFixtures.films()).when(filmRepository).findAll();

        final List<Film> result = filmService.findAll(null);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }

    @Test
    public void whenFindingAllByTitleThenReturnListOfFilmsContainingTitle() {
        doReturn(FilmDataFixtures.filmsWithTitleSpiderMan()).when(filmRepository).findByTitle(isA(String.class));

        final List<Film> result = filmService.findAll("Spider Man");

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Film::getTitle).containsOnly("Spider Man", "Spider Man 2");
    }

    @Test
    public void whenFindingByIdThenReturnFilm() {
        doReturn(Optional.of(FilmDataFixtures.oldReleaseFilm())).when(filmRepository).findById(isA(Long.class));

        final Film result = filmService.findById(4L);

        assertThat(result).isNotNull();
    }

    @Test
    public void whenFindByNonExistingIdThenThrowException() {
        final long filmId = 1L;

        thrown.expect(FilmNotFoundException.class);
        thrown.expectMessage("Film with id '" + filmId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(isA(Long.class));

        filmService.findById(filmId);
    }

    @Test
    public void whenCreatingFilmThenReturnFilm() {
        final String title = "Maze Runner: The Death Cure";
        final ReleaseType type = ReleaseType.NEW_RELEASE;
        final int quantity = 10;

        doReturn(FilmDataFixtures.film(title, type,  quantity)).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.save(title, type.name(), quantity);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("title", title);
        assertThat(result).hasFieldOrPropertyWithValue("type", type);
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity);
    }

    @Test
    public void whenCreatingFilmThenActiveIsTrue() {
        final Film film = FilmDataFixtures.newReleaseFilm();

        doReturn(film).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.save(film.getTitle(), film.getType().name(), film.getQuantity());

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", true);
    }

    @Test
    public void whenCreatingFilmWithDuplicateTitleThenThrowException() {
        final Film film = FilmDataFixtures.newReleaseFilm();

        thrown.expect(FilmUniqueViolationException.class);
        thrown.expectMessage("Film with title '" + film.getTitle() + "' already exits");

        doReturn(Collections.singletonList(film)).when(filmRepository).findByTitle(isA(String.class));

        filmService.save(film.getTitle(), film.getType().name(), film.getQuantity());
    }

    @Test
    public void whenUpdatingFilmThenReturnFilm() {
        final String title = "Maze Runner: The Death Cure";
        final String newTitle = "New Maze Runner: The Death Cure";
        final ReleaseType newType = ReleaseType.REGULAR_RELEASE;
        final int quantity = 10;

        doReturn(Optional.of(FilmDataFixtures.newReleaseFilm(title, quantity))).when(filmRepository).findById(isA(Long.class));
        doReturn(FilmDataFixtures.regularReleaseFilm(newTitle, quantity)).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.update(5L, newTitle, newType.name(), quantity);

        assertThat(result).isNotNull();

        assertThat(result).hasFieldOrPropertyWithValue("title", newTitle);
        assertThat(result).hasFieldOrPropertyWithValue("type", newType);
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity);
    }

    @Test
    public void whenUpdatingFilmWithDuplicateTitleThenThrowException() {
        final Film film = spy(FilmDataFixtures.newReleaseFilm());
        final long filmId = 5L;

        thrown.expect(FilmUniqueViolationException.class);
        thrown.expectMessage("Film with title '" + film.getTitle() + "' already exits");

        doReturn(6L).when(film).getId();
        doReturn(Collections.singletonList(film)).when(filmRepository).findByTitle(isA(String.class));

        filmService.update(filmId, film.getTitle(), film.getType().name(), film.getQuantity());
    }

    @Test
    public void whenUpdatingNonExistingFilmThenThrowException() {
        final Film film = FilmDataFixtures.newReleaseFilm();
        final long filmId = 5L;

        thrown.expect(FilmNotFoundException.class);
        thrown.expectMessage("Film with id '" + filmId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(isA(Long.class));

        filmService.update(filmId, film.getTitle(), film.getType().name(), film.getQuantity());
    }

    @Test
    public void whenDeletingFilmThenActiveFalse() {
        final Film toBeReturned = FilmDataFixtures.oldReleaseFilm();

        doReturn(Optional.of(toBeReturned)).when(filmRepository).findById(isA(Long.class));
        doReturn(toBeReturned).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.delete(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    public void whenDeletingNonExistingFilmThenThrowException() {
        final long filmId = 1L;

        thrown.expect(FilmNotFoundException.class);
        thrown.expectMessage("Film with id '" + filmId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(isA(Long.class));

        filmService.delete(filmId);
    }

    @Test
    public void whenUpdatingQuantityForFilmThenChanged() {
        final int before = 10;
        final Film toBeReturned = FilmDataFixtures.oldReleaseFilm("Out of Africa", before);

        doReturn(Optional.of(toBeReturned)).when(filmRepository).findById(isA(Long.class));
        doReturn(toBeReturned).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.updateQuantity(1L, 23);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("title", toBeReturned.getTitle());
        assertThat(result).hasFieldOrPropertyWithValue("type", toBeReturned.getType());
        assertThat(result).hasFieldOrPropertyWithValue("quantity", before + 23);
    }

    @Test
    public void whenUpdatingQuantityForNonExistingFilmThenThrowException() {
        final long filmId = 1L;

        thrown.expect(FilmNotFoundException.class);
        thrown.expectMessage("Film with id '" + filmId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(isA(Long.class));

        filmService.updateQuantity(1L, 23);
    }
}
