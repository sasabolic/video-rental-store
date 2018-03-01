package com.example.videorentalstore.film;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.isA;
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

        final Iterable<Film> result = filmService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }

    @Test
    public void whenFindAllByNameThenReturnListOfFilmsContainingName() {
        doReturn(FilmDataFixtures.filmsWithSpiderMan()).when(filmRepository).findByNameContainingIgnoreCase(isA(String.class));

        final Iterable<Film> result = filmService.findAllByName("Spider Man");

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
    }

    @Test
    public void whenFindingByIdThenReturnOneResult() {
        doReturn(FilmDataFixtures.oldReleaseFilm()).when(filmRepository).findOne(isA(Long.class));

        final Film result = filmService.findById(4L);

        assertThat(result).isNotNull();
    }

    @Test
    public void whenCreatingNewReleaseFilmThenReturnSavedFilm() {
        final String name = "Maze Runner: The Death Cure";
        final String type = "NEW_RELEASE";
        final int quantity = 10;

        final CreateFilmCmd createFilmCmd = new CreateFilmCmd(name, type, quantity);

        doReturn(FilmDataFixtures.newReleaseFilm(name, quantity)).when(filmRepository).save(isA(Film.class));

        final Film result = filmService.save(createFilmCmd);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("name", name);
        assertThat(result).isInstanceOf(NewReleaseFilm.class);
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity);

    }

    @Test
    public void whenUpdatingFilmThenReturnSavedFilm() {
        final String name = "Maze Runner: The Death Cure";
        final String newName = "New Maze Runner: The Death Cure";
        final String type = "NEW_RELEASE";
        final int quantity = 10;

        final UpdateFilmCmd updateFilmCmd = new UpdateFilmCmd(5L, name, type, quantity);

        doReturn(FilmDataFixtures.newReleaseFilm(name, quantity)).when(filmRepository).findOne(isA(Long.class));
        doReturn(FilmDataFixtures.newReleaseFilm(newName, quantity)).when(filmRepository).save(isA(Film.class));

        filmService.update(updateFilmCmd);

//        assertThat(result).isNotNull();
//        assertThat(result).hasFieldOrPropertyWithValue("name", name);
//        assertThat(result).isInstanceOf(NewReleaseFilm.class);
//        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity);

    }
}
