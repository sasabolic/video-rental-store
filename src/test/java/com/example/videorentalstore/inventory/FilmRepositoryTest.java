package com.example.videorentalstore.inventory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FilmRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    public void whenFindOneThenReturnCorrectResult() {
        final Film newFilm = new NewReleaseFilm("Murder on the Orient Express", 2);
        entityManager.persist(newFilm);
        entityManager.flush();

        final Film film = filmRepository.findOne(5L);

        assertThat(film).isNotNull();
        assertThat(film).hasFieldOrPropertyWithValue("name", "Murder on the Orient Express");
        assertThat(film).hasFieldOrPropertyWithValue("quantity", 2);
    }

    @Test
    public void whenReturnBackThenQuantityIsIncremented() {
        final int quantity = 2;
        final Film newFilm = new NewReleaseFilm("Murder on the Orient Express", quantity);
        entityManager.persist(newFilm);
        entityManager.flush();

        Film film = filmRepository.findOne(newFilm.getId());
        film.returnBack();

        entityManager.persist(film);
        entityManager.flush();

        film = filmRepository.findOne(newFilm.getId());

        assertThat(film).isNotNull();
        assertThat(film).hasFieldOrPropertyWithValue("name", "Murder on the Orient Express");
        assertThat(film).hasFieldOrPropertyWithValue("quantity", quantity + 1);
    }

    @Test
    public void whenTakeThenQuantityIsDecremented() {
        final int quantity = 2;
        final Film newFilm = new NewReleaseFilm("Murder on the Orient Express", quantity);
        entityManager.persist(newFilm);
        entityManager.flush();

        filmRepository.findAll().forEach(f -> {
            System.out.println(f.getId() + ": " + f.getName() + ":" + f.getQuantity());
        });

        Film film = filmRepository.findOne(newFilm.getId());
        film.take();

        entityManager.persist(film);
        entityManager.flush();

        film = filmRepository.findOne(newFilm.getId());

        assertThat(film).isNotNull();
        assertThat(film).hasFieldOrPropertyWithValue("name", "Murder on the Orient Express");
        assertThat(film).hasFieldOrPropertyWithValue("quantity", quantity - 1);
    }

    @Test
    public void whenFindByNameThenReturnCorrectResult() {
        final Film newFilm = new NewReleaseFilm("Murder on the Orient Express", 2);
        entityManager.persist(newFilm);
        entityManager.flush();

        filmRepository.findAll().forEach(f -> {
            System.out.println(f.getId() + ": " + f.getName() + ":" + f.getQuantity());
        });

        final Iterable<Film> films = filmRepository.findByNameContainingIgnoreCase("orient express");


        assertThat(films).isNotNull();
        assertThat(films).isNotEmpty();
        assertThat(films).hasSize(1);
        assertThat(films).extracting(Film::getName).containsExactly("Murder on the Orient Express");
    }
}
