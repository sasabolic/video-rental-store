package com.example.videorentalstore.billing;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FilmTypeTest {

    @Test
    public void test() {
        LocalDateTime dateTime = LocalDateTime.of(2017, Month.DECEMBER, 18, 6, 17, 10);

        System.out.println("LocalDateTime : " + dateTime);

        final FilmType filmType = FilmType.now(dateTime.toInstant(ZoneOffset.UTC));

        assertThat(filmType).isEqualByComparingTo(FilmType.NEW_RELEASE);
    }

    @Test
    public void test1() {
        LocalDateTime dateTime = LocalDateTime.of(2016, Month.AUGUST, 18, 6, 17, 10);

        System.out.println("LocalDateTime : " + dateTime);

        final FilmType filmType = FilmType.now(dateTime.toInstant(ZoneOffset.UTC));

        assertThat(filmType).isEqualByComparingTo(FilmType.REGULAR_FILM);
    }

    @Test
    public void givenDateOfReleaseOlderThan3YearsWhenInvokeValueOfReturnOldFilm() {
        LocalDateTime dateTime = LocalDateTime.of(2014, Month.AUGUST, 18, 6, 17, 10);

        System.out.println("LocalDateTime : " + dateTime);

        final FilmType filmType = FilmType.now(dateTime.toInstant(ZoneOffset.UTC));

        assertThat(filmType).isEqualByComparingTo(FilmType.OLD_FILM);
    }
}
