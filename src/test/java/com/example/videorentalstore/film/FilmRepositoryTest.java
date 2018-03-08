package com.example.videorentalstore.film;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FilmRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    public void whenReturnBackThenQuantityIncreased() {
        final int quantity = 2;
        final Film film = FilmDataFixtures.newReleaseFilm("Murder on the Orient Express", quantity);
        entityManager.persist(film);
        entityManager.flush();

        film.returnBack();

        final Film result = filmRepository.save(film);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity + 1);
    }

    @Test
    public void whenTakeThenQuantityDecreased() {
        final int quantity = 2;
        final Film film = FilmDataFixtures.newReleaseFilm("Murder on the Orient Express", quantity);
        entityManager.persist(film);
        entityManager.flush();

        film.take();

        final Film result = filmRepository.save(film);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("quantity", quantity - 1);
    }

    @Test
    public void whenSaveThenSizeIncreased() {
        Long before = filmRepository.count();

        filmRepository.save(FilmDataFixtures.newReleaseFilm("Maze Runner: The Death Cure", 6));

        Long result = filmRepository.count();

        assertThat(result).isEqualTo(before.intValue() + 1);
    }

    @Test
    public void whenSaveThenSearchAllContainsSavedResult() {
        Long before = filmRepository.count();

        Film film = filmRepository.save(FilmDataFixtures.newReleaseFilm());

        List<Film> result = filmRepository.findAll();

        assertThat(result).hasSize(before.intValue() + 1);
        assertThat(result).contains(film);
    }

    @Test
    public void whenSaveThenReturnCorrectResult() {
        final Film film = FilmDataFixtures.newReleaseFilm();

        final Film result = filmRepository.save(film);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(film.getTitle());
        assertThat(result.getType()).isEqualTo(film.getType());
        assertThat(result.getQuantity()).isEqualTo(film.getQuantity());
    }

    @Test
    public void whenDeactivateThenActiveIsFalse() {
        Film film = FilmDataFixtures.newReleaseFilm();

        film.deactivate();

        final Film result = filmRepository.save(film);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    public void whenDeactivateThenSizeDecreased() {
        final Film film = FilmDataFixtures.newReleaseFilm();
        entityManager.persist(film);
        entityManager.flush();

        Long before = filmRepository.count();

        film.deactivate();

        filmRepository.save(film);

        Long result = filmRepository.count();

        assertThat(result).isEqualTo(before.intValue() - 1);
    }

    @Test
    public void whenDeactivateThenSearchAllReturnsCorrectResult() {
        final Film film = FilmDataFixtures.newReleaseFilm();
        entityManager.persist(film);
        entityManager.flush();

        film.deactivate();

        filmRepository.save(film);

        final List<Film> result = filmRepository.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result).extracting(Film::isActive).containsOnly(true);
    }

    @Test
    public void whenDeactivateThenSearchByIdReturnsCorrectResult() {
        final Film film = FilmDataFixtures.newReleaseFilm();
        entityManager.persist(film);
        entityManager.flush();

        film.deactivate();

        filmRepository.save(film);

        final Optional<Film> result = filmRepository.findById(film.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void whenDeactivateThenSearchByTitleReturnsCorrectResult() {
        final Film film = FilmDataFixtures.newReleaseFilm("Murder on the Orient Express");
        entityManager.persist(film);
        entityManager.flush();

        film.deactivate();

        filmRepository.save(film);

        final List<Film> result = filmRepository.findByTitle("orient express");

        assertThat(result).isEmpty();
    }

    @Test
    public void whenSearchByIdThenReturnCorrectResult() {
        final Film film = FilmDataFixtures.newReleaseFilm();
        entityManager.persist(film);
        entityManager.flush();

        final Film result = filmRepository.findById(film.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(film.getTitle());
        assertThat(result.getType()).isEqualTo(film.getType());
        assertThat(result.getQuantity()).isEqualTo(film.getQuantity());
    }

    @Test
    public void whenSearchByTitleThenReturnListContainingThatTitle() {
        final List<Film> result = filmRepository.findByTitle("spider");

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Film::getTitle).containsExactly("Spider Man", "Spider Man 2");
    }

    @Test
    public void whenSearchByNonExistingTitleThenReturnEmptyList() {
        final List<Film> result = filmRepository.findByTitle("non-existing");

        assertThat(result).isEmpty();
    }

    @Test
    public void whenSearchAllThenReturnResult() {
        final List<Film> result = filmRepository.findAll();

        assertThat(result).isNotEmpty();
    }
}
