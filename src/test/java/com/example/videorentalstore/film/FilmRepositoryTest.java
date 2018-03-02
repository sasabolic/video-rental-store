package com.example.videorentalstore.film;

import com.example.videorentalstore.pricing.ReleaseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void whenFindOneThenReturnCorrectResult() {
        final Film newFilm = new Film("Murder on the Orient Express", ReleaseType.NEW_RELEASE, 2);
        entityManager.persist(newFilm);
        entityManager.flush();

        final Optional<Film> film = filmRepository.findById(5L);

        assertThat(film).isNotNull();
        assertThat(film.isPresent()).isTrue();
        assertThat(film.get()).hasFieldOrPropertyWithValue("name", "Murder on the Orient Express");
        assertThat(film.get()).hasFieldOrPropertyWithValue("quantity", 2);
    }

    @Test
    public void whenReturnBackThenQuantityIsIncremented() {
        final int quantity = 2;
        final Film newFilm = new Film("Murder on the Orient Express", ReleaseType.NEW_RELEASE, quantity);
        entityManager.persist(newFilm);
        entityManager.flush();

        Optional<Film> film = filmRepository.findById(newFilm.getId());
        if (film.isPresent()) {
            final Film saved = film.get();
            saved.returnBack();

            entityManager.persist(saved);
            entityManager.flush();
        }

        film = filmRepository.findById(newFilm.getId());

        assertThat(film).isNotNull();
        assertThat(film.isPresent()).isTrue();
        assertThat(film.get()).hasFieldOrPropertyWithValue("name", "Murder on the Orient Express");
        assertThat(film.get()).hasFieldOrPropertyWithValue("quantity", quantity + 1);
    }

    @Test
    public void whenTakeThenQuantityIsDecremented() {
        final int quantity = 2;
        final Film newFilm = new Film("Murder on the Orient Express", ReleaseType.NEW_RELEASE, quantity);
        entityManager.persist(newFilm);
        entityManager.flush();

        Optional<Film> film = filmRepository.findById(newFilm.getId());
        if (film.isPresent()) {
            final Film saved = film.get();
            saved.take();

            entityManager.persist(saved);
            entityManager.flush();
        }

        film = filmRepository.findById(newFilm.getId());

        assertThat(film).isNotNull();
        assertThat(film.isPresent()).isTrue();
        assertThat(film.get()).hasFieldOrPropertyWithValue("name", "Murder on the Orient Express");
        assertThat(film.get()).hasFieldOrPropertyWithValue("quantity", quantity - 1);
    }

    @Test
    public void whenFindByNameThenReturnCorrectResult() {
        final Film newFilm = new Film("Murder on the Orient Express", ReleaseType.NEW_RELEASE, 2);
        entityManager.persist(newFilm);
        entityManager.flush();

        final Iterable<Film> films = filmRepository.findByNameContainingIgnoreCase("orient express");

        assertThat(films).isNotNull();
        assertThat(films).isNotEmpty();
        assertThat(films).hasSize(1);
        assertThat(films).extracting(Film::getName).containsExactly("Murder on the Orient Express");
    }

    @Test
    public void whenSaveThenReturnCorrectResult() {
        final Film newFilm = new Film("Maze Runner: The Death Cure", ReleaseType.NEW_RELEASE, 6);

        final Film savedFilm = filmRepository.save(newFilm);

        final Optional<Film> result = filmRepository.findById(savedFilm.getId());

        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(newFilm.getName());
        assertThat(result.get().getQuantity()).isEqualTo(newFilm.getQuantity());

    }
}
